package com.wamel.roulettesurvival.object;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class RoulettePlayerFactory {

    public static HashMap<String, RoulettePlayer> roulettePlayerMap = new HashMap<>();

    public static RoulettePlayer create(Player player) {
        if(roulettePlayerMap.containsKey(player.getUniqueId().toString())) {
            return roulettePlayerMap.get(player.getUniqueId().toString());
        } else {
            RoulettePlayer rPlayer = new RoulettePlayer(player);
            roulettePlayerMap.put(player.getUniqueId().toString(), rPlayer);

            return rPlayer;
        }
    }

}
