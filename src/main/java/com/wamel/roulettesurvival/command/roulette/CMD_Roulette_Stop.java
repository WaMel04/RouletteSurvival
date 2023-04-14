package com.wamel.roulettesurvival.command.roulette;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.data.DataManager_Config;
import com.wamel.roulettesurvival.data.DataManager_Game;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.data.DataManager_Roulette;
import com.wamel.roulettesurvival.rouletteevent.REventScoreboardManager;
import com.wamel.roulettesurvival.rouletteevent.REventTimerManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CMD_Roulette_Stop extends CMD_Roulette {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static void run() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("     " + ChatColor.of("#8A2BE2") + "Roulette Survival ver " + plugin.getDescription().getVersion() + " by WaMel_");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("   §e게임이 종료되었습니다.");
        Bukkit.broadcastMessage("");

        DataManager_Game.isEnabled = false;

        if(DataManager_Game.task != null) {
            DataManager_Game.task.cancel();
            DataManager_Game.task = null;
        }

        DataManager_Game.playerLifesMap.clear();

        DataManager_REvent.enabledREvents.clear();
        DataManager_Roulette.enabledRoulettes.clear();

        for(Player player : Bukkit.getOnlinePlayers()) {
            REventTimerManager.disApplyAll(player);
            REventScoreboardManager.unRegister(player);
        }
    }

}
