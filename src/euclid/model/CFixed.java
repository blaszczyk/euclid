package euclid.model;

public class CFixed implements Constructable {
	
	private static final long BIAS_ROOT = 0x1_0000L;
	private static final long BIAS = BIAS_ROOT * BIAS_ROOT;
	
	private final long value;
	
	CFixed(double value) {
		this.value = (long)(BIAS * value);
	}
	
	private CFixed(long value) {
		this.value = value;
	}
	
	// sugar
	private static Constructable f(long value) {
		return new CFixed(value);
	}
	
	private static CFixed c(Constructable c) {
		return (CFixed) c;
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
		long v1 = value / BIAS_ROOT;
		long v2 = c(other).value / BIAS_ROOT;
		return f(v1 * v2);
	}

	@Override
	public Constructable div(Constructable other) {
		long v1 = value * BIAS_ROOT;
		long v2 = c(other).value / BIAS_ROOT;
		return f(v1 / v2);
	}

	@Override
	public Constructable negate() {
		return f(0 - value);
	}

	@Override
	public Constructable inverse() {
		return f(BIAS * BIAS_ROOT / value * BIAS_ROOT);
	}

	@Override
	public Constructable square() {
		return mul(this);
	}

	@Override
	public Constructable root() {
		return f(BIAS_ROOT * ((long)Math.sqrt(value)));
	}
	
	@Override
	public boolean isEqual(Constructable other) {
		return Math.abs(value - c(other).value) < BIAS_ROOT;
	}

	@Override
	public int hashCode() {
		return (int)value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CFixed other = (CFixed) obj;
		return isEqual(other);
	}
	
	@Override
	public String toString() {
		return Double.toString(value/((double)BIAS));
	}

	@Override
	public int compareTo(Constructable other) {
		if(isEqual(other))
			return 0;
		return Double.compare(value, c(other).value);
	}
	

}
