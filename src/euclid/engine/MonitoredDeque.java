package euclid.engine;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

abstract class MonitoredDeque<B> {
	
	static <S> MonitoredDeque<S> newQueue() {
		return new MonitoredDeque<S>() {
			@Override
			S poll() {
				return queue.pollLast();
			}
		};
	}
	
	static <S> MonitoredDeque<S> newStack() {
		return new MonitoredDeque<S>() {
			@Override
			S poll() {
				return queue.pollFirst();
			}
		};
	}

	final Deque<B> queue = new ConcurrentLinkedDeque<>();

	private final AtomicInteger totalCount = new AtomicInteger();
	
	abstract B poll();
	
	void enqueue(final B b) {
		queue.addFirst(b);
		totalCount.incrementAndGet();
	}
	
	int queuedCount() {
		return queue.size();
	}
	
	int totalCount() {
		return totalCount.get();
	}

}
