package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class REvent_DarkTempler extends REventBase {

    public REvent_DarkTempler(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#4682B4") + "§l다크 템플러", "밤일 때 이동 속도가 증가, 낮일 때 감소합니다.", 600, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(600초 지속)");
        player.sendMessage("");
        player.sendMessage("   §7낮에는 이동 속도가 감소, 밤에는 증가합니다.");
        player.sendMessage("   §b지옥, 엔더는 항상 밤으로 취급합니다.");
        player.sendMessage("");

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Player oPlayer = rPlayer.getPlayer();

                if(getIsEnabled(oPlayer) == false) {
                    oPlayer.setWalkSpeed(0.2f);
                    super.cancel();
                    return;
                }

                enum SunTime {
                    DAY, NIGHT
                }

                SunTime time;

                switch (oPlayer.getWorld().getName()) {
                    case "world_nether":
                    case "world_the_end":
                        time = SunTime.NIGHT;
                        break;
                    default:
                        if(oPlayer.getWorld().getTime() >= 13000 && oPlayer.getWorld().getTime() <= 24000)
                            time = SunTime.NIGHT;
                        else
                            time = SunTime.DAY;
                        break;
                }

                if(time.equals(SunTime.DAY))
                    oPlayer.setWalkSpeed(0.1f);
                else
                    oPlayer.setWalkSpeed(0.45f);
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
