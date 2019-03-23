package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import euclid.model.*;

public class PointBasedSearch extends AbstractSearch {
	
	@Override
	public Collection<Board> find(final Board initial, final Board required, final int depth, final boolean first) {
		final List<Board> solutions = new ArrayList<>();
		final Queue<PointSet> queue = new LinkedList<>();
		queue.add(initial.points());
		while(!queue.isEmpty())
		{
			final PointSet points = queue.remove();
			final CurveSet curves = points.curves().adjoin(initial.curves());
			if(curves.containsAll(required.curves()) && points.containsAll(required.points())) {
				solutions.add(Board.withPoints(points).andCurves(curves));
				if(first)
					break;
				else
					continue;
			}
			if(points.size() > depth)
				continue;
			final PointSet successors = curves.intersections();
			successors.removeAll(points);
			for(final Point p : successors) {
				final PointSet next = points.adjoin(p);
				if(!queue.contains(next))
					queue.add(next);
			}
		}
		return solutions;
	}
}
