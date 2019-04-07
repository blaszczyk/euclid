package euclid.cli;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import euclid.algorithm.Algorithm;
import euclid.algorithm.CurveBasedSearch;
import euclid.algorithm.PointBasedSearch;
import euclid.engine.EngineParameters;
import euclid.engine.SearchEngine;
import euclid.kpi.KpiCsvWriter;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiReporter;
import euclid.kpi.KpiStdoutLogger;
import euclid.model.*;
import euclid.problem.*;

public class EuCLId {

	public static void main(final String[] args) {
		try {
			final CliParameter parameters = new CliParameterParser(args).parse();
			if(parameters.needsHelp()) {
				usage();
				return;
			}
			final EuCLId euCLId = new EuCLId(parameters);
			euCLId.process();
		}
		catch(final CliParameterParserExcepion e) {
			System.err.println(e.getMessage());
			usage();
		}
	}

	private static void usage() {
		try {
			final URI uri = EuCLId.class.getResource("usage.txt").toURI();
			Files.copy(Paths.get(uri), System.out);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private final CurveLifeCycle lifeCycle;
	
	private final Algebra algebra;

	private final Problem problem;
	
	private final SearchEngine<Board> engine;
	
	private final KpiMonitor monitor;
	
	private EuCLId(final CliParameter params) {
		lifeCycle = params.cacheCurves() ? new CachedCurveLifeCycle() : new BasicCurveLifeCycle();
		algebra = params.cacheIntersections() ? new CachedIntersectionAlgebra(lifeCycle) : new Algebra(lifeCycle);
		problem = new ProblemParser(algebra, params.problemFile()).parse();
		
		final EngineParameters parameters = new EngineParameters("euCLId", problem.findAll(), params.threadCount(), params.cacheCandidates());
		engine = new SearchEngine<>(createAlgorithm(), parameters);

		monitor = new KpiMonitor(params.kpiInterval());				
		wireKpiMonitor(params);
	}

	private Algorithm<Board> createAlgorithm() {
		switch (problem.algorithmType()) {
		case CURVE_BASED:
			return new CurveBasedSearch(problem, algebra);
		case POINT_BASED:
			return new PointBasedSearch(problem, algebra);
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
