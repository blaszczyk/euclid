package euclid.alg.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.alg.Algorithm;

public class ThreadedSearchEngine<T, B> implements SearchEngine<B> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	private final Collection<T> collector = ConcurrentHashMap.newKeySet();

	private final Queue<B> solutions = new ConcurrentLinkedQueue<>();
	
	private final AtomicInteger finishedCount = new AtomicInteger();
	
	private final AtomicInteger dupeCount = new AtomicInteger();
	
	private final AtomicInteger currentDepth = new AtomicInteger();

	private final int threadCount = Runtime.getRuntime().availableProcessors();
	
	private final Collection<SearchThread> threads = new ArrayList<>(threadCount);
	
	private final Algorithm<T, B> algorithm;

	private final int maxDepth;
	
	private boolean findFirst;
	
	private boolean halt = false;
	
	public ThreadedSearchEngine(final Algorithm<T, B> algorithm, final int maxDepth) {
		this.algorithm = algorithm;
		this.maxDepth = maxDepth;
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
		report.put("queued", queue.size());
		report.put("total", collector.size());
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
	
	private void process(final T t) {
		final B candidate = algorithm.digest(t);
		final int depth = algorithm.depth(candidate);
		checkDepth(depth);
		if(algorithm.solves(candidate)) {
			solutions.add(candidate);
			if(findFirst) {
				halt = true;
			}
		}
		else if(depth < maxDepth) {
			final Collection<T> next = algorithm.generateNext(candidate);
			next.forEach(this::enqueue);
		}
		finishedCount.incrementAndGet();
	}

	private void checkDepth(final int depth) {
		if(depth > currentDepth.get()) {
			currentDepth.set(depth);
		}
	}

	private void enqueue(final T t) {
		if(!collector.contains(t)) {
			collector.add(t);
			queue.add(t);
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
					final T t = queue.poll();
					idle = (t == null);
					if(idle) {
						hibernate();
					}
					else {
						process(t);
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
