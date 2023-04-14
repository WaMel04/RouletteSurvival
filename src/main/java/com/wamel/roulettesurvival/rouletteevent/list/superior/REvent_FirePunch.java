package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.BarTimer;
import com.wamel.roulettesurvival.util.Util;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class REvent_FirePunch extends REventBase {

    private static ArrayList<RoulettePlayer> cooldownList = new ArrayList<>();

    private static final Integer DURATION = 5; // 단위: 초

    public REvent_FirePunch(String name, String description, Integer duration, REventRank rank) {
        super("§c§l파이어 펀치", "자신의 물리 공격에 화염 효과가 추가됩니다.", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f파이어 펀치를 날릴 수 있게 됩니다!");
        player.sendMessage("   §7자신이 입히는 물리 공격에 강력한 화염 효과가 추가됩니다.");
        player.sendMessage("   §7공격을 받은 상대는 " + DURATION + "초 동안 §4점화 §7효과에 걸리며, 화염에 의한 피해량이 증가합니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;
        if(!(event.getEntity() instanceof LivingEntity))
            return;
        if(event.getDamage() == 0 || event.isCancelled())
            return;

        Player player = (Player) event.getDamager();
        Entity victim = event.getEntity();

        if(getIsEnabled(player) == false)
            return;
        if(victim.hasMetadata("roul_isIgnition"))
            return;
        switch (event.getCause()) {
            case ENTITY_ATTACK:
                break;
            default:
                return;
        }

        victim.getWorld().spawnParticle(Particle.BLOCK_CRACK, victim.getLocation(), 50, 0, 1, 0, 40, Material.SOUL_FIRE.createBlockData());
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 5, 1);
        victim.setFireTicks(DURATION*20);

        victim.setMetadata("roul_isIgnition", new FixedMetadataValue(plugin, true));

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                count++;
                if(count>=4) {
                    victim.removeMetadata("roul_isIgnition", plugin);
                    super.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, DURATION*20);
    }

    @EventHandler
    public void onBurn(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity))
            return;
        if(!(event.getEntity().hasMetadata("roul_isIgnition")))
            return;
        if(event.getDamage() == 0 || event.isCancelled())
            return;
        if(!(event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)))
            return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        if(entity.getHealth() > 1)
            entity.setHealth(entity.getHealth()-1);
        else
            entity.damage(999999);

        entity.getWorld().spawnParticle(Particle.BLOCK_CRACK, entity.getLocation(), 200, 0, 1, 0, 0.5, Material.SOUL_FIRE.createBlockData());
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_HURT, 100, 1);
    }
}
