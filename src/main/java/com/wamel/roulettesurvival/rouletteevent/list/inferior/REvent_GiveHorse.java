package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class REvent_GiveHorse extends REventBase {

    public REvent_GiveHorse(String name, String description, Integer duration, REventRank rank) {
        super("§6§l천고마비의 계절", "길들여진 말을 획득합니다.", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f말과 함께 빠르게 달려보세요.");
        player.sendMessage("   §7안장을 채운 길들여진 말을 획득합니다.");
        player.sendMessage("");

        Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
        horse.setAdult();
        horse.setTamed(true);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
    }

}
