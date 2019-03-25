package euclid.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

abstract class ElementSet<E extends Element<? super E>> implements Iterable<E>{
	
	private int hash = 0;
	
	abstract Collection<E> set();
	
	void computeHash() {
		hash = set().stream().mapToInt(Object::hashCode).sum();
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public Iterator<E> iterator() {
		return set().iterator();
	}

	public boolean contains(final E e) {
		return set().contains(e);
	}

	public boolean contains(final ElementSet<? extends E> other) {
		return set().containsAll(other.set());
	}
	
	public int size() {
		return set().size();
	}

	public Stream<E> stream() {
		return set().stream();
	}
}
