package org.brokenarrow.blockmirror.api.utility.particels;

import com.google.common.base.Enums;
import org.broken.arrow.menu.library.utility.ServerVersion;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.utility.Pair;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConvetParticlesUntlity {

	public static List<ParticleEffectApi> convertListOfParticles(final List<String> particles) {
		if (particles == null) return null;

		final List<ParticleEffectApi> particleList = new ArrayList<>();
		for (final String particle : particles) {
			final ParticleEffect particleEffect = getParticleOrEffect(particle, null, 0.7F);
			if (particleEffect == null) continue;
			particleList.add(particleEffect);
		}
		return particleList;
	}

	public static List<ParticleEffect> convertListOfParticles(final Map<String, Object> particles) {
		if (particles == null) return null;

		final List<ParticleEffect> particleList = new ArrayList<>();
		for (final Map.Entry<String, Object> particle : particles.entrySet()) {
			final ParticleEffect particleEffect = getParticleOrEffect(particle.getKey(), (String) particle.getValue(), 0.7F);
			if (particleEffect == null) continue;
			particleList.add(particleEffect);
		}
		return particleList;
	}

	public static List<ParticleEffectApi> convertListOfParticlesPair(final List<Pair<String, Object>> particles) {
		if (particles == null) return null;
		final List<ParticleEffectApi> particleList = new ArrayList<>();
		for (final Pair<String, Object> particle : particles) {
			if (particle.getSecond() instanceof List) {
				BlockMirrorUtillity.getInstance().log(Level.WARNING, "This is an list, should be string  " + particle.getSecond());
				continue;
			}
			final ParticleEffect particleEffect = getParticleOrEffect(particle.getFirst(), (String) particle.getSecond(), 0.7F);
			if (particleEffect == null) continue;
			particleList.add(particleEffect);
		}
		return particleList;
	}

	public static List<Object> convertParticleEffectList(final List<ParticleEffect> particles) {
		if (particles == null) return null;

		final List<Object> particleList = new ArrayList<>();
		for (final ParticleEffect particle : particles) {
			if (particle == null) continue;

			final Particle partic = particle.getParticle();
			if (partic != null)
				particleList.add(partic);
			else {
				final Effect effect = particle.getEffect();
				if (effect != null) {
					particleList.add(effect);
				}
			}
		}
		return particleList;
	}

	public static ParticleEffect getParticleOrEffect(final Object particle, final String color, final float flot) {
		return getParticleOrEffect(particle, color, null, flot);
	}

	public static ParticleEffect getParticleOrEffect(final Object particle, final String firstColor, final String secondColor, final float flot) {
		if (particle == null) return null;
		Object partc;
		if (ServerVersion.olderThan(ServerVersion.V1_9)) {
			partc = getEffect(String.valueOf(particle));
		} else {
			partc = getParticle(String.valueOf(particle));
			if (partc == null) {
				partc = getEffect(String.valueOf(particle));
			}
		}

		final ParticleEffect.Builder builder = new ParticleEffect.Builder();
		if (ServerVersion.atLeast(ServerVersion.V1_9)) {
			if (partc instanceof Particle) {
				final Particle part = (Particle) partc;
				builder.setParticle(part).setDataType(part.getDataType());
				if (part.name().equals("BLOCK_MARKER")) {
					builder.setMaterial(Material.BARRIER);
				}
				if (firstColor != null && part.name().equals("REDSTONE"))
					if (secondColor != null)
						builder.setDustOptions(new ParticleDustOptions(firstColor, secondColor, flot));
					else
						builder.setDustOptions(new ParticleDustOptions(firstColor, flot));
			}
		}
		if (partc instanceof Effect) {
			final Effect part = (Effect) partc;
			builder.setEffect(part).setDataType(part.getData());
			if (firstColor != null && part.name().equals("REDSTONE"))
				if (secondColor != null)
					builder.setDustOptions(new ParticleDustOptions(firstColor, secondColor, flot));
				else
					builder.setDustOptions(new ParticleDustOptions(firstColor, flot));
		}
		if (partc == null)
			return null;
		return builder.build();
	}

	public static boolean isParticleThisClazz(final Object particle, final Class<?>... clazz) {
		if (particle == null) return false;
		for (final Class<?> obj : clazz) {
			if (obj == null) continue;
			final Particle partc = getParticle(String.valueOf(particle));

			if (partc != null) {
				if (obj == partc.getDataType()) {
					return true;
				}
			}
			if (ServerVersion.olderThan(ServerVersion.V1_9)) {
				final Effect effect = getEffect(String.valueOf(particle));
				if (effect != null)
					if (obj == effect.getData()) {
						return true;
					}
			}
		}
		return false;
	}

	public static Particle getParticle(String particle) {
		if (particle == null) return null;
		final Particle[] particles = Particle.values();
		particle = particle.toUpperCase();
		particle = replaceOldParticle(particle);
		for (final Particle partic : particles) {
			if (partic.name().equals(particle))
				return partic;
		}
		return null;
	}

	public static Effect getEffect(String particle) {
		if (particle == null) return null;
		final Effect[] effects = Effect.values();
		particle = particle.toUpperCase();

		for (final Effect effect : effects) {
			if (effect.name().equals(particle))
				return effect;
		}
		return null;
	}

	public static Effect.Type getEffectType(String effectType) {
		if (effectType == null) return null;
		final Effect[] effects = Effect.values();
		effectType = effectType.toUpperCase();

		for (final Effect effect : effects) {
			if (effect.getType().name().equals(effectType))
				return effect.getType();
		}
		return null;
	}

	public static String replaceOldParticle(final String particle) {

		if (ServerVersion.atLeast(ServerVersion.V1_17))
			if (particle.equals("BARRIER"))
				return "BLOCK_MARKER";
		return particle;
	}

	public static Effect checkParticleOld(final String particle) {

		if (ServerVersion.olderThan(ServerVersion.V1_9)) {
			if (particle != null && Enums.getIfPresent(Effect.class, particle).orNull() != null)
				return Effect.valueOf(particle);
		} else
			return null;
		return null;
	}
}
