package euclid.model;

import static euclid.model.Sugar.*;

import java.util.List;

public class AdvancedAlgebra extends Algebra {
	
	public AdvancedAlgebra(final CurveLifeCycle lifeCycle) {
		super(lifeCycle);
	}

	public Curve bisector(final Point p1, final Point p2) {
		final Point normal = p1.sub(p2);
		final Constructable offset = normal.mul(p1.add(p2)).div(two());
		return lifeCycle.line(normal, offset);
	}

	public Curve perpendicular(final Point p, final Line l) {
		final Point normal = l.normal.orth();
		final Constructable offset = normal.mul(p);
		return lifeCycle.line(normal, offset);
	}

	public CurveSet parallel(final Point p, final Line l) {
		if(doesContain(p,l)) {
			return CurveSet.empty();
		}
		final Point normal = l.normal;
		final Constructable offset = normal.mul(p);
		return CurveSet.of(lifeCycle.line(normal, offset));
	}

	public CurveSet angleBisector(final Line l1, final Line l2) {
		final List<Point> intersections = doIntersect(l1, l2).asList();
		if(intersections.isEmpty()) {
			return CurveSet.empty();
		}
		final Point intersection = intersections.get(0);
		final Point n1 = l1.normal.add(l2.normal);
		final Point n2 = l1.normal.sub(l2.normal);
		final Constructable o1 = n1.mul(intersection);
		final Constructable o2 = n2.mul(intersection);
		return CurveSet.of(lifeCycle.line(n1, o1), lifeCycle.line(n2, o2));
	}

}
