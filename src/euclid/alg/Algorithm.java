package euclid.alg;

import java.util.Collection;

public interface Algorithm<T,B> {
	
	public T first();
	
	public  B digest(final T t);
	
	public boolean solves(final B b);
	
	public  int depth(final B b);
	
	public Collection<T> generateNext(final B b);

}
