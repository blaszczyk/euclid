package euclid.problem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
		
	private static final Pattern PARENTHESIS_PATTERN = Pattern.compile("(.*[^a-zA-Z])?([a-zA-Z]*)?(\\()([0-9a-zA-Z\\*\\+\\-\\/\\.]*)(\\))(.*)");
	
	public static double evaluate(String expression)
	{
		final Matcher matcher = PARENTHESIS_PATTERN.matcher(expression);
		if(matcher.matches())
		{
			final String operator = nonNull(matcher.group(2)).toLowerCase();
			final String prefix = nonNull(matcher.group(1));
			final String inner = nonNull(matcher.group(4));
			final String apex = nonNull(matcher.group(6));
			double result = evaluate(inner);
			result = evaluateOperator(operator, result);
			final String reducedExpression = prefix	+ result + apex;
			return evaluate(reducedExpression);
		}
		return evaluateSum(expression);
	}

	private static double evaluateOperator(final String operator, final double operand)
	{
		if(operator.equals("")) {
			return operand;
		}
		try {
			final Method method = Math.class.getMethod(operator, double.class);
			return (double) method.invoke(null, operand);
		}
		catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("unknown operator " + operator);
		}
	}

	private static double evaluateSum(final String sum)
	{
		final String[] summands = sum.replaceAll("(?<=[0-9a-zA-Z])\\-", "+-")
				.replaceAll("(?<=[0-9a-zA-Z\\(\\)])\\/", "*/")
				.replaceAll("\\-\\-", "")
				.split("\\+");
		return Arrays.stream(summands)
    			.map(Calculator::evaluateProduct)
    			.reduce(0., Calculator::add);
	}
	
	private static double evaluateProduct(final String product)
	{
		final String[] factors = product.split("\\*");
		return Arrays.stream(factors)
				.map(Calculator::parseDouble)
				.reduce(1., Calculator::multiply);
	}
	
	private static double parseDouble(final String text)
	{
		if(text.equalsIgnoreCase("pi")) {
			return Math.PI;
		}
		if(text.equalsIgnoreCase("e")) {
			return Math.E;
		}
		if(text.charAt(0) == '/')
			return 1. / Double.parseDouble(text.substring(1));
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
