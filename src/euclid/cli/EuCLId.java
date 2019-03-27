package euclid.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import euclid.alg.engine.ThreadedSearchEngine;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiStdoutLogger;
import euclid.model.*;
import euclid.problem.*;

public class EuCLId {

	public static void main(final String[] args) {
		final CliParameters parameters = CliParameters.parseAndValidate(args);
		if(parameters.isValid()) {
			final EuCLId euCLId = new EuCLId(parameters);
			euCLId.process();
		}
		else {
			System.err.println(parameters.errorMessage());
		}
	}

	private final Problem problem;
	
	private final ThreadedSearchEngine<Board> engine;
	
	private final KpiMonitor monitor;
	
	private EuCLId(final CliParameters parameters) {
		this.problem = ProblemParser.parse(parameters.problemFile());
		engine = new ThreadedSearchEngine<>(problem.createAlgorithm(), problem.maxDepth(), problem.findAll());
		monitor = new KpiMonitor(parameters.kpiInterval());
		monitor.addReporter(engine.kpiReporter());
		monitor.addReporter(engine.queueKpiReporter());
		monitor.addReporter(ElementLifeTimeManager::kpiReport);
		monitor.addConsumer(new KpiStdoutLogger());
	}
	
	private void process() {
		monitor.start();
		if(problem.findAll()) {
			final Collection<Board> solutions = engine.findAll();
			printAll(solutions);
		}
		else {
			final Optional<Board> solution = engine.findFirst();
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
		problem.required().forEach(EuCLId::print);
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

}
