package euclid.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
		queues = new PriorityQueuePool<B>(algorithm.maxPriority(), parameters.depthFirst());
		kpiProvider = new EngineKpiProvider(solutions::size);
		deduper = new Deduplicator<>(parameters.dedupeDepth());
	}
	
	public Collection<KpiReporter> kpiReporters() {
		return Arrays.asList(deduper, kpiProvider, queues);
	}
	
	public boolean hasSolution() {
		return ! solutions.isEmpty();
	}

	public List<B> solutions() {
		return new ArrayList<>(solutions);
	}

	public void start(final boolean async) {
		final Collection<SearchThread> threads = IntStream.range(0, parameters.threadCount())
				.mapToObj(this::threadName)
				.map(SearchThread::new)
				.collect(Collectors.toList());
		process(algorithm.first());
		threads.forEach(SearchThread::start);
		final Thread watcher = new Thread(() -> {
			while(!(halt || threads.stream().allMatch(SearchThread::idle))) {
				hibernate(1000);
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

	private void process(final B parent) {
		final int depth = algorithm.depth(parent);
		final Collection<B> nextGeneration = algorithm.nextGeneration(parent);
		kpiProvider.reportProcessed(depth, nextGeneration.size());
		for(final B candidate : nextGeneration) {
			final int priority = algorithm.priority(candidate);
			kpiProvider.reportCandidate(priority);
			if(priority == 0) {
				solutions.add(candidate);
				if(solutions.size() >= parameters.maxSolutions()) {
					halt = true;
				}
			}
			else if(priority > 0) {
				if(deduper.checkDupe(candidate, depth)) {
					queues.enqueue(candidate, priority - 1);
				}
			}
		}
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
						hibernate(100);
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

	private static void hibernate(final int timeout) {
		try {
			Thread.sleep(timeout);
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
