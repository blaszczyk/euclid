package euclid.cli;

@SuppressWarnings("serial")
public class CliParameterParserExcepion extends RuntimeException {

	public CliParameterParserExcepion(final String message, final Object... args) {
		super(String.format(message, args));
	}
	
}