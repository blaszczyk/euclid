package euclid.algebra;

import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.geometry.Segment;
import euclid.sets.PointSet;

public class Algebra {
	
	private Algebra() {
	}

	public static boolean contains(final Point point, final Curve curve) {
		if(curve.isLine()) {
			return doesContain(point, curve.asLine());
		}
		else {
			return doesContain(point, curve.asCircle());
		}
	}
	
	public static Curve line(final Point p1, final Point p2) {
		final Point normal = p1.sub(p2).orth();
		final Number offset = normal.mul(p1);
		return new Line(normal, offset);
	}
	
	public static Curve segment(final Point p1, final Point p2) {
		final Point normal = p1.sub(p2).orth();
		final Number offset = normal.mul(p1);
		final Number end1 = normal.cross(p1);
		final Number end2 = normal.cross(p2);
		return new Segment(normal, offset, end1, end2);
	}
	
	public static Curve circle(final Point center, final Point p) {
		final Number radiusSquare = p.sub(center).square();
		return new Circle(center, radiusSquare);
	}
	
	public static PointSet intersect(final Curve curve1, final Curve curve2) {
		if(curve1.isLine()) {
			if(curve2.isLine()) {
				return doIntersect(curve1.asLine(), curve2.asLine());
			}
			else {
				return doIntersect(curve1.asLine(), curve2.asCircle());
			}
		}
		else {
			if(curve2.isLine()) {
				return doIntersect(curve2.asLine(), curve1.asCircle());
			}
			else {
				return doIntersect(curve1.asCircle(), curve2.asCircle());
			}
		}
	}
	
	static boolean doesContain(final Point point, final Line line) {
		if(point.mul(line.normal()).near(line.offset())) {
			if(line.isSegment()) {
				final Segment segment = line.asSegment();
				final Number position = line.normal().cross(point);
				return position.compareTo(segment.from()) >= 0 && position.compareTo(segment.to()) <= 0;
			}
			return true;
		}
		return false;
	}

	static boolean doesContain(final Point point, final Circle circle) {
		return point.sub(circle.center()).square().near(circle.radiusSquare());
	}

	static PointSet doIntersect(final Line l1, final Line l2) {
		final Number det = l1.normal().cross(l2.normal());
		if(det.sign() == 0) {
			return PointSet.empty();
		}
		final Number x = l1.offset().mul(l2.normal().y()).sub(l2.offset().mul(l1.normal().y())).div(det);
		final Number y = l2.offset().mul(l1.normal().x()).sub(l1.offset().mul(l2.normal().x())).div(det);
		final Point point = new Point(x, y);
		if((l1.isSegment() && !doesContain(point, l1))
			|| (l2.isSegment() && !doesContain(point, l2))) {
			return PointSet.empty();
		}
		return PointSet.of(point);
	}

	static PointSet doIntersect(final Line line, final Circle circle) {
		final Point normal = line.normal();
		final Point distance = normal.mul(line.offset().sub(normal.mul(circle.center())));
		final Number discriminant = circle.radiusSquare().sub(distance.square());
		final int sign = discriminant.sign();
		if(sign > 0) {
			final Point midpoint = circle.center().add(distance);
			final Point separation = normal.orth().mul(discriminant.root());
			final Point p1 = midpoint.add(separation);
			final Point p2 = midpoint.sub(separation);
			if(line.isSegment()) {
				final boolean hasP1 = doesContain(p1, line);
				final boolean hasP2 = doesContain(p2, line);
				return hasP1 ? ( hasP2 ? PointSet.of(p1,p2) : PointSet.of(p1))
						:  ( hasP2 ? PointSet.of(p2) : PointSet.empty());
			}
			else {
				return PointSet.of(p1, p2);
			}
		}
		else if(sign == 0) {
			final Point point = circle.center().add(distance);
			if(!line.isSegment() || doesContain(point, line)) {
				return PointSet.of(circle.center().add(distance));
			}
		}
		return PointSet.empty();
	}
	
	static PointSet doIntersect(final Circle c1, final Circle c2) {
		if(c1.center().near(c2.center()))
		{
			return PointSet.empty();
		}
		final Point normal = c1.center().sub(c2.center()).mul(Number.M_TWO);
		final Number offset = c1.radiusSquare().sub(c1.center().square()).sub(c2.radiusSquare().sub(c2.center().square()));
		final Line commonSection = new Line(normal, offset);
		return doIntersect(commonSection, c1);
	}
	
}