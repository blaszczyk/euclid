package euclid.model;

public interface Constructable extends Comparable<Constructable>
{
	public Constructable add(final Constructable other);
	public Constructable sub(final Constructable other);
	public Constructable mul(final Constructable other);
	public Constructable div(final Constructable other);

	public Constructable negate();
	public Constructable inverse();
	public Constructable square();
	public Constructable root();
	
	public boolean isEqual(final Constructable other);

}
