package euclid.kpi;

@FunctionalInterface
public interface KpiReporter {

	public void fetchReport(final KpiCollector collector);
	
}
