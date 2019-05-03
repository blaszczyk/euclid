package euclid.sets;

import java.util.ArrayList;
import java.util.List;

import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Point;

public class RootBoard extends Board {

	private final PointSet points;
	private final CurveSet curves;

	public RootBoard(final PointSet points, final CurveSet curves, final RootBoard parent) {
		this.points = points;
		this.curves = curves;
	}

	public RootBoard(final PointSet points, final CurveSet curves) {
		this(points, curves, null);
	}

	@Override
	public final PointSet points() {
		return points.copy();
	}

	@Override
	public final CurveSet curves() {
		return curves.copy();
	}

	@Override
	public List<Point> pointList() {
		return new ArrayList<>(points);
	}

	@Override
	public List<Curve> curveList() {
		return new ArrayList<>(curves);
	}
	
	@Override
	public List<Line> lineList() {
		final List<Line> lines = new ArrayList<>(curves.size());
		for(final Curve c : curves) {
			if(c.isLine()) {
				lines.add(c.asLine());
			}
		}
		return lines;
	}

	@Override
	public int pointCount() {
		return points.size();
	}

	@Override
	public int curveCount() {
		return curves.size();
	}

	@Override
	public PointSet pointsInt() {
		return points;
	}

	@Override
	public CurveSet curvesInt() {
		return curves;
	}

	@Override
	public int hashCode() {
		return points.hashCode() + curves.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final RootBoard other = (RootBoard) obj;
		return points.equals(other.points) && curves.equals(other.curves);
	}

	@Override
	public String toString() {
		return String.format("points: %s, curves: %s", points, curves);
	}

}
