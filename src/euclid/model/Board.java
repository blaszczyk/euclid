package euclid.model;

import java.util.stream.Collectors;

public class Board {

	public static BoardBuilder withPoints(final Point... points) {
		return withPoints(PointSet.of(points));
	}
	
	public static BoardBuilder withPoints(final PointSet points) {
		return new BoardBuilder(points);
	}
	
	private final CurveSet curves;
	private final PointSet points;
	
	private Board(CurveSet curves, PointSet points) {
		this.curves = curves;
		this.points = points;
	}

	public final CurveSet curves() {
		return curves;
	}

	public final PointSet points() {
		return points;
	}
	
	public void print() {
		for(Curve c : curves) {
			System.out.println(c);
			System.out.println(points.stream().filter(c::contains).collect(Collectors.toList()));
		}
			
	}
	
	public static class BoardBuilder {
		final PointSet points;
		
		private BoardBuilder(final PointSet ps) {
			points = ps;
		}

		public Board andCurves(final CurveSet curves) {
			return new Board(curves,points);
		}
		public Board andCurves(final Curve... curves) {
			return andCurves(CurveSet.of(curves));
		}
	}
	
}
