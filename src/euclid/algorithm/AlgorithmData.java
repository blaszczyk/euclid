package euclid.algorithm;

import java.util.List;

import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.sets.Board;

public class AlgorithmData {

	private final Board initial;
	private final List<Point> requiredPoints;
	private final List<Curve> requiredCurves;
	private final int maxDepth;

	AlgorithmData(final Board initial, final List<Point> requiredPoints, final List<Curve> requiredCurves, final int maxDepth) {
		this.initial = initial;
		this.requiredPoints = requiredPoints;
		this.requiredCurves = requiredCurves;
		this.maxDepth = maxDepth;
	}

	public Board initial() {
		return initial;
	}

	public List<Point> requiredPoints() {
		return requiredPoints;
	}

	public List<Curve> requiredCurves() {
		return requiredCurves;
	}

	public int maxDepth() {
		return maxDepth;
	}

}
