package euclid.kpi;

import java.util.ArrayList;
import java.util.List;

public class KpiMonitor extends Thread {
	
	@FunctionalInterface
	public interface KpiReporter {
		public String report();
	}
	
	private final List<KpiReporter> reporters = new ArrayList<>();
	
	private final long interval;
	
	private boolean halt = false;
	
	private long startTime;
	
	public KpiMonitor(final long interval) {
		this.interval = interval;
	}
	
	public void addReporter(final KpiReporter reporter) {
		reporters.add(reporter);
	}

	public void halt() {
		halt = true;
	}
	
	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		while(!halt) {
			try {
				Thread.sleep(interval);
			}
			catch(InterruptedException e) {
			}
			if(!halt) {
				report();
			}
		}
	}
	
	private void report() {
		final long runtime =  System.currentTimeMillis() - startTime;
		log("runtime: " + runtime);
		reporters.stream().map(KpiReporter::report).forEach(KpiMonitor::log);
	}
	
	private static void log(final String message) {
		System.out.println(message);
	}

}
