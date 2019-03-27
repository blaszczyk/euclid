package euclid.problem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	
	@FunctionalInterface
	public static interface ValueLookUp {
		public Optional<Double> lookUp(final String key);
	}

	private static final Pattern PARENTHESIS_PATTERN = Pattern.compile(
			"(.*[^a-zA-Z])?" // prefix
			+ "([a-zA-Z]*)" // operator
			+ "(\\()" // open
			+ "([0-9a-zA-Z\\+\\-\\*\\/\\.\\$]+)" // inner
			+ "(\\))" // close
			+ "(.*)"); // apex
	
	private final ValueLookUp external;
	
	private final Map<String, Double> cache = new HashMap<>();
		
	public Calculator(final ValueLookUp external) {
		this.external = external;
	}
	
	public Calculator() {
		this(k -> Optional.empty());
	}
	
	public double evaluate(final String expression)
	{
		final Matcher matcher = PARENTHESIS_PATTERN.matcher(expression);
		if(matcher.matches())
		{
			final String prefix = nonNull(matcher.group(1));
			final String operator = nonNull(matcher.group(2)).toLowerCase();
			final String inner = nonNull(matcher.group(4));
			final String apex = nonNull(matcher.group(6));
			double result = evaluateSum(inner);
			result = evaluateOperator(operator, result);
			final String key = "$" + cache.size() + "$";
			cache.put(key, result);
			final String reducedExpression = prefix	+ key + apex;
			return evaluate(reducedExpression);
		}
		return evaluateSum(expression);
	}

	private static double evaluateOperator(final String operator, final double operand)
	{
		if(operator.isEmpty()) {
			return operand;
		}
		try {
			final Method method = Math.class.getMethod(operator, double.class);
			return (double) method.invoke(null, operand);
		}
		catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			final List<String> allowedMethods = new ArrayList<>();
			for(final Method method : Math.class.getMethods()) {
				final Class<?>[] params = method.getParameterTypes();
				if(params.length == 1 && params[0].equals(double.class)) {
					allowedMethods.add(method.getName());
				}
			}
			throw new RuntimeException("unknown operator '" + operator + "'\nallowed operators:\n" + allowedMethods);
		}
	}

	private double evaluateSum(final String sum)
	{
		final String[] summands = sum.replaceAll("(?<=[0-9a-zA-Z\\$])\\-", "+-").split("\\+");
		return Arrays.stream(summands)
    			.map(this::evaluateProduct)
    			.reduce(0., Calculator::add);
	}

	private double evaluateProduct(final String product)
	{
		final String[] factors = product.replaceAll("(?<=[0-9a-zA-Z\\$])\\/", "*/").split("\\*");
		return Arrays.stream(factors)
				.map(this::parseDouble)
				.reduce(1., Calculator::multiply);
	}
	
	private double parseDouble(final String text)
	{
		if(text.startsWith("/"))
			return 1. / parseDouble(text.substring(1));
		if(text.startsWith("-"))
			return 0. - parseDouble(text.substring(1));
		if(cache.containsKey(text)) {
			return cache.get(text);
		}
		final Optional<Double> value = external.lookUp(text);
		if(value.isPresent()) {
			return value.get();
		}
		switch (text.toLowerCase()) {
		case "pi":
			return Math.PI;
		case "e":
			return Math.E;
		case "rand":
			return Math.random();
		}
		return Double.parseDouble(text);
	}
	
	private static double add(final double d1, final double d2)
	{
		return d1+d2;
	}
	
	private static double multiply(final double d1, final double d2)
	{
		return d1*d2;
	}
	
	private static String nonNull(final String nullable) {
		return nullable == null ? "" : nullable;
	}
}
