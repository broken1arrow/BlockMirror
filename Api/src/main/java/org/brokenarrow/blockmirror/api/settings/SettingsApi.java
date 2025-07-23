package org.brokenarrow.blockmirror.api.settings;

import javax.annotation.Nullable;

public interface SettingsApi {
	@Nullable
	SettingsDataApi getSettingsData();
}
