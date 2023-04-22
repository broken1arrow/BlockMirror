package org.brokenarrow.blockmirror.api.utility.particels;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;

import javax.annotation.Nullable;


public interface ParticleEffectApi {

	@Nullable
	Particle getParticle();

	@Nullable
	Effect getEffect();

	@Nullable
	Material getMaterial();

	int getData();

	Class<?> getDataType();

	ParticleDustOptionsApi getParticleDustOptions();

}
