package euclid.engine;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

abstract class MonitoredDeque<B> {
	
	enum State {
		EMPTY,
		ACTIVE,
		LOCKED;
	};
	
	static <S> MonitoredDeque<S> newQueue(final int maxQueueSize) {
		return new MonitoredDeque<S>(maxQueueSize) {
			@Override
			List<S> pollIntern() {
				return deque.pollFirst();
			}
		};
	}
	
	static <S> MonitoredDeque<S> newStack(final int maxQueueSize) {
		return new MonitoredDeque<S>(maxQueueSize) {
			@Override
			List<S> pollIntern() {
				return deque.pollLast();
			}
		};
	}

	final Deque<List<B>> deque = new ConcurrentLinkedDeque<>();

	private final AtomicLong totalCount = new AtomicLong();

	private final AtomicLong dequeuedCount = new AtomicLong();

	private final AtomicLong rejectedCount = new AtomicLong();
	
	private State state = State.EMPTY;
	
	private final int maxQueueSize;

	MonitoredDeque(final int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	List<B> poll() {
		if(state == State.EMPTY) {
			return null;
		}
		final List<B> next = pollIntern();
		if(next != null) {
			dequeuedCount.addAndGet(next.size());
		}
		else {
			state = State.EMPTY;
		}
		return next;
	}
	
	abstract List<B> pollIntern();

	void enqueue(final List<B> list) {
		if(state == State.LOCKED) {
			rejectedCount.addAndGet(list.size());
		}
		else {
			deque.add(list);
			final long total = totalCount.addAndGet(list.size());
			final boolean lock = total - dequeuedCount() > maxQueueSize;
			state = lock ? State.LOCKED : State.ACTIVE;
		}
	}
	
	long dequeuedCount() {
		return dequeuedCount.get();
	}
	
	long totalCount() {
		return totalCount.get();
	}
	
	long rejectedCount() {
		return rejectedCount.get();
	}

}
