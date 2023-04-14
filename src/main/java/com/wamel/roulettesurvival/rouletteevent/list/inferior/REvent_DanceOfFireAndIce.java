package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class REvent_DanceOfFireAndIce extends REventBase {

    private final double WATER_DAMAGE = 1;
    private final double FIRE_HEAL = 0.5;

    public REvent_DanceOfFireAndIce(String name, String description, Integer duration, REventRank rank) {
        super("§c§l불과 §b§l얼음의 춤", "불에 타들어가면 치유를, 물에 젖으면 피해를 받습니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f불과 얼음의 능력이 뒤바꼈습니다!");
        player.sendMessage("   §b물에 들어가거나 비에 젖을시 " + ChatColor.of("#DC143C") + "♥ x " + WATER_DAMAGE + " §b만큼의 피해를 입습니다.");
        player.sendMessage("   §c불에 타거나 용암에 들어갈시 " + ChatColor.of("#DC143C") + "♥ x " + FIRE_HEAL + " §c만큼의 체력을 회복합니다.");
        player.sendMessage("");

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Player oPlayer = rPlayer.getPlayer();

                if(oPlayer != null) {
                    if(getIsEnabled(oPlayer)== false) {
                        super.cancel();
                        return;
                    }
                    if(oPlayer.isInWaterOrRain()) {
                        oPlayer.damage(WATER_DAMAGE*2);
                    } else if(oPlayer.isInLava()) {
                        double maxHealth = oPlayer.getMaxHealth();
                        double health = oPlayer.getHealth();

                        if(health + FIRE_HEAL*2 > maxHealth) {
                            oPlayer.setHealth(maxHealth);
                        } else {
                            oPlayer.setHealth(health + FIRE_HEAL*2);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(getIsEnabled(player) == false)
            return;
        switch (event.getCause()) {
            case FIRE_TICK:
            case LAVA:
                break;
            case FIRE:
                event.setCancelled(true);
                return;
            default:
                return;
        }

        event.setCancelled(true);

        if(player.isInLava())
            return;

        double maxHealth = player.getMaxHealth();
        double health = player.getHealth();

        if(health + FIRE_HEAL*2 > maxHealth) {
            player.setHealth(maxHealth);
        } else {
            player.setHealth(health + FIRE_HEAL*2);
        }
    }
}
