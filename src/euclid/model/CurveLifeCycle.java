package euclid.model;

import euclid.kpi.KpiReporter;

public interface CurveLifeCycle extends KpiReporter {

	Line line(final Point normal, final Constructable offset);
	
	Circle curve(final Point center, final Constructable radiusSquare);

	Curve segment(final Point normal, final Constructable offset, final Constructable end1, final Constructable end2);

}
