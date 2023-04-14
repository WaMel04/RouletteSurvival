package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class REvent_Dreadful extends REventBase {

    private final double DAMAGE = 0.5;

    private final Integer BRIGHTNESS = 5;

    public REvent_Dreadful(String name, String description, Integer duration, REventRank rank) {
        super("§8§l공포", "어두운 곳에 있을시 피해를 입습니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f어둠은 가장 원초적인 공포입니다.");
        player.sendMessage("   §e밝기가 " + BRIGHTNESS + " 미만인 곳에 있을시 겁에 질려" + ChatColor.of("#DC143C") + " ♥ x " + DAMAGE + " §e만큼의 피해를 입습니다.");
        player.sendMessage("");

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Player oPlayer = rPlayer.getPlayer();
                if(getIsEnabled(oPlayer) == false) {
                    super.cancel();
                    return;
                }

                Block block = oPlayer.getLocation().getBlock();
                Integer b = Integer.valueOf(block.getLightLevel());

                oPlayer.sendActionBar("§e현재 서있는 블럭의 밝기: " + b);

                if(b < BRIGHTNESS) {
                    oPlayer.damage(DAMAGE*2);
                    Util.sendTitleWithSound(oPlayer, "§8§l공포", "이곳은 너무 어둡습니다! 다른 곳으로 가요...", 0, 30, 0, Sound.ENTITY_WARDEN_SONIC_BOOM, 100, 1);
                    oPlayer.sendActionBar("§c현재 서있는 블럭의 밝기: " + b);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
