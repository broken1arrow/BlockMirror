package org.brokenarrow.blockmirror.api.settings;

import org.brokenarrow.blockmirror.api.builders.ToolsApi;
import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatternsApi;

public interface SettingsDataApi {
  
  String getMainCommand();

  BlockPatternsApi getBlockPatterns();

  ToolsApi getTools();

  int getClassicBlockPlaceDistance();

  boolean isSilkTouch();
}
