package org.brokenarrow.blockmirror.settings;

import org.brokenarrow.blockmirror.api.settings.SettingsDataApi;
import org.brokenarrow.blockmirror.blockpatterns.builders.SettingsData;
import org.brokenarrow.blockmirror.api.filemanger.SimpleYamlHelper;
import org.brokenarrow.blockmirror.api.settings.SettingsApi;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;

public class Settings extends SimpleYamlHelper implements SettingsApi {

	private SettingsDataApi settings;

	public Settings(final Plugin plugin) {
		super(plugin, "settings.yml");
	}

	@Override
	protected void saveDataToFile(final File file) {

	}

	@Nullable
	@Override
	public SettingsDataApi getSettingsData() {
		return settings;
	}

	@Override
	protected void loadSettingsFromYaml(final File file) {

		settings = this.getData("", SettingsData.class);
	}
}
