package euclid.problem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Cliculator {

	public static void main(final String[] args) {
		
		final Map<String, Double> variables = new HashMap<>();
		int varIndex = 0;
		
		try(final Scanner scanner = new Scanner(System.in)) {
			while(scanner.hasNextLine()) {
				final String line = scanner.nextLine().replaceAll("\\s", "");
				
				if(line.equals("exit")) {
					break;
				}

				final String variable;
				final String expression;
				if(line.contains("=")) {
					final String[] split = line.split("\\=", 2);
					variable = split[0];
					expression = split[1];
				}
				else {
					variable = "$" + varIndex++;
					expression = line;
				}
				
				try {
					final Calculator calculator = new Calculator(key -> Optional.ofNullable(variables.get(key)));
					final double result = calculator.evaluate(expression);
					variables.put(variable, result);
					System.out.printf(">%s=%s%n", variable, new DecimalFormat("#.######").format(result));
				}
				catch(final Exception e) {
					System.err.printf("Invalid input: %s%n", e.getMessage());
				}
			}
		}
	}
}
