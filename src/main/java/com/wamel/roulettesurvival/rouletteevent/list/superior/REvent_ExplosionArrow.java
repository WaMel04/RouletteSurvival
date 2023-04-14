package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.BarTimer;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class REvent_ExplosionArrow extends REventBase {

    private static ArrayList<RoulettePlayer> cooldownList = new ArrayList<>();

    private static final Integer COOLDOWN = 7; // 단위: 초

    public REvent_ExplosionArrow(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#FF6347") + "§l폭발 화살", "자신이 쏘는 화살이 폭발 화살로 바뀝니다.", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f폭발하는 화살은 얼마나 강력할까요?");
        player.sendMessage("   §7자신이 쏘는 화살이 §6블럭§7에 명중시 " + ChatColor.of("#FF6347") + "폭발§7을 일으킵니다.");
        player.sendMessage("   §7자신이 쏘는 화살이 §b플레이어§7, §c몬스터§7에 명중시 " + ChatColor.of("#7B68EE") + "추가 폭발§7을 일으킵니다.");
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

        event.getEntity().setMetadata("Roulette_ExplosionArrow", new FixedMetadataValue(plugin, true));

        Util.setGlowing(event.getEntity(), org.bukkit.ChatColor.RED);
        BarTimer.start(player, ChatColor.of("#FF6347") + "§l쿨타임: 폭발 화살", BarColor.BLUE, BarStyle.SOLID, COOLDOWN);
        cooldownList.add(rPlayer);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            cooldownList.remove(rPlayer);
        }, COOLDOWN * 20);
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if(!(event.getEntity().hasMetadata("Roulette_ExplosionArrow")))
            return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(event.getHitBlock() != null) // 블럭
                event.getHitBlock().getLocation().createExplosion(4);
            else if(event.getHitEntity() != null) // 엔티티
                event.getHitEntity().getLocation().createExplosion(0.5f);
            event.getEntity().remove();
        }, 10);
    }
}
