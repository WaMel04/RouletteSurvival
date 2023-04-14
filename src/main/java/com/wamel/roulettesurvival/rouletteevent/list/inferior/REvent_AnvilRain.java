package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class REvent_AnvilRain extends REventBase {

    public REvent_AnvilRain(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#2F4F4F") + "§l모루 비가 내려와", "하늘에서 모루가 떨어집니다!", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("     " + name);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("   §f위를 조심하세요! §b" + player.getName() + " §f님이 모루 비를 불러왔습니다!");
        Bukkit.broadcastMessage("   §75초 후 모든 플레이어에게 모루 비가 내려옵니다.");
        Bukkit.broadcastMessage("");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for(Player p : Bukkit.getOnlinePlayers()) {
                makeAnvilRain(p);
            }
        }, 20*5);
    }

    private void makeAnvilRain(Player player) {
        Random random = new Random();
        Location cLoc = player.getLocation();
        int radius = 10;

        for(int x=cLoc.getBlockX() - radius; x<cLoc.getBlockX() + radius; x++) {
            for(int z=cLoc.getBlockZ() - radius; z<cLoc.getBlockZ() + radius; z++) {
                Location loc = new Location(player.getWorld(), x, cLoc.toHighestLocation().clone().getBlockY() + 30, z);

                if(random.nextInt(100) < 50)
                    loc.getBlock().setType(Material.ANVIL);
            }
        }
    }
}
