package euclid.cli;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import euclid.algorithm.Algorithm;
import euclid.algorithm.AlgorithmFactory;
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
	private static final String KEY_BUNCH_SIZE = "bunchsize";
	private static final String KEY_MAX_QUEUE_SIZE = "maxqueuesize";

	private static final String KEY_KPI_INTERVAL = "kpiinterval";
	private static final String KEY_KPI_CSV = "kpicsv";
	private static final String KEY_KPI_OUT = "kpiout";

	public static void main(final String[] args) {
		final CliParameters parameters = new CliParameters(args);
		if(parameters.getBooleanValue(KEY_HELP, false)) {
			usage();
			return;
		}
		final File problemFile = parameters.getFileValue(KEY_FILE);
		if(problemFile == null || ! problemFile.exists()) {
			System.out.println("problem file not specified or not existing");
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
	
	private final SearchEngine<Board> engine;
	
	private EuCLId(final CliParameters params) {
		problem = new ProblemParser(params.getFileValue(KEY_FILE)).parse();

		final Algorithm<Board> algorithm = AlgorithmFactory.create(problem);
		final int threadCount = params.getIntValue(KEY_THREADS, Runtime.getRuntime().availableProcessors());
		final int bunchSize = params.getIntValue(KEY_BUNCH_SIZE, 50);
		final int maxQueueSize = params.getIntValue(KEY_MAX_QUEUE_SIZE, 100000);
		final EngineParameters parameters = new EngineParameters("euCLId", problem.maxSolutions(), problem.depthFirst(), problem.shuffle(),
				threadCount, bunchSize, maxQueueSize);
		final KpiMonitor monitor = new KpiMonitor(params.getIntValue(KEY_KPI_INTERVAL, 1000));
		engine = new SearchEngine<>(algorithm, parameters, monitor);

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
		engine.join();
		final List<? extends Board> solutions = engine.solutions();
		new ResultPrinter(problem).printAll(solutions);
	}

}
