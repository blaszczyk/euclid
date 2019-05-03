package euclid.algorithm;

import java.util.List;

import euclid.geometry.*;
import euclid.sets.*;
import euclid.sets.CurveSet;
import static euclid.algorithm.ListHelper.*;

public class BasicCurveBasedSearch extends CurveBasedSearch {

	@Override
	void addSuccessors(final Board board, final List<Point> points, final CurveSet successors) {
		forEachDistinctPair(points, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));
		if(constructor.isAdvanced()) {
			final List<Line> lines = board.lineList();
			forEachPair(points, lines, (p,l) -> constructor.constructFromPointAndLine(p, l, successors));
			forEachDistinctTriple(points, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, successors));
			forEachDistinctPair(lines, (l1,l2) -> constructor.constructFromDistinctLines(l1, l2, successors));
		}
	}

}
