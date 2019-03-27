package euclid.kpi;

public class KpiItem {
	
	private final String name;
	
	private final Number value;
	
	KpiItem(final String name, final Number value) {
		this.name = name;
		this.value = value;
	}
	
	public String name() {
		return name;
	}
	
	public Number value() {
		return value;
	}

}
