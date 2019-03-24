package euclid.model;

import java.util.HashSet;

@SuppressWarnings("serial")
public class CurveSet extends HashSet<Curve> {
	
	private static final CurveSet EMPTY = new CurveSet(0);
	
	public static CurveSet create() {
		return new CurveSet(8);
	}
	
	public static CurveSet of(final Curve... curves) {
		CurveSet result = new CurveSet(curves.length);
		for(Curve c : curves)
			result.add(c);
		return result;
	}

	public static CurveSet empty() {
		return EMPTY;
	}

	private CurveSet(int length) {
		super(length);
	}

	public PointSet intersections() {
		final PointSet points = PointSet.create();
		for(final Curve c1 : this)
			for(final Curve c2 : this)
				points.addAll(c1.intersect(c2));
		return points;
	}

	public CurveSet adjoin(final Curve c) {
		final CurveSet result = new CurveSet(size() + 1);
		result.addAll(this);
		result.add(c);
		return result;
	}

	public CurveSet adjoin(final CurveSet curves) {
		final CurveSet result = new CurveSet(size() + curves.size());
		result.addAll(this);
		result.addAll(curves);
		return result;
	}

}
