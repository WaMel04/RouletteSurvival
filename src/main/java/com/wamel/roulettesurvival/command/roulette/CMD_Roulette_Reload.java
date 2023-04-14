package com.wamel.roulettesurvival.command.roulette;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.config.ConfigManager;
import com.wamel.roulettesurvival.data.DataManager_Config;
import org.bukkit.command.CommandSender;

public class CMD_Roulette_Reload extends CMD_Roulette {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static void run(CommandSender sender) {
        DataManager_Config.ROULETTE_PERIOD_MIN = (Integer) ConfigManager.get("roulette_period_min");
        DataManager_Config.ROULETTE_PERIOD_MAX = (Integer) ConfigManager.get("roulette_period_max");
        DataManager_Config.PLAYER_LIFES = (Integer) ConfigManager.get("player_lifes");
        DataManager_Config.DROP_MULTIPLE = (Integer) ConfigManager.get("drop_multiple");

        sender.sendMessage(PREFIX + "config를 리로드했습니다.");
        sender.sendMessage("§e룰렛 주기: " + DataManager_Config.ROULETTE_PERIOD_MIN + " ~ " + DataManager_Config.ROULETTE_PERIOD_MAX);
        sender.sendMessage("§c플레이어 목숨: " + DataManager_Config.PLAYER_LIFES);
        sender.sendMessage("§a드랍률: " + DataManager_Config.DROP_MULTIPLE);
    }

}
