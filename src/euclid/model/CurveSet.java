package euclid.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CurveSet extends ElementSet<Curve, CurveSet> {
	
	private static final CurveSet EMPTY = new CurveSet(0);
	
	public static CurveSet of(final Curve... curves) {
		return of(Arrays.asList(curves));
	}
	
	public static CurveSet of(final Collection<? extends Curve> curves) {
		final CurveSet result = new CurveSet(curves);
		return result;
	}

	public static CurveSet empty() {
		return EMPTY;
	}
	
	private CurveSet(final int capacity) {
		super(new LinkedHashSet<>(capacity), CurveSet::new);
	}
	
	private CurveSet(final Collection<? extends Curve> init) {
		super(new LinkedHashSet<>(init), CurveSet::new);
	}

}
