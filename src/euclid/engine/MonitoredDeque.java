package euclid.engine;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

abstract class MonitoredDeque<B> {
	
	static <S> MonitoredDeque<S> newQueue() {
		return new MonitoredDeque<S>() {
			@Override
			S poll() {
				return deque.pollFirst();
			}
		};
	}
	
	static <S> MonitoredDeque<S> newStack() {
		return new MonitoredDeque<S>() {
			@Override
			S poll() {
				return deque.pollLast();
			}
		};
	}

	final Deque<B> deque = new ConcurrentLinkedDeque<>();

	private final AtomicLong totalCount = new AtomicLong();
	
	abstract B poll();
	
	void enqueue(final List<B> list) {
		deque.addAll(list);
		totalCount.addAndGet(list.size());
	}
	
	int queuedCount() {
		return deque.size();
	}
	
	long totalCount() {
		return totalCount.get();
	}

}
