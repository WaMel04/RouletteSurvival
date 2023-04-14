package com.wamel.roulettesurvival.object;

import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.data.DataManager_Roulette;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.util.Roulette;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public class RoulettePlayer {

    public static ArrayList<RoulettePlayer> roulettePlayers = new ArrayList<>();
    private String uuid;

    public RoulettePlayer(Player player) {
        this.uuid = player.getUniqueId().toString();
    }

    @Nullable
    public Player getPlayer() {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));

        return player;
    }

    @Nullable
    public String getUuidString(Player player) {
        return uuid;
    }

    @Nullable
    public Roulette getRoulette() {
        return DataManager_Roulette.enabledRoulettes.get(this);
    }

    @Nullable
    public ArrayList<REventBase> getEnabledREvents() {
        return DataManager_REvent.enabledREvents.get(this);
    }


}
