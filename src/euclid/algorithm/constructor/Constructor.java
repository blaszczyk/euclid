package euclid.algorithm.constructor;

import euclid.geometry.*;
import euclid.sets.CurveSet;

public abstract class Constructor {
	
	public boolean isAdvanced() {
		return false;
	}
	
	public abstract void constructFromTwoDistinctPoints(final Point p1, final Point p2, final CurveSet successors);
	
	public void constructFromThreeDistinctPoints(final Point p1, final Point p2, final Point p3, final CurveSet successors) {}
	
	public void constructFromPointAndLine(final Point p, final Line l, final CurveSet successors) {}
	
	public void constructFromDistinctLines(final Line l1, final Line l2, final CurveSet successors) {}

}
