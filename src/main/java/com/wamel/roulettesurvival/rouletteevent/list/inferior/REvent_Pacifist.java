package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class REvent_Pacifist extends REventBase {

    private final double hearts = 6;

    public REvent_Pacifist(String name, String description, Integer duration, REventRank rank) {
        super("§b§l이기적 평화 주의자", "몬스터의 공격을 받지 않게됩니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f당신은 몬스터를 때릴 수 있지만, 몬스터는 당신을 때릴 수 없습니다.");
        player.sendMessage("   §7몬스터의 타겟팅을 당하지 않게 됩니다.");
        player.sendMessage("");

        for(LivingEntity entity : player.getLocation().getNearbyLivingEntities(30)) {
            if(entity instanceof Monster) {
                Monster monster = (Monster) entity;
                if(monster.getTarget() instanceof Player) {
                    Player p = (Player) monster.getTarget();
                    if(p.getName().equalsIgnoreCase(player.getName())) {
                        monster.setTarget(null);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if(!(event.getTarget() instanceof Player))
            return;
        if(getIsEnabled((Player) event.getTarget()) == false)
            return;

        event.setCancelled(true);
    }

}
