package euclid.algorithm;

import euclid.sets.Board;

public interface Prioritizer {
	
	public int priotiry(final Board b, final int pointMisses, final int curveMisses);
	
	public int maxPriority();

}
