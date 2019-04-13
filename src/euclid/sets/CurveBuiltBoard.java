package euclid.sets;

import euclid.geometry.Curve;

public class CurveBuiltBoard extends Board {

	private final Curve last;
	
	public CurveBuiltBoard(final PointSet points, final CurveSet curves, final Curve last, final Board parent) {
		super(points, curves, parent);
		this.last = last;
		if(last != null) {
			curves.add(last);
		}
	}
	
	public CurveBuiltBoard(final PointSet points, final CurveSet curves) {
		this(points, curves, null, null);
	}

	public Curve last() {
		return last;
	}

	@Override
	public int hashCode() {
		return curves().hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final Board other = (Board) obj;
		return curves().equals(other.curves());
	}

}
