package euclid.algorithm.constructor;

import euclid.algebra.AdvancedAlgebra;
import euclid.geometry.*;
import euclid.sets.CurveSet;

public class AdvancedConstructor extends BasicConstructor {
	
	@Override
	public boolean isAdvanced() {
		return true;
	}

	@Override
	public void constructFromTwoDistinctPoints(final Point p1, final Point p2, final CurveSet successors) {
		super.constructFromTwoDistinctPoints(p1, p2, successors);
		successors.add(AdvancedAlgebra.bisector(p1,p2));
	}
	
	@Override
	public void constructFromThreeDistinctPoints(final Point p1, final Point p2, final Point p3, final CurveSet successors) {
		successors.add(AdvancedAlgebra.nonCollapsingCompass(p1, p2, p3));
		successors.add(AdvancedAlgebra.nonCollapsingCompass(p2, p3, p1));
		successors.add(AdvancedAlgebra.nonCollapsingCompass(p3, p1, p2));
		successors.addNonNull(AdvancedAlgebra.angleBisector(p1, p2, p3));
		successors.addNonNull(AdvancedAlgebra.angleBisector(p2, p3, p1));
		successors.addNonNull(AdvancedAlgebra.angleBisector(p3, p1, p2));
	}
	
	@Override
	public void constructFromPointAndLine(final Point p, final Line l, final CurveSet successors) {
		successors.add(AdvancedAlgebra.perpendicular(p, l));
		successors.addNonNull(AdvancedAlgebra.parallel(p, l));
	}
	
	@Override
	public void constructFromDistinctLines(final Line l1, final Line l2, CurveSet successors) {
		successors.addAll(AdvancedAlgebra.angleBisectors(l1, l2));
	}

}
