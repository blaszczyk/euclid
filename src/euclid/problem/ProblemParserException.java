package euclid.problem;

@SuppressWarnings("serial")
public class ProblemParserException extends RuntimeException {
	
	public ProblemParserException(final String format, final Object... args) {
		super(String.format(format, args));
	}
	
	public ProblemParserException(final Throwable cause, final String format, final Object... args) {
		super(String.format(format, args), cause);
	}
}