package com.wamel.roulettesurvival.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class Util {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static Integer getEmptySlotInStorage(Inventory inv) {
        int max = inv.getStorageContents().length;
        int emptySlot = 0;

        for(int i=0; i<max; i++) {
            if(inv.getStorageContents()[i] == null)
                emptySlot++;
        }

        return emptySlot;
    }

    public static void playSoundAll(Sound sound, int volume, int pitch) {
        for(Player player: Bukkit.getOnlinePlayers()) {
            player.playSound(player, sound, volume, pitch);
        }
    }

    public static void sendTitleWithSound(Player player, String title, String subtitle, int fadeIn, int tick, int fadeOut, Sound sound, int volume, float pitch) {
        player.sendTitle(title, subtitle, fadeIn, tick, fadeOut);
        player.playSound(player, sound, volume, pitch);
    }

    public static void broadcast(String message, Player ignoredPlayer) {
        for(Player player: Bukkit.getOnlinePlayers()) {
            if(!(player.equals(ignoredPlayer)))
                player.sendMessage(message);
        }
    }

    public static void setGlowing(Entity entity, ChatColor color) {
        entity.setGlowing(true);

        for(Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = player.getScoreboard();

            if(board.getTeam("rGlow_" + entity.getUniqueId()) != null)
                return;

            Team team = board.registerNewTeam("rGlow_" + entity.getUniqueId());
            team.setColor(color);
            team.addEntry(entity.getUniqueId().toString());
        }
    }

    public static void unsetGlowing(Entity entity) {
        entity.setGlowing(false);

        for(Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = player.getScoreboard();


            if(board.getTeam("rGlow_" + entity.getUniqueId()) == null)
                return;

            Team team = board.getTeam("rGlow_" + entity.getUniqueId());
            team.unregister();
        }
    }

    public static Boolean hasREvent(ArrayList<REventBase> list, REventBase base) {
        for(REventBase b : list) {
            if(b.getName().equals(base.getName())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean hasREvent(ArrayList<REventBase> list, String name) {
        for(REventBase b : list) {
            if(b.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void spreadFirework(Player player) {
        Location loc = player.getLocation().clone();

        double radius = 5;
        double increment = (2 * Math.PI) / 15;
        for(int i = 0; i < 15; i++) {
            double angle = i * increment;
            double x = (radius * Math.cos(angle));
            double z = (radius * Math.sin(angle));

            Firework fw = (Firework) player.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta fwMeta = fw.getFireworkMeta();

            fwMeta.setPower(10);
            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.PURPLE).flicker(true).with(FireworkEffect.Type.BALL).build());
            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.FUCHSIA).flicker(true).with(FireworkEffect.Type.BALL).build());
            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).flicker(true).with(FireworkEffect.Type.BALL).build());

            fw.setFireworkMeta(fwMeta);
            fw.setMetadata("roul_nodamage", new FixedMetadataValue(plugin, true));

            fw.setGravity(false);
            fw.setVelocity((new Vector(x*1.5, 5, z*1.5)).normalize().multiply(0.1));
        }

    }

}
