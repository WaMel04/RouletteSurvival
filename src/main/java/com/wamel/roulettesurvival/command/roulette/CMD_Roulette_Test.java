package com.wamel.roulettesurvival.command.roulette;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.config.ConfigManager;
import com.wamel.roulettesurvival.data.DataManager_Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class CMD_Roulette_Test extends CMD_Roulette {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static void run(CommandSender sender) {
        Player player = (Player) sender;
        int radius = 10;
        Location cLoc = player.getLocation().toCenterLocation();
        HashMap<Location, Material> blockMap = new HashMap<>();

        for(double x=cLoc.getX() - radius; x<cLoc.getX() + radius; x++) {
            for (double y = cLoc.getY() - radius; y < cLoc.getY() + radius; y++) {
                for (double z = cLoc.getZ() - radius; z < cLoc.getZ() + radius; z++) {
                    Location loc = new Location(player.getWorld(), x, y - 1, z);

                    switch (loc.getBlock().getType()) {
                        case COAL_ORE, DEEPSLATE_COAL_ORE, ANCIENT_DEBRIS -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.BLACK, player);
                        }
                        case IRON_ORE, DEEPSLATE_IRON_ORE, RAW_IRON_BLOCK, NETHER_QUARTZ_ORE -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.WHITE, player);
                        }
                        case GOLD_ORE, DEEPSLATE_GOLD_ORE, RAW_GOLD_BLOCK, NETHER_GOLD_ORE -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.YELLOW, player);
                        }
                        case COPPER_ORE, DEEPSLATE_COPPER_ORE, RAW_COPPER_BLOCK -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.GOLD, player);
                        }
                        case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.RED, player);
                        }
                        case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.BLUE, player);
                        }
                        case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.AQUA, player);
                        }
                        case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> {
                            blockMap.put(loc, loc.getBlock().getType());
                            setPersonalGlowingBlock(loc, org.bukkit.ChatColor.GREEN, player);
                        }
                    }
                }
            }
        }
    }


    private static int entityIdCounter = Integer.MAX_VALUE / 2;

    private static void setPersonalGlowingBlock(Location loc, org.bukkit.ChatColor color, Player target) {
        // fake entity
        int entityId = entityIdCounter;
        UUID uuid = UUID.randomUUID();

        PacketContainer packet_entity = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        packet_entity.getIntegers().write(0, entityId);
        packet_entity.getEntityTypeModifier().write(0, EntityType.SHULKER);
        packet_entity.getUUIDs().write(0, uuid);
        packet_entity.getDoubles().write(0, loc.getX());
        packet_entity.getDoubles().write(1, loc.getY() - 1);
        packet_entity.getDoubles().write(2, loc.getZ());

        // fake entity - metadata
        PacketContainer packet_entity_meta = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x60);
        packet_entity_meta.getIntegers().write(0, entityId);
        packet_entity_meta.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        entityIdCounter = entityIdCounter - 1;

        // fake entity - effect
        //PacketContainer packet_entity_effect = new PacketContainer(PacketType.Play.Server.ENTITY_EFFECT);
//
        //packet_entity_effect.getIntegers().write(0, entityId);
        //packet_entity_effect.getBytes().write(0, (byte) 14);
        //packet_entity_effect.getBytes().write(1, (byte) 0);
        //packet_entity_effect.getIntegers().write(1, 20*99999999);
        //packet_entity_effect.getBytes().write(2, (byte) 0);
        //packet_entity_effect.getIntegers().write(1, 14); // invisible effect
        ////packet_entity_effect.getIntegers().write(2, 20*99999999); // duration
//
        //packet_entity_effect.getBytes().write(0, (byte) 0); // amplifier
        //packet_entity_effect.getBytes().write(1, (byte) 0x02); // hide particles

        // 팀 패킷
        PacketContainer packet_team = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet_team.getStrings().write(0, entityId + "." + target.getEntityId()); // team name
        packet_team.getIntegers().write(0, 0); // Mode: create team

        Optional<InternalStructure> optStructure = packet_team.getOptionalStructures().read(0);

        if(optStructure.isPresent()) {
            InternalStructure structure = optStructure.get();
            structure.getChatComponents().write(0,
                    WrappedChatComponent.fromText("")); // Team Display Name
            structure.getEnumModifier(org.bukkit.ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat"))
                    .write(0, color); // Team Color
            packet_team.getOptionalStructures().write(0, Optional.of(structure));
        }

        packet_team.getModifier().write(2, Lists.newArrayList(uuid.toString(), target.getName()));


        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet_entity);
            ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet_entity_meta);
            //ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet_entity_effect);
            ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet_team);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
