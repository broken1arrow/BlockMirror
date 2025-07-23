package org.brokenarrow.blockmirror.api.builders;

import org.bukkit.Material;

import java.util.List;

public interface ItemWrapperApi {

	Material getMaterial();

  String getDisplayName();

  List<String> getLore();

  boolean isGlow();

  ItemWrapperBuilder getBuilder();

}