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
			"(.*\\W)?" // prefix
			+ "(\\w*)" // operator
			+ "(\\()" // open
			+ "([^\\(\\)]+)" // inner
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
			final String operator = nonNull(matcher.group(2));
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
		final String[] summands = sum.replaceAll("(?<=[\\w\\$])\\-", "+-").split("\\+");
		return Arrays.stream(summands)
    			.map(this::evaluateProduct)
    			.reduce(0., Calculator::add);
	}

	private double evaluateProduct(final String product)
	{
		final String[] factors = product.replaceAll("(?<=[\\w\\$])\\/", "*/").split("\\*");
		return Arrays.stream(factors)
				.map(this::evaluateNumber)
				.reduce(1., Calculator::multiply);
	}
	
	private double evaluateNumber(final String number)
	{
		if(number.startsWith("-"))
			return 0. - evaluateNumber(number.substring(1));
		if(number.startsWith("/"))
			return 1. / evaluateNumber(number.substring(1));
		if(number.contains("^")) {
			final String[] split = number.split("\\^",2);
			return Math.pow(evaluateNumber(split[0]), evaluateNumber(split[1]));
		}
		if(cache.containsKey(number)) {
			return cache.get(number);
		}
		final Optional<Double> externalValue = external.lookUp(number);
		if(externalValue.isPresent()) {
			return externalValue.get();
		}
		switch (number.toLowerCase()) {
		case "pi":
			return Math.PI;
		case "e":
			return Math.E;
		case "rand":
			return Math.random();
		}
		return Double.parseDouble(number);
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
