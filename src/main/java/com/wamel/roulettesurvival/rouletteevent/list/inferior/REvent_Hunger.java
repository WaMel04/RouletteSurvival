package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class REvent_Hunger extends REventBase {

    public REvent_Hunger(String name, String description, Integer duration, REventRank rank) {
        super("§6§l굶주림", "배고픔 수치가 0이 되며, 음식으로 회복되는 배고픔이 줄어듭니다.", 45, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f당신은 지금 배가 너무나도 고픕니다.");
        player.sendMessage("   §7배고픔 수치가 0이 되며, 일정 시간동안 음식으로 회복되는 배고픔 수치가 줄어듭니다.");
        player.sendMessage("");

        player.setFoodLevel(0);
    }

    @EventHandler
    public void onEat(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        if(getIsEnabled((Player) event.getEntity()) == false)
            return;

        Player player = (Player) event.getEntity();
        Util.sendTitleWithSound(player, "§6§l꼬르륵...", "음식으로 회복되는 배고픔 수치가 줄어들었습니다", 0, 20, 0, Sound.ENTITY_ZOMBIE_AMBIENT, 100, 1);

        Integer level = event.getFoodLevel() - player.getFoodLevel();

        event.setCancelled(true);
        player.setFoodLevel(player.getFoodLevel() + Math.floorDiv(level, 2));
    }

}
