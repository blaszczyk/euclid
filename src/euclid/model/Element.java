package euclid.model;

public interface Element<T extends Element<?>> {
	public boolean isEqual(final T other);
}
