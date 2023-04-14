package com.wamel.roulettesurvival.rouletteevent;

import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class REventManager {

    public static void register(Player player, REventBase base) {
        if(base.getIsEnabled(player) == true)
            return;

        Integer duration = base.getDuration();

        if(duration == -1) // 지속시간 없음
            return;

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(DataManager_REvent.enabledREvents.get(rPlayer) == null) {
            ArrayList<REventBase> list = new ArrayList<>();
            list.add(base);
            DataManager_REvent.enabledREvents.put(rPlayer, list);
        } else {
            DataManager_REvent.enabledREvents.get(rPlayer).add(base);
        }
        if(duration == -2) // 영구 지속
            REventScoreboardManager.register(player, base);
        else
            REventTimerManager.start(player, base);
    }

    public static void unRegister(Player player, REventBase base) {
        if(base.getIsEnabled(player) == false)
            return;

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        DataManager_REvent.enabledREvents.get(rPlayer).remove(base);
    }
}
