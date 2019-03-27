package euclid.model;

import static euclid.model.Sugar.*;

public class Line implements Curve {
	
	final Point normal;
	final Constructable offset;
	final Constructable normalSquare;

	Line(final Point normal, final Constructable offset) {
		this.normal = normal;
		this.offset = offset;
		normalSquare = normal.square();
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
