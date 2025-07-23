package org.brokenarrow.blockmirror.blockpatterns.builders;

import org.brokenarrow.blockmirror.api.builders.ToolsApi;
import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatternsApi;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.brokenarrow.blockmirror.api.settings.SettingsDataApi;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingsData implements SettingsDataApi, ConfigurationSerializeUtility {

    private final ToolsApi tools;
    private final BlockPatternsApi blockPatterns;
    private final int classicBlockPlaceDistance;
    private final String mainCommand;
    private final boolean silkTouch;
    private final Builder builder;

    private SettingsData(Builder builder) {
        this.tools = builder.tools;
        this.blockPatterns = builder.blockPatterns;
        this.mainCommand = builder.mainCommand;
        this.silkTouch = builder.silkTouch;
        this.classicBlockPlaceDistance = builder.classicBlockPlaceDistance;
        this.builder = builder;
    }

    public static SettingsData deserialize(Map<String, Object> map) {

        Builder builder = new Builder()
                .setMainCommand((String) map.getOrDefault("Main_command", "blockmirror|mirror"))
                .setTools(Tools.deserialize(map))
                .setBlockPatterns(BlockPatterns.deserialize(map))
                .setSilkTouch((Boolean) map.getOrDefault("Silk_touch", false))
                .setClassicBlockPlaceDistance((Integer) map.getOrDefault("Classic_block_place_distance", -1));


        return new SettingsData(builder);
    }

    public ToolsApi getTools() {
        return tools;
    }

    public BlockPatternsApi getBlockPatterns() {
        return blockPatterns;
    }

    public String getMainCommand() {
        return mainCommand;
    }

    public boolean isSilkTouch() {
        return silkTouch;
    }

    public int getClassicBlockPlaceDistance() {
        return classicBlockPlaceDistance;
    }

    public Builder getBuilder() {
        return builder;
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Tools", tools);
        map.put("Patterns", tools);
        map.put("Main_command", mainCommand);
        map.put("Silk_touch", silkTouch);
        return map;
    }

    @Override
    public String toString() {
        return "SettingsData{" +
                "tools=" + tools +
                ", blockPatterns=" + blockPatterns +
                ", mainCommand='" + mainCommand + '\'' +
                ", builder=" + builder +
                '}';
    }

    public static class Builder {

        public boolean silkTouch;
        public int classicBlockPlaceDistance;
        private ToolsApi tools;
        private BlockPatterns blockPatterns;
        private String mainCommand;

        public Builder setTools(final ToolsApi tools) {
            this.tools = tools;
            return this;
        }

        public Builder setSilkTouch(final boolean silkTouch) {
            this.silkTouch = silkTouch;
            return this;
        }

        public Builder setBlockPatterns(final BlockPatterns blockPatterns) {
            this.blockPatterns = blockPatterns;
            return this;
        }

        public Builder setMainCommand(final String mainCommand) {
            this.mainCommand = mainCommand;
            return this;
        }

        public Builder setClassicBlockPlaceDistance(int classicBlockPlaceDistance) {
            this.classicBlockPlaceDistance = classicBlockPlaceDistance;
            return this;
        }

        public SettingsData build() {
            return new SettingsData(this);
        }
    }
}