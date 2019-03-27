package euclid.kpi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class KpiCsvWriter implements KpiConsumer {
	
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	
	private final File csvFile;
	
	public KpiCsvWriter() {
		final String fileName = String.format("log/kpi-%s.csv", 
				new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
		csvFile = new File(fileName);
	}

	@Override
	public void consume(final KpiReport report) {
		try {
			if(!csvFile.exists()) {
				csvFile.getParentFile().mkdirs();
				final String header = report.items()
						.map(KpiItem::name)
						.collect(toCsvLine());
				Files.write(csvFile.toPath(), header.getBytes(UTF_8), StandardOpenOption.CREATE_NEW);
			}
			final String csvLine = report.items()
					.map(KpiItem::value)
					.map(Number::toString)
					.collect(toCsvLine());
			Files.write(csvFile.toPath(), csvLine.getBytes(UTF_8), StandardOpenOption.APPEND);
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

	}
	
	private static Collector<CharSequence, ?, String> toCsvLine () {
		return Collectors.joining(",", "", "\r\n");
	}

}
