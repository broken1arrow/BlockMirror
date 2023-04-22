package org.brokenarrow.blockmirror.settings;

import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.language.PluginMessages;
import org.brokenarrow.blockmirror.api.filemanger.SimpleYamlHelper;
import org.brokenarrow.blockmirror.api.settings.LanguageCacheApi;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageCache extends SimpleYamlHelper implements LanguageCacheApi {

	//private final Map<String, Language> language = new HashMap<>();
	private Language language;

	public LanguageCache(final Plugin plugin) {
		super(plugin, "language/language.yml");
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	protected void saveDataToFile(final File file) {

	}

	@Override
	protected void loadSettingsFromYaml(final File file) {
		final FileConfiguration templateConfig = this.getCustomConfig();

		ConfigurationSection configurationSection = templateConfig.getConfigurationSection("");
		Map<String, Object> map = new HashMap<>();
		if (configurationSection != null)
			for (final String key : configurationSection.getKeys(false)) {
			/*	ConfigurationSection configuration = templateConfig.getConfigurationSection(key);
				if (configuration != null)
					for (final String data : configuration.getKeys(true)) {
						final Object object = templateConfig.get(key + "." + data);
						if (object instanceof MemorySection) continue;
						map.put(key + "." + data, object);
					}*/
				if (key.equals("Placeholders"))
					map.put(key, this.getData(key, PlaceholderText.class));
				if (key.equals("Messages") || key.equals("Plugin_prefix"))
					map.put(key, this.getData(key, PluginMessages.class));
			}
		Object pluginMessage = map.get("Messages");
		if (pluginMessage instanceof PluginMessages) {
			PluginMessages pluginMessages = (PluginMessages) pluginMessage;
			pluginMessages.setPluginName(templateConfig.getString("Plugin_name"));
			pluginMessages.setPrefixDecor(templateConfig.getString("Prefix_decor"));
			pluginMessages.setSuffixDecor(templateConfig.getString("Suffix_decor"));
		}

		this.language = Language.deserialize(map);
	}
}
