package euclid.model;

public class Sugar {
	
	public static Constructable n(final double x) {
		return new CFloat(x);
//		return new CFixed(x); // not good
	}
	
	public static Point p(final Constructable x, final Constructable y) {
		return new Point(x, y);
	}
	
	public static Point p(final double x, final double y) {
		return p(n(x), n(y));
	}

	private static final Constructable M_TWO = n(-2);
	private static final Constructable M_ONE = n(-1);
	private static final Constructable ZERO = n(0);
	private static final Constructable ONE = n(1);
	private static final Constructable TWO = n(2);
	
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
