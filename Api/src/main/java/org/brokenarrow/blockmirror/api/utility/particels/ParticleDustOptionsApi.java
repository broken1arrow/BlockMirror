package org.brokenarrow.blockmirror.api.utility.particels;

import org.bukkit.Color;

import javax.annotation.Nonnull;

public interface ParticleDustOptionsApi {

	Color getFromColor();

	Color getToColor();

	float getSize();

	/**
	 * Need format colors like #,#,# or # # #. Is in this order rgb.
	 * Suports from 0 to 255. for example 0,255,50.
	 *
	 * @param s string you want to convert to rgb.
	 * @return color class with your set colors.
	 */
	static Color convertToColor(@Nonnull final String s) {
		return null;
	}
}
