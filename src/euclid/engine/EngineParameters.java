package euclid.engine;

public class EngineParameters {
	
	private final String id;
	
	private final boolean findAll;
	
	private final int threadCount;

	public EngineParameters(final String id, final boolean findAll, final int threadCount) {
		this.id = id;
		this.findAll = findAll;
		this.threadCount = threadCount;
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

}
