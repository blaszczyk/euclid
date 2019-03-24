package euclid.model;

import java.text.DecimalFormat;
import static euclid.model.ElementLifeTimeManager.*;

public class CFloat implements Constructable {
	
	private static final double TOLERANCE = 1E-10;
	
	private final double value;
	
	CFloat(double value) {
		this.value = value;
	}
	
	// sugar
	private static CFloat c(Constructable c) {
		return (CFloat) c;
	}

	@Override
	public Constructable add(Constructable other) {
		return n(value + c(other).value);
	}

	@Override
	public Constructable sub(Constructable other) {
		return n(value - c(other).value);
	}

	@Override
	public Constructable mul(Constructable other) {
		return n(value * c(other).value);
	}

	@Override
	public Constructable div(Constructable other) {
		return n(value / c(other).value);
	}

	@Override
	public Constructable negate() {
		return n(0 - value);
	}

	@Override
	public Constructable inverse() {
		return n(1 / value);
	}

	@Override
	public Constructable square() {
		return n(value * value);
	}

	@Override
	public Constructable root() {
		return n(Math.sqrt(value));
	}
	
	@Override
	public boolean isEqual(Constructable other) {
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
