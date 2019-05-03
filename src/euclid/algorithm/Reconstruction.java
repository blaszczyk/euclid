package euclid.algorithm;

import java.util.ArrayList;
import java.util.List;

import euclid.algorithm.constructor.Constructor;
import euclid.geometry.*;
import euclid.sets.*;
import static euclid.algorithm.ListHelper.*;

public class Reconstruction {

	public static Reconstruction from(final Board solution, final Constructor constructor) {
		Board current = solution;
		Reconstruction construction = null;
		while(current.hasParent()) {
			final Board parent = current.parent();
			final Curve newCurve = current.curve();
			final Board constituents = reconstruct(newCurve, parent, constructor);
			construction = new Reconstruction(constituents, current, construction);
			current = parent;
		}
		return construction;
	}

	private static Board reconstruct(final Curve newCurve, final Board parent, final Constructor constructor) {
		final List<Point> points = parent.pointList();
		final List<Board> candidates = new ArrayList<>();
		forEachDistinctPair(points, (p1,p2) -> {
			final CurveSet constructed = new CurveSet();
			constructor.constructFromTwoDistinctPoints(p1, p2, constructed);
			if(constructed.contains(newCurve)) {
				candidates.add(new RootBoard(new PointSet(p1,p2), CurveSet.EMPTY));
			}
		});

		if(constructor.isAdvanced()) {
			forEachDistinctTriple(points, (p1,p2,p3) -> {
				final CurveSet constructed = new CurveSet();
				constructor.constructFromThreeDistinctPoints(p1, p2, p3, constructed);
				if(constructed.contains(newCurve)) {
					candidates.add(new RootBoard(new PointSet(p1,p2,p3), CurveSet.EMPTY));
				}
			});
			final List<Line> lines = parent.lineList();
			forEachPair(points, lines, (p,l) -> {
				final CurveSet constructed = new CurveSet();
				constructor.constructFromPointAndLine(p, l, constructed);
				if(constructed.contains(newCurve)) {
					candidates.add(new RootBoard(new PointSet(p), new CurveSet(l)));
				}
			});
			forEachDistinctPair(lines, (l1,l2) -> {
				final CurveSet constructed = new CurveSet();
				constructor.constructFromDistinctLines(l1, l2, constructed);
				if(constructed.contains(newCurve)) {
					candidates.add(new RootBoard(PointSet.EMPTY, new CurveSet(l1,l2)));
				}
			});
		}
		if(candidates.isEmpty()) {
			throw new IllegalStateException("curve inconstructable");
		}
		return candidates.get(0);
	}

	private final Reconstruction next;
	private final Board constituents;
	private final Board board;

	private Reconstruction(final Board constituents, final Board board, final Reconstruction next) {
		this.next = next;
		this.constituents = constituents;
		this.board = board;
	}

	public Reconstruction next() {
		return next;
	}

	public Board constituents() {
		return constituents;
	}
	
	public Curve curve() {
		return board.curve();
	}
	
	public List<Point> newPoints() {
		return board.newPoints();
	}

}
