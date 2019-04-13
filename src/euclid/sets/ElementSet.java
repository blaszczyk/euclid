package euclid.sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import euclid.geometry.Element;

@SuppressWarnings("serial")
abstract class ElementSet<E extends Element<? super E>, S extends ElementSet<E,S>> extends TreeSet<E> {

	ElementSet() {
	}

	ElementSet(final Collection<? extends E> set) {
		super(set);
	}

	public boolean containsNot(final E e) {
		return !contains(e);
	}
	
	public void addNonNull(final E e) {
		if(e != null) {
			add(e);
		}
	}
	
	public List<E> asList() {
		return new ArrayList<>(this);
	}
	
	public abstract S copy();

	public S adjoin(final E e) {
		final S result = copy();
		result.add(e);
		return result;
	}

	public S adjoin(final S other) {
		final S result = copy();
		result.addAll(other);
		return result;
	}
}
