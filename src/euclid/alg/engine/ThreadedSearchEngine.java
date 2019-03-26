package euclid.alg.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import euclid.alg.Algorithm;
import euclid.kpi.KpiReporter;

public class ThreadedSearchEngine<B> implements SearchEngine<B> {

	private final PriorityQueuePool<B> queues;
	
	private final EngineKpiProvider kpiProvider;

	private final Collection<B> collector = ConcurrentHashMap.newKeySet();

	private final Queue<B> solutions = new ConcurrentLinkedQueue<>();

	private final int threadCount = Runtime.getRuntime().availableProcessors();
	
	private final Collection<SearchThread> threads = new ArrayList<>(threadCount);
	
	private final Algorithm<B> algorithm;

	private final int maxDepth;

	private final boolean findFirst;

	private boolean halt = false;
	
	public ThreadedSearchEngine(final Algorithm<B> algorithm, final int maxDepth, final boolean findFirst) {
		this.algorithm = algorithm;
		this.maxDepth = maxDepth;
		this.findFirst = findFirst;
		queues = new PriorityQueuePool<>(algorithm.maxMisses());
		kpiProvider = findFirst ? new EngineKpiProvider(collector::size) : new EngineKpiProvider(collector::size, solutions::size);
	}
	
	public KpiReporter kpiReporter() {
		return kpiProvider;
	}
	
	public KpiReporter queueKpiReporter() {
		return queues;
	}

	@Override
	public Collection<B> findAll() {
		execute();
		return new ArrayList<>(solutions);
	}

	@Override
	public Optional<B> findFirst() {
		execute();
		return solutions.isEmpty() ? Optional.empty() : Optional.of(solutions.iterator().next());
	}

	private void execute() {
		testAndEnqueue(algorithm.first());
		for(int i = 0; i < threadCount; i++) {
			final String threadName = String.format("search-%08X-%d", hashCode(), i);
			final SearchThread thread = new SearchThread(threadName);
			threads.add(thread);
			thread.start();
		}
		while(!(halt || threads.stream().allMatch(SearchThread::idle))) {
			hibernate();
		}
		halt = true;
		threads.forEach(ThreadedSearchEngine::join);
	}
	
	private void process(final B b) {
		final B candidate = algorithm.digest(b);
		final int misses = test(candidate);
		final int depth = algorithm.depth(candidate);
		kpiProvider.reportDepth(depth);
		if(misses > 0 && depth < maxDepth) {
			final Collection<B> next = algorithm.nextGeneration(candidate);
			next.forEach(this::testAndEnqueue);
		}
		kpiProvider.incrementFinished();
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
			if(findFirst) {
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
					idle = (b == null);
					if(idle) {
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
