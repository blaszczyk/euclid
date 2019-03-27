package euclid.cli;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliParameterParser {

	private static final String KEY_FILE = "file";

	private static final String KEY_KPI_INTERVAL = "kpiinterval";
	private static final String KEY_KPI_WRITER = "kpiwriter";
	private static final String VAL_KPI_CSV = "csv";
	private static final String VAL_KPI_OUT = "out";

	private static final String KEY_CACHE = "cache";
	private static final String VAL_CACHE_CURVES = "curves";
	private static final String VAL_CACHE_INTERSECTIONS = "intersections";

	private final Map<String, String> keyValues;
	
	CliParameterParser(final String[] args) {
		keyValues = parseKeyValues(args);
	}
	
	CliParameter parse() {
		final String fileName = getValue(KEY_FILE, null);
		if(fileName == null) {
			failure("file name not specified");
		}
		final File file = new File(fileName);
		if(!file.exists()) {
			failure("file '%s' does not exist%n", fileName);
		}
		
		final int kpiInterval = getIntValue(KEY_KPI_INTERVAL, 5000);
		
		final List<String> kpiWriters = getValues(KEY_KPI_WRITER, VAL_KPI_CSV);
		final boolean kpiCsv = kpiWriters.contains(VAL_KPI_CSV);
		final boolean kpiOut = kpiWriters.contains(VAL_KPI_OUT);

		final List<String> caches = getValues(KEY_CACHE);
		final boolean cacheCurves = caches.contains(VAL_CACHE_CURVES);
		final boolean cacheIntersections = caches.contains(VAL_CACHE_INTERSECTIONS);

		return new CliParameter(file, kpiInterval, kpiCsv, kpiOut, cacheCurves, cacheIntersections);
	}
	
	private static Map<String, String> parseKeyValues(final String[] args) {
		final Map<String, String> keyValues = new HashMap<>();
		String lastKey = "_dummy";
		String lastValue = "";
		for(final String arg : args) {
			if(arg.startsWith("-")) {
				keyValues.put(lastKey, lastValue.replaceFirst("^\\s", ""));
				lastKey = arg.substring(1).replaceAll("[\\-\\_]", "").toLowerCase();
				lastValue = "";
			}
			else {
				lastValue += " " + arg;
			}
		}
		keyValues.put(lastKey, lastValue.replaceFirst("^\\s", ""));
		return keyValues;
	}

	private void failure(final String message, final Object... args) {
		throw new CliParameterParserExcepion(String.format(message, args));
	}
	
	private String getValue(final String key, final String defaultValue) {
		final String value = keyValues.get(key);
		return value != null ? value : defaultValue;
	}
	
	private int getIntValue(final String key, final int defaultValue) {
		final String value = keyValues.get(key);
		return value != null ? Integer.parseInt(value) : defaultValue;
	}
	
	private List<String> getValues(final String key, final String... defaultValues) {
		final String value = keyValues.get(key);
		return Arrays.asList(value != null ? value.split("\\s+") : defaultValues);
	}
	
}
