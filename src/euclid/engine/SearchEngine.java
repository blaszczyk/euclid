package euclid.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import euclid.algorithm.Algorithm;
import euclid.kpi.KpiReporter;

public class SearchEngine<B> {
	
	private final Collection<B> solutions = new CopyOnWriteArrayList<>();

	private final EngineKpiProvider kpiProvider;

	private final PriorityQueuePool<B> queues;
	
	private final Collection<SearchThread> threads;
	
	private final Algorithm<B> algorithm;

	private final EngineParameters parameters;

	private boolean halt = false;

	public SearchEngine(final Algorithm<B> algorithm,  final EngineParameters parameters) {
		this.algorithm = algorithm;
		this.parameters = parameters;
		queues = new PriorityQueuePool<B>(algorithm.maxPriority(), parameters.depthFirst());
		kpiProvider = new EngineKpiProvider();
		threads = IntStream.range(0, parameters.threadCount())
				.mapToObj(i -> String.format("search-%s-%d", parameters.id(), i))
				.map(SearchThread::new)
				.collect(Collectors.toList());
	}
	
	public Collection<KpiReporter> kpiReporters() {
		return Arrays.asList(kpiProvider, queues);
	}

	public List<B> solutions() {
		return new ArrayList<>(solutions);
	}

	public void start() {
		process(algorithm.first());
		threads.forEach(SearchThread::start);
	}

	public void join() {
		try {
			for(final SearchThread thread : threads) {
				thread.join();
			}
		}
		catch(InterruptedException ignore) {
		}
	}

	public void halt() {
		halt = true;
	}
	
	public void cleanUp() {
		queues.cleanUp();
	}
	
	public boolean finished() {
		return halt;
	}

	private void process(final B parent) {
		final int depth = algorithm.depth(parent);
		final PrioritizedGeneration<B> generation = new PrioritizedGeneration<>(algorithm.maxPriority());
		final List<B> candidates = algorithm.nextGeneration(parent);
		if(parameters.shuffle()) {
			Collections.shuffle(candidates);
		}
		for(final B candidate : candidates) {
			final int priority = algorithm.priority(candidate);
			if(priority == 0) {
				solutions.add(candidate);
				if(solutions.size() >= parameters.maxSolutions()) {
					halt();
				}
			}
			generation.add(candidate, priority-1);
		}
		kpiProvider.report(depth, generation);
		queues.enqueue(generation);
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
						if(threads.stream().allMatch(SearchThread::idle)) {
							halt();
						}
						else {
							Thread.sleep(100);
						}
					}
					else {
						process(b);
					}
				}
			}
			catch(InterruptedException ignore) {
			}
			finally {
				halt();
			}
		}
	}

}
