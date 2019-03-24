package euclid.model;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class CurveSet extends ArrayList<Curve> {
	
	private static final CurveSet EMPTY = new CurveSet(0);
	
	public static CurveSet create() {
		return new CurveSet(8);
	}
	
	public static CurveSet of(final Curve... curves) {
		CurveSet result = new CurveSet(curves.length);
		for(Curve c : curves)
			result.add(c);
		return result;
	}

	public static CurveSet empty() {
		return EMPTY;
	}

	private CurveSet(int length) {
		super(length);
	}

	public PointSet intersections() {
		final PointSet points = PointSet.create();
		for(int i = 0; i < size(); i++)
			for(int j = i+1; j < size(); j++)
				points.addAll(get(i).intersect(get(j)));
		return points;
	}

	public CurveSet adjoin(final Curve c) {
		final CurveSet result = new CurveSet(size() + 1);
		result.addAll(this);
		result.add(c);
		return result;
	}

	public CurveSet adjoin(final CurveSet curves) {
		final CurveSet result = new CurveSet(size() + curves.size());
		result.addAll(this);
		result.addAll(curves);
		return result;
	}
	
	@Override
	public boolean addAll(Collection<? extends Curve> c) {
		int s = size();
		c.forEach(this::add);
		return s != size();
	}
	
	@Override
	public boolean add(Curve e) {
		if(contains(e))
			return false;
		return super.add(e);
	}

	@Override
	public int hashCode() {
		return stream().mapToInt(Object::hashCode).sum();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		CurveSet other = (CurveSet) obj;
		return (size() == other.size()) && containsAll(other);
	}

}
