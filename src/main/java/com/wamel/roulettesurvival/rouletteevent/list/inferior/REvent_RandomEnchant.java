package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class REvent_RandomEnchant extends REventBase {

    public REvent_RandomEnchant(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#8A2BE2") + "§l랜덤 인챈트", "손에 들고 있는 아이템을 랜덤한 수치로 인챈트합니다.", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f마법의 힘으로 아이템을 인챈트합니다! 근데 뭔가 좀 이상하네요...");
        player.sendMessage("   §75초 후 손에 들고 있는 아이템에 랜덤한 수치의 랜덤한 마법을 부여합니다. (1~3개)");
        player.sendMessage("   §e아이템을 손에 꼭 들어주세요!");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                Random random = new Random();
                Integer chance = random.nextInt(100);
                Integer number = 0;

                if(chance <= 40)
                    number = 1;
                else if(chance <= 80)
                    number = 2;
                else if(chance <= 95)
                    number = 3;
                else if(chance <= 100)
                    number = 4;

                for(int i=0; i<number; i++) {
                    Enchantment enchantment = Enchantment.values()[random.nextInt(Enchantment.values().length)];

                    if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        player.sendActionBar("§e아이템을 손에 들어주세요!");
                        continue;
                    }

                    Integer level = random.nextInt(4) + 1;
                    String romaLevel = null;

                    player.getInventory().getItemInMainHand().addUnsafeEnchantment(enchantment, level);

                    if(level == 1)
                        romaLevel = "I";
                    else if(level == 2)
                        romaLevel = "II";
                    else if(level == 3)
                        romaLevel = "III";
                    else if(level == 4)
                        romaLevel = "IV";

                    Util.sendTitleWithSound(player, ChatColor.of("#8A2BE2") + "§l축하합니다", "§c" + enchantment.getName() + " " + romaLevel + "§f을(를) 부여했습니다!", 0, 60, 20, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 1);
                    Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + enchantment.getName() + " " + romaLevel + "§f을(를) 부여했습니다!");
                }
            }
        }.runTaskLater(plugin, 100);
    }

}
