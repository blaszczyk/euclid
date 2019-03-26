package euclid.alg.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, Number> report() {
		final Map<String, Number> report = new LinkedHashMap<>();
		for(int i = 0; i < queues.size(); i++) {
			final MonitoredQueue<?> queue = queues.get(i);
			final int queuedCount = queue.queuedCount();
			final int totalCount = queue.totalCount();
			report.put("dequeued-" + i, totalCount - queuedCount);
			report.put("queued-" + i, queuedCount);
			report.put("total-" + i, totalCount);
		}
		return report;
	}

}
