package euclid.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import euclid.algorithm.constructor.Constructor;
import euclid.algorithm.priority.Prioritizer;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.ElementSet;

abstract class CurveBasedSearch<B extends Board> extends ListHelper implements Algorithm<B> {

	Problem problem;
	Constructor constructor;
	Prioritizer prioritizer;
	Comparator<Curve> curveComparator;

	void init(final Problem problem, final Constructor constructor, final Prioritizer prioritizer, final Comparator<Curve> curveComparator) {
		this.problem = problem;
		this.constructor = constructor;
		this.prioritizer = prioritizer;
		this.curveComparator = curveComparator;
	}

	@Override
	public int depth(final B board) {
		return board.depth();
	}

	@Override
	public int maxPriority() {
		return prioritizer.maxPriority();
	}

	@Override
	public int priority(final B b) {
		final int pointMisses = misses(b.points(), problem.required().points());
		final int curveMisses = misses(b.curves(), problem.required().curves());
		if(pointMisses + curveMisses == 0) {
			return 0;
		}
		if(Math.max(curveMisses, pointMisses > 0 ? 1 : 0) > problem.maxDepth() - b.depth()) {
			return -1;
		}
		return prioritizer.priotiry(b, pointMisses, curveMisses);
	}

	@Override
	public List<B> nextGeneration(final B board) {
		final CurveSet successors = new CurveSet(curveComparator);
		addSuccessors(board, successors);
		successors.removeAll(board.curves());
		final List<B> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			final B next = next(board, successor);
			nextGeneration.add(next);
		}
		return nextGeneration;
	}

	abstract void addSuccessors(final B board, final CurveSet successors);

	abstract B next(final B parent, final Curve successor);

	static <E extends Element<E>, S extends ElementSet<E,S>> int misses(final S set, final S required) {
		int count = 0;
		for(final E e : required) {
			if(set.containsNot(e)) {
				count++;
			}
		}
		return count;
	}

	static List<Line> pickLines(final Board board) {
		final CurveSet curves = board.curves();
		final List<Line> lines = new ArrayList<>(curves.size());
		for(final Curve c : curves) {
			if(c.isLine()) {
				lines.add(c.asLine());
			}
		}
		return lines;
	}
}
