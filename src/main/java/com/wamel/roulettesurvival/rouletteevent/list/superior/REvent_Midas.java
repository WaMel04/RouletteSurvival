package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class REvent_Midas extends REventBase {

    private static final Integer RADIUS = 5;

    public static HashMap<RoulettePlayer, HashMap<Location, Integer>> xrayMap = new HashMap<>();

    public REvent_Midas(String name, String description, Integer duration, REventRank rank) {
        super("§e§l미다스의 손", "황금의 저주에 걸리게 됩니다...", -1, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f만지는 것마다 모두 황금이 되어버리는 축복아닌 저주를 받았습니다.");
        player.sendMessage("   §7인벤토리에 있는 모든 도구가 금 도구가 되어버립니다.");
        player.sendMessage("   §7반경 " + RADIUS + "m 내의 모든 블럭이 금 블럭으로 변합니다.");
        player.sendMessage("");

        for(ItemStack item : player.getInventory()) {
            if(item == null)
                continue;
            switch (item.getType()) {
                case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD, NETHERITE_SWORD -> {
                    item.setType(Material.GOLDEN_SWORD);
                }
                case WOODEN_AXE, STONE_AXE, IRON_AXE, DIAMOND_AXE, NETHERITE_AXE -> {
                    item.setType(Material.GOLDEN_AXE);
                }
                case WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE -> {
                    item.setType(Material.GOLDEN_PICKAXE);
                }
                case WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL -> {
                    item.setType(Material.GOLDEN_SHOVEL);
                }
                case WOODEN_HOE, STONE_HOE, IRON_HOE, DIAMOND_HOE, NETHERITE_HOE -> {
                    item.setType(Material.GOLDEN_HOE);
                }
            }
        }


        Location cLoc = player.getLocation();

        for(double x=cLoc.getX() - RADIUS; x<cLoc.getX() + RADIUS; x++) {
            for(double y=cLoc.getY() - RADIUS; y<cLoc.getY() + RADIUS; y++) {
                for(double z=cLoc.getZ() - RADIUS; z<cLoc.getZ() + RADIUS; z++) {
                    Location loc = new Location(cLoc.getWorld(), x, y, z);
                    if(!(loc.getBlock().getType().equals(Material.AIR)))
                        loc.getBlock().setType(Material.GOLD_BLOCK);
                }
            }
        }
    }

}

