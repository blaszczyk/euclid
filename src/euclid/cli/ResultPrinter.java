package euclid.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;
import euclid.problem.*;

public class ResultPrinter {

	private final PrintStream printStream;
	
	private final Problem problem;
	
	private final Algebra algebra;

	public ResultPrinter(final Problem problem, final Algebra algebra, final PrintStream printStream) {
		this.problem = problem;
		this.algebra = algebra;
		this.printStream = printStream;
	}

	public ResultPrinter(final Problem problem, final Algebra algebra) {
		this(problem, algebra, System.out);
	}

	void printAll(final Collection<Board> solutions) {
		printProblem();
		println(solutions.size() + " solution(s):");
		solutions.forEach(this::printSolution);
	}
	
	void printFirst(final Optional<Board> solution) {
		printProblem();
		if(solution.isPresent()) {
			println("solution:");
			printSolution(solution.get());
		}
		else {
			println("no solution");
		}
	}
	
	private void printProblem() {
		println("\r\n");
		println("initial:");
		print(problem.initial());
		println("required:");
		print(problem.required());
	}
	
	private void printSolution(final Board solution) {
		final List<Curve> curves = new ArrayList<>(solution.curves().size());
		problem.initial().curves().forEach(curves::add);
		for(final Curve curve : solution.curves()) {
			final Set<Point> newPoints = new TreeSet<>();
			curves.stream()
				.map(c -> algebra.intersect(c, curve))
				.map(PointSet::asList)
				.forEach(newPoints::addAll);
			println(curve);
			println("  " + newPoints);
			curves.add(curve);
		}
		println();
	}
	
	private void print(final Board board) {
		board.curves().forEach(printStream::println);
		println(board.points());
		println();
	}

	private void println() {
		printStream.println();
	}
	
	private void println(final Object o) {
		printStream.println(o);
	}

}
