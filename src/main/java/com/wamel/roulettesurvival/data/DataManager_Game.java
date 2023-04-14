package com.wamel.roulettesurvival.data;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class DataManager_Game {

    public static Boolean isEnabled = false;
    public static BukkitTask task;
    public static HashMap<RoulettePlayer, Integer> playerLifesMap = new HashMap<>();

}
