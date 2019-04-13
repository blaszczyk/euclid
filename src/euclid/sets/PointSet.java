package euclid.sets;

import java.util.Collection;

import euclid.geometry.Point;

@SuppressWarnings("serial")
public class PointSet extends ElementSet<Point, PointSet> {
	
	public static final PointSet EMPTY = new PointSet();
	
	public PointSet() {
	}
	
	public PointSet(final Collection<? extends Point> points) {
		super(points);
	}
	
	public PointSet(final Point... points) {
		for(final Point p : points) {
			add(p);
		}
	}
	
	@Override
	public PointSet copy() {
		return new PointSet(this);
	}

}
