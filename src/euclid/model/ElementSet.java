package euclid.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class ElementSet<E extends Element<? super E>, S extends ElementSet<E,S>> implements Iterable<E> {

	final Collection<E> set;
	
	E last;
	
	private final Function<Collection<? extends E>, S> copyConstructor;

	ElementSet(final Collection<E> set, final Function<Collection<? extends E>, S> copyConstructor) {
		this.set = set;
		this.copyConstructor = copyConstructor;
	}

	public boolean containsNot(final E e) {
		return !set.contains(e);
	}

	public boolean contains(final S other) {
		return set.containsAll(other.set);
	}
	
	public int size() {
		return set.size();
	}

	public Stream<E> stream() {
		return set.stream();
	}
	
	public List<E> asList() {
		return new ArrayList<>(set);
	}

	public S adjoin(final E e) {
		final S result = copyConstructor.apply(set);
		result.set.add(e);
		result.last = e;
		return result;
	}

	public S adjoin(final S other) {
		final S result = copyConstructor.apply(set);
		result.set.addAll(other.set);
		return result;
	}
	
	@Override
	public String toString() {
		return set.toString();
	}
	
	@Override
	public int hashCode() {
		return stream().mapToInt(Object::hashCode).sum();
	}
	
	@Override
	public boolean equals(final Object obj) {
		final ElementSet<?,?> other = (ElementSet<?,?>) obj;
		return set.equals(other.set);
	}

	@Override
	public Iterator<E> iterator() {
		return set.iterator();
	}
}
