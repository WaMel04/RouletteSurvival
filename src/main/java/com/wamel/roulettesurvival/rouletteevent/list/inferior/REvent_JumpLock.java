package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class REvent_JumpLock extends REventBase {

    public REvent_JumpLock(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#4B0082") + "§l점프 봉인", "점프를 할 수 없게 됩니다.", 120, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f몸이 너무 무거워졌습니다...");
        player.sendMessage("   §7점프를 할 수 없게 됩니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        if(getIsEnabled(event.getPlayer()) == false)
            return;

        event.setCancelled(true);
    }
}
