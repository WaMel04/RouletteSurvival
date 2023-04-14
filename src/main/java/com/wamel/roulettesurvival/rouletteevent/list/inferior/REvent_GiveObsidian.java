package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class REvent_GiveObsidian extends REventBase {

    public REvent_GiveObsidian(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#4B0082") + "§l흑요석", "흑요석을 3개 획득합니다.", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f네더에 가고 싶거나, 네더에서 나가고 싶은 당신을 위한 선물!");
        player.sendMessage("   §7흑요석을 3개 획득합니다.");
        player.sendMessage("");

        if(Util.getEmptySlotInStorage(player.getInventory()) < 1) {
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.OBSIDIAN, 3));
        } else {
            player.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 3));
        }
    }

}
