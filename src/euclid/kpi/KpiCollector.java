package euclid.kpi;

@FunctionalInterface
public interface KpiCollector {
	
	public void add(final String key, final Number value);

}
