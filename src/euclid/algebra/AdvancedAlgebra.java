package euclid.algebra;

import java.util.List;

import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.sets.CurveSet;

public class AdvancedAlgebra {
	
	private AdvancedAlgebra() {
	}

	public static Curve bisector(final Point p1, final Point p2) {
		final Point normal = p1.sub(p2);
		final Number offset = normal.mul(p1.add(p2)).div(Number.TWO);
		return new Line(normal, offset);
	}

	public static Curve perpendicular(final Point p, final Line l) {
		final Point normal = l.normal().orth();
		final Number offset = normal.mul(p);
		return new Line(normal, offset);
	}

	public static Curve parallel(final Point p, final Line l) {
		if(Algebra.doesContain(p,l)) {
			return null;
		}
		final Point normal = l.normal();
		final Number offset = normal.mul(p);
		return new Line(normal, offset);
	}

	public static CurveSet angleBisector(final Line l1, final Line l2) {
		final List<Point> intersections = Algebra.doIntersect(l1, l2).asList();
		if(intersections.isEmpty()) {
			return CurveSet.EMPTY;
		}
		final Point intersection = intersections.get(0);
		final Point n1 = l1.normal().add(l2.normal());
		final Point n2 = l1.normal().sub(l2.normal());
		final Number o1 = n1.mul(intersection);
		final Number o2 = n2.mul(intersection);
		return new CurveSet(new Line(n1, o1), new Line(n2, o2));
	}

	public static Curve nonCollapsingCompass(final Point p1, final Point p2, final Point center) {
		return new Circle(center, p1.sub(p2).square());
	}

}
