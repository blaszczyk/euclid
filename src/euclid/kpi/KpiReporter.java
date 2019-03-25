package euclid.kpi;

import java.util.Map;

@FunctionalInterface
public interface KpiReporter {

	public Map<String, Number> report();
	
}
