package euclid.cli;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CliParameters {

	private static final String KEY_ERROR = "error";

	private static final String KEY_FILE = "file";

	private static final String KEY_KPI_INTERVAL = "kpiinterval";
	
	static CliParameters parseAndValidate(final String[] args) {
		final Map<String, String> keyValues = parseKeyValues(args);
		if(keyValues.containsKey(KEY_ERROR)) {
			return failure("%s", keyValues.get(KEY_ERROR));
		}

		final String fileName = keyValues.get(KEY_FILE);
		if(fileName == null) {
			return failure("file name not specified");
		}
		final File file = new File(fileName);
		if(!file.exists()) {
			return failure("file '%s' does not exist%n", fileName);
		}
		
		final String kpiIntervalString = keyValues.get(KEY_KPI_INTERVAL);
		final int kpiInterval = kpiIntervalString == null ? 5000 : Integer.parseInt(kpiIntervalString);
		
		return new CliParameters(file, kpiInterval);
	}
	
	private static Map<String, String> parseKeyValues(final String[] args) {
		final Map<String, String> keyValues = new HashMap<>();
		String lastKey = null;
		for(final String arg : args) {
			if(arg.startsWith("-")) {
				if(lastKey == null) {
					lastKey = arg;
				}
				else {
					return keyValueFailure("value missing for '%s'", lastKey);
				}
			}
			else {
				if(lastKey == null) {
					return keyValueFailure("key missing for '%s'", arg);
				}
				else {
					keyValues.put(lastKey.substring(1).toLowerCase(), arg);
					lastKey = null;
				}
			}
		}
		if(lastKey != null) {
			return keyValueFailure("value missing for '%s'", lastKey);
		}
		return keyValues;
	}
	
	private static CliParameters failure(final String message, final Object... args) {
		return new CliParameters(String.format(message, args));
	}
	
	private static Map<String,String> keyValueFailure(final String message, final Object... args) {
		return Collections.singletonMap(KEY_ERROR, String.format(message, args));
	}
	
	private String errorMessage = null;

	private File problemFile = null;
	
	private int kpiInterval;
	
	private CliParameters(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private CliParameters(final File problemFile, final int kpiInterval) {
		this.problemFile = problemFile;
		this.kpiInterval = kpiInterval;
	}
	
	boolean isValid() {
		return errorMessage == null;
	}
	
	String errorMessage() {
		return errorMessage;
	}

	File problemFile() {
		return problemFile;
	}

	int kpiInterval() {
		return kpiInterval;
	}

}
