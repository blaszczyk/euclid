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
import euclid.kpi.SystemKpi;
import euclid.problem.*;
import euclid.sets.Board;

public class EuCLId {

	private static final String KEY_FILE = "file";
	private static final String KEY_HELP = "help";
	
	private static final String KEY_THREADS = "threads";
	private static final String KEY_BUNCHSIZE = "bunchsize";

	private static final String KEY_KPI_INTERVAL = "kpiinterval";
	private static final String KEY_KPI_CSV = "kpicsv";
	private static final String KEY_KPI_OUT = "kpiout";

	public static void main(final String[] args) {
		final CliParameters parameters = new CliParameters(args);
		if(parameters.getBooleanValue(KEY_HELP, false)) {
			usage();
			return;
		}
		final EuCLId euCLId = new EuCLId(parameters);
		euCLId.process();
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
	
	private EuCLId(final CliParameters params) {
		final File problemFile = params.getFileValue(KEY_FILE);
		problem = new ProblemParser(problemFile).parse();

		final Algorithm<? extends Board> algorithm = problem.algorithm().create(problem);
		final int threadCount = params.getIntValue(KEY_THREADS, Runtime.getRuntime().availableProcessors());
		final int bunchSize = params.getIntValue(KEY_BUNCHSIZE, 50);
		final EngineParameters parameters = new EngineParameters("euCLId", problem.maxSolutions(), problem.depthFirst(), problem.shuffle(),
				threadCount, bunchSize);
		engine = new SearchEngine<>(algorithm, parameters);

		monitor = new KpiMonitor(params.getIntValue(KEY_KPI_INTERVAL, 1000));
		engine.kpiReporters().forEach(monitor::addReporter);
		monitor.addReporter(new SystemKpi());
		if(params.getBooleanValue(KEY_KPI_CSV, true)) {
			monitor.addConsumer(new KpiCsvWriter(new File("log")));
		}
		if(params.getBooleanValue(KEY_KPI_OUT, false)) {
			monitor.addConsumer(new KpiStdoutLogger());
		}
	}

	private void process() {
		engine.start();
		monitor.start();
		engine.join();
		monitor.halt();
		final Collection<? extends Board> solutions = engine.solutions();
		new ResultPrinter(problem).printAll(solutions);
	}

}
