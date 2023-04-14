package com.wamel.roulettesurvival.util;

import com.wamel.roulettesurvival.RouletteSurvival;
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

public class BarTimer {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static HashMap<RoulettePlayer, ArrayList<BossBar>> barMap = new HashMap<>();

    public static void start(Player player, String title, BarColor color, BarStyle style, Integer duration) {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        final BossBar bar = Bukkit.createBossBar(title, color, style);
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
            int second = duration;
            @Override
            public void run() {
                if(second <= 0) {
                    bar.removeAll();

                    barMap.get(rPlayer).remove(bar);

                    super.cancel();
                    return;
                }

                double progress = second*1D / duration*1D;
                bar.setProgress(progress);
                bar.setTitle(title + " §f| " + ChatColor.of("#1E90FF") + "남은 시간: " + second + "초");

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
