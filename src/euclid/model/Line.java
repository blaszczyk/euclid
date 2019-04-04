package euclid.model;

public class Line implements Curve {
	
	final Point normal;
	final Constructable offset;

	Line(final Point normal, final Constructable offset) {
		final Constructable norm = norm(normal, offset);
		this.normal = normal.div(norm);
		this.offset = offset.div(norm);
	}
	
	public Point normal() {
		return normal;
	}
	
	public Constructable offset() {
		return offset;
	}
	
	private static Constructable norm(final Point normal, final Constructable offset) {
		final Constructable norm = normal.square().root();
		final boolean negate;
		final int signO = offset.sign();
		if(signO == 0) {
			final int signX = normal.x.sign();
			if(signX == 0) {
				negate = normal.y.sign() < 0;
			}
			else {
				negate = signX < 0;
			}
		}
		else {
			negate = signO < 0;
		}
		return negate ? norm.negate() : norm;
	}

	@Override
	public boolean isLine() {
		return true;
	}
	
	@Override
	public Line asLine() {
		return this;
	}
	
	@Override
	public boolean near(final Curve other) {
		if(other.isLine()) {
			final Line line = other.asLine();
			return offset.near(line.offset) && normal.near(line.normal);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 137 * normal.hashCode() + 353 * offset.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		final Curve other = (Curve) obj;
		if(other.isLine()) {
			final Line line = other.asLine();
			return offset.equals(line.offset) && normal.equals(line.normal);
		}
		return false;
	}

	@Override
	public String toString() {
		return "line " + normal + " * p = " + offset;
	}

	@Override
	public int compareTo(final Curve other) {
		if(other.isCircle()) {
			return -1;
		};
		final Line line = other.asLine();
		final int compNormal = normal.compareTo(line.normal);
		return compNormal != 0 ? compNormal : offset.compareTo(line.offset);
	}

}
