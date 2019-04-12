package euclid.sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import euclid.geometry.Element;

abstract class ElementSet<E extends Element<? super E>, S extends ElementSet<E,S>> implements Iterable<E> {

	final Set<E> set;
	
	private final Function<Collection<? extends E>, S> copyConstructor;

	ElementSet(final Function<Collection<? extends E>, S> copyConstructor) {
		this.set = new TreeSet<>();
		this.copyConstructor = copyConstructor;
	}

	ElementSet(final Collection<? extends E> set, final Function<Collection<? extends E>, S> copyConstructor) {
		this.set = new TreeSet<>(set);
		this.copyConstructor = copyConstructor;
	}

	public boolean contains(final E e) {
		return set.contains(e);
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
	
	public List<E> asList() {
		return new ArrayList<>(set);
	}

	public S adjoin(final E e) {
		final S result = copyConstructor.apply(set);
		result.set.add(e);
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
		return set.hashCode();
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
