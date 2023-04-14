package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class REvent_Exile extends REventBase {

    public REvent_Exile(String name, String description, Integer duration, REventRank rank) {
        super( ChatColor.of("#2F4F4F") + "§l유배", "랜덤한 플레이어를 네더로 보내버립니다.", -1, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f네더로 유배당하는 것은 좋은 걸까요? 아님 나쁜 걸까요?");
        player.sendMessage("   §75초 후 랜덤한 플레이어가 네더로 이동됩니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size() < 2) {
                    player.sendMessage(PREFIX + "유배보낼 대상이 없어 이동에 실패하였습니다.");
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

                if(target.getWorld().getName().equalsIgnoreCase("world_nether")) {
                    Util.sendTitleWithSound(player, "§c§l유배 실패", "§c" + target.getName()+ "§f님은 이미 네더에 있습니다!",
                            0, 60, 20, Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
                    Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + target.getName()+ "§f님을 네더로 유배보내려 했지만, 이미 네더에 있어 실패했습니다!");

                    Util.sendTitleWithSound(target,  ChatColor.of("#2F4F4F") + "§l유배", "§c" + player.getName()+ "§f님에 의해 네더로 유배당할 뻔했지만, 이미 네더에 있어 무산되었습니다",
                            0, 60, 20, Sound.ENTITY_VILLAGER_YES, 100, 1);
                } else {
                    Util.sendTitleWithSound(player, "§a§l유배 완료", "§c" + target.getName()+ "§f님을 네더로 유배보냈습니다!",
                            0, 60, 20, Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                    Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + target.getName()+ "§f님을 네더로 유배보냈습니다!");

                    World nether = Bukkit.getWorld("world_nether");
                    target.teleport(nether.getSpawnLocation());
                    Util.sendTitleWithSound(target,  ChatColor.of("#2F4F4F") + "§l유배", "§c" + player.getName()+ "§f님에 의해 네더로 유배당했습니다.",
                            0, 60, 20, Sound.ENTITY_IRON_GOLEM_DEATH, 100, 0.5f);
                }

            }
       }.runTaskLater(plugin, 100);
    }
}

