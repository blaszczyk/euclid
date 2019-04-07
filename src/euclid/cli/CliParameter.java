package euclid.cli;

import java.io.File;

public class CliParameter {
	
	static final CliParameter HELP = new CliParameter(null, 0, false, false, false, false, false, 0);

	private final File problemFile;
	
	private final int kpiInterval;
	
	private final boolean kpiCsv;
	
	private final boolean kpiOut;
	
	private final boolean cacheCurves;
	
	private final boolean cacheIntersections;
	
	private final int threadCount;

	private final boolean cacheCandidates;

	public CliParameter(final File problemFile, final int kpiInterval, final boolean kpiCsv, final boolean kpiOut, final boolean cacheCurves,
			final boolean cacheIntersections, final boolean cacheCandidates, final int threadCount) {
		this.problemFile = problemFile;
		this.kpiInterval = kpiInterval;
		this.kpiCsv = kpiCsv;
		this.kpiOut = kpiOut;
		this.cacheCurves = cacheCurves;
		this.cacheIntersections = cacheIntersections;
		this.cacheCandidates = cacheCandidates;
		this.threadCount = threadCount;
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

	public boolean cacheCandidates() {
		return cacheCandidates;
	}
	
	int threadCount() {
		return threadCount;
	}
	
	boolean needsHelp() {
		return problemFile == null;
	}

}
