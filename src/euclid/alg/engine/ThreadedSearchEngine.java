package euclid.alg.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.alg.Algorithm;

public class ThreadedSearchEngine<B> implements SearchEngine<B> {

	private final List<Queue<B>> queues;

	private final List<AtomicInteger> queueTotal;

	private final List<AtomicInteger> queueFinished;

	private final Collection<B> collector = ConcurrentHashMap.newKeySet();

	private final Queue<B> solutions = new ConcurrentLinkedQueue<>();
	
	private final AtomicInteger finishedCount = new AtomicInteger();
	
	private final AtomicInteger dupeCount = new AtomicInteger();
	
	private final AtomicInteger currentDepth = new AtomicInteger();

	private final int threadCount = Runtime.getRuntime().availableProcessors();
	
	private final Collection<SearchThread> threads = new ArrayList<>(threadCount);
	
	private final Algorithm<B> algorithm;

	private final int maxDepth;
	
	private boolean findFirst;
	
	private boolean halt = false;
	
	public ThreadedSearchEngine(final Algorithm<B> algorithm, final int maxDepth) {
		this.algorithm = algorithm;
		this.maxDepth = maxDepth;
		final int maxMisses = algorithm.maxMisses();
		queues = new ArrayList<>(maxMisses);
		queueTotal = new ArrayList<>(maxMisses);
		queueFinished = new ArrayList<>(maxMisses);
		for(int i = 0; i < maxMisses; i++) {
			queues.add(new ConcurrentLinkedQueue<>());
			queueTotal.add(new AtomicInteger());
			queueFinished.add(new AtomicInteger());
		}
	}

	@Override
	public Collection<B> findAll() {
		findFirst = false;
		execute();
		return new ArrayList<>(solutions);
	}

	@Override
	public Optional<B> findFirst() {
		findFirst = true;
		execute();
		return solutions.isEmpty() ? Optional.empty() : Optional.of(solutions.iterator().next());
	}

	@Override
	public Map<String, Number> report() {
		final Map<String, Number> report = new LinkedHashMap<>();
		report.put("finished", finishedCount.get());
		report.put("total", collector.size());
		for(int i = 0; i < queues.size(); i++) {
			report.put("finished-" + i, queueFinished.get(i).get());
			report.put("queued-" + i, queues.get(i).size());
			report.put("total-" + i, queueTotal.get(i).get());
		}
		report.put("dupes", dupeCount.get());
		report.put("depth", currentDepth.get());
		if(!findFirst) {
			report.put("solutions", solutions.size());
		}
		return report;
	}
	
	private void execute() {
		enqueue(algorithm.first());
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
	
	private B poll() {
		for(int i = 0; i < queues.size(); i++) {
			final Queue<B> queue = queues.get(i);
			final B b = queue.poll();
			if(b != null) {
				queueFinished.get(i).incrementAndGet();
				return b;
			}
		}
		return null;
	}
	
	private void process(final B b) {
		final B candidate = algorithm.digest(b);
		test(candidate);
		if(depth(candidate) < maxDepth) {
			final Collection<B> next = algorithm.nextGeneration(candidate);
			next.forEach(this::enqueue);
		}
		finishedCount.incrementAndGet();
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

	private int depth(final B candidate) {
		final int depth = algorithm.depth(candidate);
		if(depth > currentDepth.get()) {
			currentDepth.set(depth);
		}
		return depth;
	}

	private void enqueue(final B candidate) {
		if(!collector.contains(candidate)) {
			collector.add(candidate);
			final int misses = test(candidate);
			if(misses > 0) {
				final int queueIndex = misses - 1;
				queues.get(queueIndex).add(candidate);
				queueTotal.get(queueIndex).incrementAndGet();
			}
		}
		else {
			dupeCount.incrementAndGet();
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
					final B b = poll();
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
