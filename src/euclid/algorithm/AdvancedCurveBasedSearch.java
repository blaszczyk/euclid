package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;
import euclid.problem.Problem;

public class AdvancedCurveBasedSearch extends CurveBasedSearch {
	
	private final AdvancedAlgebra advanced;
	
	public AdvancedCurveBasedSearch(final Problem problem, final AdvancedAlgebra algebra) {
		super(problem, algebra);
		advanced = algebra;
	}

	@Override
	Set<Curve> successors(final Board board) {
		final Set<Curve> successors = super.successors(board);
		final List<Line> lines = pickLines(board.curves());
		successors.addAll(bisectors(board.points()));
		successors.addAll(angleBisectors(lines));
		successors.addAll(perpendiculars(board.points(), lines));
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

	private Set<Curve> bisectors(final PointSet set) {
		final Set<Curve> curves = new TreeSet<>();
		forEachDistinctPair(set.asList(), (p1,p2) -> {
			curves.add(advanced.bisector(p1,p2));
		});
		return curves;
	}

	private Set<Curve> angleBisectors(final List<Line> lines) {
		final Set<Curve> curves = new TreeSet<>();
		forEachDistinctPair(lines, (l1,l2) -> {
			advanced.angleBisector(l1, l2).forEach(curves::add);
		});
		return curves;
	}

	private Collection<? extends Curve> perpendiculars(final PointSet points, final List<Line> lines) {
		final Set<Curve> curves = new TreeSet<>();
		for(final Point p : points) {
			for(final Line l : lines) {
				curves.add(advanced.perpendicular(p, l));
			}
		}
		return curves;
	}
	
	
}
