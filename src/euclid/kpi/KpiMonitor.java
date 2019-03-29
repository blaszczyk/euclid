package euclid.kpi;

import java.util.ArrayList;
import java.util.List;

public class KpiMonitor {
	
	private final List<KpiReporter> reporters = new ArrayList<>();
	
	private final List<KpiConsumer> consumers = new ArrayList<>();
	
	private final Thread thread;
	
	private final long interval;
	
	private boolean halt = false;
	
	private long startTime;

	public KpiMonitor(final long interval) {
		this.interval = interval;
		reporters.add(c -> c.add("runtime", System.currentTimeMillis() - startTime));
		thread = new Thread(this::run, "kpi-monitor");
	}
	
	public void addReporter(final KpiReporter reporter) {
		reporters.add(reporter);
	}
	
	public void addConsumer(final KpiConsumer consumer) {
		consumers.add(consumer);
	}
	
	public void start() {
		if(consumers.isEmpty()) {
			return;
		}
		startTime = System.currentTimeMillis();
		thread.start();
	}

	public void halt() {
		halt = true;
	}

	private void run() {
		while(!halt) {
			try {
				Thread.sleep(interval);
				fetchAndProcessReport();
			}
			catch(InterruptedException e) {
			}
		}
	}
	
	private void fetchAndProcessReport() {
		final KpiReport report = new KpiReport();
		for(final KpiReporter reporter : reporters) {
			reporter.fetchReport(report::add);
		}
		for(final KpiConsumer consumer : consumers) {
			consumer.consume(report);
		}
	}

}
