package euclid.engine;

public class EngineParameters {
	
	private final String id;
	
	private final boolean findAll;
	
	private final int threadCount;
	
	private final boolean deduplicate;

	public EngineParameters(final String id, final boolean findAll, final int threadCount, final boolean deduplicate) {
		this.id = id;
		this.findAll = findAll;
		this.threadCount = threadCount;
		this.deduplicate = deduplicate;
	}

	public String id() {
		return id;
	}

	public boolean findAll() {
		return findAll;
	}

	public int threadCount() {
		return threadCount;
	}

	public boolean deduplicate() {
		return deduplicate;
	}

}
