package euclid.alg;

import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class ThreadedSearch<T, B> implements Search<B> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	private final Queue<B> solutions = new ConcurrentLinkedQueue<>();
	
	private final Collection<SearchThread> threads = new ConcurrentLinkedQueue<>();
	
	private final int maxThreads = Runtime.getRuntime().availableProcessors();
	
	private boolean first;

	@Override
	public Collection<B> findAll() {
		first = false;
		return find();
	}

	@Override
	public Optional<B> findFirst() {
		first = true;
		final Collection<B> sol = find();
		return sol.isEmpty() ? Optional.empty() : Optional.of(sol.iterator().next());
	}
	
	private Collection<B> find() {
		addToQueue(first());
		while(threads.stream().anyMatch(SearchThread::running)) {
			hibernate();
		}
		return solutions;
	}
	
	private void proceed() {
		T t = null;
		synchronized (queue) {
			if(!queue.isEmpty()) {
				t = queue.remove();
			}
		}
		if(t == null) {
			hibernate();
		}
		else {
			proceed(t);
		}
	}
	
	private void proceed(final T next) {
		final B candidate = digest(next);
		if(solves(candidate)) {
			solutions.add(candidate);
			if(first) {
				threads.forEach(SearchThread::halt);
			}
		}
		else if(!exceedsDepth(candidate)) {
			final Collection<T> ts = generateNext(candidate);
			synchronized (queue) {
				ts.forEach(this::addToQueue);
			}
		}
	}

	private void addToQueue(final T t) {
		if(!queue.contains(t))
			queue.add(t);
		if(threads.size() < maxThreads) {
			final SearchThread thread = new SearchThread();
			threads.add(thread);
			thread.start();
		}
	}
	
	abstract T first();
	
	abstract B digest(final T t);
	
	abstract boolean solves(final B b);
	
	abstract boolean exceedsDepth(final B b);
	
	abstract Collection<T> generateNext(final B b);
	
	class SearchThread extends Thread{
		private boolean running = true;
		
		private void halt() {
			running = false;
		}
		
		private boolean running() {
			return running;
		}
		
		@Override
		public void run() {
			try {
				while(running) {
					proceed();
				}
			}
			finally {
				running = false;
				threads.remove(this);
			}
		}
	}
	
	private static void hibernate() {
		try {
			Thread.sleep(500);
		}
		catch(InterruptedException e) {
		}
	}
	
}
