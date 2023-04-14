package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

public class REvent_TimeRewind extends REventBase {

    private static HashMap<RoulettePlayer, HashMap<Integer, Location>> locMap = new HashMap<>();

    private static final Integer BACK_MIN = 3;

    public REvent_TimeRewind(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#AFEEEE") + "§l시간 역행", BACK_MIN + "분 전 위치로 되돌아갑니다.", -1, REventRank.INFERIOR);

        // 1분마다 모든 플레이어의 위치 기록
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

                    if(locMap.containsKey(rPlayer)) {
                        HashMap<Integer, Location> map = locMap.get(rPlayer);
                        HashMap<Integer, Location> result = new HashMap<>();

                        for(Integer min : map.keySet()) {
                            if(min == 1) {
                                result.put(1, player.getLocation());
                            }
                            if(min != BACK_MIN) {
                                result.put(min + 1, map.get(min));
                            }

                            locMap.put(rPlayer, result);
                        }
                    } else {
                        HashMap<Integer, Location> result = new HashMap<>();
                        result.put(1, player.getLocation());

                        locMap.put(rPlayer, result);
                    }
                }

            }
        }.runTaskTimer(plugin, 0, 20*60);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f시간을 되돌릴 수 있다면 얼마나 좋을까요?");
        player.sendMessage("   §75초 후 " + BACK_MIN + "분 전의 위치로 되돌아갑니다.");
        player.sendMessage("");

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Player target = rPlayer.getPlayer();

                if(target == null)
                    return;
                if(locMap.containsKey(rPlayer) && locMap.get(rPlayer).containsKey(BACK_MIN)) {
                    Util.sendTitleWithSound(target, ChatColor.of("#AFEEEE") + "§l시간 역행!", "§f3분 전의 위치로 되돌아왔습니다.",
                            0, 60, 20, Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                    target.teleport(locMap.get(rPlayer).get(BACK_MIN));
                } else {
                    Util.sendTitleWithSound(target, "§c§l실패", "§f되돌아갈 위치가 없습니다...",
                            0, 60, 20, Sound.BLOCK_ANVIL_LAND, 100, 0.5f);
                }
            }
        }.runTaskLater(plugin, 20*5);
    }

}
