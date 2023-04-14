package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class REvent_ChaosInventory extends REventBase {

    public REvent_ChaosInventory(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#F0FFF0") + "§l혼돈의 인벤토리", "인벤토리가 혼란스러워 집니다.", -1, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f잘 정리해둔 인벤토리가 어지럽혀진다면 얼마나 화가 날까요?");
        player.sendMessage("   §7인벤토리의 아이템이 랜덤으로 재배치됩니다.");
        player.sendMessage("");

        List<ItemStack> shuffledList = new ArrayList<>();

        for(int i=0; i<36; i++) {
            shuffledList.add(null);
        }

        if(player.getInventory().getStorageContents() == null)
            return;

        int j=0;
        for(ItemStack item : player.getInventory().getStorageContents().clone()) {
            if(item == null) {
                j++;
                continue;
            }

            shuffledList.set(j, item);
            j++;
        }

        Collections.shuffle(shuffledList);

        player.getInventory().setStorageContents(new ItemStack[]{});

        for(int k=0; k<shuffledList.size(); k++) {
            if(shuffledList.get(k) != null)
                player.getInventory().setItem(k, shuffledList.get(k));
        }
    }

}
