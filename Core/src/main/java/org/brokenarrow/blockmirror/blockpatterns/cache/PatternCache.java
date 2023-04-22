package org.brokenarrow.blockmirror.blockpatterns.cache;

import org.brokenarrow.blockmirror.api.blockpattern.PatternData;

import java.util.ArrayList;
import java.util.List;

public class PatternCache {

	private final List<PatternData> patterns = new ArrayList<>();


	public List<PatternData> getPatterns() {
		return patterns;
	}

	public void addPattern(PatternData pattern) {
		patterns.add(pattern);
	}
}
