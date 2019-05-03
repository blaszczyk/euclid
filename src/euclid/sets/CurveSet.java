package euclid.sets;

import java.util.Collection;
import java.util.Comparator;

import euclid.geometry.Curve;

@SuppressWarnings("serial")
public class CurveSet extends ElementSet<Curve, CurveSet> {
	
	public static final CurveSet EMPTY = new CurveSet();
	
	public CurveSet() {
	}
	
	public CurveSet(final Collection<? extends Curve> init) {
		super(init);
	}

	public CurveSet(final Comparator<Curve> comparator) {
		super(comparator);
	}

	public CurveSet(final Curve... curves) {
		for(final Curve c : curves) {
			add(c);
		}
	}

	@Override
	public CurveSet copy() {
		return new CurveSet(this);
	}

	@Override
	CurveSet self() {
		return this;
	}

}
