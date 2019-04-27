package euclid.algorithm.constructor;

import euclid.algebra.Algebra;
import euclid.geometry.Point;
import euclid.sets.CurveSet;

public class CompassOnly extends Constructor {

	@Override
	public void constructFromTwoDistinctPoints(final Point p1, final Point p2, final CurveSet successors) {
		successors.add(Algebra.circle(p1,p2));
		successors.add(Algebra.circle(p2,p1));
	}

}
