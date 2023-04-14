package com.wamel.roulettesurvival.data;

import com.wamel.roulettesurvival.config.ConfigManager;

public class DataManager_Config {

    public static Integer ROULETTE_PERIOD_MIN = (Integer) ConfigManager.get("roulette_period_min");
    public static Integer ROULETTE_PERIOD_MAX = (Integer) ConfigManager.get("roulette_period_max");
    public static Integer PLAYER_LIFES = (Integer) ConfigManager.get("player_lifes");
    public static Integer DROP_MULTIPLE = (Integer) ConfigManager.get("drop_multiple");
}
