package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.BarTimer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class REvent_LikeAFrog extends REventBase {

    private static ArrayList<RoulettePlayer> cooldownList = new ArrayList<>();

    private static final Integer COOLDOWN = 3; // 단위: 초
    private static final Integer STOP_TIME = 10; // 단위: 초

    private static final Integer RADIUS = 30;

    public REvent_LikeAFrog(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#66CDAA") + "§l낼름낼름 개구리처럼", "혀를 내밀어 상대를 끌 수 있게됩니다.", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f개구리처럼 낼름낼름 혀를 내밀어보세요.");
        player.sendMessage("   §7웅크리기 상태에서 상대를 클릭시 혀를 내밀어 자신에게 끌고옵니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(getIsEnabled(player) == false)
            return;
        if(player.isSneaking() == false)
            return;
        if(event.getAction().isRightClick())
            return;
        if(cooldownList.contains(rPlayer) == true)
            return;

        Entity target = player.getTargetEntity(RADIUS);
        if(target == null)
            return;
        if(!(target instanceof Entity))
            return;

        Location cLoc = player.getLocation();
        Location tLoc = target.getLocation();

        if(Math.abs(cLoc.getY() - tLoc.getY()) >= 15) // y 좌표 15 이상 차이날 시 불가능
            return;

        final double delta = 0.035;
        LinkedList<double[]> array = new LinkedList<>();

        final double[] pointA = {cLoc.getX(), cLoc.getY(), cLoc.getZ()}, pointB = {tLoc.getX(), tLoc.getY(), tLoc.getZ()};
        final double[] pointC = {(pointB[0] - pointA[0]), (pointB[1] - pointA[1]), (pointB[2] - pointA[2])};
        int i = 0;
        double k = 0;
        while (k < 1.0) {
            array.add(new double[]{pointA[0] +( pointC[0] * k), pointA[1] + (pointC[2] * k), pointA[2] + (pointC[2] * k)});
            i++;
            k += delta;
        }
        for(double[] points : array) {
            Location loc = new Location(player.getWorld(), points[0], points[1], points[2]).toCenterLocation();
            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0, 1, 0, 40, Material.PINK_WOOL.createBlockData());
        }

        cLoc.getWorld().playSound(cLoc, Sound.ENTITY_SLIME_JUMP, 100, 1);

        BarTimer.start(player, "§a§l쿨타임: 낼름낼름", BarColor.GREEN, BarStyle.SOLID, COOLDOWN);
        cooldownList.add(rPlayer);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            cooldownList.remove(rPlayer);
        }, COOLDOWN * 20);

        new BukkitRunnable() {
            int tick;
            @Override
            public void run() {
                if(target.getLocation().distance(rPlayer.getPlayer().getLocation()) <= 3) {
                    target.setGravity(true);
                    super.cancel();
                    return;
                }
                if(tick >= STOP_TIME*20) {
                    target.setGravity(true);
                    super.cancel();
                    return;
                }

                double xDif = rPlayer.getPlayer().getLocation().getX() - target.getLocation().getX();
                double yDif = rPlayer.getPlayer().getLocation().getY() - target.getLocation().getY();
                double zDif = rPlayer.getPlayer().getLocation().getZ() - target.getLocation().getZ();

                target.setVelocity((new Vector(xDif, yDif, zDif)).normalize().multiply(0.7));
                target.setGravity(false);

                tick = tick + 2;

            }
        }.runTaskTimer(plugin, 0, 2);
    }

}
