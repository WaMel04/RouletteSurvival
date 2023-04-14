package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.shop.RouletteShop_Item;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class REvent_ItemGatcha extends REventBase {

    private ArrayList<RouletteShop_Item> items = new ArrayList<>();

    public REvent_ItemGatcha(String name, String description, Integer duration, REventRank rank) {
        super( ChatColor.of("#1E90FF") + "§l아이템 가챠", "랜덤한 아이템을 획득합니다.", -1, REventRank.INFERIOR);

        items.add(new RouletteShop_Item(Material.COOKED_BEEF, 3, null, null, null));
        items.add(new RouletteShop_Item(Material.COOKED_PORKCHOP, 3, null, null, null));
        items.add(new RouletteShop_Item(Material.COOKED_COD, 5, null, null, null));
        items.add(new RouletteShop_Item(Material.COOKED_SALMON, 5, null, null, null));
        items.add(new RouletteShop_Item(Material.APPLE, 5, null, null, null));
        items.add(new RouletteShop_Item(Material.COBBLESTONE, 32, null, null, null));
        items.add(new RouletteShop_Item(Material.OAK_WOOD, 16, null, null, null));
        items.add(new RouletteShop_Item(Material.OAK_LEAVES, 32, null, null, null));
        items.add(new RouletteShop_Item(Material.OAK_BOAT, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_INGOT, 6, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_AXE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_SWORD, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_PICKAXE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_HELMET, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_CHESTPLATE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_LEGGINGS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.IRON_BOOTS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.GOLD_INGOT, 8, null, null, null));

        HashMap<Enchantment, Integer> map = new HashMap<>();
        map.put(Enchantment.DAMAGE_ALL, 3);
        map.put(Enchantment.DIG_SPEED, 3);
        items.add(new RouletteShop_Item(Material.GOLDEN_AXE, 1, map, null, null));

        map = new HashMap<>();
        map.put(Enchantment.DAMAGE_ALL, 3);
        map.put(Enchantment.FIRE_ASPECT, 1);
        items.add(new RouletteShop_Item(Material.GOLDEN_SWORD, 1, map, null, null));

        map = new HashMap<>();
        map.put(Enchantment.DIG_SPEED, 3);
        items.add(new RouletteShop_Item(Material.GOLDEN_PICKAXE, 1, map, null, null));

        map = new HashMap<>();
        map.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        items.add(new RouletteShop_Item(Material.GOLDEN_HELMET, 1, map, null, null));
        items.add(new RouletteShop_Item(Material.GOLDEN_CHESTPLATE, 1, map, null, null));
        items.add(new RouletteShop_Item(Material.GOLDEN_LEGGINGS, 1, map, null, null));
        items.add(new RouletteShop_Item(Material.GOLDEN_BOOTS, 1, map, null, null));

        items.add(new RouletteShop_Item(Material.GOLDEN_APPLE, 3, null, null, null));
        items.add(new RouletteShop_Item(Material.LEATHER_HELMET, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.LEATHER_CHESTPLATE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.LEATHER_LEGGINGS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.LEATHER_BOOTS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.CHAINMAIL_HELMET, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.CHAINMAIL_CHESTPLATE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.CHAINMAIL_LEGGINGS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.CHAINMAIL_BOOTS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND, 2, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_AXE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_SWORD, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_PICKAXE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_HELMET, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_CHESTPLATE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_LEGGINGS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.DIAMOND_BOOTS, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.NETHERITE_INGOT, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.NETHERITE_HOE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.FISHING_ROD, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.RED_BED, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.RESPAWN_ANCHOR, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.ARROW, 16, null, null, null));
        items.add(new RouletteShop_Item(Material.ENCHANTING_TABLE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.WATER_BUCKET, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.LAVA_BUCKET, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.OBSIDIAN, 3, null, null, null));
        items.add(new RouletteShop_Item(Material.FLINT_AND_STEEL, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.EGG, 8, null, null, null));
        items.add(new RouletteShop_Item(Material.CAKE, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.ANVIL, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.SHIELD, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.CROSSBOW, 1, null, null, null));
        items.add(new RouletteShop_Item(Material.TNT, 4, null, null, null));

        map = new HashMap<>();
        map.put(Enchantment.LOYALTY, 1);
        items.add(new RouletteShop_Item(Material.TRIDENT, 1, null, null, null));
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f아이템을 가챠로 뽑습니다! 당신의 운을 시험해보세요!");
        player.sendMessage("   §73초 후 아이템이 지급됩니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                Random random = new Random();

                ItemStack item = items.get(random.nextInt(items.size())).getItemStack();

                player.getWorld().strikeLightningEffect(player.getLocation());

                if(Util.getEmptySlotInStorage(player.getInventory()) < 1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                } else {
                    player.getInventory().addItem(item);
                }

                Util.sendTitleWithSound(player, "§e§l축하합니다", "§c" + item.getType() + "§f을(를) 뽑았습니다!",
                        0, 60, 20, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 1);
                Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + item.getType() + "§f을(를) 뽑았습니다!");

                //1초 후 소리 재생
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 100, 1);
                }, 20L);
            }
       }.runTaskLater(plugin, 60);
    }
}

