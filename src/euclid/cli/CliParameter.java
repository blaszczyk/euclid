package euclid.cli;

import java.io.File;

public class CliParameter {
	
	static final CliParameter HELP = new CliParameter(null, 0, false, false, 0);

	private final File problemFile;
	
	private final int kpiInterval;
	
	private final boolean kpiCsv;
	
	private final boolean kpiOut;
	
	private final int threadCount;

	public CliParameter(final File problemFile, final int kpiInterval, final boolean kpiCsv, final boolean kpiOut,
			final int threadCount) {
		this.problemFile = problemFile;
		this.kpiInterval = kpiInterval;
		this.kpiCsv = kpiCsv;
		this.kpiOut = kpiOut;
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
	
	int threadCount() {
		return threadCount;
	}
	
	boolean needsHelp() {
		return problemFile == null;
	}

}
