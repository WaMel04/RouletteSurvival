package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class REvent_ReUnion extends REventBase {

    public REvent_ReUnion(String name, String description, Integer duration, REventRank rank) {
        super( ChatColor.of("#FF8C00") + "§l지금 만나러 갑니다", "랜덤한 플레이어에게 이동합니다.", -1, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f극적인 상봉이 이루어졌습니다.");
        player.sendMessage("   §75초 후 랜덤한 플레이어에게 이동합니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size() < 2) {
                    player.sendMessage(PREFIX + "이동할 대상이 없어 이동에 실패하였습니다.");
                    super.cancel();
                    return;
                }

                Random random = new Random();

                Player target = null;

                do {
                    int select = random.nextInt(Bukkit.getOnlinePlayers().size());

                    int i = 0;
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(i == select)
                            target = p;
                        i++;
                    }
                } while(target.getName().equals(player.getName()));



                player.teleport(target.getLocation());
                Util.sendTitleWithSound(player, "§b§l이동!", "§c" + target.getName()+ "§f님에게 이동했습니다",
                        0, 60, 20, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 1);
                Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + target.getName()+ "§f님에게 이동했습니다");

                //1초 후 소리 재생
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 100, 1);
                }, 20L);
            }
       }.runTaskLater(plugin, 100);
    }
}

