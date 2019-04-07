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

	private static final PrintStream out = System.out;
	
	private final Problem problem;
	
	private final Algebra algebra;

	public ResultPrinter(final Problem problem, final Algebra algebra) {
		this.problem = problem;
		this.algebra = algebra;
	}

	void printAll(final Collection<Board> solutions) {
		printProblem();
		out.println(solutions.size() + " solution(s):");
		solutions.forEach(this::printSolution);
	}
	
	void printFirst(final Optional<Board> solution) {
		printProblem();
		if(solution.isPresent()) {
			out.println("solution:");
			printSolution(solution.get());
		}
		else {
			out.println("no solution");
		}
	}
	
	private void printProblem() {
		out.println("\r\n");
		out.println("initial:");
		print(problem.initial());
		out.println("required:");
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
			out.println(curve);
			out.println("  " + newPoints);
			curves.add(curve);
		}
		out.println();
	}
	
	private static void print(final Board board) {
		board.curves().forEach(out::println);
		out.println(board.points());
		out.println();
	}

}
