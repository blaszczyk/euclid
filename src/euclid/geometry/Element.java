package euclid.geometry;

public interface Element<T extends Element<T>> extends Comparable<T> {
	
	public boolean near(final T other);
	
	public default boolean greater(final T other) {
		return compareTo(other) > 0;
	}
	
	public default boolean greaterEq(final T other) {
		return compareTo(other) >= 0;
	}
	
	public default boolean less(final T other) {
		return compareTo(other) < 0;
	}
	
	public default boolean lessEq(final T other) {
		return compareTo(other) <= 0;
	}
	
}
