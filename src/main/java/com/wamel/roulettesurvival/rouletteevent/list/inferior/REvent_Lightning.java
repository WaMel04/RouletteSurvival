package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class REvent_Lightning extends REventBase {

    public REvent_Lightning(String name, String description, Integer duration, REventRank rank) {
        super("§e§l낙뢰", "벼락을 맞습니다.", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f마른 하늘에 날벼락이라니...");
        player.sendMessage("   §73초 후 벼락을 맞습니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 벼락을 맞았습니다!");
            }
        }.runTaskLater(plugin, 60);
    }

}
