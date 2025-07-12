package org.brokenarrow.blockmirror.settings;

import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SimpleYamlHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenusCache extends SimpleYamlHelper {
	private final Plugin plugin;
	private static final int version = 1;
	private final Map<String, MenuTemplate> templates = new HashMap<>();

	public MenusCache(final Plugin plugin) {
		super(plugin, "language/menu_templets.yml");
		this.plugin = plugin;
		//checkFileVersion();
	}

	public MenuTemplate getTemplate(String menuName) {
		return templates.get(menuName);
	}

	public Map<String, MenuTemplate> getTemplates() {
		return templates;
	}

	@Override
	public void loadSettingsFromYaml(final File file) {
		final FileConfiguration templateConfig = this.getCustomConfig();
		ConfigurationSection configurationSection = templateConfig.getConfigurationSection("Menus");
		if (configurationSection != null)
			for (final String key : configurationSection.getKeys(false)) {
				final ConfigurationSection menuData = templateConfig.getConfigurationSection("Menus." + key + ".buttons");
				final Map<List<Integer>, MenuButtonData> menuButtonMap = new HashMap<>();

				final String menuSettings = templateConfig.getString("Menus." + key + ".menu_settings.name");
				final List<Integer> fillSpace = parseRange(templateConfig.getString("Menus." + key + ".menu_settings.fill-space"));
				final String sound = templateConfig.getString("Menus." + key + ".menu_settings.sound");
				if (menuData != null) {
					for (final String menuButtons : menuData.getKeys(false)) {
						final MenuButtonData menuButton = this.getData("Menus." + key + ".buttons." + menuButtons, MenuButtonData.class);
						menuButtonMap.put(parseRange(menuButtons), menuButton);
					}
				}
				final MenuTemplate menuTemplate = new MenuTemplate(menuSettings, fillSpace, menuButtonMap, sound);

				templates.put(key, menuTemplate);
			}
	}

	private List<Integer> parseRange(final String range) {
		final List<Integer> slots = new ArrayList<>();

		//Allow empty ranges.
		if (range == null || range.equals("")) return slots;

		try {
			for (final String subRange : range.split(",")) {
				if (Objects.equals(subRange, "")) continue;
				if (subRange.contains("-")) {
					final String[] numbers = subRange.split("-");
					if (numbers[0].isEmpty() || numbers[1].isEmpty()) {
						slots.add(Integer.parseInt(subRange));
						continue;
					}
					final int first = Integer.parseInt(numbers[0]);
					final int second = Integer.parseInt(numbers[1]);
					slots.addAll(IntStream.range(first, second + 1).boxed().collect(Collectors.toList()));
				} else slots.add(Integer.parseInt(subRange));
			}
		} catch (final NumberFormatException e) {
			plugin.getLogger().log(Level.WARNING, "Couldn't parse range " + range);
		}
		return slots;
	}

	@Override
	protected void saveDataToFile(final File file) {

	}


}
