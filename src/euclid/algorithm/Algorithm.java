package euclid.algorithm;

import java.util.Collection;

/**
 * contract:
 * <br/>
 * priority(b) &lt;= maxPriority()
 * <br/>
 * nextGeneration(b1).contains(b2) implies depth(b2) = depth(b1) + 1
 * 
 */
public interface Algorithm<B> {
	
	public B first();
	
	public int priority(final B b);
	
	public int maxPriority();
	
	public int depth(final B b);
	
	public Collection<B> nextGeneration(final B b);

}
