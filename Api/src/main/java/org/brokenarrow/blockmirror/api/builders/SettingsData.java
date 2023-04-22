package org.brokenarrow.blockmirror.api.builders;


import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatterns;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingsData implements ConfigurationSerializeUtility {


	private final Tools tools;
	private final BlockPatterns blockPatterns;
	private final String mainCommand;
	private final boolean silkTouch;
	private final Builder builder;

	private SettingsData(Builder builder) {
		this.tools = builder.tools;
		this.blockPatterns = builder.blockPatterns;
		this.mainCommand = builder.mainCommand;
		this.silkTouch = builder.silkTouch;
		this.builder = builder;
	}

	public Tools getTools() {
		return tools;
	}

	public BlockPatterns getBlockPatterns() {
		return blockPatterns;
	}

	public String getMainCommand() {
		return mainCommand;
	}

	public boolean isSilkTouch() {
		return silkTouch;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {

		private Tools tools;
		private BlockPatterns blockPatterns;
		private String mainCommand;
		public boolean silkTouch;

		public Builder setTools(final Tools tools) {
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

		public SettingsData build() {
			return new SettingsData(this);
		}
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

	public static SettingsData deserialize(Map<String, Object> map) {

		Builder builder = new Builder()
				.setMainCommand((String) map.getOrDefault("Main_command", "blockmirror|mirror"))
				.setTools(Tools.deserialize(map))
				.setBlockPatterns(BlockPatterns.deserialize(map))
				.setSilkTouch((Boolean) map.getOrDefault("Silk_touch", false));

		return new SettingsData(builder);
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
}