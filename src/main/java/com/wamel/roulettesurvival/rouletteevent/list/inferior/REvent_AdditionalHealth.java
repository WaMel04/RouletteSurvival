package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class REvent_AdditionalHealth extends REventBase {

    private final double hearts = 6;

    public REvent_AdditionalHealth(String name, String description, Integer duration, REventRank rank) {
        super("§d§l추가 체력", "추가적인 체력을 획득합니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f강한 체력을 얻게됩니다.");
        player.sendMessage("   §7체력이 " + ChatColor.of("#DC143C") + "♥ x " + hearts + " §7만큼 증가합니다.");
        player.sendMessage("");

        player.setMaxHealth(20 + hearts*2);

        //duration초 후 체력 초기화
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.setMaxHealth(20);
        }, 20L*duration);
    }

}
