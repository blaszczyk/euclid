package euclid.engine;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

abstract class MonitoredDeque<B> {
	
	static <S> MonitoredDeque<S> newQueue() {
		return new MonitoredDeque<S>() {
			@Override
			List<S> pollIntern() {
				return deque.pollFirst();
			}
		};
	}
	
	static <S> MonitoredDeque<S> newStack() {
		return new MonitoredDeque<S>() {
			@Override
			List<S> pollIntern() {
				return deque.pollLast();
			}
		};
	}

	final Deque<List<B>> deque = new ConcurrentLinkedDeque<>();

	private final AtomicLong totalCount = new AtomicLong();

	private final AtomicLong dequeuedCount = new AtomicLong();

	List<B> poll() {
		final List<B> next = pollIntern();
		if(next != null) {
			dequeuedCount.addAndGet(next.size());
		}
		return next;
	}
	
	abstract List<B> pollIntern();

	void enqueue(final List<B> list) {
		deque.add(list);
		totalCount.addAndGet(list.size());
	}
	
	long dequeuedCount() {
		return dequeuedCount.get();
	}
	
	long totalCount() {
		return totalCount.get();
	}

}
