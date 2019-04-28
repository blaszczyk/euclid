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
			final CurveSet newCurveSet = current.curves().without(parent.curves());
			if(newCurveSet.size() != 1) {
				throw new IllegalStateException("no unique new curve");
			}
			final Curve newCurve = newCurveSet.first();
			final Board constituents = reconstruct(newCurve, parent, constructor);
			construction = new Reconstruction(newCurve, constituents, current, construction);
			current = parent;
		}
		return construction;
	}

	private static Board reconstruct(final Curve newCurve, final Board parent, final Constructor constructor) {
		final List<Point> points = parent.points().asList();
		final List<Board> candidates = new ArrayList<>();
		forEachDistinctPair(points, (p1,p2) -> {
			final CurveSet constructed = new CurveSet();
			constructor.constructFromTwoDistinctPoints(p1, p2, constructed);
			if(constructed.contains(newCurve)) {
				candidates.add(new Board(new PointSet(p1,p2), CurveSet.EMPTY));
			}
		});

		if(constructor.isAdvanced()) {
			forEachDistinctTriple(points, (p1,p2,p3) -> {
				final CurveSet constructed = new CurveSet();
				constructor.constructFromThreeDistinctPoints(p1, p2, p3, constructed);
				if(constructed.contains(newCurve)) {
					candidates.add(new Board(new PointSet(p1,p2,p3), CurveSet.EMPTY));
				}
			});
			final List<Line> lines = pickLines(parent);
			forEachPair(points, lines, (p,l) -> {
				final CurveSet constructed = new CurveSet();
				constructor.constructFromPointAndLine(p, l, constructed);
				if(constructed.contains(newCurve)) {
					candidates.add(new Board(new PointSet(p), new CurveSet(l)));
				}
			});
			forEachDistinctPair(lines, (l1,l2) -> {
				final CurveSet constructed = new CurveSet();
				constructor.constructFromDistinctLines(l1, l2, constructed);
				if(constructed.contains(newCurve)) {
					candidates.add(new Board(PointSet.EMPTY, new CurveSet(l1,l2)));
				}
			});
		}
		if(candidates.isEmpty()) {
			throw new IllegalStateException("curve inconstructable");
		}
		return candidates.get(0);
	}

	private final Reconstruction next;
	private final Curve curve;
	private final Board constituents;
	private final Board board;

	private Reconstruction(final Curve curve, final Board constituents, final Board board, final Reconstruction next) {
		this.next = next;
		this.curve = curve;
		this.constituents = constituents;
		this.board = board;
	}

	public Reconstruction next() {
		return next;
	}

	public Curve curve() {
		return curve;
	}

	public Board constituents() {
		return constituents;
	}
	
	public Board board() {
		return board;
	}

}
