package com.wamel.roulettesurvival.command.roulette;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.data.DataManager_Config;
import com.wamel.roulettesurvival.data.DataManager_Game;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.util.Roulette;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CMD_Roulette_Start extends CMD_Roulette {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static void run() {
        if(DataManager_Game.isEnabled == true)
            return;

        DataManager_Game.isEnabled = true;

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("     " + ChatColor.of("#8A2BE2") + "Roulette Survival ver " + plugin.getDescription().getVersion() + " by WaMel_");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("   §b게임 방법: §f룰렛을 돌려 다양한 이벤트를 경험하면서 목표를 달성하세요!");
        Bukkit.broadcastMessage("   §c목표: §f엔더 드래곤 가장 먼저 처치하기");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("   §7인벤 세이브가 활성화됩니다. 플레이어의 목숨은 총 " + DataManager_Config.PLAYER_LIFES + "개 이며, 모두 소진시 관전 모드로 전환됩니다.");
        Bukkit.broadcastMessage("   §7블럭과 몬스터의 드랍률이 " + DataManager_Config.DROP_MULTIPLE + "배가 되며, Shift + F(손 바꾸기)키로 상점을 열 수 있습니다.");
        Bukkit.broadcastMessage("");
        for(Player player : Bukkit.getOnlinePlayers()) {
            Util.sendTitleWithSound(player, ChatColor.of("#8A2BE2") + "Roulette Survival", "10초 후 게임이 시작됩니다", 0, 60, 0, Sound.BLOCK_NOTE_BLOCK_CHIME, 100, 1);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(DataManager_Game.isEnabled == false)
                return;

            for(Player player : Bukkit.getOnlinePlayers()) {
                Util.sendTitleWithSound(player, ChatColor.of("#8A2BE2") + "Roulette Survival", "게임이 시작되었습니다!", 0, 60, 0, Sound.ITEM_TOTEM_USE, 100, 1);
            }

            Random random = new Random();

            final Integer FIRST_PERIOD = DataManager_Config.ROULETTE_PERIOD_MIN + random.nextInt(DataManager_Config.ROULETTE_PERIOD_MAX - DataManager_Config.ROULETTE_PERIOD_MIN) + 1;

            DataManager_Game.task = new BukkitRunnable() {
                Boolean first = true;
                int period;
                int tick;
                @Override
                public void run() {
                    if(tick >= period || first == true) {
                        first = false;
                        period = DataManager_Config.ROULETTE_PERIOD_MIN + random.nextInt(DataManager_Config.ROULETTE_PERIOD_MAX - DataManager_Config.ROULETTE_PERIOD_MIN) + 1;
                        tick = 0;

                        for(Player player : Bukkit.getOnlinePlayers()) {
                            RoulettePlayer rPlayer = new RoulettePlayer(player);

                            if(DataManager_Game.playerLifesMap.get(rPlayer) != null)
                                if(DataManager_Game.playerLifesMap.get(rPlayer) <= 0)
                                    continue;

                            Roulette roulette = new Roulette(player);
                            roulette.start();
                        }
                    }

                    tick++;
                }
            }.runTaskTimerAsynchronously(plugin, FIRST_PERIOD*20, 20);
        }, 200L);
    }

}
