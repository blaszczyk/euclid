package euclid.cli;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliParameters {

	private final Map<String, String> keyValues;
	
	CliParameters(final String[] args) {
		keyValues = new HashMap<>();
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
	}
	
	int getIntValue(final String key, final int defaultValue) {
		final String value = getValue(key);
		return value != null ? Integer.parseInt(value) : defaultValue;
	}
	
	List<String> getValues(final String key, final String... defaultValues) {
		final String value = getValue(key);
		return Arrays.asList(value != null ? value.split("\\s+") : defaultValues);
	}
	
	boolean getBooleanValue(final String key, final boolean defaultValue) {
		final String value = getValue(key);
		return value != null ? (value.equals("") || Boolean.parseBoolean(value)) : defaultValue;
	}

	File getFileValue(final String key) {
		final String value = getValue(key);
		if(value == null) {
			return null;
		}
		return new File(value);
	}
	
	String getValue(final String key) {
		return keyValues.get(key);
	}
	
}
