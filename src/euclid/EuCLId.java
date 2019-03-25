package euclid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import euclid.alg.*;
import euclid.kpi.KpiMonitor;
import euclid.model.*;
import euclid.problem.*;

public class EuCLId {

	public static void main(final String[] args) {
		final File file = getFile(args);
		final EuCLId euCLId = new EuCLId(file);
		euCLId.process();
	}

	private final Problem problem;
	
	private final Search<Board> search;
	
	private final KpiMonitor monitor;
	
	private EuCLId(final File problemFile) {
		problem = ProblemParser.parse(problemFile);
		search = problem.createSearch();
		monitor = new KpiMonitor(1000);
		monitor.addReporter(search);
		monitor.addReporter(ElementLifeTimeManager::kpiReport);
	}
	
	private void process() {
		monitor.start();
		if(problem.findAll()) {
			final Collection<Board> solutions = search.findAll();
			printAll(solutions);
		}
		else {
			final Optional<Board> solution = search.findFirst();
			printFirst(solution);
		}
		monitor.halt();
	}
	
	private void printAll(final Collection<Board> solutions) {
		printProblem();
		System.out.println(solutions.size() + " solution(s):");
		solutions.forEach(this::printSolution);
	}
	
	private void printFirst(final Optional<Board> solution) {
		printProblem();
		if(solution.isPresent()) {
			System.out.println("solution:");
			printSolution(solution.get());
		}
		else {
			System.out.println("no solution");
		}
	}
	
	private void printProblem() {
		System.out.println("\r\n");
		System.out.println("initial:");
		print(problem.initial());
		System.out.println("required:");
		print(problem.required());
	}
	
	private void printSolution(final Board solution) {
		final List<Curve> curves = new ArrayList<>(solution.curves().size());
		problem.initial().curves().forEach(curves::add);
		for(final Curve c : solution.curves()) {
			final Set<Point> newPoints = new TreeSet<>();
			curves.stream()
				.map(c::intersect)
				.map(PointSet::asList)
				.forEach(newPoints::addAll);
			System.out.println(c);
			System.out.println("  " + newPoints);
			curves.add(c);
		}
		System.out.println();
	}
	
	private static void print(final Board board) {
		board.curves().forEach(System.out::println);
		System.out.println(board.points());
		System.out.println();
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
