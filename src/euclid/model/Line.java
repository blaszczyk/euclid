package euclid.model;

import static euclid.model.ElementLifeTimeManager.*;

public class Line extends IntersectionCache implements Curve {
	
	final Point normal;
	final Constructable offset;
	final Constructable normalSquare;
	
	Line(final Point x, final Point y) {
		normal = x.sub(y).orth();
		offset = normal.mul(x);
		normalSquare = normal.square();
	}

	Line(final Point normal, final Constructable offset) {
		this.normal = normal;
		this.offset = offset;
		normalSquare = normal.square();
	}
	
	@Override
	public boolean contains(final Point point) {
		return point.mul(normal).isEqual(offset);
	}

	@Override
	public PointSet doIntersect(final Curve other) {
		if(other instanceof Line) {
			final Line line = (Line) other;
			if(!normal.colinear(line.normal)) {
				final Constructable det = normal.cross(line.normal);
				final Constructable x = offset.mul(line.normal.y).sub(line.offset.mul(normal.y)).div(det);
				final Constructable y = line.offset.mul(normal.x).sub(offset.mul(line.normal.x)).div(det);
				return PointSet.of(p(x, y));
			}
		}
		else if(other instanceof Circle) {
			final Circle circle = (Circle) other;
			return circle.intersect(this);
		}
		return PointSet.empty();
	}

	@Override
	public boolean isEqual(final Curve other) {
		if(other instanceof Line) {
			final Line line = (Line)other;
			if(offset.isEqual(zero()) && line.offset.isEqual(zero())) {
				return normal.colinear(line.normal);
			}
			return line.normal.mul(offset).isEqual(normal.mul(line.offset));
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "line " + normal + " * p = " + offset;
	}

}
