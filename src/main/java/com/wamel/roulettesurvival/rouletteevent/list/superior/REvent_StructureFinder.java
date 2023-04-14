package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class REvent_StructureFinder extends REventBase {

    public REvent_StructureFinder(String name, String description, Integer duration, REventRank rank) {
        super( ChatColor.of("#660029") + "§l네더 탐색대", "랜덤한 네더 구조물을 가리키는 나침반을 획득합니다.", -1, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f네더 구조물을 찾는 것이야 말로 빠른 클리어의 지름길이죠.");
        player.sendMessage("   §75초 후 랜덤한 네더 구조물을 가리키는 나침반을 획득합니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack compass = new ItemStack(Material.COMPASS, 1);

                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
                compassMeta.setLodestoneTracked(false);

                Random random = new Random();
                Integer chance = random.nextInt(100);
                World nether = Bukkit.getWorld("world_nether");
                Location loc;

                if(chance < 70) {
                    loc = nether.locateNearestStructure(nether.getSpawnLocation(), StructureType.BASTION_REMNANT, 1000, true);
                    compassMeta.setDisplayName("§8바스티온");
                } else {
                    loc = nether.locateNearestStructure(nether.getSpawnLocation(), StructureType.NETHER_FORTRESS, 1000, true);
                    compassMeta.setDisplayName(ChatColor.of("#660029") + "네더 유적");
                }
                if(loc == null) {
                    player.sendMessage(PREFIX + "네더 구조물을 찾는 데 실패했습니다!");
                    return;
                }

                compassMeta.setLodestone(loc);
                compass.setItemMeta(compassMeta);

                player.getInventory().addItem(compass);
            }
        }.runTaskLater(plugin, 100);
    }
}

