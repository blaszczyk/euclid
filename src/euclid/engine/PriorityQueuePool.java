package euclid.engine;

import java.util.ArrayList;
import java.util.List;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class PriorityQueuePool<B> implements KpiReporter{
	
	private final List<MonitoredDeque<B>> queues;
	
	PriorityQueuePool(final int queueCount, final boolean stack) {
		queues = new ArrayList<>(queueCount);
		for(int i = 0; i < queueCount; i++) {
			queues.add(stack ? MonitoredDeque.newStack() : MonitoredDeque.newQueue());
		}
	}
	
	B poll() {
		for(final MonitoredDeque<B> queue : queues) {
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
			final MonitoredDeque<?> queue = queues.get(i);
			final int queuedCount = queue.queuedCount();
			final long totalCount = queue.totalCount();
			collector.add("dequeued-" + i, totalCount - queuedCount);
			collector.add("queued-" + i, queuedCount);
			collector.add("total-" + i, totalCount);
		}
	}

}
