package euclid.model;

import static euclid.model.Sugar.*;

public class Algebra {
	
	private final CurveLifeCycle lifeCycle;
	
	public Algebra(final CurveLifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public boolean contains(final Point point, final Curve curve) {
		if(curve.isLine()) {
			return doesContain(point, curve.asLine());
		}
		else {
			return doesContain(point, curve.asCircle());
		}
	}
	
	public Curve line(final Point p1, final Point p2) {
		final Point	normal = p1.sub(p2).orth();
		final Constructable	offset = normal.mul(p1);
		return lifeCycle.line(normal, offset);
	}

	
	public Curve circle(final Point center, final Point p) {
		final Constructable radiusSquare = p.sub(center).square();
		return lifeCycle.curve(center, radiusSquare);
	}
	
	public PointSet intersect(final Curve curve1, final Curve curve2) {
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
	
	private boolean doesContain(final Point point, final Line line) {
		return point.mul(line.normal).isEqual(line.offset);
	}
	
	private boolean doesContain(final Point point, final Circle circle) {
		return point.sub(circle.center).square().isEqual(circle.radiusSquare);
	}

	private PointSet doIntersect(final Line l1, final Line l2) {
		final Constructable det = l1.normal.cross(l2.normal);
		if(det.isEqual(zero())) {
			return PointSet.empty();
		}
		final Constructable x = l1.offset.mul(l2.normal.y).sub(l2.offset.mul(l1.normal.y)).div(det);
		final Constructable y = l2.offset.mul(l1.normal.x).sub(l1.offset.mul(l2.normal.x)).div(det);
		return PointSet.of(point(x, y));
	}
	
	private PointSet doIntersect(final Line line, final Circle circle) {
		final Point normal = line.normal;
		final Point distance = normal.mul(line.offset.sub(normal.mul(circle.center)));
		final Constructable discriminant = circle.radiusSquare.sub(distance.square());
		final int comp = discriminant.compareTo(zero());
		if(comp > 0) {
			final Point midpoint = circle.center.add(distance);
			final Point separation = normal.orth().mul(discriminant.root());
			return PointSet.of(midpoint.add(separation), midpoint.sub(separation));
		}
		else if(comp == 0) {
			return PointSet.of(circle.center.add(distance));
		}
		else {
			return PointSet.empty();
		}
	}
	
	private PointSet doIntersect(final Circle c1, final Circle c2) {
		if(c1.center.isEqual(c2.center))
		{
			return PointSet.empty();
		}
		final Point normal = c1.center.sub(c2.center).mul(m_two());
		final Constructable offset = c1.radiusSquare.sub(c1.center.square()).sub(c2.radiusSquare.sub(c2.center.square()));
		final Line commonSection = lifeCycle.line(normal, offset);
		return doIntersect(commonSection, c1);
	}
	
}
