package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.rouletteevent.REventScoreboardManager;
import com.wamel.roulettesurvival.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class REvent_HeavensDoor extends REventBase {

    public REvent_HeavensDoor(String name, String description, Integer duration, REventRank rank) {
        super("§6§l헤븐즈 도어", "죽음을 1회 피해갑니다.", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f성스러운 힘의 가호로 죽음을 1회 피해갑니다.");
        player.sendMessage("   §7죽음에 처할시 이를 회피하며, 체력을 절반 회복합니다.");
        player.sendMessage("   §7회피시 헤븐즈 도어는 사라집니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(getIsEnabled(player) == false)
            return;

        if(player.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);

            player.setHealth(player.getMaxHealth() / 2);

            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

            DataManager_REvent.enabledREvents.get(rPlayer).remove(this);

            Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation().clone().add(0, 1, 0), EntityType.FIREWORK);
            FireworkMeta fwMeta = fw.getFireworkMeta();

            fwMeta.setPower(0);
            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).with(FireworkEffect.Type.STAR).build());
            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).flicker(true).with(FireworkEffect.Type.STAR).build());

            fw.setFireworkMeta(fwMeta);
            fw.setMetadata("roul_nodamage", new FixedMetadataValue(plugin, true));
            fw.detonate();

            Util.sendTitleWithSound(player, "§6§l헤븐즈 도어", "§f성스러운 힘의 가호로 죽음을 피해갔습니다!", 0, 60, 20, Sound.ITEM_TOTEM_USE, 100, 1);
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("     " + name);
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("   §a" + player.getName() + "§f님이 성스러운 힘의 가호로 죽음을 피해갔습니다!");
            Bukkit.broadcastMessage("");

            REventScoreboardManager.refresh(player);
        }
    }

}
