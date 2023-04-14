package com.wamel.roulettesurvival.rouletteevent;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class REventBase implements Listener {

    public REventBase(String name, String description, Integer duration, REventRank rank) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.rank = rank;
    }

    protected RouletteSurvival plugin = RouletteSurvival.getInstance();

    // 룰렛 이벤트의 이름입니다.
    protected String name;
    // 룰렛 이벤트의 설명입니다.
    protected String description;
    // 룰렛 이벤트의 지속시간입니다. | 단위: 초
    // -1 : 지속시간 없음
    // -2 : 영구 지속
    protected Integer duration;
    // 룰렛 이벤트의 등급입니다. SUPERIOR / INFERIOR
    protected REventRank rank;

    protected static final String PREFIX = ChatColor.of("#8A2BE2") + "[RouletteSurvival] §f";

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDuration() {
        return duration;
    }

    public Boolean getIsEnabled(Player player) {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(DataManager_REvent.enabledREvents.get(rPlayer) == null)
            return false;
        if(DataManager_REvent.enabledREvents.get(rPlayer).contains(this))
            return true;
        else
            return false;
    }

    public REventRank getRank() {
        return rank;
    }

    // 룰렛 이벤트가 플레이어에 의해 선택되었을 때
    // 반드시 REventManager.register(player, this); 구문을 구현해야 함.
    public abstract void onChosenByPlayer(Player player);

}
