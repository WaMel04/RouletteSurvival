package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.BarTimer;
import com.wamel.roulettesurvival.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class REvent_GuidedArrow extends REventBase {

    private static ArrayList<RoulettePlayer> cooldownList = new ArrayList<>();

    private static final Integer COOLDOWN = 3; // 단위: 초
    private static final Integer STOP_TIME = 10; // 단위: 초

    public REvent_GuidedArrow(String name, String description, Integer duration, REventRank rank) {
        super("§b§l유도 화살", "자신이 쏘는 화살이 유도 화살로 바뀝니다.", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f피할 수 없는 화살을 쏠 수 있게 됩니다.");
        player.sendMessage("   §7자신이 쏘는 화살이 적을 추적하게 됩니다. (플레이어 우선)");
        player.sendMessage("");
    }

    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getType().equals(EntityType.ARROW) || event.getEntity().getType().equals(EntityType.SPECTRAL_ARROW)))
            return;
        if(!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(getIsEnabled(player) == false)
            return;
        if(cooldownList.contains(rPlayer) == true)
            return;

        Util.setGlowing(event.getEntity(), ChatColor.BLUE);
        BarTimer.start(player, "§b§l쿨타임: 유도 화살", BarColor.BLUE, BarStyle.SOLID, COOLDOWN);
        cooldownList.add(rPlayer);

        Entity arrow = event.getEntity();

        new BukkitRunnable() {
            int tick;
            @Override
            public void run() {
                if(arrow.isDead()) {
                    super.cancel();
                    return;
                }
                if(tick >= STOP_TIME*20) {
                    arrow.remove();

                    super.cancel();
                    return;
                }

                Location location = arrow.getLocation();
                ArrayList<Entity> playerList = new ArrayList<>();
                ArrayList<Entity> otherList = new ArrayList<>();
                ArrayList<Entity> combinedList= new ArrayList<>();

                for(Entity entity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
                    if(entity instanceof LivingEntity) {
                        if(entity instanceof Player) {
                            if(((Player) entity).equals(player))
                                continue;
                            playerList.add(entity);
                            continue;
                        } else {
                            otherList.add(entity);
                            continue;
                        }
                    }
                }

                for(Entity entity1 : playerList) {
                    combinedList.add(entity1);
                }
                for(Entity entity2 : otherList) {
                    combinedList.add(entity2);
                }

                if(combinedList.size() != 0) {
                    Location entityLoc = combinedList.get(0).getLocation();
                    double xDif = entityLoc.getX() - location.getX();
                    double yDif = entityLoc.getY() - location.getY();
                    double zDif = entityLoc.getZ() - location.getZ();

                    arrow.setGravity(false);
                    arrow.setVelocity((new Vector(xDif, yDif, zDif)).normalize().multiply(0.7));
                }

                tick = tick + 2;

            }
        }.runTaskTimer(plugin, 0, 2);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            cooldownList.remove(rPlayer);
        }, COOLDOWN * 20);
    }
}
