package euclid.kpi;

public class SystemKpi implements KpiReporter {

	@Override
	public void fetchReport(final KpiCollector collector) {
		final Runtime runtime = Runtime.getRuntime();
		collector.add("free-memory", runtime.freeMemory());
		collector.add("total-memory", runtime.totalMemory());
	}

}
