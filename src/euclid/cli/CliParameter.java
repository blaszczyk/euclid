package euclid.cli;

import java.io.File;

public class CliParameter {

	private final File problemFile;
	
	private final int kpiInterval;
	
	private final boolean kpiCsv;
	
	private final boolean kpiOut;
	
	private final boolean cacheCurves;
	
	private final boolean cacheIntersections;

	CliParameter(final File problemFile, final int kpiInterval, final boolean kpiCsv, final boolean kpiOut, 
			final boolean cacheCurves, final boolean cacheIntersections) {
		this.problemFile = problemFile;
		this.kpiInterval = kpiInterval;
		this.kpiCsv = kpiCsv;
		this.kpiOut = kpiOut;
		this.cacheCurves = cacheCurves;
		this.cacheIntersections = cacheIntersections;
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

}
