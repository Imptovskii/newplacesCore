package dev.imptovskii.aeflite.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.imptovskii.aeflite.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProtocolLib implements Listener {
    public static void protocolLibWrapper(Main plugin) {
        final Set<String> crafting = new HashSet<>();
        final Map<Player, Integer> levels = new HashMap<>();
        final Map<Player, Integer> boatLevels = new HashMap<>();
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        if (plugin.getConfig().getBoolean("PreventCraftingRecipeLagExploit")) {
            protocolManager.addPacketListener(
                    new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.AUTO_RECIPE) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            if (!event.isPlayerTemporary()) {
                                if (event.getPacketType() == PacketType.Play.Client.AUTO_RECIPE) {
                                    if (crafting.contains(event.getPlayer().getName())) {
                                        event.setCancelled(true);

                                    } else {
                                        crafting.add(event.getPlayer().getName());
                                        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> crafting.remove(event.getPlayer().getName()), plugin.getConfig().getInt("CraftingRecipeDelay"));
                                    }
                                }
                            }
                        }
                    });
        }

        if (plugin.getConfig().getBoolean("PreventPacketFly")) {
            protocolManager.addPacketListener(
                    new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.TELEPORT_ACCEPT) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            if (!event.isPlayerTemporary()) {
                                Player e = event.getPlayer();
                                Location l = event.getPlayer().getLocation();
                                if (event.getPlayer().getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ()).getType() == Material.AIR && !e.isGliding() && !e.isInsideVehicle()) {
                                    if (event.getPacketType() == PacketType.Play.Client.TELEPORT_ACCEPT) {
                                        if (levels.get(e) != null) {
                                            if (levels.get(e) > plugin.getConfig().getInt("MaxTeleportPacketsPer10Seconds")) {
                                                event.setCancelled(true);
                                                if (plugin.getConfig().getBoolean("LogPacketFlyEvents")) {
                                                    plugin.getLogger().warning(e.getName() + " prevented from packetflying");
                                                }
                                            } else {
                                                levels.merge(e, 1, Integer::sum);
                                                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> levels.put(e, levels.get(e) - 1), 200L);
                                            }
                                        } else {
                                            levels.put(e, 1);
                                            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> levels.put(e, levels.get(e) - 1), 200L);
                                        }
                                    }
                                }
                            }
                        }
                    });
        }

        if (plugin.getConfig().getBoolean("BoatflyPatch")) {
            protocolManager.addPacketListener(
                    new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ENTITY) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            if (!event.isPlayerTemporary()) {
                                Player e = event.getPlayer();
                                if (e.isInsideVehicle() && e.getVehicle().getType() == EntityType.BOAT) {
                                    if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                                        if (boatLevels.get(e) != null) {
                                            if (boatLevels.get(e) > plugin.getConfig().getInt("MaxEntityPacketsPer10Seconds")) {
                                                new BukkitRunnable() {
                                                    public void run() {
                                                        e.getVehicle().remove();
                                                    }
                                                }.runTask(plugin);
                                                if (plugin.getConfig().getBoolean("LogBoatFlyEvents")) {
                                                    plugin.getLogger().warning(e.getName() + " prevented from boatflying");
                                                }
                                            } else {
                                                boatLevels.merge(e, 1, Integer::sum);
                                                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> boatLevels.put(e, boatLevels.get(e) - 1), 200L);
                                            }
                                        } else {
                                            boatLevels.put(e, 1);
                                            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> boatLevels.put(e, boatLevels.get(e) - 1), 200L);
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
    }
}


