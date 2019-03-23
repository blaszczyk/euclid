package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import euclid.model.*;

public class CurvedBasedSearch extends AbstractSearch{
	
	@Override
	Collection<Board> find(final Board initial, final Board required, final int depth, final boolean first) {
		final List<Board> solutions = new ArrayList<>();
		final Queue<CurveSet> queue = new LinkedList<>();
		queue.add(initial.curves());
		while(!queue.isEmpty())
		{
			final CurveSet curves = queue.remove();
			final PointSet points = curves.intersections().adjoin(initial.points());
			if(curves.containsAll(required.curves()) && points.containsAll(required.points())) {
				solutions.add(Board.withPoints(points).andCurves(curves));
				if(first)
					break;
				else
					continue;
			}
			if(curves.size() > depth)
				continue;
			final CurveSet successors = points.curves();
			successors.removeAll(curves);
			for(final Curve c : successors) {
				final CurveSet next = curves.adjoin(c);
				if(!queue.contains(next))
					queue.add(next);
			}
		}
		
		return solutions;
	}
}
