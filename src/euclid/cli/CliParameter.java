package euclid.cli;

import java.io.File;

public class CliParameter {

	private final File problemFile;
	
	private final int kpiInterval;
	
	private final boolean kpiCsv;
	
	private final boolean kpiOut;
	
	private final boolean cacheCurves;
	
	private final boolean cacheIntersections;
	
	private final int threadCount;
	
	private final boolean needsHelp;
	
	

	CliParameter(final File problemFile, final int kpiInterval, final boolean kpiCsv, final boolean kpiOut, final boolean cacheCurves,
			final boolean cacheIntersections, final int threadCount, final boolean needsHelp) {
		this.problemFile = problemFile;
		this.kpiInterval = kpiInterval;
		this.kpiCsv = kpiCsv;
		this.kpiOut = kpiOut;
		this.cacheCurves = cacheCurves;
		this.cacheIntersections = cacheIntersections;
		this.threadCount = threadCount;
		this.needsHelp = needsHelp;
	}

	File problemFile() {
		return problemFile;
	}

	int kpiInterval() {
		return kpiInterval;
	}

	boolean kpiCsv() {
		return kpiCsv;
	}

	boolean kpiOut() {
		return kpiOut;
	}

	boolean cacheCurves() {
		return cacheCurves;
	}

	boolean cacheIntersections() {
		return cacheIntersections;
	}
	
	int threadCount() {
		return threadCount;
	}
	
	boolean needsHelp() {
		return needsHelp;
	}

}
