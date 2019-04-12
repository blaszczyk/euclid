package euclid.geometry;

abstract class AbstractElement<E extends Element<E>> implements Element<E> {

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		return near((E) obj);
	}

}
