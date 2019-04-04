package euclid.cli;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import euclid.alg.Algorithm;
import euclid.alg.CurveBasedSearch;
import euclid.alg.PointBasedSearch;
import euclid.alg.engine.ThreadedSearchEngine;
import euclid.kpi.KpiCsvWriter;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiReporter;
import euclid.kpi.KpiStdoutLogger;
import euclid.model.*;
import euclid.problem.*;

public class EuCLId {

	public static void main(final String[] args) {
		final CliParameter parameters = new CliParameterParser(args).parse();
		final EuCLId euCLId = new EuCLId(parameters);
		euCLId.process();
	}

	private final CurveLifeCycle lifeCycle;
	
	private final Algebra algebra;

	private final Problem problem;
	
	private final ThreadedSearchEngine<Board> engine;
	
	private final KpiMonitor monitor;
	
	private EuCLId(final CliParameter params) {
		lifeCycle = params.cacheCurves() ? new CachedCurveLifeCycle() : new BasicCurveLifeCycle();
		algebra = params.cacheIntersections() ? new CachedIntersectionAlgebra(lifeCycle) : new Algebra(lifeCycle);

		problem = new ProblemParser(algebra, params.problemFile()).parse();
		engine = new ThreadedSearchEngine<>(createAlgorithm(), problem.maxDepth(), problem.findAll(), params.threadCount());

		monitor = new KpiMonitor(params.kpiInterval());				
		wireKpiMonitor(params);
	}

	private Algorithm<Board> createAlgorithm() {
		switch (problem.algorithmType()) {
		case CURVE_BASED:
			return new CurveBasedSearch(problem.initial(), problem.required(), algebra);
		case POINT_BASED:
			return new PointBasedSearch(problem.initial(), problem.required(), algebra);
		}
		return null;
	}

	private void wireKpiMonitor(final CliParameter params) {
		engine.kpiReporters().forEach(monitor::addReporter);
		monitor.addReporter(lifeCycle);
		if(algebra instanceof KpiReporter) {
			monitor.addReporter((KpiReporter) algebra);
		}
		if(params.kpiCsv()) {
			monitor.addConsumer(new KpiCsvWriter(new File("log")));
		}
		if(params.kpiOut()) {
			monitor.addConsumer(new KpiStdoutLogger());
		}
	}

	private void process() {
		monitor.start();
		engine.start(false);
		if(problem.findAll()) {
			final Collection<Board> solutions = engine.allSolutions();
			new ResultPrinter(problem, algebra).printAll(solutions);
		}
		else {
			final Optional<Board> solution = engine.firstSolution();
			new ResultPrinter(problem, algebra).printFirst(solution);
		}
		monitor.halt();
	}

}
