package org.brokenarrow.blockmirror.api.builders.menu;

public enum ButtonType {

	Nxt_page(""),
	Prv_page(""),
	Back(""),
	Search(""),
	Buyb(""),
	Sellb(""),
	Add_auction(""),
	Save(""),
	Prepare_to_save(""),
	Start(""),
	CentreLoc(""),
	mirrorX(""),
	mirrorY(""),
	mirrorZ(""),
	mirrorXY(""),
	mirrorZY(""),
	mirrorXZ(""),
	mirrorZX(""),
	rotateUp_90(""),
	rotateClockWise_90(""),
	rotateCounterClockWise_90(""),
	rotate_180(""),
	block_face(""),
	block_replace_block(""),
	opposite_face_of_block("");
	private final String type;

	ButtonType(String type) {

		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static ButtonType valueOfType(String buttontype) {
		ButtonType[] buttonTypes = ButtonType.values();
		for (ButtonType buttonType : buttonTypes) {
			if (buttonType.name().equalsIgnoreCase(buttontype))
				return buttonType;
		}
		return null;
	}
}