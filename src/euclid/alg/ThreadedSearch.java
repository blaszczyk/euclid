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
	
	private final AtomicInteger finishedCount = new AtomicInteger();
	
	private final AtomicInteger dupeCount = new AtomicInteger();
	
	private final AtomicInteger currentDepth = new AtomicInteger();

	private final int threadCount = Runtime.getRuntime().availableProcessors();
	
	private final Collection<SearchThread> threads = new ArrayList<>(threadCount);
	
	private boolean findFirst;
	
	private boolean halt = false;
	
	private final int maxDepth;
	
	ThreadedSearch(final int maxDepth) {
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
	public String report() {
		return String.format("queued: %d, finished: %d, dupes: %d, depth: %d", queue.size(), finishedCount.get(), dupeCount.get(), currentDepth.get());
	}
	
	private void execute() {
		enqueue(first());
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
	}
	
	private void process(final T t) {
		final B candidate = digest(t);
		final int depth = depth(candidate);
		checkDepth(depth);
		if(solves(candidate)) {
			solutions.add(candidate);
			if(findFirst) {
				halt = true;
			}
		}
		else if(depth < maxDepth) {
			final Collection<T> next = generateNext(candidate);
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
		if(!queue.contains(t)) {
			queue.add(t);
		}
		else {
			dupeCount.incrementAndGet();
		}
	}
	
	abstract T first();
	
	abstract B digest(final T t);
	
	abstract boolean solves(final B b);
	
	abstract int depth(final B b);
	
	abstract Collection<T> generateNext(final B b);
	
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

}
