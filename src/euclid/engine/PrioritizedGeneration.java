package euclid.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PrioritizedGeneration<B> {
	
	private final List<List<B>> byPriority;
	
	private int deadEnds = 0;

	PrioritizedGeneration(final int maxPriority) {
		byPriority = new ArrayList<>(maxPriority);
		for(int i = 0; i < maxPriority; i++) {
			byPriority.add(new LinkedList<>());
		}
	}

	void add(final B b, final int priority) {
		if(priority < 0 || priority >= byPriority.size()) {
			deadEnds++;
		}
		else {
			get(priority).add(b);
		}
	}
	
	List<B> get(final int priority) {
		return byPriority.get(priority);
	}
	
	int totalSize() {
		int size = 0;
		for(final List<?> list : byPriority) {
			size += list.size();
		}
		return size;
	}
	
	int deadEnds() {
		return deadEnds;
	}

	void shuffle() {
		for(final List<B> list : byPriority) {
			Collections.shuffle(list);
		}
	}
	
}
