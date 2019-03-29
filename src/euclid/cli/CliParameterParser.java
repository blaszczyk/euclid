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
	
	private static final String KEY_THREAD_COUNT = "threads";
	private static final String KEY_HELP = "help";

	private final Map<String, String> keyValues;
	
	CliParameterParser(final String[] args) {
		keyValues = parseKeyValues(args);
	}
	
	CliParameter parse() {
		final File file = getFileValue(KEY_FILE, true, true);
		
		final int kpiInterval = getIntValue(KEY_KPI_INTERVAL, false, 5000);
		
		final List<String> kpiWriters = getValues(KEY_KPI_WRITER, false, VAL_KPI_CSV);
		final boolean kpiCsv = kpiWriters.contains(VAL_KPI_CSV);
		final boolean kpiOut = kpiWriters.contains(VAL_KPI_OUT);

		final List<String> caches = getValues(KEY_CACHE, false, VAL_CACHE_CURVES);
		final boolean cacheCurves = caches.contains(VAL_CACHE_CURVES);
		final boolean cacheIntersections = caches.contains(VAL_CACHE_INTERSECTIONS);
		
		final int threadCount = getIntValue(KEY_THREAD_COUNT, false, Runtime.getRuntime().availableProcessors());

		final boolean needsHelp = getBooleanValue(KEY_HELP, false, false);
		return new CliParameter(file, kpiInterval, kpiCsv, kpiOut, cacheCurves, cacheIntersections, threadCount, needsHelp);
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
	
	private int getIntValue(final String key, final boolean mandatory, final int defaultValue) {
		final String value = getValue(key,mandatory);
		return value != null ? Integer.parseInt(value) : defaultValue;
	}
	
	private List<String> getValues(final String key, final boolean mandatory, final String... defaultValues) {
		final String value = getValue(key,mandatory);
		return Arrays.asList(value != null ? value.split("\\s+") : defaultValues);
	}
	
	private boolean getBooleanValue(final String key, final boolean mandatory, final boolean defaultValue) {
		final String value = getValue(key,mandatory);
		return value != null ? (value.equals("") || Boolean.parseBoolean(value)) : defaultValue;
	}
	
	private File getFileValue(final String key, final boolean mandatory, final boolean mustExist) {
		final String value = getValue(key,mandatory);
		if(value == null) {
			return null;
		}
		final File file = new File(value);
		if(!file.exists() && mustExist) {
			throw new CliParameterParserExcepion("file '%s' does not exist", file);
		}
		return file;
	}
	
	private String getValue(final String key, final boolean mandatory) {
		final String value = keyValues.get(key);
		if(value == null && mandatory) {
			throw new CliParameterParserExcepion("missing mandatory key'%s'", key);
		}
		return value;
	}
	
}
