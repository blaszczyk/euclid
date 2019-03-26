package euclid.alg.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

import euclid.kpi.KpiReporter;

public class EngineKpiProvider implements KpiReporter {

	private final AtomicInteger finishedCount = new AtomicInteger();

	private final AtomicInteger dupeCount = new AtomicInteger();

	private final AtomicInteger cumulatedDepth = new AtomicInteger();

	private final IntSupplier totalCount;

	private final IntSupplier solutionsCount;

	public EngineKpiProvider(final IntSupplier totalCount) {
		this(totalCount,() -> -1);
	}

	public EngineKpiProvider(final IntSupplier totalCount, final IntSupplier solutionsCount) {
		this.totalCount = totalCount;
		this.solutionsCount = solutionsCount;
	}

	@Override
	public Map<String, Number> report() {
		final Map<String, Number> report = new LinkedHashMap<>();
		final int total = totalCount.getAsInt();
		final int depth = (int)(cumulatedDepth.get() / (double) total);
		report.put("finished", finishedCount.get());
		report.put("total", total);
		report.put("dupes", dupeCount.get());
		report.put("depth", depth);
		final int solutions = solutionsCount.getAsInt();
		if(solutions >= 0) {
			report.put("solutions", solutions);
		}
		return report;
	}
	
	public void incrementFinished() {
		finishedCount.incrementAndGet();
	}

	public void incrementDupes() {
		dupeCount.incrementAndGet();
	}
	
	public void reportDepth(final int depth) {
		cumulatedDepth.addAndGet(depth);
	}

}
