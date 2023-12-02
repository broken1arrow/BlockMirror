package org.brokenarrow.blockmirror.api.utility.particels;


import org.broken.arrow.menu.library.utility.ServerVersion;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;

/**
 * This class is for create the particle, it will check auto what type of effect
 * and use right method for it (so this working from 1.8 to 1.18).
 */
public class CreateParticle {

	private final Effect effect;
	private final Particle particle;
	private final Material material;
	private final Class<?> dataType;
	private final ParticleDustOptionsApi particleDustOptions;
	private final Player player;
	private final int data;
	private final World world;
	private final double x;
	private final double y;
	private final double z;


	/**
	 * Create a effect from location.
	 *
	 * @param effect   the ParticleEffect class wrapper.
	 * @param location location were the effect shall spawn.
	 */

	public CreateParticle(@Nonnull final Player player, @Nonnull final ParticleEffectApi effect, @Nonnull final Location location) {
		this(player, effect, location.getWorld(), location.getX(), location.getY(), location.getZ());
	}

	/**
	 * Create a effect from location.
	 *
	 * @param effect   the ParticleEffect class wrapper.
	 * @param location location were the effect shall spawn.
	 */

	public CreateParticle(final ParticleEffectApi effect, @Nonnull final Location location) {
		this(null, effect, location.getWorld(), location.getX(), location.getY(), location.getZ());
	}

	/**
	 * Create a effect from location.
	 *
	 * @param effect the ParticleEffect class wrapper.
	 * @param world  it shall spawn the particle.
	 * @param x      cordinat were it shall spawn.
	 * @param y      cordinat were it shall spawn.
	 * @param z      cordinat were it shall spawn.
	 */
	public CreateParticle(@Nonnull final ParticleEffectApi effect, final World world, final double x, final double y, final double z) {
		this(null, effect, world, x, y, z);
	}

	/**
	 * Create a effect from location.
	 *
	 * @param effect the ParticleEffect class wrapper.
	 * @param world  it shall spawn the particle.
	 * @param x      cordinat were it shall spawn.
	 * @param y      cordinat were it shall spawn.
	 * @param z      cordinat were it shall spawn.
	 */
	public CreateParticle(final Player player, final ParticleEffectApi effect, final World world, final double x, final double y, final double z) {
		this.particle = effect.getParticle();
		this.effect = effect.getEffect();
		this.material = effect.getMaterial();
		this.data = effect.getData();
		this.dataType = effect.getDataType();
		this.particleDustOptions = effect.getParticleDustOptions();
		this.world = world;
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Create the effect/ particle effect from {@link ParticleEffect} class.
	 *
	 * @return true if it could spawn the effect/ particle effect.
	 */

	public boolean create() {
		final ParticleDustOptionsApi particleDustOptions = this.particleDustOptions;
		if (particleDustOptions != null) {
			if (ServerVersion.newerThan(ServerVersion.V1_16) && particleDustOptions.getToColor() != null)
				spawnDustTransitionParticle(new Particle.DustTransition(particleDustOptions.getFromColor(), particleDustOptions.getToColor(), particleDustOptions.getSize()));
			else
				spawnDustOptionsParticle(new Particle.DustOptions(particleDustOptions.getFromColor(), particleDustOptions.getSize()));
			return true;
		} else
			return checkTypeParticle();
	}

	public void spawnDustOptionsParticle(final Particle.DustOptions dustOptions) {
		if (player != null) {
			if (this.effect != null)
				player.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.data);
			else
				player.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, dustOptions);
		} else {
			if (this.effect != null)
				this.world.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.data);
			else
				this.world.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, dustOptions);
		}
	}

	public void spawnDustTransitionParticle(final Particle.DustTransition dustOptions) {
		if (player != null) {
			if (this.effect != null)
				player.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.data);
			else
				player.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, dustOptions);
		} else {
			if (this.effect != null)
				this.world.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.data);
			else
				this.world.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, dustOptions);
		}
	}

	public boolean checkTypeParticle() {
		if (this.effect != null)
			return this.spawnEffect();
		else
			return spawnParticle();
	}

	public boolean spawnEffect() {
		if (this.effect == null) return false;

		if (this.material != null) {
			if (player != null) {
				if (this.dataType == Material.class)
					player.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.material);
				if (this.dataType == MaterialData.class)
					player.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.material.getData());
			} else {
				if (this.dataType == Material.class)
					this.world.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.material);
				if (this.dataType == MaterialData.class)
					this.world.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.material.getData());
			}
			return true;
		} else {
			if (player != null)
				player.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.data);
			else
				this.world.playEffect(new Location(this.world, this.x, this.y, this.z), this.effect, this.data);
			return true;
		}
	}

	public boolean spawnParticle() {
		if (particle == null) return false;

		if (this.material != null && this.dataType != Void.class) {
			if (player != null) {
				if (this.dataType == BlockData.class)
					player.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, 3, this.material.createBlockData());
				if (this.dataType == ItemStack.class)
					player.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, 3, new ItemStack(this.material));
			} else {
				if (this.dataType == BlockData.class)
					this.world.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, 3, this.material.createBlockData());
				if (this.dataType == ItemStack.class)
					this.world.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, 3, new ItemStack(this.material));
			}
			return true;
		} else if (this.dataType == Void.class) {
			if (player != null) {
				player.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, 3);
			}
			this.world.spawnParticle(particle, this.x, this.y, this.z, 0, 0.0, 0.0, 0.0, 3);
			return true;
		}
		return false;
	}
}
