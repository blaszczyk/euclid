package euclid.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

abstract class ElementSet<E extends Element<? super E>> implements Iterable<E>{
	
	private int hash = 0;
	
	abstract Collection<E> set();
	
	void computeHash() {
		hash = set().stream().mapToInt(Object::hashCode).sum();
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
	
	public List<E> asList() {
		return new ArrayList<>(set());
	}
	
	@Override
	public String toString() {
		return set().toString();
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementSet<?> other = (ElementSet<?>) obj;
		if (hash != other.hash)
			return false;
		return set().equals(other.set());
	}

	@Override
	public Iterator<E> iterator() {
		return set().iterator();
	}
}
