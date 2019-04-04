package euclid.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

public class CurveSet extends ElementSet<Curve, CurveSet> {
	
	private static final CurveSet EMPTY = new CurveSet();
	
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
	
	private CurveSet() {
		super(new TreeSet<>(), CurveSet::new);
	}
	
	private CurveSet(final Collection<? extends Curve> init) {
		super(new TreeSet<>(init), CurveSet::new);
	}

}
