package euclid.problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static euclid.model.Sugar.*;
import euclid.model.*;
import euclid.problem.Problem.Algorithm;

public class ProblemParser {
	
	public static Problem parse(final File file) {
		return new ProblemParser().parseProblem(file);
	}

	private static final String KEY_INIT_POINTS = "initialpoints";
	private static final String KEY_INIT_CURVES = "initialcurves";
	private static final String KEY_REQ_POINTS = "requiredpoints";
	private static final String KEY_REQ_CURVES = "requiredcurves";
	private static final String KEY_MAX_DEPTH = "maxdepth";
	private static final String KEY_ALGORITHM = "algorithm";
	private static final String KEY_FIND_ALL = "findall";
	
	private static final List<String> KEYWORDS = Arrays.asList(KEY_INIT_POINTS, KEY_INIT_CURVES, KEY_REQ_POINTS, KEY_REQ_CURVES, KEY_MAX_DEPTH, KEY_ALGORITHM, KEY_FIND_ALL);
	
	private static final String NUM_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)]+)";
	private static final Pattern POINT_PATTERN = Pattern.compile(
			"(p\\()" // declaration
			+ NUM_PTRN // number
			+ "(\\,)" // separator
			+ NUM_PTRN // number
			+ "(\\))"); // close

	private static final String PT_PTRN = "([\\w\\.\\+\\-\\*\\/\\(\\)\\,]+)";
	private static final Pattern CURVE_PATTERN = Pattern.compile(
			"([cl]\\()" // declaration
			+ PT_PTRN // point
			+ "(\\;)" // separator
			+ PT_PTRN // point
			+ "(\\))"); // close

	private final Map<String, Constructable> constants = new HashMap<>();
	private final Map<String, Point> points = new HashMap<>();
	private final Map<String, Curve> curves = new HashMap<>();
	
	private Problem parseProblem(final File file) {
		try {
			final List<String> lines = Files.readAllLines(file.toPath());
			final Problem problem = parseProblem(lines);
			return problem;
		}
		catch (IOException e) {
			throw new ProblemParserException(e, "error reading file '%': '%s'", file, e.getMessage());
		}
	}

	private Problem parseProblem(final List<String> lines) {
		return parseProblem(keyValues(lines));
	}
	
	private Map<String, String> keyValues(final List<String> lines) {
		final Map<String, String> keyValues = new LinkedHashMap<>();
		for(final String l : lines) {
			final String line = l.replaceAll("\\s+|#.*", "");
			if(line.contains("=")) {
				final String[] split = line.split("\\=", 2);
				final String key = split[0].toLowerCase();
				final String value = split[1];
				keyValues.put(key, value);
			}
		}
		return keyValues;
	}
	
	private Problem parseProblem(final Map<String,String> keyValues) {
		validateKeywords(keyValues);
		parseVariables(keyValues);
		
		final PointSet initPoints = parsePoints(keyValues.get(KEY_INIT_POINTS));
		final CurveSet initCurves = parseCurves(keyValues.get(KEY_INIT_CURVES));
		final PointSet reqPoints = parsePoints(keyValues.get(KEY_REQ_POINTS));
		final CurveSet reqCurves = parseCurves(keyValues.get(KEY_REQ_CURVES));
		final int maxDepth = Integer.valueOf(keyValues.get(KEY_MAX_DEPTH));
		final Algorithm algorithm = Algorithm.valueOf(keyValues.get(KEY_ALGORITHM).toUpperCase());
		final boolean findAll = Boolean.parseBoolean(keyValues.get(KEY_FIND_ALL));
		
		return new Problem(Board.withPoints(initPoints).andCurves(initCurves),
				Board.withPoints(reqPoints).andCurves(reqCurves), maxDepth, findAll, algorithm);
	}
	
	private void validateKeywords(final Map<String, String> keyValues) {
		final Set<String> missingKeys = new HashSet<>(KEYWORDS);
		missingKeys.removeAll(keyValues.keySet());
		if(!missingKeys.isEmpty()) {
			throw new ProblemParserException("mandatory keys are missing: %s", missingKeys);
		}
	}

	private void parseVariables(final Map<String,String> keyValues) {
		for(final String key : keyValues.keySet()) {
			if(KEYWORDS.contains(key)) {
				continue;
			}
			final String value = keyValues.get(key);
			if(value.isEmpty())
				throw new ProblemParserException("empty value for variable '%s'", key);
			final char firstChar = value.toLowerCase().charAt(0);
			if(firstChar == 'p') {
				points.put(key, parsePoint(value));
			}
			else if(firstChar == 'c' || firstChar == 'l') {
				curves.put(key, parseCurve(value));
			}
			else {
				constants.put(key, parseConstant(value));
			}
		}
	}

	private CurveSet parseCurves(final String values) {
		if(values == null || values.isEmpty()) {
			return CurveSet.empty();
		}
		final String[] split = values.split("\\:");
		final CurveSet curves = CurveSet.create();
		for(final String value : split) {
			curves.add(parseCurve(value));
		}
		return curves;
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
			final Curve curve = isLine ? l(x,y) :  c(x,y);
			curves.put(value, curve);
			return curve;
		}
		else {
			throw new ProblemParserException("invalid or unknown curve:'%s'", value);
		}
	}

	private PointSet parsePoints(final String values) {
		if(values == null || values.isEmpty()) {
			return PointSet.empty();
		}
		final String[] split = values.split("\\:");
		final PointSet points = PointSet.create();
		for(final String value : split) {
			points.add(parsePoint(value));
		}
		return points;
	}

	private Point parsePoint(final String value) {
		if(points.containsKey(value)) {
			return points.get(value);
		}
		final Matcher matcher = POINT_PATTERN.matcher(value);
		if(matcher.matches()) {
			final Constructable x = parseConstant(matcher.group(2));
			final Constructable y = parseConstant(matcher.group(4));
			final Point point = p(x,y);
			points.put(value, point);
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
			final double numValue = Calculator.evaluate(value);
			final Constructable number = n(numValue);
			constants.put(value, number);
			return number;
		}
		catch (Exception e) {
			throw new ProblemParserException(e, "error parsing numerical value '%s': %s", value, e.getMessage());
		}
	}
	
	@SuppressWarnings("serial")
	public static class ProblemParserException extends RuntimeException {
		
		public ProblemParserException(final String format, final Object... args) {
			super(String.format(format, args));
		}
		
		public ProblemParserException(final Throwable cause, final String format, final Object... args) {
			super(String.format(format, args), cause);
		}
	}

}
