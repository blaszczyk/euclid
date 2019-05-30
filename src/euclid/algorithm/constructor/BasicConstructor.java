package euclid.algorithm.constructor;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.sets.CurveSet;

public class BasicConstructor extends Constructor {

	@Override
	public void constructFromTwoDistinctPoints(final Point p1, final Point p2, final CurveSet successors) {
		successors.addNonNull(Algebra.line(p1,p2));
		successors.addAll(Algebra.circles(p1,p2));
	}

}
