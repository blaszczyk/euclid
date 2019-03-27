package euclid.model;

public class Sugar {
	
	public static Constructable number(final double x) {
		return new CFloat(x);
//		return new CFixed(x); // not good
	}
	
	public static Point point(final Constructable x, final Constructable y) {
		return new Point(x, y);
	}

	private static final Constructable M_TWO = number(-2);
	private static final Constructable M_ONE = number(-1);
	private static final Constructable ZERO = number(0);
	private static final Constructable ONE = number(1);
	private static final Constructable TWO = number(2);
	
	public static Constructable m_two() {
		return M_TWO;
	}
	public static Constructable m_one() {
		return M_ONE;
	}
	public static Constructable zero() {
		return ZERO;
	}
	public static Constructable one() {
		return ONE;
	}
	public static Constructable two() {
		return TWO;
	}
	
}
