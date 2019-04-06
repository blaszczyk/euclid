package euclid.engine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

class MonitoredQueue<B> {

	private final Queue<B> queue = new ConcurrentLinkedQueue<>();

	private final AtomicInteger totalCount = new AtomicInteger();
	
	B poll() {
		return queue.poll();
	}
	
	void enqueue(final B b) {
		queue.add(b);
		totalCount.incrementAndGet();
	}
	
	int queuedCount() {
		return queue.size();
	}
	
	int totalCount() {
		return totalCount.get();
	}

}
