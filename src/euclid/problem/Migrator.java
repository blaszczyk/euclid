package euclid.problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Migrator {

	public static void main(String[] args) throws IOException {
		final File problemsDir = new File(args[0]);
		for(final File file : problemsDir.listFiles(f -> f.getName().endsWith(".euclid")) ) {
			final List<String> lines = Files.lines(file.toPath())
					.map(Migrator::replace)
					.collect(Collectors.toList());
			migrateAlgorithmType(lines);
			Files.write(file.toPath(), lines, StandardOpenOption.TRUNCATE_EXISTING);
		}
	}
	
	private static void migrateAlgorithmType(final List<String> lines) {
		final String oldAlgorithm = lines.stream()
				.filter(Migrator::isAlgorithm)
				.findFirst()
				.orElseThrow(() -> new ProblemParserException("no algorithm type found: %s", lines.toString()))
				.replaceAll(".*\\=", "").trim();
		final String newAlgorithm;
		final String constructor;
		switch(oldAlgorithm.toUpperCase()) {
		case "CURVE_BASED":
			newAlgorithm = "CURVE_BASED";
			constructor = "BASIC";
			break;
		case "CURVE_ADVANCED":
			newAlgorithm = "CURVE_BASED";
			constructor = "ADVANCED";
			break;
		case "CURVE_DEDUPE":
			newAlgorithm = "CURVE_DEDUPE";
			constructor = "BASIC";
			break;
		case "LINES_ONLY":
			newAlgorithm = "CURVE_BASED";
			constructor = "STRAIGHTEDGE";
			break;
		case "CIRCLES_ONLY":
			newAlgorithm = "CURVE_BASED";
			constructor = "COMPASS";
			break;
		default:
			throw new IllegalArgumentException("illegal algorithm " + oldAlgorithm);
		}
		final int algLineIndex = IntStream.range(0, lines.size())
			.filter(i -> isAlgorithm(lines.get(i)))
			.findFirst().getAsInt();
		final String newAlgLine = lines.get(algLineIndex).replace(oldAlgorithm, newAlgorithm);
		final String constLine = ProblemParser.KEY_CONSTRUCTOR + "=" + constructor;
		lines.set(algLineIndex, newAlgLine);
		lines.add(algLineIndex + 1, constLine);
	}
	
	static boolean isAlgorithm(final String line) {
		return line.replaceAll("\\s+", "").toLowerCase().startsWith(ProblemParser.KEY_ALGORITHM);
	}

	static String replace(String line) {
		return line.replaceAll("SEGMENT_IS_NOT_LINE", "LINE_TYPES_DISTINCT").replaceAll("SEGMENT_IS_LINE", "LINE_TYPES_EQUAL");
	}
}
