package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ThreadedSearch<T, B> implements Search<B> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	private final Queue<B> solutions = new ConcurrentLinkedQueue<>();
	
	private final Collection<SearchThread> threads = new ConcurrentLinkedQueue<>();
	
	private final AtomicInteger count = new AtomicInteger(0);

	private final int maxThreads = Runtime.getRuntime().availableProcessors();
	
	private boolean firstSolution;
	
	private boolean halt = false;
	
	private final int maxDepth;
	
	ThreadedSearch(final int maxDepth) {
		this.maxDepth = maxDepth;
	}

	@Override
	public Collection<B> findAll() {
		firstSolution = false;
		execute();
		return new ArrayList<>(solutions);
	}

	@Override
	public Optional<B> findFirst() {
		firstSolution = true;
		execute();
		return solutions.isEmpty() ? Optional.empty() : Optional.of(solutions.iterator().next());
	}
	
	private void execute() {
		enqueue(first());
		log("executing search with %d threads", maxThreads);
		while(!(halt || threads.stream().allMatch(SearchThread::idle))) {
			hibernate();
			log("queued: %d, finished: %d", queue.size(), count.get());
		}
		halt = true;
	}
	
	private void process(final T t) {
		final B candidate = digest(t);
		if(solves(candidate)) {
			solutions.add(candidate);
			if(firstSolution) {
				halt = true;
			}
		}
		else if(depth(candidate) < maxDepth) {
			final Collection<T> next = generateNext(candidate);
			next.forEach(this::enqueue);
		}
		count.incrementAndGet();
	}

	private void enqueue(final T t) {
		if(!queue.contains(t)) {
			queue.add(t);
		}
		if(!halt && (threads.size() < maxThreads)) {
			final SearchThread thread = new SearchThread();
			threads.add(thread);
			thread.start();
		}
	}
	
	abstract T first();
	
	abstract B digest(final T t);
	
	abstract boolean solves(final B b);
	
	abstract int depth(final B b);
	
	abstract Collection<T> generateNext(final B b);
	
	private class SearchThread extends Thread {
		private boolean idle = false;
		
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
				threads.remove(this);
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
	
	void log(final String format, final Object... args) {
		System.out.println(String.format(format, args));
	}

}
