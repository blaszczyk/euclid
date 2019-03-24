package euclid;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;

import euclid.alg.*;
import euclid.model.*;
import euclid.problem.*;
import euclid.problem.Problem.Algorithm;
import euclid.problem.ProblemParser.ProblemParserException;

public class EuCLId {
	
	public static void main(final String[] args) {
		final File file = getFile(args);
		final Problem problem = parse(file);
		final Search<Board> search = getSearch(problem);
		if(problem.findAll()) {
			search.findAll().forEach(EuCLId::print);
		}
		else {
			final Optional<Board> solution = search.findFirst();
			if(solution.isPresent())
				print(solution.get());
			else
				System.out.println("no solution");
		}
	}
	
	private static void print(final Board board) {
		for(Curve c : board.curves()) {
			System.out.println(c);
			System.out.println("  " + board.points().stream().filter(c::contains).collect(Collectors.toList()));
		}
	}
	
	private static Problem parse(final File file) {
		try {
			return ProblemParser.parse(file);
		} catch (ProblemParserException e) {
			fail("error parsing file'%s': %s", file, e.getMessage());
			return null;
		}
	}

	private static Search<Board> getSearch(final Problem problem) {
		final Board initial = problem.initial();
		final Board required = problem.required();
		final int maxDepth = problem.maxDepth();
		final Algorithm algorithm = problem.algorithm();
		switch (algorithm) {
		case CURVE_BASED:
			return new CurveBasedSearch(initial, required, maxDepth);
		case POINT_BASED:
			return new PointBasedSearch(initial, required, maxDepth);
		}
		return null;
	}

	private static File getFile(final String[] args) {
		if(args.length == 0) {
			fail("no file name specified");
		}
		final File file = new File(args[0]);
		if(!file.exists()) {
			fail("file '%s' does not exist", args[0]);
		}
		return file;
	}
	
	private static void fail(final String format, final Object... args) {
		System.err.printf(format, args);
		System.exit(-1);
	}

}
