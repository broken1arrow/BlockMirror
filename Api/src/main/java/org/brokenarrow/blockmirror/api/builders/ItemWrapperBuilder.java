package org.brokenarrow.blockmirror.api.builders;

import org.bukkit.Material;

import java.util.List;

public interface ItemWrapperBuilder {

  ItemWrapperBuilder setMaterial(final Material material);

  ItemWrapperBuilder setDisplayName(final String displayName);

  ItemWrapperBuilder setLore(final List<String> lore);

  ItemWrapperBuilder setGlow(final boolean glow);

  ItemWrapperApi build();
}
