package euclid.sets;

import java.util.Collections;
import java.util.List;

import euclid.geometry.*;

public abstract class Board {
	
	public static final Board EMPTY = new RootBoard(PointSet.EMPTY, CurveSet.EMPTY);

	public abstract PointSet points();

	public abstract CurveSet curves();

	public abstract List<Point> pointList();

	public abstract List<Curve> curveList();
	
	public abstract List<Line> lineList();

	public List<Point> newPoints() { return Collections.emptyList(); }

	public abstract int pointCount();

	public abstract int curveCount();
	
	public Board parent() { return null; }
	
	public int depth() { return 0; }

	public boolean hasParent() { return false; }

	public Curve curve() { return null; }

	public PointSet allNewPoints() { return new PointSet(); }
	
	public CurveSet allNewCurves() { return new CurveSet(); }
	
	PointSet pointsInt() { return PointSet.EMPTY; }
	
	CurveSet curvesInt() { return CurveSet.EMPTY; }
	
}
