package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.rouletteevent.list.inferior.REvent_JumpLock;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class REvent_LikeARabbit extends REventBase {

    private static HashMap<RoulettePlayer, BossBar> barMap = new HashMap<>();
    private static HashMap<RoulettePlayer, Integer> gaugeMap = new HashMap<>();
    private static ArrayList<RoulettePlayer> jumpingList = new ArrayList<>();

    private final Double JUMP_STRENGTH = 0.3;

    public REvent_LikeARabbit(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#00FF7F") + "§l깡총깡총 토끼처럼", "슈퍼 점프를 할 수 있게됩니다", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f토끼처럼 깡총깡총 뛰어보세요.");
        player.sendMessage("   §7웅크리기 상태에서 점프 게이지를 차징할 수 있고, 웅크리기를 해제시 슈퍼 점프를 할 수 있습니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onSneaking(PlayerToggleSneakEvent event) {

        Player player = event.getPlayer();

        if(getIsEnabled(player) == false)
            return;

        if(player.isSneaking() == false && player.isOnGround()) {
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

            final BossBar bar = Bukkit.createBossBar(ChatColor.of("#00FF7F") + "§l점프 게이지 차징 중...", BarColor.WHITE, BarStyle.SEGMENTED_6);
            bar.setProgress(0D);
            bar.addPlayer(player);

            barMap.put(rPlayer, bar);
            gaugeMap.put(rPlayer, 0);

            new BukkitRunnable() {
                Integer gauge = 0;
                @Override
                public void run() {
                    if(gaugeMap.get(rPlayer) == null) {
                        super.cancel();
                        return;
                    }

                    gauge = gaugeMap.get(rPlayer);

                    double progress = gauge / 6D;
                    bar.setProgress(progress);

                    if(gauge < 6) {
                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, (float) progress + 0.3f);
                    } else {
                        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                        bar.setColor(BarColor.GREEN);
                        super.cancel();
                        return;
                    }

                    if(getIsEnabled(player) == false) {
                        Player player1 = rPlayer.getPlayer();

                        if(player1 == null) {
                            barMap.remove(rPlayer);
                            gaugeMap.remove(rPlayer);

                            super.cancel();
                            return;
                        }

                            BossBar bar = barMap.get(rPlayer);
                            bar.removePlayer(player1);

                            barMap.remove(rPlayer);
                            gaugeMap.remove(rPlayer);

                            super.cancel();
                            return;
                    }

                    gaugeMap.put(rPlayer, gauge + 1);
                }
            }.runTaskTimer(plugin, 0, 10);
        }
        if(player.isSneaking() == true) {
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

            if(gaugeMap.get(rPlayer) == null)
                return;

            Integer gauge = gaugeMap.get(rPlayer);

            player.setVelocity(new Vector(0, gauge*JUMP_STRENGTH, 0));
            player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, 10, 1);

            barMap.get(rPlayer).removePlayer(player);
            barMap.remove(rPlayer);
            gaugeMap.remove(rPlayer);

            jumpingList.add(rPlayer);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(player.isOnGround()) {
                        jumpingList.remove(rPlayer);
                        super.cancel();
                    }
                }
            }.runTaskTimer(plugin, 20, 1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(getIsEnabled(player) == false)
            return;
        if(!jumpingList.contains(rPlayer))
            return;
        if(!(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)))
            return;

        event.setCancelled(true);
        event.setDamage(0);
        jumpingList.remove(rPlayer);
    }

}
