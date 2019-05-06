package euclid.problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import euclid.algorithm.AlgorithmType.ConstructorType;
import euclid.algorithm.AlgorithmType.CurveIdentification;
import euclid.algorithm.AlgorithmType.PriorityType;
import euclid.geometry.Curve;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.sets.*;

public class ProblemParser {

	public static final String KEY_INITIAL = "initial";
	public static final String KEY_REQUIRED = "required";
	public static final String KEY_ASSIST = "assist";
	public static final String KEY_MAX_DEPTH = "maxdepth";
	public static final String KEY_DEPTH_FIRST = "depthfirst";
	public static final String KEY_SHUFFLE = "shuffle";
	public static final String KEY_MAX_SOLUTIONS = "maxsolutions";
	public static final String KEY_ALGORITHM = "algorithm";
	public static final String KEY_CONSTRUCTOR = "constructor";
	public static final String KEY_PRIORITY = "priority";
	public static final String KEY_CURVE_IDENTIFIACTION = "curveidentification";
	
	private static final List<String> KEYWORDS = Arrays.asList(KEY_INITIAL, KEY_REQUIRED, KEY_ASSIST, KEY_MAX_DEPTH, KEY_DEPTH_FIRST,
			KEY_SHUFFLE, KEY_ALGORITHM, KEY_CONSTRUCTOR, KEY_PRIORITY, KEY_CURVE_IDENTIFIACTION, KEY_MAX_SOLUTIONS);
	
	private static final String NUM_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\$\\^]+)";
	private static final Pattern POINT_PATTERN = Pattern.compile(
			"(p\\()" // declaration
			+ NUM_PTRN // number
			+ "(\\,)" // separator
			+ NUM_PTRN // number
			+ "(\\))"); // close

	private static final String PT_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\$\\^\\,]+)";
	private static final Pattern CURVE_PATTERN = Pattern.compile(
			"([clrs]\\()" // declaration
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
		final Board assist = parseBoard(getValue(KEY_ASSIST));

		final int maxDepth = Integer.parseInt(getValue(KEY_MAX_DEPTH));
		final boolean depthFirst = Boolean.parseBoolean(getValue(KEY_DEPTH_FIRST));
		final boolean shuffle = Boolean.parseBoolean(getValue(KEY_SHUFFLE));
		final AlgorithmType algorithm = AlgorithmType.valueOf(getValue(KEY_ALGORITHM).toUpperCase());
		final ConstructorType constructor = ConstructorType.valueOf(getValue(KEY_CONSTRUCTOR).toUpperCase());
		final PriorityType priority = PriorityType.valueOf(getValue(KEY_PRIORITY).toUpperCase());
		final CurveIdentification curveIdentification = CurveIdentification.valueOf(getValue(KEY_CURVE_IDENTIFIACTION).toUpperCase());
		final int maxSolutions = Integer.parseInt(getValue(KEY_MAX_SOLUTIONS));

		return new Problem(initial, required, assist, maxDepth, depthFirst, shuffle, maxSolutions, algorithm, constructor, priority, curveIdentification);
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
			if(isPoint(value)) {
				points.put(key, parsePoint(value));
			}
			else if(isCurve(value)) {
				curves.put(key, parseCurve(value));
			}
			else {
				constants.put(key, parseConstant(value, false));
			}
		}
	}
	
	private Board parseBoard(final String values) {
		if(values == null || values.isEmpty()) {
			return Board.EMPTY;
		}
		final String[] split = values.split("\\:");
		final PointSet ps = new PointSet();
		final CurveSet cs = new CurveSet();
		for(final String value : split) {
			if(isCurve(value)) {
				cs.add(parseCurve(value));
			}
			else {
				ps.add(parsePoint(value));
			}
		}
		return new RootBoard(ps, cs);
	}

	private boolean isPoint(final String value) {
		return value.matches("p\\(.*") || points.containsKey(value);
	}
	
	private boolean isCurve(final String value) {
		return value.matches("[clrs]\\(.*") || curves.containsKey(value);
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
			final Curve curve = createCurve(type, x, y);
			cacheByValue(curve, value, curves);
			return curve;
		}
		else {
			throw new ProblemParserException("invalid or unknown curve:'%s'", value);
		}
	}
	
	private static Curve createCurve(final char type, final Point x, final Point y) {
		switch(type) {
		case 'l':
			return Algebra.line(x, y);
		case 'r':
			return Algebra.ray(x, y);
		case 's':
			return Algebra.segment(x, y);
		case 'c':
			return Algebra.circle(x, y);
		}
		throw new IllegalArgumentException("illegal curve type " + type);
	}

	private Point parsePoint(final String value) {
		if(points.containsKey(value)) {
			return points.get(value);
		}
		final Matcher matcher = POINT_PATTERN.matcher(value);
		if(matcher.matches()) {
			final Number x = parseConstant(matcher.group(2), false);
			final Number y = parseConstant(matcher.group(4), false);
			final Point point = new Point(x,y);
			cacheByValue(point, value, points);
			return point;
		}
		else {
			final Number x = parseConstant(value, true);
			if(x != null) {
				return new Point(x, Number.ZERO);
			}
			else {
				throw new ProblemParserException("invalid or unknown point: '%s'", value);
			}
		}
	}

	private Number parseConstant(final String value, final boolean silent) {
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
		catch (final Exception e) {
			if(silent) {
				return null;
			}
			else {
				throw new ProblemParserException(e, "error parsing numerical value '%s': %s", value, e.getMessage());
			}
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
