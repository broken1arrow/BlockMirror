package org.brokenarrow.blockmirror.api.settings;

import org.brokenarrow.blockmirror.api.builders.SettingsData;

import javax.annotation.Nullable;

public interface SettingsApi {
	@Nullable
	SettingsData getSettingsData();
}
