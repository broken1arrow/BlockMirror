package org.brokenarrow.blockmirror.api.builders.language;


import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderText implements ConfigurationSerializeUtility {

    private final String blockFaceNotSet;
    private final Map<String, String> placeholders;
    private final String booleanTrue;
    private final String booleanFalse;
    private final Builder builder;


    private PlaceholderText(Builder builder) {
        this.placeholders = builder.placeholders;
        this.blockFaceNotSet = builder.blockFaceNotSet;
        this.booleanTrue = builder.booleanTrue;
        this.booleanFalse = builder.booleanFalse;

        this.builder = builder;
    }

    public static PlaceholderText deserialize(Map<String, Object> map) {
        String blockFaceSet = (String) map.getOrDefault("Block_face_set", null);
        String centerLocationNotSet = (String) map.getOrDefault("Center_location_not_set", null);
        String playerLocation = (String) map.getOrDefault("Player_location", null);

        String fillBlocksSet = (String) map.getOrDefault("Fill_blocks_set", null);
        String fillBlocksNotSet = (String) map.getOrDefault("Fill_blocks_not_set", null);

        String booleanTrue = (String) map.getOrDefault("Booleans_rename.True", null);
        String booleanFalse = (String) map.getOrDefault("Booleans_rename.False", null);


        Builder builder = new Builder()
                .setBlockFaceNotSet(blockFaceSet)
                .putPlacerholder("Center_location_not_set", centerLocationNotSet)
                .putPlacerholder("Player_location", playerLocation)
                .putPlacerholder("fill_blocks_set", fillBlocksSet)
                .putPlacerholder("fill_blocks_not_set", fillBlocksNotSet)
                .setBooleanFalse(booleanTrue)
                .setBooleanTrue(booleanFalse);

        return new PlaceholderText(builder);
    }

    public String getBlockFaceNotSet() {
        return blockFaceNotSet;
    }

    @Nullable
    public String getPlaceholder(String key) {
        return placeholders.get(key);
    }

    public String getBooleanTrue() {
        return booleanTrue;
    }

    public String getBooleanFalse() {
        return booleanFalse;
    }

    public Builder getBuilder() {
        return builder;
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Block_face_set", blockFaceNotSet);
        map.put("Booleans_rename.True", booleanTrue);
        map.put("Booleans_rename.False", booleanFalse);

        return map;
    }

    public static class Builder {
        private final Map<String, String> placeholders = new HashMap<>();
        private String blockFaceNotSet;
        private String booleanTrue;
        private String booleanFalse;

        public Builder setBlockFaceNotSet(final String blockFaceNotSet) {
            this.blockFaceNotSet = blockFaceNotSet;
            return this;
        }

        public Builder putPlacerholder(String key, String centerLocNotSet) {
            placeholders.put(key, centerLocNotSet);
            return this;
        }

        public Builder setBooleanTrue(final String booleanTrue) {
            this.booleanTrue = booleanTrue;
            return this;
        }

        public Builder setBooleanFalse(final String booleanFalse) {
            this.booleanFalse = booleanFalse;
            return this;
        }


        public PlaceholderText build() {
            return new PlaceholderText(this);
        }


    }
}