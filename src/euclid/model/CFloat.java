package euclid.model;

import java.text.DecimalFormat;
import static euclid.model.Sugar.*;

public class CFloat implements Constructable {
	
	private static final double PRECISION = 0.000_001;
	private static final double INV_PRECISION = 1_000_000.;
	private final double value;
	
	CFloat(double value) {
		this.value = value;
	}
	
	// sugar
	private static CFloat c(final Object c) {
		return (CFloat) c;
	}

	@Override
	public Constructable add(final Constructable other) {
		return number(value + c(other).value);
	}

	@Override
	public Constructable sub(final Constructable other) {
		return number(value - c(other).value);
	}

	@Override
	public Constructable mul(final Constructable other) {
		return number(value * c(other).value);
	}

	@Override
	public Constructable div(final Constructable other) {
		return number(value / c(other).value);
	}

	@Override
	public Constructable negate() {
		return number(0 - value);
	}

	@Override
	public Constructable inverse() {
		return number(1 / value);
	}

	@Override
	public Constructable square() {
		return number(value * value);
	}

	@Override
	public Constructable root() {
		return number(Math.sqrt(value));
	}
	
	@Override
	public int sign() {
		if(Math.abs(value) < PRECISION) {
			return 0;
		}
		return value > 0 ? 1 : -1;
	}
	
	@Override
	public double doubleValue() {
		return value;
	}
	
	@Override
	public boolean near(final Constructable other) {
		return Math.abs(value - c(other).value) < PRECISION;
	}
	
	@Override
	public String toString() {
		return new DecimalFormat("#.######").format(value);
	}
	
	@Override
	public int hashCode() {
		final long rounded = rounded();
		final int bits = (int)(rounded >> 32 ^ rounded);
		return bits ^ bits >> 16;
	}

	@Override
	public boolean equals(final Object obj) {
		final CFloat other = c(obj);
		if(this == obj) {
			return true;
		}
		return rounded() == other.rounded();
	}
	
	private long rounded() {
		return Math.round(INV_PRECISION * value);
	}
	

	@Override
	public int compareTo(final Constructable other) {
		if(near(other))
			return 0;
		return Double.compare(value, c(other).value);
	}
	
}
