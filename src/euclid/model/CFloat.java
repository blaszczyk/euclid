package euclid.model;

import java.text.DecimalFormat;
import static euclid.model.Sugar.*;

public class CFloat implements Constructable {
	
	private static final double TOLERANCE = 1E-10;
	
	private final double value;
	
	CFloat(double value) {
		this.value = value;
	}
	
	// sugar
	private static CFloat c(final Constructable c) {
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
	public boolean isEqual(final Constructable other) {
		return Math.abs(value - c(other).value) < TOLERANCE;
	}
	
	@Override
	public double doubleValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return new DecimalFormat("#.#####").format(value);
	}

	@Override
	public int compareTo(Constructable other) {
		if(isEqual(other))
			return 0;
		return Double.compare(value, c(other).value);
	}
	
}
