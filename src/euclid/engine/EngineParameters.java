package euclid.engine;

public class EngineParameters {
	
	private final String id;
	
	private final int maxSolutions;

	private final boolean depthFirst;

	private final boolean shuffle;
	
	private final int threadCount;

	private final int bunchSize;

	private final int maxQueueSize;

	public EngineParameters(final String id, final int maxSolutions, final boolean depthFirst, final boolean shuffle, final int threadCount, final int bunchSize, final int maxQueueSize) {
		this.id = id;
		this.maxSolutions = maxSolutions;
		this.depthFirst = depthFirst;
		this.shuffle = shuffle;
		this.threadCount = threadCount;
		this.bunchSize = bunchSize;
		this.maxQueueSize = maxQueueSize;
	}

	public String id() {
		return id;
	}

	public int maxSolutions() {
		return maxSolutions;
	}

	public boolean depthFirst() {
		return depthFirst;
	}

	public boolean shuffle() {
		return shuffle;
	}

	public int threadCount() {
		return threadCount;
	}

	public int bunchSize() {
		return bunchSize;
	}
	
	public int maxQueueSize() {
		return maxQueueSize;
	}

}
