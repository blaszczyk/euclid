package euclid.alg.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import euclid.alg.Algorithm;
import euclid.kpi.KpiReporter;

public class ThreadedSearchEngine<B> {

	private final PriorityQueuePool<B> queues;
	
	private final EngineKpiProvider kpiProvider;

	private final Collection<B> collector = ConcurrentHashMap.newKeySet();

	private final Collection<B> solutions = new ConcurrentLinkedQueue<>();
	
	private final Collection<SearchThread> threads;
	
	private final Algorithm<B> algorithm;

	private final int maxDepth;

	private final boolean findAll;

	private boolean halt = false;
	
	public ThreadedSearchEngine(final Algorithm<B> algorithm, final int maxDepth, final boolean findAll, final int threadCount) {
		this.algorithm = algorithm;
		this.maxDepth = maxDepth;
		this.findAll = findAll;
		threads = IntStream.range(0, threadCount)
				.mapToObj(this::threadName)
				.map(SearchThread::new)
				.collect(Collectors.toList());
		queues = new PriorityQueuePool<>(algorithm.maxMisses());
		kpiProvider = findAll ? new EngineKpiProvider(collector::size, solutions::size) : new EngineKpiProvider(collector::size);
	}

	private String threadName(final int count) {
		return String.format("search-%08X-%d", hashCode(), count);
	}
	
	public Collection<KpiReporter> kpiReporters() {
		return Arrays.asList(kpiProvider, queues);
	}
	
	public boolean hasSolution() {
		return ! solutions.isEmpty();
	}

	public Collection<B> allSolutions() {
		return new ArrayList<>(solutions);
	}

	public Optional<B> firstSolution() {
		return solutions.isEmpty() ? Optional.empty() : Optional.of(solutions.iterator().next());
	}

	public void start(final boolean async) {
		testAndEnqueue(algorithm.first());
		threads.forEach(SearchThread::start);
		final Thread watcher = new Thread(() -> {
			while(!(halt || threads.stream().allMatch(SearchThread::idle))) {
				hibernate();
			}
			halt();
		},"engine-watcher");
		watcher.start();
		if(!async) {
			join(watcher);
			threads.forEach(ThreadedSearchEngine::join);
		}
	}

	public void halt() {
		halt=true;
	}
	
	public boolean finished() {
		return halt;
	}
	
	private void process(final B b) {
		final B candidate = algorithm.digest(b);
		final int misses = test(candidate);
		final int depth = algorithm.depth(candidate);
		kpiProvider.incrementProcessedAndAddDepth(depth);
		if(misses > 0 && depth <= maxDepth - misses) {
			final Collection<B> next = algorithm.nextGeneration(candidate);
			next.forEach(this::testAndEnqueue);
		}
	}

	private void testAndEnqueue(final B candidate) {
		if(collector.add(candidate)) {
			final int misses = test(candidate);
			if(misses > 0) {
				queues.enqueue(candidate, misses - 1);
			}
		}
		else {
			kpiProvider.incrementDupes();
		}
	}
	
	private int test(final B candidate) {
		final int misses = algorithm.misses(candidate);
		if(misses == 0) {
			solutions.add(candidate);
			if(!findAll) {
				halt = true;
			}
		}
		return misses;
	}
	
	private class SearchThread extends Thread {
		private boolean idle = false;
		
		public SearchThread(final String name) {
			super(name);
		}

		private boolean idle() {
			return idle;
		}

		@Override
		public void run() {
			try {
				while(!halt) {
					final B b = queues.poll();
					if(idle = (b == null)) {
						hibernate();
					}
					else {
						process(b);
					}
				}
			}
			finally {
				idle = true;
			}
		}
	}

	private static void hibernate() {
		try {
			Thread.sleep(1000);
		}
		catch(InterruptedException e) {
		}
	}
	
	private static void join(final Thread thread) {
		try {
			thread.join();
		}
		catch(InterruptedException e) {
		}
	}

}
