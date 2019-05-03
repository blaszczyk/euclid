package euclid.algorithm;

import static euclid.algorithm.ListHelper.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import euclid.algebra.Algebra;
import euclid.algorithm.constructor.Constructor;
import euclid.algorithm.priority.Prioritizer;
import euclid.geometry.*;
import euclid.sets.Board;
import euclid.sets.ChildBoard;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

abstract class CurveBasedSearch implements Algorithm<Board> {

	AlgorithmData data;
	Constructor constructor;
	Prioritizer prioritizer;
	Comparator<Curve> curveComparator;

	void init(final AlgorithmData data, final Constructor constructor, final Prioritizer prioritizer, final Comparator<Curve> curveComparator) {
		this.data = data;
		this.constructor = constructor;
		this.prioritizer = prioritizer;
		this.curveComparator = curveComparator;
	}

	@Override
	public Board first() {
		return data.initial();
	}

	@Override
	public int depth(final Board board) {
		return board.depth();
	}

	@Override
	public int maxPriority() {
		return prioritizer.maxPriority();
	}

	@Override
	public int priority(final Board board) {
		final PointSet points = board.points();
		final CurveSet curves = board.curves();
		final int pointMisses = misses(points, data.requiredPoints());
		final int curveMisses = misses(curves, data.requiredCurves());
		if(pointMisses + curveMisses == 0) {
			return 0;
		}
		if(Math.max(curveMisses, pointMisses > 0 ? 1 : 0) > data.maxDepth() - board.depth()) {
			return -1;
		}
		return prioritizer.priotiry(points, curves, pointMisses, curveMisses);
	}

	@Override
	public List<Board> nextGeneration(final Board board) {
		final CurveSet successors = new CurveSet(curveComparator);
		final List<Point> points = board.pointList();
		final List<Curve> curves = board.curveList();
		addSuccessors(board, points, successors);
		successors.removeAll(curves);
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			final PointSet newPoints = new PointSet();
			for(final Curve curve : curves) {
				newPoints.addAll(Algebra.intersect(successor, curve));
			}
			newPoints.removeAll(points);
			final Board next = new ChildBoard(newPoints, successor, board);
			nextGeneration.add(next);
		}
		return nextGeneration;
	}

	abstract void addSuccessors(final Board board, final List<Point> points, final CurveSet successors);
}
