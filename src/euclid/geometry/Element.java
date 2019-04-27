package euclid.geometry;

public abstract class Element<E extends Element<E>> implements Comparable<E> {
	
	public abstract boolean near(final E other);
	
	public boolean greater(final E other) {
		return compareTo(other) > 0;
	}
	
	public boolean greaterEq(final E other) {
		return compareTo(other) >= 0;
	}
	
	public boolean less(final E other) {
		return compareTo(other) < 0;
	}
	
	public boolean lessEq(final E other) {
		return compareTo(other) <= 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		return obj == this || near((E) obj);
	}
	
}
