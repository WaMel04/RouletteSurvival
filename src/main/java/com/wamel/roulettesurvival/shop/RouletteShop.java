package com.wamel.roulettesurvival.shop;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.listener.MinecraftEvent;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouletteShop implements Listener {

    private RouletteSurvival plugin = RouletteSurvival.getInstance();

    private static final String INVENTORY_NAME = "§0§l룰렛 서바이벌 상점 by WaMel_";
    private static final String PREFIX = ChatColor.of("#8A2BE2") + "[RouletteSurvival] §f";

    public static HashMap<Integer, ArrayList<RouletteShop_PriceData>> priceMap = new HashMap<>();

    private static Inventory inventory = Bukkit.createInventory(null, 54, INVENTORY_NAME);


    public RouletteShop() {
        for(int i=9; i<18; i++) {
            setSlot(i, new RouletteShop_Item(Material.WHITE_STAINED_GLASS_PANE, 1, null, "§f", null), null);
        }

        setSlot(0, new RouletteShop_Item(Material.MUSIC_DISC_MALL, 1, null, MinecraftEvent.ROULETTE_ITEM_NAME, "§7룰렛을 사용할 수 있는 아이템입니다."),
                new RouletteShop_PriceData(new RouletteShop_Item(Material.IRON_INGOT, 1, null, null, null), 30));
        setSlot(1, new RouletteShop_Item(Material.MUSIC_DISC_13, 1, null, MinecraftEvent.SUPERIOR_ROULETTE_ITEM_NAME, "§e황금 룰렛§7을 사용할 수 있는 아이템입니다."),
                new RouletteShop_PriceData(new RouletteShop_Item(Material.DIAMOND, 1, null, null, null), 6));
    }

    public static void open(Player player) {
        player.openInventory(inventory);
    }

    public static void setSlot(Integer slot, RouletteShop_Item item, RouletteShop_PriceData... datas) {
        if(priceMap.containsKey(slot))
            return;

        ItemStack stack = item.getItemStack();

        if(datas != null) {
            ArrayList<RouletteShop_PriceData> priceDataList = new ArrayList<>();

            int i = 0;
            for(RouletteShop_PriceData data : datas) {
                priceDataList.add(data);
                i++;
            }

            List<String> lores;

            if(stack.getLore() == null) {
                lores = new ArrayList<>();
            } else {
                lores = stack.getLore();
            }

            lores.add("§f");
            lores.add("   §6구매시 " + i + "개의 아이템이 필요합니다!");
            lores.add("   " + ChatColor.of("#FFFF00") + "클릭시 아이템을 구매합니다.");
            lores.add("   " + ChatColor.of("#FFFACD") + "Shift + 좌클릭시 필요한 아이템 목록을 확인합니다.");
            lores.add("§f");

            stack.setLore(lores);
            priceMap.put(slot, priceDataList);
        }

        inventory.setItem(slot, stack);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getView().getTitle().equals(INVENTORY_NAME)))
            return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType().isAir()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if(clickedItem.getLore() == null)
            return;

        for(String lore : clickedItem.getLore()) {
            if(lore.contains("구매시")) {
                if(!(priceMap.containsKey(event.getRawSlot())))
                    return;

                if(event.getClick().equals(ClickType.SHIFT_LEFT)) {
                    RouletteShop_DisplayGUI.open(player, event.getRawSlot());
                    return;
                } else {
                    ArrayList<ItemStack> stacks = new ArrayList<>();

                    for(RouletteShop_PriceData data : priceMap.get(event.getRawSlot())) {
                        if(!(player.getInventory().containsAtLeast(data.getItemStack(), data.getAmount()))) {
                            player.sendMessage(PREFIX + "아이템이 부족합니다.");
                            return;
                        }

                        ItemStack stack = data.getItemStack();
                        stack.setAmount(data.getAmount());

                        stacks.add(stack);
                    }

                    for(ItemStack stack : stacks) {
                        player.getInventory().removeItem(stack);
                    }

                    ItemStack buyItem = clickedItem.clone();
                    List<String> buyLores = new ArrayList<>();

                    for(String lore1 : buyItem.getLore()) {
                        if(lore1.contains("구매시"))
                            break;
                        buyLores.add(lore1);
                    }
                    buyLores.remove(buyLores.size() - 1);

                    buyItem.setLore(buyLores);

                    if(Util.getEmptySlotInStorage(player.getInventory()) < 1) {
                        player.getWorld().dropItemNaturally(player.getLocation(), buyItem);
                    } else {
                        player.getInventory().addItem(buyItem);
                    }
                    player.playSound(player, Sound.BLOCK_CHAIN_PLACE, 100, 1);
                    player.sendMessage(PREFIX + "아이템을 구매했습니다.");
                    return;
                }
            }
        }
    }


}
