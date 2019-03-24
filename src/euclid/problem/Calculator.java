package euclid.problem;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
		
	private static final Pattern PARENTHESIS_PATTERN = Pattern.compile("(.*[^t])?(sqrt)?(\\()([0-9\\*\\+\\-\\/\\.]*)(\\))(.*)");
	
	public static double evaluate(final String expression)
	{
		final Matcher matcher = PARENTHESIS_PATTERN.matcher(expression);
		if(matcher.matches())
		{
			final boolean sqrt = nonNull(matcher.group(2)).equals("sqrt");
			final String prefix = nonNull(matcher.group(1));
			final String inner = nonNull(matcher.group(4));
			final String apex = nonNull(matcher.group(6));
			double result = evaluate(inner);
			if(sqrt) {
				result = Math.sqrt(result);
			}
			final String reducedExpression = prefix	+ result + apex;
			return evaluate(reducedExpression);
		}
		return evaluateSum(expression);
	}

	private static double evaluateSum(final String sum)
	{
		final String[] summands = sum.replaceAll("(?<=[0-9])\\-", "+-")
				.replaceAll("(?<=[0-9\\(\\)])\\/", "*/")
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
