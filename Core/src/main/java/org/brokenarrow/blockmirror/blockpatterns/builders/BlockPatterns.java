package org.brokenarrow.blockmirror.blockpatterns.builders;

import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtility;
import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatternsApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternDisplayItem;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.brokenarrow.blockmirror.api.utility.Pair;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockPatterns implements BlockPatternsApi,  ConfigurationSerializeUtility {


    private final PatternDisplayItem circlePattern;
    private final PatternDisplayItem squarePattern;
    private final Builder builder;

    private BlockPatterns(Builder builder) {
        this.circlePattern = builder.circlePattern;
        this.squarePattern = builder.squarePattern;
        this.builder = builder;
    }

    public static BlockPatterns deserialize(Map<String, Object> map) {
        BlockMirrorAPI blockMirror = BlockMirrorUtility.getInstance();
        Builder builder = new Builder();
        String patternKey = "Patterns.";
        String circle = "Circle.";
        String square = "Square.";
        getCircle(map, blockMirror, builder, patternKey, circle);
        getSquare(map, blockMirror, builder, patternKey, square);

        return new BlockPatterns(builder);
    }

    private static void getCircle(final Map<String, Object> map, final BlockMirrorAPI blockMirror, final Builder builder, final String patternKey, final String pattenType) {
        String fillBlocks = pattenType + "Settings.Fill_blocks.";
        String changeFacing = pattenType + "Settings.Change_facing.";
        ItemWrapperApi circlePassive = getDeserialize(map, patternKey, pattenType, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
        ItemWrapperApi circleActive = getDeserialize(map, patternKey, pattenType, "Active.Display_name", "Active.Matrial", "Active.Lore");

        ItemWrapperApi fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
        ItemWrapperApi fillBlocksInPatternActive = getDeserialize(map, patternKey, fillBlocks, "Active.Display_name", "Active.Matrial", "Active.Lore");

        ItemWrapperApi changeFacingToPatternPassive = getDeserialize(map, patternKey, changeFacing, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
        ItemWrapperApi changeFacingToPatternActive = getDeserialize(map, patternKey, changeFacing, "Active.Display_name", "Active.Matrial", "Active.Lore");

        if (fillBlocksInPatternPassive.getDisplayName() == null || fillBlocksInPatternPassive.getDisplayName().isEmpty())
            fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Display_name", "Matrial", "Lore");
        if (fillBlocksInPatternActive.getDisplayName() == null || fillBlocksInPatternActive.getDisplayName().isEmpty())
            fillBlocksInPatternActive = null;

        boolean fillAllBlocks = (boolean) map.getOrDefault(patternKey + pattenType + "Fill_all_blocks", false);

        builder.setCirclePattern(blockMirror.createPatternWrapperApi(circlePassive, circleActive)
                .fillAllBlocks(fillAllBlocks)
                .setPatternSettingsWraper(
                        blockMirror.createSetFillBlocksInPattern("blockmirror.change.circle_pattern", fillBlocksInPatternPassive, fillBlocksInPatternActive)
                        , blockMirror.createChangeFacingToPattern("blockmirror.change.circle_pattern", changeFacingToPatternPassive, changeFacingToPatternActive))
        );
    }

    private static void getSquare(final Map<String, Object> map, final BlockMirrorAPI blockMirror, final Builder builder, final String patternKey, final String pattenType) {
        String fillBlocks = pattenType + "Settings.Fill_blocks.";
        String changeFacing = pattenType + "Settings.Change_facing.";
        ItemWrapperApi squarePassive = getDeserialize(map, patternKey, pattenType, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
        ItemWrapperApi squareActive = getDeserialize(map, patternKey, pattenType, "Active.Display_name", "Active.Matrial", "Active.Lore");

        ItemWrapperApi fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
        ItemWrapperApi fillBlocksInPatternActive = getDeserialize(map, patternKey, fillBlocks, "Active.Display_name", "Active.Matrial", "Active.Lore");

        ItemWrapperApi changeFacingToPatternPassive = getDeserialize(map, patternKey, changeFacing, "Passive.Display_name", "Passive.Matrial", "Passive.Lore");
        ItemWrapperApi changeFacingToPatternActive = getDeserialize(map, patternKey, changeFacing, "Active.Display_name", "Active.Matrial", "Active.Lore");

        if (fillBlocksInPatternPassive.getDisplayName() == null || fillBlocksInPatternPassive.getDisplayName().isEmpty())
            fillBlocksInPatternPassive = getDeserialize(map, patternKey, fillBlocks, "Display_name", "Matrial", "Lore");
        if (fillBlocksInPatternActive.getDisplayName() == null || fillBlocksInPatternActive.getDisplayName().isEmpty())
            fillBlocksInPatternActive = null;

        boolean fillAllBlocks = (boolean) map.getOrDefault(patternKey + pattenType + "Fill_all_blocks", false);

        builder.setSquarePattern(blockMirror.createPatternWrapperApi(squarePassive, squareActive)
                .fillAllBlocks(fillAllBlocks)
                .setPatternSettingsWraper(
                        blockMirror.createSetFillBlocksInPattern("blockmirror.change.square_pattern", fillBlocksInPatternPassive, fillBlocksInPatternActive)
                        , blockMirror.createChangeFacingToPattern("blockmirror.change.square_pattern", changeFacingToPatternPassive, changeFacingToPatternActive)));
    }

    private static ItemWrapperApi getDeserialize(final Map<String, Object> map, final String patternKey, final String subKey, final String displayName, final String matrial, final String lore) {
        return ItemWrapper.deserialize(deserializeData(
                new Pair<>("Display_name", map.get(patternKey + subKey + displayName)),
                new Pair<>("Material", map.get(patternKey + subKey + matrial)),
                new Pair<>("Lore", map.get(patternKey + subKey + lore))));
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
    public PatternDisplayItem getCirclePattern() {
        return circlePattern;
    }

    @Override
    public PatternDisplayItem getSquarePattern() {
        return squarePattern;
    }

    public Builder getBuilder() {
        return builder;
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Circle", circlePattern);
        map.put("Square", squarePattern);
        return map;
    }

    @Override
    public String toString() {
        return "BlockPatterns{" + "circlePattern=" + circlePattern + ", builder=" + builder + '}';
    }

    public static class Builder {
        private PatternDisplayItem circlePattern;
        private PatternDisplayItem squarePattern;

        public Builder setCirclePattern(final PatternDisplayItem circlePattern) {
            this.circlePattern = circlePattern;
            return this;
        }

        public Builder setSquarePattern(final PatternDisplayItem squarePattern) {
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
}