package euclid.geometry;

import java.text.DecimalFormat;

public class Number extends AbstractElement<Number> {

	public static final Number M_TWO = new Number(-2);
	public static final Number M_ONE = new Number(-1);
	public static final Number ZERO = new Number(0);
	public static final Number ONE = new Number(1);
	public static final Number TWO = new Number(2);
	
	private static final double PRECISION = 0.000_001;
	private static final double INV_PRECISION = 1_000_000.;
	private final double value;
	
	public Number(double value) {
		this.value = value;
	}

	public Number add(final Number other) {
		return new Number(value + other.value);
	}

	public Number sub(final Number other) {
		return new Number(value - other.value);
	}

	public Number mul(final Number other) {
		return new Number(value * other.value);
	}

	public Number div(final Number other) {
		return new Number(value / other.value);
	}

	public Number negate() {
		return new Number(0 - value);
	}

	public Number inverse() {
		return new Number(1 / value);
	}

	public Number square() {
		return new Number(value * value);
	}

	public Number root() {
		return new Number(Math.sqrt(value));
	}
	
	public Number min(final Number other) {
		return compareTo(other) < 0 ? this : other;
	}
	public Number max(final Number other) {
		return compareTo(other) > 0 ? this : other;
	}

	public int sign() {
		if(Math.abs(value) < PRECISION) {
			return 0;
		}
		return value > 0 ? 1 : -1;
	}

	public double doubleValue() {
		return value;
	}
	
	@Override
	public boolean near(final Number other) {
		return Math.abs(value - other.value) < PRECISION;
	}
	
	@Override
	public String toString() {
		return new DecimalFormat("#.######").format(value);
	}
	
	@Override
	public int hashCode() {
		final long rounded = Math.round(INV_PRECISION * value);
		final int bits = (int)(rounded >> 32 ^ rounded);
		return bits ^ bits >> 16;
	}

	@Override
	public int compareTo(final Number other) {
		if(near(other))
			return 0;
		return Double.compare(value, other.value);
	}
	
}
