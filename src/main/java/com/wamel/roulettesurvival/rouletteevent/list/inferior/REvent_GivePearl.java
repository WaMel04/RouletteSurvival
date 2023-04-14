package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class REvent_GivePearl extends REventBase {

    public REvent_GivePearl(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#2E8B57") + "§l엔더 진주", "엔더 진주를 1개 획득합니다.", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f엔더드래곤을 잡기 위해선 엔더 진주가 필요합니다.");
        player.sendMessage("   §7엔더 진주를 획득합니다.");
        player.sendMessage("");

        if(Util.getEmptySlotInStorage(player.getInventory()) < 1) {
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.ENDER_PEARL, 1));
        } else {
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
        }
    }

}
