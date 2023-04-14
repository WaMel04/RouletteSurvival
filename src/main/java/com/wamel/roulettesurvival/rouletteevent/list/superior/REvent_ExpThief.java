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

public class REvent_ExpThief extends REventBase {

    private static final Integer LEVEL = 5;

    public REvent_ExpThief(String name, String description, Integer duration, REventRank rank) {
        super( ChatColor.of("#ADFF2F") + "§l경험치 도둑", "랜덤한 플레이어의 레벨을 5 만큼 뺏어옵니다.", -1, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f도둑이야! 레벨이 궁한 나머지 당신은 경험치 도둑으로 전락해버렸습니다...");
        player.sendMessage("   §75초 후 랜덤한 플레이어의 레벨을 5 만큼 뺏어옵니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size() < 2) {
                    player.sendMessage(PREFIX + "경험치를 뺏어올 대상이 없어 실패하였습니다.");
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

                Integer result = 0;

                if(target.getLevel() >= LEVEL) {
                    result = LEVEL;
                    target.setLevel(target.getLevel() - LEVEL);
                } else if(target.getLevel() < LEVEL) {
                    Bukkit.broadcastMessage("! " + LEVEL + "// " + target.getLevel());
                    result = target.getLevel();
                    target.setLevel(0);
                }

                player.setLevel(player.getLevel() + result);

                if(result > 0) {
                    Util.sendTitleWithSound(player, ChatColor.of("#ADFF2F") + "§l레벨업!", "§c" + target.getName()+ "§f님의 레벨을 " + result + " 만큼 뺏었습니다.",
                            0, 60, 20, Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                    Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + target.getName()+ "§f님의 레벨을 " + result + " 만큼 뺏었습니다.");

                    Util.sendTitleWithSound(target,  ChatColor.of("#ADFF2F") + "§l도둑이야!", "§c" + player.getName()+ "§f님에 의해 레벨이 " + result + " 만큼 뺏겼습니다...",
                            0, 60, 20, Sound.ENTITY_AXOLOTL_DEATH, 100, 1);
                } else {
                    Util.sendTitleWithSound(player, "§c§l실패!", "§c" + target.getName() + "§f님에게서 뺏어올 레벨이 없습니다...",
                            0, 60, 20, Sound.ENTITY_ITEM_BREAK, 100, 1);
                    Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + target.getName()+ "§f님에게서 레벨을 뺏으려 했지만 뺏어올 레벨이 없어 실패했습니다.");

                    Util.sendTitleWithSound(target,  ChatColor.of("#ADFF2F") + "§l도둑이야!", "§c" + player.getName()+ "§f님에게 레벨을 도둑맞을 뻔했지만... 다행히 가진게 없었습니다",
                            0, 60, 20, Sound.ENTITY_VILLAGER_YES, 100, 1);
                }
            }
       }.runTaskLater(plugin, 100);
    }
}

