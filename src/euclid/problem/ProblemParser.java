package euclid.problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import euclid.algebra.Algebra;
import euclid.algorithm.AlgorithmType;
import euclid.geometry.Curve;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.sets.Board;

public class ProblemParser {

	public static final String KEY_INITIAL = "initial";
	public static final String KEY_REQUIRED = "required";
	public static final String KEY_MAX_DEPTH = "maxdepth";
	public static final String KEY_DEPTH_FIRST = "depthfirst";
	public static final String KEY_MAX_SOLUTIONS = "maxsolutions";
	public static final String KEY_ALGORITHM = "algorithm";
	
	private static final List<String> KEYWORDS = Arrays.asList(KEY_INITIAL, KEY_REQUIRED, KEY_MAX_DEPTH, KEY_DEPTH_FIRST,
			KEY_ALGORITHM, KEY_MAX_SOLUTIONS);
	
	private static final String NUM_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\$]+)";
	private static final Pattern POINT_PATTERN = Pattern.compile(
			"(p\\()" // declaration
			+ NUM_PTRN // number
			+ "(\\,)" // separator
			+ NUM_PTRN // number
			+ "(\\))"); // close

	private static final String PT_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\$\\,]+)";
	private static final Pattern CURVE_PATTERN = Pattern.compile(
			"([cls]\\()" // declaration
			+ PT_PTRN // point
			+ "(\\;)" // separator
			+ PT_PTRN // point
			+ "(\\))"); // close

	private final Map<String, Number> constants = new HashMap<>();
	private final Map<String, Point> points = new HashMap<>();
	private final Map<String, Curve> curves = new HashMap<>();
	
	private final Map<String, String> keyValues;
	
	public ProblemParser(final File file) {
		try {
			final List<String> lines = Files.readAllLines(file.toPath());
			keyValues = keyValues(lines);
		}
		catch (IOException e) {
			throw new ProblemParserException(e, "error reading file '%': '%s'", file, e.getMessage());
		}
	}
	
	public ProblemParser(final List<String> lines) {
		keyValues = keyValues(lines);
	}
	
	private static Map<String, String> keyValues(final List<String> lines) {
		final Map<String, String> keyValues = new LinkedHashMap<>();
		for(final String l : lines) {
			final String line = l.replaceAll("\\s+|#.*", "");
			if(line.contains("=")) {
				final String[] split = line.split("\\=", 2);
				final String key = split[0].toLowerCase();
				final String value = split[1];
				if(keyValues.containsKey(key)) {
					throw new ProblemParserException("duplicate key: '%s'", key);
				}
				keyValues.put(key, value);
			}
		}
		return keyValues;
	}
	
	public Problem parse() {
		validateKeywords();
		parseVariables();

		final Board initial = parseBoard(getValue(KEY_INITIAL));
		final Board required = parseBoard(getValue(KEY_REQUIRED));

		final int maxDepth = Integer.parseInt(getValue(KEY_MAX_DEPTH));
		final boolean depthFirst = Boolean.parseBoolean(getValue(KEY_DEPTH_FIRST));
		final AlgorithmType algorithm = AlgorithmType.valueOf(getValue(KEY_ALGORITHM).toUpperCase());
		final int maxSolutions = Integer.parseInt(getValue(KEY_MAX_SOLUTIONS));

		return new Problem(initial, required, maxDepth, depthFirst, maxSolutions, algorithm);
	}

	private void validateKeywords() {
		final Set<String> missingKeys = new HashSet<>(KEYWORDS);
		missingKeys.removeAll(keys());
		if(!missingKeys.isEmpty()) {
			throw new ProblemParserException("mandatory keys are missing: %s", missingKeys);
		}
	}

	private void parseVariables() {
		for(final String key : keys()) {
			if(KEYWORDS.contains(key)) {
				continue;
			}
			final String value = getValue(key);
			if(value.matches("p\\(.*")) {
				points.put(key, parsePoint(value));
			}
			else if(value.matches("[cls]\\(.*")) {
				curves.put(key, parseCurve(value));
			}
			else {
				constants.put(key, parseConstant(value));
			}
		}
	}
	
	private Board parseBoard(final String values) {
		if(values == null || values.isEmpty()) {
			return Board.EMPTY;
		}
		final String[] split = values.split("\\:");
		final List<Curve> cs = new ArrayList<>();
		final List<Point> ps = new ArrayList<>();
		for(final String value : split) {
			if(isPoint(value)) {
				ps.add(parsePoint(value));
			}
			else {
				cs.add(parseCurve(value));
			}
		}
		return Board.withPoints(ps).andCurves(cs);
	}

	private boolean isPoint(final String value) {
		return value.toLowerCase().startsWith("p(") || points.containsKey(value);
	}

	private Curve parseCurve(final String value) {
		if(curves.containsKey(value)) {
			return curves.get(value);
		}
		final Matcher matcher = CURVE_PATTERN.matcher(value);
		if(matcher.matches()) {
			final char type = matcher.group(1).charAt(0);
			final Point x = parsePoint(matcher.group(2));
			final Point y = parsePoint(matcher.group(4));
			final Curve curve = (type == 'l') ? Algebra.line(x,y) : 
				( (type == 's') ? Algebra.segment(x, y) : Algebra.circle(x,y) );
			cacheByValue(curve, value, curves);
			return curve;
		}
		else {
			throw new ProblemParserException("invalid or unknown curve:'%s'", value);
		}
	}

	private Point parsePoint(final String value) {
		if(points.containsKey(value)) {
			return points.get(value);
		}
		final Matcher matcher = POINT_PATTERN.matcher(value);
		if(matcher.matches()) {
			final Number x = parseConstant(matcher.group(2));
			final Number y = parseConstant(matcher.group(4));
			final Point point = new Point(x,y);
			cacheByValue(point, value, points);
			return point;
		}
		else {
			throw new ProblemParserException("invalid or unknown point: '%s'", value);
		}
	}

	private Number parseConstant(final String value) {
		if(constants.containsKey(value)) {
			return constants.get(value);
		}
		try {
			final Calculator calculator = new Calculator(this::lookUpConstant);
			final double numValue = calculator.evaluate(value);
			final Number number = new Number(numValue);
			cacheByValue(number, value, constants);
			return number;
		}
		catch (Exception e) {
			throw new ProblemParserException(e, "error parsing numerical value '%s': %s", value, e.getMessage());
		}
	}
	
	private Optional<Double> lookUpConstant(final String key) {
		final String keyLower = key.toLowerCase();
		if(constants.containsKey(keyLower)) {
			return Optional.of(constants.get(keyLower).doubleValue());
		}
		else {
			return Optional.empty();
		}
	}
	
	private static <E> void cacheByValue(final E element, final String value, final Map<String,E> cache) {
		if(!value.contains("rand")) {
			cache.put(value, element);
		}
	}
	
	private String getValue(final String key) {
		return keyValues.get(key);
	}
	
	private Collection<String> keys() {
		return keyValues.keySet();
	}

}
