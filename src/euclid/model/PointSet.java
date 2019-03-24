package euclid.model;

import static euclid.model.ElementLifeTimeManager.*;

import java.util.TreeSet;

@SuppressWarnings("serial")
public class PointSet extends TreeSet<Point> {
	
	private static final PointSet EMPTY = new PointSet();

	public static PointSet create() {
		return new PointSet();
	}
	
	public static PointSet of(final Point... points) {
		final PointSet result = create();
		for(Point p : points)
			result.add(p);
		return result;
	}

	public static PointSet empty() {
		return EMPTY;
	}
	
	private PointSet() {
	}
	
	public CurveSet curves() {
		final CurveSet curves = CurveSet.create();
		for(Point p1 : this)
			for(Point p2 : this)
				if(p1.compareTo(p2) < 0)
				{
					curves.add(l(p1,p2));
					curves.add(c(p1,p2));
					curves.add(c(p2,p1));
				}
		return curves;
	}

	public PointSet adjoin(final Point p) {
		final PointSet result = create();
		result.addAll(this);
		result.add(p);
		return result;
	}

	public PointSet adjoin(PointSet other) {
		final PointSet result = create();
		result.addAll(this);
		result.addAll(other);
		return result;
	}

}
