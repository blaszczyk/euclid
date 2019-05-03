package euclid.sets;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import euclid.geometry.Element;

@SuppressWarnings("serial")
public abstract class ElementSet<E extends Element<? super E>, S extends ElementSet<E,S>> extends TreeSet<E> {

	ElementSet() {
	}

	ElementSet(final Collection<? extends E> set) {
		super(set);
	}

	ElementSet(final Comparator<E> comparator) {
		super(comparator);
	}

	public boolean containsNot(final E e) {
		return !contains(e);
	}
	
	public void addNonNull(final E e) {
		if(e != null) {
			add(e);
		}
	}
	
	abstract S self();

	abstract S copy();

	S adjoin(final E e) {
		add(e);
		return self();
	}

	S adjoin(final S other) {
		addAll(other);
		return self();
	}
}
