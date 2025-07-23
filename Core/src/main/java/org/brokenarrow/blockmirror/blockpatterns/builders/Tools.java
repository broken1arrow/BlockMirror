package org.brokenarrow.blockmirror.blockpatterns.builders;

import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ToolBuilder;
import org.brokenarrow.blockmirror.api.builders.ToolsApi;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.brokenarrow.blockmirror.api.utility.Pair;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class Tools implements ToolsApi,ConfigurationSerializeUtility {

	private final ItemWrapperApi markertool;
	private final Builder builder;

	private Tools(Builder builder) {
		this.markertool = builder.markertool;

		this.builder = builder;
	}

	public ItemWrapperApi getMarkertool() {
		return markertool;
	}

	public ToolBuilder getBuilder() {
		return builder;
	}

	public static class Builder implements ToolBuilder{

		private ItemWrapperApi markertool;

		public Builder setMarkertool(final ItemWrapperApi markertool) {
			this.markertool = markertool;
			return this;
		}

		public Tools build() {
			return new Tools(this);
		}
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Marker_tool", markertool);
		return map;
	}

	public static Tools deserialize(Map<String, Object> map) {
		String toolsKey = "Tools.";

		ItemWrapper marker_tool = ItemWrapper.deserialize(deserializeData(
				new Pair<>("Display_name", map.get(toolsKey + "Marker_tool.Display_name")),
				new Pair<>("Material", map.get(toolsKey + "Marker_tool.Matrial")),
				new Pair<>("Lore", map.get(toolsKey + "Marker_tool.Lore"))));
		Builder builder = new Builder()
				.setMarkertool(marker_tool);

		return new Tools(builder);
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
		return "Tools{" +
				"markertool=" + markertool +
				", builder=" + builder +
				'}';
	}
}