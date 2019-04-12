package euclid.geometry;

public interface Element<T extends Element<T>> extends Comparable<T> {
	
	public boolean near(final T other);
	
}
