package euclid.model;

public class CFloat implements Constructable {
	
	private static final double TOLERANCE = 1E-10;
	
	private static final double PRECISION = 0x1_000_000;
	
	private final double value;
	
	CFloat(double value) {
		this.value = value;
	}
	
	// sugar
	private static Constructable f(double value) {
		return new CFloat(value);
	}
	
	private static CFloat c(Constructable c) {
		return (CFloat) c;
	}

	@Override
	public Constructable add(Constructable other) {
		return f(value + c(other).value);
	}

	@Override
	public Constructable sub(Constructable other) {
		return f(value - c(other).value);
	}

	@Override
	public Constructable mul(Constructable other) {
		return f(value * c(other).value);
	}

	@Override
	public Constructable div(Constructable other) {
		return f(value / c(other).value);
	}

	@Override
	public Constructable negate() {
		return f(0 - value);
	}

	@Override
	public Constructable inverse() {
		return f(1 / value);
	}

	@Override
	public Constructable square() {
		return f(value * value);
	}

	@Override
	public Constructable root() {
		return f(Math.sqrt(value));
	}
	
	@Override
	public boolean isEqual(Constructable other) {
		return Math.abs(value - c(other).value) < TOLERANCE;
	}

	@Override
	public int hashCode() {
//		return (int) (value / TOLERANCE);
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CFloat other = (CFloat) obj;
		return isEqual(other);
	}
	
	@Override
	public String toString() {
		return Double.toString(Math.round(value*PRECISION) / PRECISION);
	}

	@Override
	public int compareTo(Constructable other) {
		if(isEqual(other))
			return 0;
		return Double.compare(value, c(other).value);
	}
	
}
