package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class REvent_Vegan extends REventBase {

    public REvent_Vegan(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#ADFF2F") + "§l채식주의자", "채소만 먹을 수 있습니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f당신은 동물을 사랑하는 채식주의자가 되었습니다.");
        player.sendMessage("   §7채소 종류의 음식만 먹을 수 있게됩니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if(getIsEnabled(player) == false)
            return;
        switch (event.getItem().getType()) {
            case BREAD, PORKCHOP, COOKED_PORKCHOP, COD, SALMON, TROPICAL_FISH, PUFFERFISH, COOKED_COD, COOKED_SALMON, CAKE, COOKIE, BEEF, COOKED_BEEF, CHICKEN, COOKED_CHICKEN, ROTTEN_FLESH, SPIDER_EYE, PUMPKIN_PIE, RABBIT, COOKED_RABBIT, RABBIT_STEW, MUTTON, COOKED_MUTTON -> {
                Util.sendTitleWithSound(player, ChatColor.of("#ADFF2F") + "§l신념을 지키세요...", "아무리 배가 고파도 동물을 먹을 수는 없습니다...",
                        0, 40, 0, Sound.ENTITY_VILLAGER_NO, 100, 1);
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEatCake(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(getIsEnabled(player) == false)
            return;
        if(event.getClickedBlock() == null)
            return;
        if(event.getClickedBlock().getType().toString().contains("CAKE")) {
            Util.sendTitleWithSound(player, ChatColor.of("#ADFF2F") + "§l신념을 지키세요...", "아무리 배가 고파도 동물을 먹을 수는 없습니다...",
                    0, 40, 0, Sound.ENTITY_VILLAGER_NO, 100, 1);
            event.setCancelled(true);
        }
    }
}
