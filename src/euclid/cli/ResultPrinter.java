package euclid.cli;

import java.io.PrintStream;
import java.util.List;
import euclid.algorithm.Reconstruction;
import euclid.problem.*;
import euclid.sets.Board;

public class ResultPrinter {

	private final PrintStream printStream;
	
	private final Problem problem;

	public ResultPrinter(final Problem problem, final PrintStream printStream) {
		this.problem = problem;
		this.printStream = printStream;
	}

	public ResultPrinter(final Problem problem) {
		this(problem, System.out);
	}

	void printAll(final List<? extends Board> solutions) {
		printProblem();
		println(solutions.size() + " solution(s):");
		solutions.forEach(this::printSolution);
	}
	
	private void printProblem() {
		println("\r\n");
		println("initial:");
		print(problem.initial());
		println("required:");
		print(problem.required());
	}
	
	private void printSolution(final Board solution) {
		Reconstruction construction = Reconstruction.from(solution, problem.constructor().create());
		while(construction != null) {
			println("construct " + construction.curve() + " from " + construction.constituents());
			println("new points: " + construction.newPoints());
			construction = construction.next();
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
