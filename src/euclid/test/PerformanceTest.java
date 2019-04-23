package euclid.test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import euclid.algorithm.Algorithm;
import euclid.engine.EngineParameters;
import euclid.engine.SearchEngine;
import euclid.kpi.KpiReporter;
import euclid.problem.Problem;
import euclid.problem.ProblemParser;
import euclid.sets.Board;

public class PerformanceTest {
	
	public static void main(final String[] args) throws InterruptedException {
		final File testCasesDir = new File(args[0]);
		final int repetitions = Integer.parseInt(args[1]);
		final int timeOut = Integer.parseInt(args[2]);
		final int bunchSize = Integer.parseInt(args[3]);
		
		for(final File testCaseFile : testCasesDir.listFiles(f -> f.getName().endsWith(".euclid"))) {
			final String testName = testCaseFile.getName().substring(0, testCaseFile.getName().lastIndexOf('.'));
			System.out.println("executing " + testName);
			final List<Map<String, Number>> results = new ArrayList<>();
			for(int i = 0; i < repetitions; i++) {
				final Map<String, Number> result = process(testCaseFile, timeOut, bunchSize);
				results.add(result);
			}
			printResults(results);
		}
	}

	private static Map<String, Number> process(final File testCaseFile, final int timeout, final int bunchSize) throws InterruptedException {
		System.gc();
		Thread.sleep(1000);
		
		final Problem problem = new ProblemParser(testCaseFile).parse();
		final Algorithm<? extends Board> algorithm = problem.algorithm().create(problem);
		final EngineParameters params = new EngineParameters(testCaseFile.getName(), problem.maxSolutions(), problem.depthFirst(), 
				false, Runtime.getRuntime().availableProcessors(), bunchSize, 100000);
		final SearchEngine<? extends Board> engine = new SearchEngine<>(algorithm, params);
		
		final long startTime = System.currentTimeMillis();
		engine.start();
		final Timer timer = new Timer(timeout, e -> engine.halt());
		timer.start();
		engine.join();
		timer.stop();
		final long runtime = System.currentTimeMillis() - startTime;
		
		final Map<String, Number> result = new LinkedHashMap<>();
		result.put("runtime", runtime);
		result.put("solutions", engine.solutions().size());
		final KpiReporter engineKpi = engine.kpiReporters().iterator().next();
		engineKpi.fetchReport(result::put);
		engine.cleanUp();
		return result;
	}

	private static void printResults(final List<Map<String, Number>> results) {
		print("run");
		results.get(0).keySet().forEach(PerformanceTest::print);
		System.out.println();
		for(int i = 0; i < results.size(); i++) {
			print(i);
			results.get(i).values().forEach(PerformanceTest::print);
			System.out.println();
		}
		print("avg");
		results.get(0).keySet().forEach(key -> 
			print(results.stream().mapToLong(m -> m.get(key).longValue()).average().getAsDouble()));
		System.out.println();
	}
	
	private static void print(final String key) {
		System.out.printf("%15.14s", key.substring(0, Math.min(key.length(), 25)));
	}
	
	private static void print(final Number value) {
		System.out.printf("%,15d", value.longValue());
	}

}
