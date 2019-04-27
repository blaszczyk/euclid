package euclid.algorithm;

import java.util.List;

public class ListHelper {
	
	@FunctionalInterface
	public static interface DistinctPairConsumer<E> {
		public void accept(final E e1, final E e2);
	}
	
	@FunctionalInterface
	public static interface PairConsumer<E, F> {
		public void accept(final E e, final F f);
	}
	
	@FunctionalInterface
	public static interface DistinctTripleConsumer<E> {
		public void accept(final E e1, final E e2, final E e3);
	}

	public static <E> void forEachDistinctPair(final List<E> es, final DistinctPairConsumer<E> consumer ) {
		for(int i = 1; i < es.size(); i++) {
			for(int j = 0; j < i; j++) {
				final E e1 = es.get(i);
				final E e2 = es.get(j);
				consumer.accept(e1, e2);
			}
		}
	}
	
	public static <E,F> void forEachPair(final List<E> es, final List<F> fs, final PairConsumer<E, F> consumer ) {
		for(final E e : es) {
			for(final F f : fs) {
				consumer.accept(e, f);
			}
		}
	}
	
	public static <E> void forEachDistinctTriple(final List<E> es, final DistinctTripleConsumer<E> consumer ) {
		for(int i = 2; i < es.size(); i++) {
			for(int j = 1; j < i; j++) {
				for(int k = 0; k < j; k++) {
					final E e1 = es.get(i);
					final E e2 = es.get(j);
					final E e3 = es.get(k);
					consumer.accept(e1, e2, e3);
				}
			}
		}
	}
	
	public static <E> void forEachDistinctPairAndSingle(final List<E> distincts, final List<E> singles, final DistinctTripleConsumer<E> consumer ) {
		for(final E e1 : singles) {
			forEachDistinctPair(distincts, (e2,e3) -> consumer.accept(e1, e2, e3));
		}
	}

}
