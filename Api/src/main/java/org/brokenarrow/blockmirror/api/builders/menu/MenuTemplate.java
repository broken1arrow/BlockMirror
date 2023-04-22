package org.brokenarrow.blockmirror.api.builders.menu;


import org.brokenarrow.blockmirror.api.filemanger.SoundUtility;
import org.bukkit.Sound;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MenuTemplate {

	private final String menuTitel;
	private final List<Integer> fillSlots;
	private final Map<List<Integer>, MenuButtonData> menuButtons;
	private final int amountOfButtons;
	private final Sound sound;

	public MenuTemplate(String menuTitel, List<Integer> fillSlots, Map<List<Integer>, MenuButtonData> menuButtons, String sound) {
		this.menuTitel = menuTitel;
		this.fillSlots = fillSlots;
		this.menuButtons = menuButtons;
		if (fillSlots != null && !fillSlots.isEmpty())
			this.amountOfButtons = calculateAmountOfButtons(menuButtons, fillSlots);
		else
			this.amountOfButtons = calculateAmountOfButtons(menuButtons);
		this.sound = SoundUtility.getSound(sound);
	}

	public int getAmountOfButtons() {
		return amountOfButtons;
	}

	/**
	 * This method check if value can match inventory size like 9 * x and not go over inventory max size of 6 rows.
	 *
	 * @param menu the menu you set the inventory size so you know what menu has problems.
	 * @return inventory size.
	 */
	public int getinvSize(final String menu) {
		int size = getAmountOfButtons();
		if (size < 9) return 9;
		if (size < 18) return 18;
		if (size < 27) return 27;
		if (size < 36) return 36;
		if (size < 45) return 45;
		if (size > 54)
			System.out.println("This menu " + menu + " has set bigger inventory size an it can handle, your set size " + size + ". will defult to 54.");
		return 54;
	}

	public String getMenuTitel() {
		return menuTitel;
	}

	public List<Integer> getFillSlots() {
		return fillSlots;
	}

	public Sound getSound() {
		return sound;
	}

	public Map<List<Integer>, MenuButtonData> getMenuButtons() {
		return menuButtons;
	}

	public MenuButtonData getMenuButton(int slot) {
		for (Entry<List<Integer>, MenuButtonData> slots : menuButtons.entrySet()) {
			for (int menuSlot : slots.getKey())
				if (menuSlot == slot)
					return slots.getValue();
		}
		return null;
	}

	public int calculateAmountOfButtons(final Map<List<Integer>, MenuButtonData> menuButtons, final List<Integer> fillSlots) {
		int lastButton = 0;
		for (final List<Integer> slots : menuButtons.keySet()) {
			for (final Integer slot : slots) {
				lastButton = Math.max(lastButton, slot);
			}
		}
		return lastButton;
	}

	public int calculateAmountOfButtons(Map<List<Integer>, MenuButtonData> menuButtons) {
		int amountOfButtons = 0;
		for (List<Integer> slots : menuButtons.keySet()) {
			for (final Integer slot : slots) {
				amountOfButtons = Math.max(amountOfButtons, slot);
			}
		}
		return amountOfButtons;
	}

}