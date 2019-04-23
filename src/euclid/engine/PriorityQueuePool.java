package euclid.engine;

import java.util.ArrayList;
import java.util.List;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class PriorityQueuePool<B> implements KpiReporter{
	
	private final List<MonitoredDeque<B>> queues;
	
	private final int bunchSize;
	
	PriorityQueuePool(final int queueCount, final boolean stack, final int bunchSize, final int maxQueueSize) {
		this.bunchSize = bunchSize;
		queues = new ArrayList<>(queueCount);
		for(int i = 0; i < queueCount; i++) {
			queues.add(stack ? MonitoredDeque.newStack(maxQueueSize) : MonitoredDeque.newQueue(maxQueueSize));
		}
	}

	void enqueue(final PrioritizedGeneration<B> generation) {
		for(int priority = 0; priority < queues.size(); priority++) {
			final List<B> list = generation.get(priority);
			final int listSize = list.size();
			if(listSize > 0) {
				for(int i = 0; i < listSize; i += bunchSize) {
					final int toIndex = Math.min(i + bunchSize, listSize);
					final List<B> bunch = list.subList(i, toIndex);
					queues.get(priority).enqueue(bunch);
				}
			}
		}
	}
	
	List<B> poll() {
		for(final MonitoredDeque<B> queue : queues) {
			final List<B> b = queue.poll();
			if(b != null) {
				return b;
			}
		}
		return null;
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		for(int i = 0; i < queues.size(); i++) {
			final MonitoredDeque<?> queue = queues.get(i);
			final long dequeuedCount = queue.dequeuedCount();
			final long totalCount = queue.totalCount();
			collector.add("dequeued-" + i, dequeuedCount);
			collector.add("queued-" + i, totalCount - dequeuedCount);
			collector.add("total-" + i, totalCount);
			collector.add("rejected-" + i, queue.rejectedCount());
		}
	}
	
	void cleanUp() {
		queues.clear();
	}

}
