package euclid.sets;

import java.util.ArrayList;
import java.util.List;

import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Point;

public class ChildBoard extends Board {

	private final Curve curve;
	
	private final PointSet newPoints;
	
	private final Board parent;
	
	public ChildBoard(final PointSet newPoints, final Curve curve, final Board parent) {
		this.newPoints = newPoints;
		this.curve = curve;
		this.parent = parent;
	}

	@Override
	public Curve curve() {
		return curve;
	}

	@Override
	public PointSet points() {
		return parent.points().adjoin(newPoints);
	}

	@Override
	public CurveSet curves() {
		return parent.curves().adjoin(curve);
	}

	@Override
	public Board parent() {
		return parent;
	}

	@Override
	public int depth() {
		return parent.depth() + 1;
	}

	@Override
	public boolean hasParent() {
		return true;
	}
	
	@Override
	PointSet pointsInt() {
		return newPoints;
	}

	@Override
	public List<Point> pointList() {
		final List<Point> list = new ArrayList<>(pointCount());
		Board b = this;
		while(b != null) {
			list.addAll(b.pointsInt());
			b = b.parent();
		}
		return list;
	}

	@Override
	public List<Curve> curveList() {
		final List<Curve> list = new ArrayList<>(curveCount());
		Board b = this;
		while(b.hasParent()) {
			list.add(b.curve());
			b = b.parent();
		}
		list.addAll(b.curvesInt());
		return list;
	}
	
	@Override
	public List<Line> lineList() {
		final List<Line> lines = new ArrayList<>(curveCount());
		Board b = this;
		while(b.hasParent()) {
			final Curve c = b.curve();
			if(c.isLine()) {
				lines.add(c.asLine());	
			}
			b = b.parent();
		}
		for(final Curve c : b.curvesInt()) {
			if(c.isLine()) {
				lines.add(c.asLine());
			}
		}
		return lines;
	}
	
	@Override
	public List<Point> newPoints() {
		return new ArrayList<>(newPoints);
	}

	@Override
	public int pointCount() {
		return parent.pointCount() + newPoints.size();
	}

	@Override
	public int curveCount() {
		return parent.curveCount() + 1;
	}
	
	@Override
	public PointSet allNewPoints() {
		return parent.allNewPoints().adjoin(newPoints);
	}
	
	@Override
	public CurveSet allNewCurves() {
		return parent.allNewCurves().adjoin(curve);
	}
}
