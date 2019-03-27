package euclid.kpi;

@FunctionalInterface
public interface KpiConsumer {
	
	public void consume(final KpiReport report);

}
