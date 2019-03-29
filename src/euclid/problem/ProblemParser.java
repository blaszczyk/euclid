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

import static euclid.model.Sugar.*;
import euclid.model.*;
import euclid.problem.Problem.AlgorithmType;

public class ProblemParser {

	private static final String KEY_INITIAL = "initial";
	private static final String KEY_REQUIRED = "required";
	private static final String KEY_MAX_DEPTH = "maxdepth";
	private static final String KEY_ALGORITHM = "algorithm";
	private static final String KEY_FIND_ALL = "findall";
	
	private static final List<String> KEYWORDS = Arrays.asList(KEY_INITIAL, KEY_MAX_DEPTH, KEY_ALGORITHM, KEY_FIND_ALL);
	
	private static final String NUM_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\$]+)";
	private static final Pattern POINT_PATTERN = Pattern.compile(
			"(p\\()" // declaration
			+ NUM_PTRN // number
			+ "(\\,)" // separator
			+ NUM_PTRN // number
			+ "(\\))"); // close

	private static final String PT_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\$\\,]+)";
	private static final Pattern CURVE_PATTERN = Pattern.compile(
			"([cl]\\()" // declaration
			+ PT_PTRN // point
			+ "(\\;)" // separator
			+ PT_PTRN // point
			+ "(\\))"); // close

	private final Map<String, Constructable> constants = new HashMap<>();
	private final Map<String, Point> points = new HashMap<>();
	private final Map<String, Curve> curves = new HashMap<>();
	
	private final Algebra algebra;
	
	private final Map<String, String> keyValues;
	
	public ProblemParser(final Algebra algebra, final File file) {
		this.algebra = algebra;
		try {
			final List<String> lines = Files.readAllLines(file.toPath());
			keyValues = keyValues(lines);
		}
		catch (IOException e) {
			throw new ProblemParserException(e, "error reading file '%': '%s'", file, e.getMessage());
		}
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
		final Collection<Board> required = parseRequired();

		final int maxDepth = Integer.parseInt(getValue(KEY_MAX_DEPTH));
		final AlgorithmType algorithm = AlgorithmType.valueOf(getValue(KEY_ALGORITHM).toUpperCase());
		final boolean findAll = Boolean.parseBoolean(getValue(KEY_FIND_ALL));

		return new Problem(initial, required, maxDepth, findAll, algorithm);
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
			if(isKeyword(key)) {
				continue;
			}
			final String value = getValue(key);
			if(value.matches("p\\(.*")) {
				points.put(key, parsePoint(value));
			}
			else if(value.matches("[cl]\\(.*")) {
				curves.put(key, parseCurve(value));
			}
			else {
				constants.put(key, parseConstant(value));
			}
		}
	}
	
	private static boolean isKeyword(final String key) {
		return KEYWORDS.contains(key) || key.startsWith(KEY_REQUIRED);
	}
	
	private Collection<Board> parseRequired() {
		final List<Board> required = new ArrayList<>();
		for(final String key : keys()) {
			if(key.startsWith(KEY_REQUIRED)) {
				required.add(parseBoard(getValue(key)));
			}
		}
		return required;
	}
	
	private Board parseBoard(final String values) {
		if(values == null || values.isEmpty()) {
			return Board.withPoints().andCurves();
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
			final boolean isLine = matcher.group(1).charAt(0) == 'l';
			final Point x = parsePoint(matcher.group(2));
			final Point y = parsePoint(matcher.group(4));
			final Curve curve = isLine ? algebra.line(x,y) :  algebra.circle(x,y);
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
			final Constructable x = parseConstant(matcher.group(2));
			final Constructable y = parseConstant(matcher.group(4));
			final Point point = point(x,y);
			cacheByValue(point, value, points);
			return point;
		}
		else {
			throw new ProblemParserException("invalid or unknown point: '%s'", value);
		}
	}

	private Constructable parseConstant(final String value) {
		if(constants.containsKey(value)) {
			return constants.get(value);
		}
		try {
			final Calculator calculator = new Calculator(this::lookUpConstant);
			final double numValue = calculator.evaluate(value);
			final Constructable number = number(numValue);
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
