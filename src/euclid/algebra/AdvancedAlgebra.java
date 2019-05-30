package euclid.algebra;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;

public class AdvancedAlgebra {
	
	private AdvancedAlgebra() {
	}

	public static Curve bisector(final Point p1, final Point p2) {
		final Point normal = p1.sub(p2);
		final Number offset = normal.mul(p1.add(p2)).div(Number.TWO);
		return Algebra.lineOrNull(normal, offset);
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

	public static List<Curve> angleBisectors(final Line l1, final Line l2) {
		final List<Point> intersections = Algebra.doIntersect(l1, l2);
		if(intersections.isEmpty()) {
			return Collections.emptyList();
		}
		final Point intersection = intersections.get(0);
		final Point n1 = l1.normal().add(l2.normal());
		final Point n2 = l1.normal().sub(l2.normal());
		final Number o1 = n1.mul(intersection);
		final Number o2 = n2.mul(intersection);
		return Arrays.asList(new Line(n1, o1), new Line(n2, o2));
	}

	public static Curve angleBisector(final Point p1, final Point p2, final Point center) {
		if(center.near(p1) || center.near(p2)) {
			return null;
		}
		final Point n1 = p1.sub(center).orth();
		final Point n2 = p2.sub(center).orth();
		final Point n1n = n1.div(n1.square().root());
		final Point n2n = n2.div(n2.square().root());
		Point normal = n1n.add(n2n);
		if(normal.near(Point.ORIGIN)) {
			normal = n1n.orth();
		}
		final Number offset = normal.mul(center);
		return new Line(normal, offset);
	}

	public static Curve nonCollapsingCompass(final Point p1, final Point p2, final Point center) {
		final Number radiusSquare = p1.sub(p2).square();
		return radiusSquare.near(Number.ZERO) ? null : new Circle(center, radiusSquare);
	}

}
