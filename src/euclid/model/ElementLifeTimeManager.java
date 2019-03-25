package euclid.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ElementLifeTimeManager {

	private static final Collection<Line> lines = ConcurrentHashMap.newKeySet();
	
	private static final Collection<Circle> circles = ConcurrentHashMap.newKeySet();
	
	private static final AtomicInteger dupeLines = new AtomicInteger();
	
	private static final AtomicInteger dupeCircles = new AtomicInteger();
	
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

	public static Line l(final Point x, final Point y) {
		return cached(new Line(x, y), lines, dupeLines);
	}
	
	public static Circle c(final Point c, final Point y) {
		return cached(new Circle(c, y), circles, dupeCircles);
	}

	private static <E extends Element<? super E>> E cached(final E newE, final Collection<E> es, final AtomicInteger dupeCounter) {
		for(final E e : es) {
			if(e.isEqual(newE)) {
				dupeCounter.incrementAndGet();
				return e;
			}
		}
		es.add(newE);
		return newE;
	}
	
	public static Map<String,Number> kpiReport() {
		final Map<String, Number> report = new LinkedHashMap<>();
		report.put("cached-lines", lines.size());
		report.put("dupe-lines", dupeLines.get());
		report.put("cached-circles", circles.size());
		report.put("dupe-circles", dupeCircles.get());
		return report;
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
