package org.brokenarrow.blockmirror.api.utility.particels;

import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.bukkit.Color.fromRGB;

/**
 * Wrapper to {@link org.bukkit.Particle.DustTransition} and {@link org.bukkit.Particle.DustTransition}
 * So you can you use this class instead, to easy suport any minecraft version some
 * not have one or both this classes in the api.
 * <p>
 * You use the getters method to parse the colors for DustTransition classes.
 */
public final class ParticleDustOptions implements ConfigurationSerializable, ParticleDustOptionsApi {

	private final Color fromColor;
	private final Color toColor;
	private final float size;

	/**
	 * Parse colors from string need be ether formated #,#,# or # # #.
	 *
	 * @param fromColor the color from you want to convert.
	 * @param size      the size if the redstone particle.
	 */
	public ParticleDustOptions(@Nonnull final String fromColor, final float size) {
		this(convertToColor(fromColor), null, size);
	}

	/**
	 * Parse colors from string need be ether formated #,#,# or # # #.
	 *
	 * @param fromColor the color from you want to convert.
	 * @param toColor   the color to you want to convert.
	 * @param size      the size if the redstone particle.
	 */
	public ParticleDustOptions(@Nonnull final String fromColor, final String toColor, final float size) {
		this(convertToColor(fromColor), convertToColor(toColor), size);
	}

	/**
	 * Set colors for the redstone particle.
	 *
	 * @param fromColor the color you want your redstone particle.
	 * @param size      the size if the redstone particle.
	 */
	public ParticleDustOptions(@Nonnull final Color fromColor, final float size) {
		this(fromColor, null, size);
	}

	/**
	 * Set colors for the redstone particle.
	 *
	 * @param fromColor the color you want your redstone particle shall start from.
	 * @param toColor   the color you want your redstone particle shall end with.
	 * @param size      the size if the redstone particle.
	 */
	public ParticleDustOptions(@Nonnull final Color fromColor, final Color toColor, final float size) {
		this.fromColor = fromColor;
		this.toColor = toColor;
		this.size = size;
	}

	@Override
	public Color getFromColor() {
		return fromColor;
	}

	@Override
	public Color getToColor() {
		return toColor;
	}

	@Override
	public float getSize() {
		return size;
	}

	/**
	 * Need format colors like #,#,# or # # #. Is in this order rgb.
	 * Suports from 0 to 255. for example 0,255,50.
	 *
	 * @param s string you want to convert to rgb.
	 * @return color class with your set colors.
	 */
	public static Color convertToColor(@Nonnull final String s) {

		if (s != null)
			if (s.contains(",")) {
				final String[] string;
				final int size = (string = s.split(",")).length;
				if (size == 3)
					return fromRGB(numberCheck(string[0]), numberCheck(string[1]), numberCheck(string[2]));

			} else if (s.contains(" ")) {
				final String[] string;
				final int size = (string = s.split(" ")).length;
				if (size == 3)
					return fromRGB(numberCheck(string[0]), numberCheck(string[1]), numberCheck(string[2]));
			}
		return fromRGB(0, 0, 0);
	}

	/**
	 * Check so the input is valid integer.
	 *
	 * @param number string you want to check.
	 * @return the number or 0 if the string not contains valid number.
	 */
	public static int numberCheck(final String number) {
		try {
			return Integer.parseInt((number));
		} catch (final NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "ParticleDustOptions{" +
				"fromColor=" + fromColor +
				", toColor=" + toColor +
				", particleSize=" + size +
				'}';
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> particleData = new LinkedHashMap<>();
		particleData.put("From_color", this.fromColor);
		if (this.toColor != null)
			particleData.put("To_color", this.toColor);
		particleData.put("Particle_size", this.size);
		return particleData;
	}

	public static ParticleDustOptions deserialize(final Map<String, Object> map) {
		final Color fromColor = (Color) map.get("From_color");
		final Color toColor = (Color) map.get("To_color");
		final Double size = (Double) map.getOrDefault("Particle_size", 1);

		return new ParticleDustOptions(fromColor, toColor, (float) (size == null ? 0.5 : size));
	}
}


