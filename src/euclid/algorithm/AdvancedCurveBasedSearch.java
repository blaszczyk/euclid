package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import euclid.algebra.AdvancedAlgebra;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;

public class AdvancedCurveBasedSearch extends CurveBasedSearch {
	
	public AdvancedCurveBasedSearch(final Problem problem) {
		super(problem);
	}

	@Override
	Set<Curve> successors(final Board board) {
		final Set<Curve> successors = super.successors(board);
		final List<Line> lines = pickLines(board.curves());
		final List<Point> points = board.points().asList();
		successors.addAll(bisectors(points));
		successors.addAll(angleBisectors(lines));
		successors.addAll(perpendicularsAndParallels(points, lines));
		return successors;
	}

	private List<Line> pickLines(final CurveSet curves) {
		final List<Line> lines = new ArrayList<>(curves.size());
		for(final Curve c : curves) {
			if(c.isLine()) {
				lines.add(c.asLine());
			}
		}
		return lines;
	}

	private Set<Curve> bisectors(final List<Point> points) {
		final Set<Curve> curves = new TreeSet<>();
		forEachDistinctPair(points, (p1,p2) -> {
			curves.add(AdvancedAlgebra.bisector(p1,p2));
		});
		return curves;
	}

	private Set<Curve> angleBisectors(final List<Line> lines) {
		final Set<Curve> curves = new TreeSet<>();
		forEachDistinctPair(lines, (l1,l2) -> {
			AdvancedAlgebra.angleBisector(l1, l2).forEach(curves::add);
		});
		return curves;
	}

	private Collection<? extends Curve> perpendicularsAndParallels(final List<Point> points, final List<Line> lines) {
		final Set<Curve> curves = new TreeSet<>();
		for(final Point p : points) {
			for(final Line l : lines) {
				curves.add(AdvancedAlgebra.perpendicular(p, l));
				AdvancedAlgebra.parallel(p, l).forEach(curves::add);
			}
		}
		return curves;
	}

}
