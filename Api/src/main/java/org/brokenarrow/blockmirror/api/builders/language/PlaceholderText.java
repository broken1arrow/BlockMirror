package org.brokenarrow.blockmirror.api.builders.language;


import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderText implements ConfigurationSerializeUtility {

	private final String blockFaceNotSet;
	private final String booleanTrue;
	private final String booleanFalse;
	private final Builder builder;

	private PlaceholderText(Builder builder) {
		this.blockFaceNotSet = builder.blockFaceNotSet;
		this.booleanTrue = builder.booleanTrue;
		this.booleanFalse = builder.booleanFalse;

		this.builder = builder;
	}

	public String getBlockFaceNotSet() {
		return blockFaceNotSet;
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

	public static class Builder {
		private String blockFaceNotSet;
		private String booleanTrue;
		private String booleanFalse;

		public Builder setBlockFaceNotSet(final String blockFaceNotSet) {
			this.blockFaceNotSet = blockFaceNotSet;
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

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Block_face_set", blockFaceNotSet);
		map.put("Booleans_rename.True", booleanTrue);
		map.put("Booleans_rename.False", booleanFalse);

		return map;
	}

	public static PlaceholderText deserialize(Map<String, Object> map) {
		String blockFaceSet = (String) map.getOrDefault("Block_face_set", null);
		String booleanTrue = (String) map.getOrDefault("Booleans_rename.True", null);
		String booleanFalse = (String) map.getOrDefault("Booleans_rename.False", null);
		
		Builder builder = new Builder()
				.setBlockFaceNotSet(blockFaceSet)
				.setBooleanFalse(booleanTrue)
				.setBooleanTrue(booleanFalse);

		return new PlaceholderText(builder);
	}
}