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

public class ProblemParser {
	
	public static Problem parse(final File file) {
		return new ProblemParser().parseProblem(file);
	}

	private static final String KEY_INIT_POINTS = "initialpoints";
	private static final String KEY_INIT_CURVES = "initialcurves";
	private static final String KEY_REQ_POINTS = "requiredpoints";
	private static final String KEY_REQ_CURVES = "requiredcurves";
	private static final String KEY_MAX_DEPTH = "maxdepth";
	
	private static final List<String> KEYWORDS = Arrays.asList(KEY_INIT_POINTS, KEY_INIT_CURVES, KEY_REQ_POINTS, KEY_REQ_CURVES, KEY_MAX_DEPTH);
	
	private static final Pattern POINT_DEF = Pattern.compile("(p\\()([\\w\\.\\-]+)(\\,)([\\w\\.\\-]+)(\\))");
	private static final Pattern CURVE_DEF = Pattern.compile("([cl])(\\()([\\w\\.\\-\\,]+)(\\-)([\\w\\.\\-\\,]+)(\\))");

	private final Map<String, Double> constants = new HashMap<>();
	private final Map<String, Point> points = new HashMap<>();
	private final Map<String, Curve> curves = new HashMap<>();
	
	private Problem parseProblem(final File file) {
		try {
			final List<String> lines = Files.readAllLines(file.toPath());
			final Problem problem = parseProblem(keyValues(lines));
			return problem;
		}
		catch (IOException e) {
			throw new ProblemParserException("error reading file '%': '%s'", file, e.getMessage());
		}
	}
	
	private Map<String, String> keyValues(final List<String> lines) {
		final Map<String, String> keyValues = new LinkedHashMap<>();
		for(final String l : lines) {
			final String line = removeWhitespaces(l);
			if(isComment(line)) {
				continue;
			}
			final String[] split = line.split("\\=", 2);
			final String key = split[0].toLowerCase();
			final String value = split[1];
			keyValues.put(key, value);
		}		
		return keyValues;
	}

	private String removeWhitespaces(final String line) {
		return line.replaceAll("\\s+", "");
	}

	private boolean isComment(final String line) {
		return line.isEmpty() || line.startsWith("#") || !line.contains("=");
	}
	
	private Problem parseProblem(final Map<String,String> keyValues) {
		parseVariables(keyValues);
		final Set<String> missingKeys = new HashSet<>(KEYWORDS);
		missingKeys.removeAll(keyValues.keySet());
		if(!missingKeys.isEmpty()) {
//			if(!keyValues.keySet().containsAll(KEYWORDS)) {
			throw new ProblemParserException("mandatory keys are missing: %s", missingKeys);
		}
		final PointSet initPoints = parsePoints(keyValues.get(KEY_INIT_POINTS));
		final CurveSet initCurves = parseCurves(keyValues.get(KEY_INIT_CURVES));
		final PointSet reqPoints = parsePoints(keyValues.get(KEY_REQ_POINTS));
		final CurveSet reqCurves = parseCurves(keyValues.get(KEY_REQ_CURVES));
		final int maxDepth = Integer.valueOf(keyValues.get(KEY_MAX_DEPTH));
		
		return new Problem(Board.withPoints(initPoints).andCurves(initCurves),
				Board.withPoints(reqPoints).andCurves(reqCurves), maxDepth);
	}
	
	private void parseVariables(final Map<String,String> keyValues) {
		for(final String key : keyValues.keySet()) {
			if(KEYWORDS.contains(key)) {
				continue;
			}
			final String value = keyValues.get(key);
			if(value.isEmpty())
				throw new ProblemParserException("empty no value for variable '%s'", key);
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
		final String[] split = values.split("\\;");
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
		final Matcher matcher = CURVE_DEF.matcher(value);
		if(matcher.matches()) {
			final char firstChar = matcher.group(1).charAt(0);
			final Point x = parsePoint(matcher.group(3));
			final Point y = parsePoint(matcher.group(5));
			if(firstChar == 'l') {
				return l(x,y);
			}
			if(firstChar == 'c') {
				return c(x,y);
			}
		}
		throw new ProblemParserException("invalid or unknown curve:'%s'", value);
	}

	private PointSet parsePoints(final String values) {
		if(values == null || values.isEmpty()) {
			return PointSet.empty();
		}
		final String[] split = values.split("\\;");
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
		final Matcher matcher = POINT_DEF.matcher(value);
		if(matcher.matches()) {
			final double x = parseConstant(matcher.group(2));
			final double y = parseConstant(matcher.group(4));
			return p(x,y);
		}
		throw new ProblemParserException("invalid or unknown point: '%s'", value);
	}

	private double parseConstant(final String value) {
		if(constants.containsKey(value)) {
			return constants.get(value);
		}
		try {
			return Calculator.evaluate(value);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ProblemParserException("error parsing numerical value '%s': %s", value, e.getMessage());
		}
	}
	
	@SuppressWarnings("serial")
	public static class ProblemParserException extends RuntimeException {
		public ProblemParserException(final String format, final Object... args) {
			super(String.format(format, args));
		}
	}

}
