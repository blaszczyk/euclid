package euclid.algebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.geometry.Ray;
import euclid.geometry.Segment;

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
		return lineOrNull(normal, offset);
	}

	public static Curve ray(final Point p1, final Point p2) {
		final Point normal = p1.sub(p2).orth();
		final Number offset = normal.mul(p1);
		final Number end = normal.cross(p1);
		final boolean orientation = normal.cross(p2).greater(end);
		return new Ray(normal, offset, end, orientation);
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

	public static List<Curve> circles(final Point p1, final Point p2) {
		final Number radiusSquare = p1.sub(p2).square();
		if(!radiusSquare.near(Number.ZERO)) {
			return Arrays.asList(new Circle(p1, radiusSquare), new Circle(p2, radiusSquare));
		}
		return Collections.emptyList();
	}
	
	public static List<Point> intersect(final Curve curve1, final Curve curve2) {
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
			if(line.isRay()) {
				final Ray ray = line.asRay();
				final Number position = line.normal().cross(point);
				return ray.orientation() ? position.greaterEq(ray.end()) : position.lessEq(ray.end());
			}
			if(line.isSegment()) {
				final Segment segment = line.asSegment();
				final Number position = line.normal().cross(point);
				return position.greaterEq(segment.from()) && position.lessEq(segment.to());
			}
			return true;
		}
		return false;
	}

	static boolean doesContain(final Point point, final Circle circle) {
		return point.sub(circle.center()).square().near(circle.radiusSquare());
	}

	static List<Point> doIntersect(final Line l1, final Line l2) {
		final Number det = l1.normal().cross(l2.normal());
		if(det.sign() == 0) {
			return Collections.emptyList();
		}
		final Number x = l1.offset().mul(l2.normal().y()).sub(l2.offset().mul(l1.normal().y())).div(det);
		final Number y = l2.offset().mul(l1.normal().x()).sub(l1.offset().mul(l2.normal().x())).div(det);
		final Point point = new Point(x, y);
		if((l1.hasEnds() && !doesContain(point, l1))
			|| (l2.hasEnds() && !doesContain(point, l2))) {
			return Collections.emptyList();
		}
		return Collections.singletonList(point);
	}

	static List<Point> doIntersect(final Line line, final Circle circle) {
		final boolean hasNoEnds = !line.hasEnds();
		final Point normal = line.normal();
		final Point distance = normal.mul(line.offset().sub(normal.mul(circle.center())));
		final Number discriminant = circle.radiusSquare().sub(distance.square());
		final int sign = discriminant.sign();
		if(sign > 0) {
			final Point midpoint = circle.center().add(distance);
			final Point separation = normal.orth().mul(discriminant.root());
			final Point p1 = midpoint.add(separation);
			final Point p2 = midpoint.sub(separation);
			final List<Point> result = new ArrayList<>(2);
			if(hasNoEnds || doesContain(p1, line)) {
				result.add(p1);
			}
			if(hasNoEnds || doesContain(p2, line)) {
				result.add(p2);
			}
			return result;
		}
		else if(sign == 0) {
			final Point point = circle.center().add(distance);
			if(hasNoEnds || doesContain(point, line)) {
				return Collections.singletonList(circle.center().add(distance));
			}
		}
		return Collections.emptyList();
	}
	
	static List<Point> doIntersect(final Circle c1, final Circle c2) {
		if(c1.center().near(c2.center()))
		{
			return Collections.emptyList();
		}
		final Point normal = c1.center().sub(c2.center());
		final Number offset = c1.radiusSquare().sub(c1.center().square()).sub(c2.radiusSquare().sub(c2.center().square())).div(Number.M_TWO);
		final Line commonSection = new Line(normal, offset);
		return doIntersect(commonSection, c1);
	}

	public static Point endPoint(final Ray ray) {
		final Point tangent = ray.normal().orth();
		return ray.normal().mul(ray.offset()).add(tangent.mul(ray.end()));
	}
	
	public static List<Point> endPoints(final Segment segment) {
		final Point basePoint = segment.normal().mul(segment.offset());
		final Point tangent = segment.normal().orth();
		final Point from = basePoint.add(tangent.mul(segment.from()));
		final Point to = basePoint.add(tangent.mul(segment.to()));
		return Arrays.asList(from, to);
	}
	
	static Line lineOrNull(final Point normal, final Number offset) {
		if(normal.near(Point.ORIGIN)) {
			return null;
		}
		return new Line(normal, offset);
	}
	
}
