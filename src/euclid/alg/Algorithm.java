package euclid.alg;

import java.util.Collection;

public interface Algorithm<B> {
	
	public B first();
	
	public B digest(final B t);
	
	public int misses(final B b);
	
	public int maxMisses();
	
	public int depth(final B b);
	
	public Collection<B> nextGeneration(final B b);

}
