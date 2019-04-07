package euclid.model;

public interface Constructable extends Comparable<Constructable>, Element<Constructable>
{
	public Constructable add(final Constructable other);
	public Constructable sub(final Constructable other);
	public Constructable mul(final Constructable other);
	public Constructable div(final Constructable other);

	public Constructable negate();
	public Constructable inverse();
	public Constructable square();
	public Constructable root();
	
	public default Constructable min(final Constructable other) {
		return compareTo(other) < 0 ? this : other;
	}
	public default Constructable max(final Constructable other) {
		return compareTo(other) > 0 ? this : other;
	}
	
	public int sign();
	public double doubleValue();

}
