package euclid;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import euclid.alg.*;
import euclid.kpi.KpiMonitor;
import euclid.model.*;
import euclid.problem.*;

public class EuCLId {

	public static void main(final String[] args) {
		
		final File file = getFile(args);
		final Problem problem = parse(file);
		
		System.out.println("initial:");
		print(problem.initial());
		System.out.println("required:");
		print(problem.required());
		
		final Search<Board> search = problem.createSearch();
		final KpiMonitor monitor = new KpiMonitor(1000);
		monitor.addReporter(ElementLifeTimeManager::kpiReport);
		monitor.addReporter(search);
		monitor.start();
		
		if(problem.findAll()) {
			final Collection<Board> solutions = search.findAll();
			System.out.println("\r\nsolutions count=" + solutions.size());
			solutions.forEach(EuCLId::print);
		}
		else {
			final Optional<Board> solution = search.findFirst();
			if(solution.isPresent()) {
				System.out.println("\r\nsolution");
				print(solution.get());
			}
			else
				System.out.println("\r\nno solution");
		}
		
		monitor.halt();
	}
	
	private static void print(final Board board) {
		for(final Curve c : board.curves()) {
			System.out.println(c);
			System.out.println("  " + board.points().stream().filter(c::contains).collect(Collectors.toList()));
		}
		System.out.println();
	}
	
	private static Problem parse(final File file) {
		try {
			return ProblemParser.parse(file);
		} catch (ProblemParserException e) {
			e.printStackTrace();
			fail("error parsing file'%s': %s", file, e.getMessage());
			return null;
		}
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
