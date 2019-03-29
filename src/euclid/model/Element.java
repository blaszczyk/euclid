package euclid.model;

public interface Element<T extends Element<T>> {
	
	public boolean near(final T other);
	
}
