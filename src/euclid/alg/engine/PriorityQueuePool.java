package euclid.alg.engine;

import java.util.ArrayList;
import java.util.List;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class PriorityQueuePool<B> implements KpiReporter{
	
	private final List<MonitoredQueue<B>> queues;
	
	PriorityQueuePool(final int queueCount) {
		queues = new ArrayList<>(queueCount);
		for(int i = 0; i < queueCount; i++) {
			queues.add(new MonitoredQueue<>());
		}
	}
	
	B poll() {
		for(final MonitoredQueue<B> queue : queues) {
			final B b = queue.poll();
			if(b != null) {
				return b;
			}
		}
		return null;
	}
	
	void enqueue(final B b, final int priority) {
		if(priority < 0 || priority >= queues.size()) {
			final String message = String.format("illegal priority %d for element: %s", priority, b);
			throw new IndexOutOfBoundsException(message);
		}
		queues.get(priority).enqueue(b);
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		for(int i = 0; i < queues.size(); i++) {
			final MonitoredQueue<?> queue = queues.get(i);
			final int queuedCount = queue.queuedCount();
			final int totalCount = queue.totalCount();
			collector.add("dequeued-" + i, totalCount - queuedCount);
			collector.add("queued-" + i, queuedCount);
			collector.add("total-" + i, totalCount);
		}
	}

}
