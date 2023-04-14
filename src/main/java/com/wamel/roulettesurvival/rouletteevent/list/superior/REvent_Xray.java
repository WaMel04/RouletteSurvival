package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.*;

public class REvent_Xray extends REventBase {

    private static final Integer PERIOD = 20*3;
    private static final Integer RADIUS = 10;

    public static HashMap<RoulettePlayer, HashMap<Location, Integer>> xrayMap = new HashMap<>();

    public REvent_Xray(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#008080") + "§lX-Ray", "주변 광물의 위치를 탐지합니다.", 180, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f걸어다니는 금속 탐지기가 됩니다.");
        player.sendMessage("   §7일정 주기마다 반경 " + RADIUS + "m 내의 광물의 위치를 표시합니다.");
        player.sendMessage("   §7석탄, 철, 금, 구리, 레드스톤, 청금석, 다이아몬드, 에메랄드, 석영, 네더라이트를 탐지할 수 있습니다.");
        player.sendMessage("");

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        HashMap<Location, Integer> blockMap = new HashMap<>(); // loc, entityId
        xrayMap.put(rPlayer, blockMap);

        new BukkitRunnable() {
            @Override
            public void run() {
                Player oPlayer = rPlayer.getPlayer();

                if(oPlayer != null) {
                    if(getIsEnabled(oPlayer) == false) {
                        for(Integer entityId : blockMap.values()) {
                            unsetPersonalGlowingBlock(entityId, oPlayer);
                        }

                        xrayMap.remove(rPlayer);
                        super.cancel();
                        return;
                    }
                    for(Integer entityId : blockMap.values()) {
                        unsetPersonalGlowingBlock(entityId, oPlayer);
                    }

                    Location cLoc = oPlayer.getLocation().toCenterLocation();

                    for(double x=cLoc.getX() - RADIUS; x<cLoc.getX() + RADIUS; x++) {
                        for(double y=cLoc.getY() - RADIUS; y<cLoc.getY() + RADIUS; y++) {
                            for(double z=cLoc.getZ() - RADIUS; z<cLoc.getZ() + RADIUS; z++) {
                                Location loc = new Location(oPlayer.getWorld(), x, y - 1, z);

                                switch (loc.getBlock().getType()) {
                                    case COAL_ORE, DEEPSLATE_COAL_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.BLACK, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case IRON_ORE, DEEPSLATE_IRON_ORE, RAW_IRON_BLOCK, NETHER_QUARTZ_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.WHITE, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case GOLD_ORE, DEEPSLATE_GOLD_ORE, RAW_GOLD_BLOCK, NETHER_GOLD_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.YELLOW, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case COPPER_ORE, DEEPSLATE_COPPER_ORE, RAW_COPPER_BLOCK -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.GOLD, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.RED, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.BLUE, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.AQUA, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.GREEN, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                    case ANCIENT_DEBRIS -> {
                                        Integer entityId = setPersonalGlowingBlock(loc, org.bukkit.ChatColor.DARK_PURPLE, oPlayer);
                                        blockMap.put(loc, entityId);
                                    }
                                }
                            }
                        }
                    }

                    xrayMap.put(rPlayer, blockMap);
                }
            }
       }.runTaskTimer(plugin, PERIOD, PERIOD);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(event.getPlayer());

        if(!(xrayMap.containsKey(rPlayer)))
            return;

        HashMap<Location, Integer> blockMap = xrayMap.get(rPlayer);
        Location loc = event.getBlock().getLocation().toCenterLocation();

        if(blockMap.containsKey(loc)) {
            unsetPersonalGlowingBlock(blockMap.get(loc), event.getPlayer());
        }
    }

    private static int entityIdCounter = Integer.MAX_VALUE / 2;

    public static Integer setPersonalGlowingBlock(Location loc, org.bukkit.ChatColor color, Player target) {
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

        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x60); // 0x20(invisible) + 0x40(glowing)
        packet_entity_meta.getIntegers().write(0, entityId);
        packet_entity_meta.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        entityIdCounter = entityIdCounter - 1;


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
            ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet_team);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entityId;
    }

    public static void unsetPersonalGlowingBlock(Integer entityId, Player target) {
        PacketContainer packet_entity_destroy = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);

        int[] list = new int[1];
        list[0] = entityId;

        packet_entity_destroy.getIntLists().write(0, List.of(entityId));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet_entity_destroy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

