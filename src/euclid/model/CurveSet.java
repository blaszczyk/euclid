package euclid.model;

import java.util.Arrays;
import java.util.Collection;

public class CurveSet extends ElementSet<Curve, CurveSet> {
	
	private static final CurveSet EMPTY = new CurveSet();
	
	public static CurveSet of(final Curve... curves) {
		return of(Arrays.asList(curves));
	}
	
	public static CurveSet of(final Collection<? extends Curve> curves) {
		return new CurveSet(curves);
	}

	public static CurveSet empty() {
		return EMPTY;
	}
	
	private CurveSet() {
		super(CurveSet::new);
	}
	
	private CurveSet(final Collection<? extends Curve> init) {
		super(init, CurveSet::new);
	}

}
