package euclid.cli;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import euclid.algorithm.Algorithm;
import euclid.engine.EngineParameters;
import euclid.engine.SearchEngine;
import euclid.kpi.KpiCsvWriter;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiStdoutLogger;
import euclid.problem.*;
import euclid.sets.Board;

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

	private final Problem problem;
	
	private final SearchEngine<? extends Board> engine;
	
	private final KpiMonitor monitor;
	
	private EuCLId(final CliParameter params) {
		problem = new ProblemParser(params.problemFile()).parse();

		final Algorithm<? extends Board> algorithm = problem.algorithm().create(problem);
		final EngineParameters parameters = new EngineParameters("euCLId", problem.maxSolutions(), problem.depthFirst(), problem.shuffle(), params.threadCount());
		engine = new SearchEngine<>(algorithm, parameters);

		monitor = new KpiMonitor(params.kpiInterval());				
		wireKpiMonitor(params);
	}

	private void wireKpiMonitor(final CliParameter params) {
		engine.kpiReporters().forEach(monitor::addReporter);
		if(params.kpiCsv()) {
			monitor.addConsumer(new KpiCsvWriter(new File("log")));
		}
		if(params.kpiOut()) {
			monitor.addConsumer(new KpiStdoutLogger());
		}
	}

	private void process() {
		monitor.start();
		engine.start();
		engine.join();
		final Collection<? extends Board> solutions = engine.solutions();
		new ResultPrinter(problem).printAll(solutions);
		monitor.halt();
	}

}
