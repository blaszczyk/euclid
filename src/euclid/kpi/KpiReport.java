package euclid.kpi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class KpiReport implements Iterable<KpiItem>{

	private List<KpiItem> items = new LinkedList<>();
	
	KpiReport() {
	}
	
	void add(final String name, final Number value) {
		items.add(new KpiItem(name, value));
	}
	
	public Stream<KpiItem> items() {
		return items.stream();
	}

	@Override
	public Iterator<KpiItem> iterator() {
		return items.iterator();
	}

}
