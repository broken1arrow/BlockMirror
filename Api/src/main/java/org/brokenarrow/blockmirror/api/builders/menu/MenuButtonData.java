package org.brokenarrow.blockmirror.api.builders.menu;

import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MenuButtonData implements ConfigurationSerializeUtility {

	private final MenuButton passive;
	private final MenuButton active;
	private final ButtonType buttonType;

	private MenuButtonData(final MenuButton passive, final MenuButton active, ButtonType buttonType) {
		this.passive = passive;
		this.active = active;
		this.buttonType = buttonType;
	}

	public MenuButton getPassive() {
		return passive;
	}

	public MenuButton getActive() {
		return active;
	}

	public ButtonType getButtonType() {
		return buttonType;
	}

	@Override
	public String toString() {
		return "MenuButtonData{" +
				"passive=" + passive +
				", active=" + active +
				", buttonType=" + buttonType +
				'}';
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("passive", this.passive);
		map.put("active", this.active);
		if (buttonType != null) map.put("buttonType", buttonType);
		return map;
	}

	public static MenuButtonData deserialize(final Map<String, Object> map) {
		Map<String, Object> activeData = new LinkedHashMap<>();
		Map<String, Object> passiveData = new LinkedHashMap<>();
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().startsWith("active.")) {
				String key = entry.getKey().replace("active.", "");
				activeData.put(key, entry.getValue());
			}
			if (entry.getKey().startsWith("passive.")) {
				String key = entry.getKey().replace("passive.", "");
				passiveData.put(key, entry.getValue());
			}
		}
		MenuButton deserializeActiveData = null;
		MenuButton deserializePassiveData;
		if (!activeData.isEmpty())
			deserializeActiveData = MenuButton.deserialize(activeData);
		if (!passiveData.isEmpty())
			deserializePassiveData = MenuButton.deserialize(passiveData);
		else
			deserializePassiveData = MenuButton.deserialize(map);
		String buttonType = (String) map.get("buttonType");
		return new MenuButtonData(deserializePassiveData, deserializeActiveData, ButtonType.valueOfType(buttonType));
	}
}
