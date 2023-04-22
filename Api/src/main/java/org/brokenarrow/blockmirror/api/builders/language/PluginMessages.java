package org.brokenarrow.blockmirror.api.builders.language;


import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PluginMessages implements ConfigurationSerializeUtility {

	private final Map<String, List<String>> messages;
	private final PluginMessages pluginMessages;
	private String pluginName;
	private String prefixDecor;
	private String suffixDecor;

	private PluginMessages(Map<String, List<String>> messages) {
		this.messages = messages;
		this.pluginMessages = this;
	}

	@Nonnull
	public List<String> getMessage(String key) {
		List<String> message = messages.get(key);

		if (message != null) return message;
		return new ArrayList<>();
	}

	public PluginMessages getPluginMessages() {
		return pluginMessages;
	}

	@Nullable
	public String getPluginName() {
		return pluginName;
	}

	@Nullable
	public String getPrefixDecor() {
		return prefixDecor;
	}

	@Nullable
	public String getSuffixDecor() {
		return suffixDecor;
	}

	public void setPrefixDecor(final String prefixDecor) {
		this.prefixDecor = prefixDecor;
	}

	public void setSuffixDecor(final String suffixDecor) {
		this.suffixDecor = suffixDecor;
	}

	public void setPluginName(final String pluginName) {
		this.pluginName = pluginName;
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		return new LinkedHashMap<>(messages);
	}

	public static PluginMessages deserialize(Map<String, Object> map) {
		final Map<String, List<String>> messages = new HashMap<>();
		for (Entry<String, Object> entry : map.entrySet())
			messages.put(entry.getKey(), convertToList(entry.getValue() != null ? entry.getValue() : null));
/*		messages.put("Classic_command_clear", convertToList(map.getOrDefault("Classic_command_clear", null)));
		messages.put("Custom_command_clear", convertToList(map.getOrDefault("Custom_command_clear", null)));
		messages.put("Custom_command_set", convertToList(map.getOrDefault("Custom_command_set", null)));
		messages.put("Custom_command_place", convertToList(map.getOrDefault("Custom_command_place", null)));
		messages.put("Command_reload", convertToList(map.getOrDefault("Command_reload", null)));
		messages.put("Drop_item", convertToList(map.getOrDefault("Drop_item", null)));
		messages.put("Switch_slot", convertToList(map.getOrDefault("Switch_slot", null)));*/
		return new PluginMessages(messages);
	}

	public static List<String> convertToList(Object object) {
		if (object instanceof String) {
			return Collections.singletonList((String) object);
		}
		if (object instanceof List) {
			return (List<String>) object;
		}
		return null;
	}

}