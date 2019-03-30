package euclid.model;

import java.util.Collection;

public class Board {
	
	public static final Board EMPTY = Board.withPoints().andCurves();

	public static BoardBuilder withPoints(final Point... points) {
		return withPoints(PointSet.of(points));
	}

	public static BoardBuilder withPoints(final Collection<Point> points) {
		return withPoints(PointSet.of(points));
	}
	
	public static BoardBuilder withPoints(final PointSet points) {
		return new BoardBuilder(points);
	}
	
	public static class BoardBuilder {
		final PointSet points;
		
		private BoardBuilder(final PointSet points) {
			this.points = points;
		}
		
		public Board andCurves(final Curve... curves) {
			return andCurves(CurveSet.of(curves));
		}

		public Board andCurves(final Collection<Curve> curves) {
			return andCurves(CurveSet.of(curves));
		}

		public Board andCurves(final CurveSet curves) {
			return new Board(points, curves);
		}
	}

	private final PointSet points;
	private final CurveSet curves;
	
	private Board(final PointSet points, final CurveSet curves) {
		this.points = points;
		this.curves = curves;
	}

	public final PointSet points() {
		return points;
	}

	public final CurveSet curves() {
		return curves;
	}
	
	public Board identifyByPoints() {
		return new Board(points, curves) {
			@Override
			public int hashCode() {
				return points.hashCode();
			}

			@Override
			public boolean equals(final Object obj) {
				final Board other = (Board) obj;
				return points.equals(other.points);
			}
		};
	}
	
	public Board identifyByCurves() {
		return new Board(points, curves) {
			@Override
			public int hashCode() {
				return curves.hashCode();
			}

			@Override
			public boolean equals(final Object obj) {
				final Board other = (Board) obj;
				return curves.equals(other.curves);
			}
		};
	}
	
	@Override
	public int hashCode() {
		return points.hashCode() + curves.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final Board other = (Board) obj;
		return points.equals(other.points) && curves.equals(other.curves);
	}
	
	@Override
	public String toString() {
		return String.format("points: %s, curves: %s", points, curves);
	}
}
