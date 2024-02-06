package org.brokenarrow.blockmirror.api.builders.patterns;


import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.brokenarrow.blockmirror.api.utility.Pair;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockPatterns implements ConfigurationSerializeUtility {


	private final PatternWrapperApi circlePattern;
	private final PatternWrapperApi squarePattern;
	private final Builder builder;

	private BlockPatterns(Builder builder) {
		this.circlePattern = builder.circlePattern;
		this.squarePattern = builder.squarePattern;
		this.builder = builder;
	}

	public PatternWrapperApi getCirclePattern() {
		return circlePattern;
	}

	public PatternWrapperApi getSquarePattern() {
		return squarePattern;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {
		private PatternWrapperApi circlePattern;
		private PatternWrapperApi squarePattern;

		public Builder setCirclePattern(final PatternWrapperApi circlePattern) {
			this.circlePattern = circlePattern;
			return this;
		}

		public Builder setSquarePattern(final PatternWrapperApi squarePattern) {
			this.squarePattern = squarePattern;
			return this;
		}

		public BlockPatterns build() {
			return new BlockPatterns(this);
		}

		@Override
		public String toString() {
			return "Builder{" +
					"circlePattern=" + circlePattern +
					'}';
		}
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Circle", circlePattern);
		map.put("Square", squarePattern);
		return map;
	}

	public static BlockPatterns deserialize(Map<String, Object> map) {
		BlockMirrorAPI blockMirror = BlockMirrorUtillity.getInstance();
		Builder builder = new Builder();
		String patternKey = "Patterns.";
		String circle = "Circle.";
		String square = "Square.";
		getCircle(map, blockMirror, builder, patternKey, circle);
		getSquare(map, blockMirror, builder, patternKey, square);

		return new BlockPatterns(builder);
	}

	private static void getCircle(final Map<String, Object> map, final BlockMirrorAPI blockMirror, final Builder builder, final String patternKey, final String circle) {
		String fillBlocks = circle + "Settings.Fill_blocks.";
		String changeFacing = circle + "Settings.Change_facing.";
		ItemWrapper circlePassive = getDeserialize(map, patternKey, circle, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
		ItemWrapper circleActive = getDeserialize(map, patternKey, circle, "Active.Display_name", "Active.Matrial", "Active.Lore");

		ItemWrapper fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
		ItemWrapper fillBlocksInPatternActive = getDeserialize(map, patternKey, fillBlocks, "Active.Display_name", "Active.Matrial", "Active.Lore");

		ItemWrapper changeFacingToPatternPassive = getDeserialize(map, patternKey, changeFacing, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
		ItemWrapper changeFacingToPatternActive = getDeserialize(map, patternKey, changeFacing, "Active.Display_name", "Active.Matrial", "Active.Lore");

		if (fillBlocksInPatternPassive.getDisplayName() == null || fillBlocksInPatternPassive.getDisplayName().isEmpty())
			fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Display_name", "Matrial", "Lore");
		if (fillBlocksInPatternActive.getDisplayName() == null || fillBlocksInPatternActive.getDisplayName().isEmpty())
			fillBlocksInPatternActive = null;

		boolean fillAllBlocks = (boolean) map.getOrDefault(patternKey + circle + "Fill_all_blocks", false);

		builder.setCirclePattern(blockMirror.createPatternWrapperApi(circlePassive, circleActive).setFillBlocks(fillAllBlocks)
				.setPatternSettingsWraperApi(
						blockMirror.createSetFillBlocksInPattern(fillBlocksInPatternPassive, fillBlocksInPatternActive)
						, blockMirror.createChangeFacingToPattern(changeFacingToPatternPassive, changeFacingToPatternActive)));
	}

	private static void getSquare(final Map<String, Object> map, final BlockMirrorAPI blockMirror, final Builder builder, final String patternKey, final String circle) {
		String fillBlocks = circle + "Settings.Fill_blocks.";
		String changeFacing = circle + "Settings.Change_facing.";
		ItemWrapper circlePassive = getDeserialize(map, patternKey, circle, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
		ItemWrapper circleActive = getDeserialize(map, patternKey, circle, "Active.Display_name", "Active.Matrial", "Active.Lore");

		ItemWrapper fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
		ItemWrapper fillBlocksInPatternActive = getDeserialize(map, patternKey, fillBlocks, "Active.Display_name", "Active.Matrial", "Active.Lore");

		ItemWrapper changeFacingToPatternPassive = getDeserialize(map, patternKey, changeFacing, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
		ItemWrapper changeFacingToPatternActive = getDeserialize(map, patternKey, changeFacing, "Active.Display_name", "Active.Matrial", "Active.Lore");

		if (fillBlocksInPatternPassive.getDisplayName() == null || fillBlocksInPatternPassive.getDisplayName().isEmpty())
			fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Display_name", "Matrial", "Lore");
		if (fillBlocksInPatternActive.getDisplayName() == null || fillBlocksInPatternActive.getDisplayName().isEmpty())
			fillBlocksInPatternActive = null;

		boolean fillAllBlocks = (boolean) map.getOrDefault(patternKey + circle + "Fill_all_blocks", false);

		builder.setSquarePattern(blockMirror.createPatternWrapperApi(circlePassive, circleActive).setFillBlocks(fillAllBlocks)
				.setPatternSettingsWraperApi(
						blockMirror.createSetFillBlocksInPattern(fillBlocksInPatternPassive, fillBlocksInPatternActive)
						, blockMirror.createChangeFacingToPattern(changeFacingToPatternPassive, changeFacingToPatternActive)));
	}

	private static ItemWrapper getDeserialize(final Map<String, Object> map, final String patternKey, final String circle, final String x, final String x1, final String x2) {
		return ItemWrapper.deserialize(deserializeData(
				new Pair<>("Display_name", map.get(patternKey + circle + x)),
				new Pair<>("Material", map.get(patternKey + circle + x1)),
				new Pair<>("Lore", map.get(patternKey + circle + x2))));
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