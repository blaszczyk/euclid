package euclid.problem;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
		
	private static final Pattern DOUBLE_PATTERN = Pattern.compile("(sqrt)?(\\()([0-9\\*\\+\\-\\/\\.]*)(\\))");
	
	public static double evaluate(final String expression)
	{
		final Matcher matcher = DOUBLE_PATTERN.matcher(expression);
		if(matcher.find())
		{
			final boolean sqrt = matcher.group(1).equals("sqrt");
			final String innerExpression = matcher.group(3);
			double result = evaluate(innerExpression);
			if(sqrt) {
				result = Math.sqrt(result);
			}
			final String reducedExpression = expression.substring(0,matcher.start())
											+ result
											+ expression.substring(matcher.end());
			return evaluate(reducedExpression);
		}
		return evaluateInnerDouble(expression);
	}

	private static double evaluateInnerDouble(final String expression)
	{
		return Collections.singletonList(expression)
    			.stream()
    			.map(s -> s.replaceAll("(?<=[0-9])\\-", "+-")
    					.replaceAll("(?<=[0-9\\(\\)])\\/", "*/")
    					.replaceAll("\\-\\-", "")
    					.split("\\+"))
    			.flatMap(Arrays::stream)
    			.map(s -> s.split("\\*"))
    			.map(Arrays::stream)
    			.map(s -> s.map( Calculator::parseDouble )
    					.reduce(1., Calculator::multiply))
    			.reduce(0., Calculator::add);
	}
	
	private static double multiply(final double d1, final double d2)
	{
		return d1*d2;
	}
	
	private static double add(final double d1, final double d2)
	{
		return d1+d2;
	}
	
	private static double parseDouble(String text)
	{
		if(text.charAt(0) == '/')
			return 1. / Double.parseDouble(text.substring(1));
		return Double.parseDouble(text);
	}
}
