package euclid.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CurveSet extends ElementSet<Curve> {
	
	private static final CurveSet EMPTY = new CurveSet(0);
	
	public static CurveSet of(final Curve... curves) {
		return of(Arrays.asList(curves));
	}
	
	public static CurveSet of(final Collection<Curve> curves) {
		final CurveSet result = new CurveSet(curves);
		result.computeHash();
		return result;
	}

	public static CurveSet empty() {
		return EMPTY;
	}
	
	private CurveSet(final int capacity) {
		super(new LinkedHashSet<>(capacity));
	}
	
	private CurveSet(final Collection<Curve> init) {
		super(new LinkedHashSet<>(init));
	}

	public CurveSet adjoin(final Curve c) {
		final CurveSet result = new CurveSet(set);
		result.set.add(c);
		result.computeHash();
		return result;
	}

	public CurveSet adjoin(final CurveSet curves) {
		final CurveSet result = new CurveSet(set);
		result.set.addAll(curves.set);
		result.computeHash();
		return result;
	}

}
