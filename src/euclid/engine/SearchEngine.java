package euclid.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;	
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import euclid.algorithm.Algorithm;
import euclid.kpi.KpiReporter;

public class SearchEngine<B> {
	
	private final Collection<B> solutions = new CopyOnWriteArrayList<>();

	private final Deduplicator<B> deduper;

	private final EngineKpiProvider kpiProvider;

	private final PriorityQueuePool<B> queues;
	
	private final Algorithm<B> algorithm;

	private final EngineParameters parameters;

	private boolean halt = false;
	
	public SearchEngine(final Algorithm<B> algorithm,  final EngineParameters parameters) {
		this.algorithm = algorithm;
		this.parameters = parameters;
		queues = new PriorityQueuePool<B>(algorithm.maxMisses(), parameters.depthFirst());
		kpiProvider = new EngineKpiProvider(solutions::size);
		deduper = new Deduplicator<>(parameters.dedupeDepth());
	}
	
	public Collection<KpiReporter> kpiReporters() {
		return Arrays.asList(deduper, kpiProvider, queues);
	}
	
	public boolean hasSolution() {
		return ! solutions.isEmpty();
	}

	public Collection<B> solutions() {
		return new ArrayList<>(solutions);
	}

	public void start(final boolean async) {
		final Collection<SearchThread> threads = IntStream.range(0, parameters.threadCount())
				.mapToObj(this::threadName)
				.map(SearchThread::new)
				.collect(Collectors.toList());
		testAndEnqueue(algorithm.first(), 0);
		threads.forEach(SearchThread::start);
		final Thread watcher = new Thread(() -> {
			while(!(halt || threads.stream().allMatch(SearchThread::idle))) {
				hibernate();
			}
			halt();
		}, String.format("engine-watcher-%s", parameters.id()));
		watcher.start();
		if(!async) {
			join(watcher);
			threads.forEach(SearchEngine::join);
		}
	}

	private String threadName(final int count) {
		return String.format("search-%s-%d", parameters.id(), count);
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
		if(misses > 0 && depth < algorithm.maxDepth()) {
			final Collection<B> next = algorithm.nextGeneration(candidate);
			next.forEach(n -> testAndEnqueue(n, depth));
		}
	}

	private void testAndEnqueue(final B candidate, final int depth) {
		if(deduper.checkDupe(candidate, depth)) {
			final int misses = test(candidate);
			if(misses > 0) {
				queues.enqueue(candidate, misses - 1);
			}
		}
	}
	
	private int test(final B candidate) {
		final int misses = algorithm.misses(candidate);
		if(misses == 0) {
			solutions.add(candidate);
			if(solutions.size() >= parameters.maxSolutions()) {
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
