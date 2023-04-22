package org.brokenarrow.blockmirror.utily;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TextConvertPlaceholders {

	public static ItemStack convertTextItemstack(final ItemStack itemstack, final Object... placeholders) {
		if (itemstack == null) return null;
		ItemStack itemstackClone = itemstack.clone();

		ItemMeta itemMeta = itemstackClone.getItemMeta();
		if (itemMeta == null) return itemstackClone;

		itemMeta.setDisplayName(translatePlaceholders(itemMeta.getDisplayName(), placeholders));
		if (itemMeta.getLore() != null)
			itemMeta.setLore(translatePlaceholders(itemMeta.getLore(), placeholders));
		itemstackClone.setItemMeta(itemMeta);

		return itemstackClone;
	}

	public static String translatePlaceholders(String rawText, final Object... placeholders) {
		if (rawText != null && placeholders != null)
			for (int i = 0; i < placeholders.length; i++) {
				rawText = rawText.replace("{" + i + "}", placeholders[i] != null ? placeholders[i].toString() : "");
			}
		return rawText;
	}

	public static List<String> translatePlaceholders(final List<String> rawText, final Object... placeholders) {
		final List<String> textList = new ArrayList<>();
		for (String text : rawText) {
			for (int i = 0; i < placeholders.length; i++) {
				text = text.replace("{" + i + "}", placeholders[i] != null ? placeholders[i].toString() : "");
			}
			textList.add(text);
		}
		return textList;
	}

	public static List<String> translatePlaceholdersList(final List<String> rawLore, final Object... placeholder) {
		final List<String> lores = new ArrayList<>();
		if (rawLore == null) return lores;
		for (final String lore : rawLore) {
			int index = containsList(placeholder);
			if (lore.contains("{" + index + "}") && index != -1)
				for (final String text : (List<String>) placeholder[index])
					lores.add(lore.replace("{" + containsList(placeholder) + "}", text));
			else
				lores.add(translatePlaceholders(lore, placeholder));
		}
		return lores;
	}

	public static int containsList(final Object... placeholder) {
		if (placeholder != null)
			for (int i = 0; i < placeholder.length; i++)
				if (placeholder[i] instanceof List)
					return i;
		return -1;
	}

}
