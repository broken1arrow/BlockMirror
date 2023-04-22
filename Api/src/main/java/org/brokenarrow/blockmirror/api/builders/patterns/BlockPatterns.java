package org.brokenarrow.blockmirror.api.builders.patterns;


import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.brokenarrow.blockmirror.api.utility.Pair;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockPatterns implements ConfigurationSerializeUtility {


	private final PatternWrapperApi circlePattern;
	private final Builder builder;

	private BlockPatterns(Builder builder) {
		this.circlePattern = builder.circlePattern;

		this.builder = builder;
	}

	public PatternWrapperApi getCirclePattern() {
		return circlePattern;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {
		private PatternWrapperApi circlePattern;

		public Builder setCirclePattern(final PatternWrapperApi circlePattern) {
			this.circlePattern = circlePattern;
			return this;
		}

		public BlockPatterns build() {
			return new BlockPatterns(this);
		}
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Circle", circlePattern);
		return map;
	}

	public static BlockPatterns deserialize(Map<String, Object> map) {
		String patternKey = "Patterns.";

		ItemWrapper circlePassive = ItemWrapper.deserialize(deserializeData(
				new Pair<>("Display_name", map.get(patternKey + "Circle.Passive.Display_name")),
				new Pair<>("Material", map.get(patternKey + "Circle.Passive.Matrial")),
				new Pair<>("Lore", map.get(patternKey + "Circle.Passive.Lore"))));
		ItemWrapper circleActive = ItemWrapper.deserialize(deserializeData(
				new Pair<>("Display_name", map.get(patternKey + "Circle.Active.Display_name")),
				new Pair<>("Material", map.get(patternKey + "Circle.Active.Matrial")),
				new Pair<>("Lore", map.get(patternKey + "Circle.Active.Lore"))));

		ItemWrapper patternSettingsPassive = ItemWrapper.deserialize(deserializeData(
				new Pair<>("Display_name", map.get(patternKey + "Circle.Settings.Passive.Display_name")),
				new Pair<>("Material", map.get(patternKey + "Circle.Settings.Passive.Matrial")),
				new Pair<>("Lore", map.get(patternKey + "Circle.Settings.Passive.Lore"))));
		ItemWrapper patternSettingsActive = ItemWrapper.deserialize(deserializeData(
				new Pair<>("Display_name", map.get(patternKey + "Circle.Settings.Active.Display_name")),
				new Pair<>("Material", map.get(patternKey + "Circle.Settings.Active.Matrial")),
				new Pair<>("Lore", map.get(patternKey + "Circle.Settings.Active.Lore"))));

		if (patternSettingsPassive.getDisplayName() == null || patternSettingsPassive.getDisplayName().isEmpty())
			patternSettingsPassive = ItemWrapper.deserialize(deserializeData(new Pair<>("Display_name", map.get(patternKey + "Circle.Settings.Display_name")), new Pair<>("Material", map.get(patternKey + "Circle.Settings.Matrial")), new Pair<>("Lore", map.get(patternKey + "Circle.Settings.Lore"))));
		if (patternSettingsActive.getDisplayName() == null || patternSettingsActive.getDisplayName().isEmpty())
			patternSettingsActive = null;

		boolean fillAllBlocks = (boolean) map.getOrDefault(patternKey + "Circle.Fill_all_blocks", false);

		Builder builder = new Builder().setCirclePattern(BlockMirrorUtillity.getInstance().createPatternWrapperApi(circlePassive, circleActive).setFillBlocks(fillAllBlocks).setPatternSettingsWraperApi(BlockMirrorUtillity.getInstance().createPatternSettingsWraper(patternSettingsPassive, patternSettingsActive)));
		return new BlockPatterns(builder);
	}

	@SafeVarargs
	public static Map<String, Object> deserializeData(Pair<String, Object>... objects) {
		Map<String, Object> tools = new LinkedHashMap<>();
		if (objects == null) return tools;
		for (Pair<String, Object> object : objects) {
			tools.put(object.getFirst(), object.getSecond());
		}
		return tools;
	}

	@Override
	public String toString() {
		return "BlockPatterns{" + "circlePattern=" + circlePattern + ", builder=" + builder + '}';
	}
}