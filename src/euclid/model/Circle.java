package euclid.model;

import static euclid.model.ElementLifeTimeManager.*;

public class Circle extends IntersectionCache implements Curve {
	
	final Point center;
	final Constructable radiusSquare;
	
	Circle(final Point center, final Point outer) {
		this.center = center;
		this.radiusSquare = outer.sub(center).square();
	}
	
	@Override
	public boolean contains(final Point point) {
		return point.sub(center).square().isEqual(radiusSquare);
	}

	@Override
	public PointSet doIntersect(final Curve other) {
		if(other instanceof Line) {
			final Line line = (Line) other;
			final Point normal = line.normal;
			final Constructable normalSquare = line.normalSquare;
			final Point distance = normal.mul(line.offset.sub(normal.mul(center)).div(normalSquare));
			final Constructable discriminant = radiusSquare.sub(distance.square());
			final int comp = discriminant.compareTo(zero());
			if(comp > 0) {
				final Point midpoint = center.add(distance);
				final Point separation = normal.orth().mul(discriminant.div(normalSquare).root());
				return PointSet.of(midpoint.add(separation), midpoint.sub(separation));
			}
			else if(comp == 0) {
				return PointSet.of(center.add(distance));
			}
		}
		else if(other instanceof Circle) {
			final Circle circle = (Circle) other;
			if(!center.equals(circle.center))
			{
				final Point normal = center.sub(circle.center).mul(m_two());
				final Constructable distance = radiusSquare.sub(center.square()).sub(circle.radiusSquare.sub(circle.center.square()));
				final Line commonSection = new Line(normal, distance);
				return intersect(commonSection);
			}
		}
		return PointSet.empty();
	}
	
	@Override
	public boolean isEqual(final Curve other) {
		if(other instanceof Circle) {
			final Circle circle = (Circle) other;
			return circle.center.isEqual(center) && circle.radiusSquare.isEqual(radiusSquare);
		}
		return false;
	}

	@Override
	public String toString() {
		return "circle ( p - " + center + " )^2 = " + radiusSquare;
	}

}
