package euclid.algorithm.priority;

import java.util.ArrayList;
import java.util.List;

import euclid.algorithm.AlgorithmData;
import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public abstract class Prioritizer {

	AlgorithmData data;
	
	List<Point> points;
	
	List<Curve> curves;
	
	public void init(final AlgorithmData data) {
		this.data = data;
		this.points = new ArrayList<>(data.required().points());
		points.addAll(data.assist().pointList());
		this.curves = new ArrayList<>(data.required().curves());
		curves.addAll(data.assist().curveList());
	}

	public abstract int priotiry(final PointSet points, final CurveSet curves);
	
	public abstract int maxPriority();

}
