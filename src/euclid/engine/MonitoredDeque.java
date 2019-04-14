package euclid.engine;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

abstract class MonitoredDeque<B> {
	
	static <S> MonitoredDeque<S> newQueue() {
		return new MonitoredDeque<S>() {
			@Override
			S poll() {
				return deque.pollLast();
			}
		};
	}
	
	static <S> MonitoredDeque<S> newStack() {
		return new MonitoredDeque<S>() {
			@Override
			S poll() {
				return deque.pollFirst();
			}
		};
	}

	final Deque<B> deque = new ConcurrentLinkedDeque<>();

	private final AtomicLong totalCount = new AtomicLong();
	
	abstract B poll();
	
	void enqueue(final B b) {
		deque.addFirst(b);
		totalCount.incrementAndGet();
	}
	
	int queuedCount() {
		return deque.size();
	}
	
	long totalCount() {
		return totalCount.get();
	}

}
