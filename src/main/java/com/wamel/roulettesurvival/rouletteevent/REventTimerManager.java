package com.wamel.roulettesurvival.rouletteevent;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class REventTimerManager {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();
    public static HashMap<RoulettePlayer, ArrayList<BossBar>> barMap = new HashMap<>();

    public static void start(Player player, REventBase base) {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        final int MAX_SECOND = base.getDuration();
        
        if(MAX_SECOND <= 0)
            return;

        final BossBar bar = Bukkit.createBossBar(base.getName(), BarColor.GREEN, BarStyle.SOLID);
        bar.setProgress(1D);
        bar.addPlayer(player);
        ArrayList<BossBar> barList;

        if(barMap.get(rPlayer) == null)
            barList = new ArrayList<>();
        else
            barList = barMap.get(rPlayer);

        barList.add(bar);
        barMap.put(rPlayer, barList);

        new BukkitRunnable() {
            int second = MAX_SECOND;
            @Override
            public void run() {
                if(base.getIsEnabled(player) == false) {
                    bar.removeAll();
                    barMap.get(rPlayer).remove(bar);

                    super.cancel();
                    return;
                }
                if(second <= 0) {
                    bar.removeAll();

                    DataManager_REvent.enabledREvents.get(rPlayer).remove(base);
                    barMap.get(rPlayer).remove(bar);

                    player.sendMessage(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §f"
                            + ChatColor.of("#7B68EE") + base.getName() + "§f의 지속시간이 종료되었습니다.");

                    super.cancel();
                    return;
                }

                double progress = second*1D / MAX_SECOND*1D;
                bar.setProgress(progress);

                if(second <= 15) {
                    bar.setTitle(base.getName() + " §f| " + ChatColor.of("#DC143C") + "남은 시간: " + second + "초");
                    bar.setColor(BarColor.RED);
                } else if(second <= 60) {
                    bar.setTitle(base.getName() + " §f| " + ChatColor.of("#FFFF00") + "남은 시간: " + second + "초");
                    bar.setColor(BarColor.YELLOW);
                } else {
                    bar.setTitle(base.getName() + " §f| " + ChatColor.of("#00FA9A") + "남은 시간: " + second + "초");
                    bar.setColor(BarColor.GREEN);
                }

                second = second - 1;
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public static void applyAll(Player player) {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(barMap.get(rPlayer) == null)
            return;
        for(BossBar bar : barMap.get(rPlayer)) {
            bar.addPlayer(player);
        }
    }

    public static void disApplyAll(Player player) {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(barMap.get(rPlayer) == null)
            return;
        for(BossBar bar : barMap.get(rPlayer)) {
            bar.removePlayer(player);
        }
    }
}
