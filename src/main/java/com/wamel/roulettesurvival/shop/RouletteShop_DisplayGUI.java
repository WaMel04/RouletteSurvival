package com.wamel.roulettesurvival.shop;

import com.wamel.roulettesurvival.RouletteSurvival;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class RouletteShop_DisplayGUI implements Listener {

    private RouletteSurvival plugin = RouletteSurvival.getInstance();

    private static final String INVENTORY_NAME = "§0§l룰렛 서바이벌 상점 - 필요 아이템";
    private static final String PREFIX = ChatColor.of("#8A2BE2") + "[RouletteSurvival] §f";

    private static HashMap<Integer, ArrayList<RouletteShop_PriceData>> priceMap = new HashMap<>();


    public static void open(Player player, Integer slot) {
        Inventory inventory = Bukkit.createInventory(null, 54, INVENTORY_NAME);

        int i = 0;
        for(RouletteShop_PriceData data : RouletteShop.priceMap.get(slot)) {
            ItemStack item = data.getItemStack();
            item.setAmount(data.getAmount());

            inventory.setItem(i, item);
            i++;
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getView().getTitle().equals(INVENTORY_NAME)))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getView().getTitle().equals(INVENTORY_NAME)))
            return;

        Player player = (Player) event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            RouletteShop.open(player);
        }, 1);
    }

}
