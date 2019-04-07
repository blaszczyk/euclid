package euclid.engine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class EngineKpiProvider implements KpiReporter {

	private final AtomicInteger processedCount = new AtomicInteger();

	private final AtomicInteger cumulatedDepth = new AtomicInteger();

	private final IntSupplier solutionsCount;

	public EngineKpiProvider() {
		this(() -> -1);
	}

	public EngineKpiProvider(final IntSupplier solutionsCount) {
		this.solutionsCount = solutionsCount;
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		final int processed = processedCount.get();
		final int depth = Math.round(1000f * cumulatedDepth.get() / processed);
		collector.add("processed", processed);
		collector.add("depth", depth);
		final int solutions = solutionsCount.getAsInt();
		if(solutions >= 0) {
			collector.add("solutions", solutions);
		}
	}
	
	public void incrementProcessedAndAddDepth(final int depth) {
		processedCount.incrementAndGet();
		cumulatedDepth.addAndGet(depth);
	}

}
