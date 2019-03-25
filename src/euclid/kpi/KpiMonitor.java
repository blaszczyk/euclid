package euclid.kpi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KpiMonitor extends Thread implements KpiReporter {
	
	private final List<KpiReporter> reporters = new ArrayList<>();
	
	private final long interval;
	
	private boolean halt = false;
	
	private long startTime;
	
	public KpiMonitor(final long interval) {
		this.interval = interval;
		reporters.add(this);
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
				fetchReport();
			}
		}
	}
	
	private void fetchReport() {
		final String report = reporters.stream()
			.map(KpiReporter::report)
			.map(Map::entrySet)
			.flatMap(Set::stream)
			.map(KpiMonitor::toString)
			.collect(Collectors.joining(", "));
		System.out.println(report);
	}
	
	private static String toString(final Map.Entry<String, Number> e) {
		return String.format("%s: %,d", e.getKey(), e.getValue().longValue());
	}

	@Override
	public Map<String, Number> report() {
		final long runtime =  System.currentTimeMillis() - startTime;
		return Collections.singletonMap("runtime", runtime);
	}

}
