package org.brokenarrow.blockmirror.utily;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.utility.BossBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBar implements Listener, BossBarAPI {

    private static final float SERVERVERSION = BlockMirror.getPlugin().getItemCreator().getServerVersion();
    private final Map<UUID, Integer> activeBars = new HashMap<>();
    private final Map<UUID, org.bukkit.boss.BossBar> activeBarsModern = new HashMap<>();

    @Override
    public void sendBossBar(@Nonnull Player player, String title) {
        if (SERVERVERSION < 9.0f) {
            if (activeBars.containsKey(player.getUniqueId())) {
                removeBossBar(player.getUniqueId());
            }
            sendBossBar(player, title, 1.0f);
        } else {
            org.bukkit.boss.BossBar bar = activeBarsModern.get(player.getUniqueId());
            if (bar != null) {
                bar.removePlayer(player);
                // bar.setTitle(title);
            }
            org.bukkit.boss.BossBar bossBar = Bukkit.createBossBar(title, BarColor.BLUE, BarStyle.SOLID);
            bossBar.setProgress(1.0); // full bar
            bossBar.setVisible(true);
            bossBar.addPlayer(player);
            activeBarsModern.put(player.getUniqueId(), bossBar);
        }
    }

    @Override
    public void hideFromPlayer(@Nonnull Player player) {
        if (SERVERVERSION < 9.0f) {
            removeBossBar(player.getUniqueId());
        } else {
            activeBarsModern.computeIfPresent(player.getUniqueId(), (uuid, bossBar) -> {
                Player player1 = Bukkit.getPlayer(uuid);
                if (player1 != null)
                    bossBar.removePlayer(player1);
                else
                    bossBar.removeAll();
                return null;
            });
        }
    }

    @Override
    public void hideFromPlayer(@Nonnull UUID uuid) {
        if (SERVERVERSION < 9.0f) {
            removeBossBar(uuid);
        } else {
            activeBarsModern.computeIfPresent(uuid, (uuidPlayer, bossBar) -> {
                Player playerFromUuid = Bukkit.getPlayer(uuidPlayer);
                if (playerFromUuid != null)
                    bossBar.removePlayer(playerFromUuid);
                else
                    bossBar.removeAll();
                return null;
            });
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        hideFromPlayer(event.getPlayer());
    }

    private void sendBossBar(Player player, String message, float healthPercent) {
        try {
            Class<?> craftPlayerClass = getCraftBukkitClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Method getHandle = craftPlayerClass.getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(craftPlayer);

            Object world = entityPlayer.getClass().getField("world").get(entityPlayer);

            // Create Wither
            Class<?> entityWitherClass;
            try {
                entityWitherClass = getNMSClass("EntityWither");
            } catch (ClassNotFoundException e) {
                player.sendMessage("Wither-based boss bars not supported on this server.");
                return;
            }

            Constructor<?> witherConstructor = entityWitherClass.getConstructor(getNMSClass("World"));
            Object wither = witherConstructor.newInstance(world);

            // Set location (far below)
            Method setLocation = entityWitherClass.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            Location loc = player.getLocation().add(0, -300, 0);
            setLocation.invoke(wither, loc.getX(), loc.getY(), loc.getZ(), 0f, 0f);

            // Set invisible: wither.setInvisible(true)
            Method setInvisible = entityWitherClass.getMethod("setInvisible", boolean.class);
            setInvisible.invoke(wither, true);

            // Set custom name
            Method setCustomName = entityWitherClass.getMethod("setCustomName", String.class);
            setCustomName.invoke(wither, message);

            // Show name
            Method setCustomNameVisible = entityWitherClass.getMethod("setCustomNameVisible", boolean.class);
            setCustomNameVisible.invoke(wither, true);

            // Set health
            Method setHealth = entityWitherClass.getMethod("setHealth", float.class);
            float maxHealth = 300f;
            setHealth.invoke(wither, Math.max(1.0f, healthPercent * maxHealth));

            // Get entity ID
            Method getId = getNMSClass("Entity").getMethod("getId");
            int entityId = (int) getId.invoke(wither);
            activeBars.put(player.getUniqueId(), entityId);

            // Send spawn packet
            Class<?> packetClass = getNMSClass("PacketPlayOutSpawnEntityLiving");
            Constructor<?> packetConstructor = packetClass.getConstructor(getNMSClass("EntityLiving"));
            Object packet = packetConstructor.newInstance(wither);

            sendPacket(player, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeBossBar(UUID uuid) {
        try {
            Integer id = activeBars.remove(uuid);
            if (id == null) return;

            Class<?> destroyPacket = getNMSClass("PacketPlayOutEntityDestroy");
            Object packet;

            try {
                // Newer constructors (int[])
                Constructor<?> destroyConstructor = destroyPacket.getConstructor(int[].class);
                packet = destroyConstructor.newInstance((Object) new int[]{id});
            } catch (NoSuchMethodException e) {
                // Older constructor (int)
                Constructor<?> destroyConstructor = destroyPacket.getConstructor(int.class);
                packet = destroyConstructor.newInstance(id);
            }
            Player playerFromUuid = Bukkit.getPlayer(uuid);
            if (playerFromUuid != null)
                sendPacket(playerFromUuid, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPacket(Player player, Object packet) throws Exception {
        Object craftPlayer = getCraftBukkitClass("entity.CraftPlayer").cast(player);
        Object entityPlayer = craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
        Object connection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
        Method sendPacket = connection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(connection, packet);
    }

    private Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
    }

    private Class<?> getNMSClass(String name) throws ClassNotFoundException {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            // Mojang-mapped or newer package structure (MC 1.17+)
            return Class.forName("net.minecraft." + name.toLowerCase());
        }
    }

    private String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
