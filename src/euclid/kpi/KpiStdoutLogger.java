package euclid.kpi;

import java.util.stream.Collectors;

public class KpiStdoutLogger implements KpiConsumer {

	@Override
	public void consume(final KpiReport report) {
		final String reportString = report.items()
				.map(KpiStdoutLogger::toString)
				.collect(Collectors.joining(", "));
		System.out.println(reportString);
	}
	
	private static String toString(final KpiItem item) {
		return String.format("%s: %,d", item.name(), item.value().longValue());
	}

}
