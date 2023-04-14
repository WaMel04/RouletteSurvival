package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.shop.RouletteShop_Item;
import com.wamel.roulettesurvival.util.Pair;
import com.wamel.roulettesurvival.util.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class REvent_EverywhereDoor extends REventBase {

    private static final HashMap<RoulettePlayer, Integer> doorIdMap = new HashMap<>();
    private static final HashMap<String, Pair<Location, Location>> doorMap = new HashMap<>(); // doorId, <A 위치, B 위치>
    private static final HashMap<Location, Location> holoMap = new HashMap<>(); // 문 위치, 홀로그램 위치

    public REvent_EverywhereDoor(String name, String description, Integer duration, REventRank rank) {
        super("§d§l어디로든 문", "워프가 가능한 문 2개를 획득합니다.", -1, REventRank.SUPERIOR);
    }

    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f당신의 어릴적 추억이 담겨있는 어디로든 문입니다.");
        player.sendMessage("   §7'§b어디로든 문 A§7'와 §7'§c어디로든 문 B§7'를 모두 설치 후, 문을 클릭시 다른 문으로 이동합니다.");
        player.sendMessage("");

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(!doorIdMap.containsKey(rPlayer))
            doorIdMap.put(rPlayer, 1);

        Integer count = doorIdMap.get(rPlayer);
        String doorId = player.getName() + "(" + count + ")";

        doorIdMap.put(rPlayer, count + 1);

        HashMap<Enchantment, Integer> map = new HashMap<>();
        map.put(Enchantment.LUCK, 1);

        ItemStack doorA = new RouletteShop_Item(Material.WARPED_DOOR, 1, map, "§b어디로든 문 A | " + doorId,
                "§f이 문과 '§c어디로든 문 B | " + doorId + "§7'§f을(를) 설치 후",
                "§f문을 우클릭시 다른 문으로 이동합니다.").getItemStack();
        ItemStack doorB = new RouletteShop_Item(Material.CRIMSON_DOOR, 1, map, "§c어디로든 문 B | " + doorId,
                "§f이 문과 '§b어디로든 문 A | " + doorId + "§7'§f을(를) 설치 후",
                "§f문을 우클릭시 다른 문으로 이동합니다.").getItemStack();

        if(Util.getEmptySlotInStorage(player.getInventory()) < 2) {
            player.getWorld().dropItemNaturally(player.getLocation(), doorA);
            player.getWorld().dropItemNaturally(player.getLocation(), doorB);
        } else {
            player.getInventory().addItem(doorA, doorB);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(event.getPlayer() == null)
            return;

        Player player = event.getPlayer();
        ItemStack heldItem = event.getItemInHand();
        Location loc = event.getBlock().getLocation();

        if(heldItem == null)
            return;
        if(heldItem.getItemMeta().getDisplayName().contains("§b어디로든 문 A")) {
            String doorId = ChatColor.stripColor(heldItem.getItemMeta().getDisplayName()).replace("어디로든 문 A | ", "");

            if(!(doorMap.containsKey(doorId))) {
                Pair<Location, Location> pair = new Pair<>(event.getBlock().getLocation(), null);

                player.sendMessage(PREFIX + "어디로든 문 A를 설치했습니다. §e식별 Id: " + doorId);

                Location holoLoc = loc.clone().toCenterLocation().add(0, 1, 0);
                holoMap.put(loc, holoLoc);
                spawnHologram(holoLoc, "§b어디로든 문 A §4§l(연결 X) " + "§e" + doorId);

                doorMap.put(doorId, pair);
                return;
            }

            Pair<Location, Location> pair = doorMap.get(doorId);

            if(pair.right == null) {
                event.setCancelled(true);
                return;
            }
            if(pair.left != null && pair.right != null) {
                event.setCancelled(true);
                return;
            }

            pair.left = event.getBlock().getLocation();
            player.sendMessage(PREFIX + "모든 어디로든 문이 설치되었습니다! §e식별 Id: " + doorId);
            player.sendMessage(PREFIX + "§b어디로든 문 A: " + pair.left.getWorld().getName() + ", " + pair.left.getBlockX() + ", " + pair.left.getBlockY() + ", " + pair.left.getBlockZ());
            player.sendMessage(PREFIX + "§c어디로든 문 B: " + pair.right.getWorld().getName() + ", " + pair.right.getBlockX() + ", " + pair.right.getBlockY() + ", " + pair.right.getBlockZ());
            player.playSound(player, Sound.BLOCK_PORTAL_TRAVEL, 100, 1f);

            Location holoLocA = pair.left.clone().toCenterLocation().add(0, 1, 0);
            holoMap.put(pair.left, holoLocA);
            despawnHologram(holoLocA);
            spawnHologram(holoLocA, "§b어디로든 문 A §a§l(연결 O) " + "§e" + doorId);

            Location holoLocB = pair.right.clone().toCenterLocation().add(0, 1, 0);
            holoMap.put(pair.right, holoLocB);
            despawnHologram(holoLocB);
            spawnHologram(holoLocB, "§c어디로든 문 B §a§l(연결 O) " + "§e" + doorId);

            doorMap.put(doorId, pair);
        } else if(heldItem.getItemMeta().getDisplayName().contains("어디로든 문 B")) {
            String doorId = ChatColor.stripColor(heldItem.getItemMeta().getDisplayName()).replace("어디로든 문 B | ", "");

            if(!(doorMap.containsKey(doorId))) {
                Pair<Location, Location> pair = new Pair<>(null, event.getBlock().getLocation());
                player.sendMessage(PREFIX + "어디로든 문 B를 설치했습니다. §e식별 Id: " + doorId);

                Location holoLoc = loc.clone().toCenterLocation().add(0, 1, 0);
                holoMap.put(loc, holoLoc);
                spawnHologram(holoLoc, "§c어디로든 문 B §4§l(연결 X) " + "§e" + doorId);

                doorMap.put(doorId, pair);
                return;
            }

            Pair<Location, Location> pair = doorMap.get(doorId);

            if(pair.left == null) {
                event.setCancelled(true);
                return;
            }
            if(pair.left != null && pair.right != null) {
                event.setCancelled(true);
                return;
            }

            pair.right = event.getBlock().getLocation();
            player.sendMessage(PREFIX + "모든 어디로든 문이 설치되었습니다! §e식별 Id: " + doorId);
            player.sendMessage(PREFIX + "§b어디로든 문 A: " + pair.left.getWorld().getName() + ", " + pair.left.getBlockX() + ", " + pair.left.getBlockY() + ", " + pair.left.getBlockZ());
            player.sendMessage(PREFIX + "§c어디로든 문 B: " + pair.right.getWorld().getName() + ", " + pair.right.getBlockX() + ", " + pair.right.getBlockY() + ", " + pair.right.getBlockZ());
            player.playSound(player, Sound.BLOCK_PORTAL_TRAVEL, 100, 1f);

            Location holoLocA = pair.left.clone().toCenterLocation().add(0, 1, 0);
            holoMap.put(pair.left, holoLocA);
            despawnHologram(holoLocA);
            spawnHologram(holoLocA, "§b어디로든 문 A §a§l(연결 O) " + "§e" + doorId);

            Location holoLocB = pair.right.clone().toCenterLocation().add(0, 1, 0);
            holoMap.put(pair.right, holoLocB);
            despawnHologram(holoLocB);
            spawnHologram(holoLocB, "§c어디로든 문 B §a§l(연결 O) " + "§e" + doorId);

            doorMap.put(doorId, pair);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null)
            return;
        if(event.getAction().isLeftClick())
            return;

        Location loc = event.getClickedBlock().getLocation();
        Location locA = null;
        Location locB = null;
        Boolean isDoorA = null;

        for(Pair<Location, Location> pair : doorMap.values()) {
            if(pair.left != null && pair.right != null) {
                if(pair.left.equals(loc) || pair.left.equals(loc.clone().add(0, -1, 0))) {
                    locA = pair.left;

                    locB = pair.right;
                    isDoorA = true;

                    break;
                }
                if(pair.right.equals(loc) || pair.right.equals(loc.clone().add(0, -1, 0))) {
                    locA = pair.left;

                    locB = pair.right;
                    isDoorA = false;

                    break;
                }
            }
        }
        if(isDoorA == null)
            return;

        Util.sendTitleWithSound(event.getPlayer(), "§d§l어디로든 문", "이동에 성공했습니다!", 0, 40, 0, Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
        if(isDoorA == true) {
            event.getPlayer().teleport(locB);
        } else if(isDoorA == false) {
            event.getPlayer().teleport(locA);
        }

    }

    @EventHandler
    public void onBreakUnderBlock(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();

        for(Pair<Location, Location> pair : doorMap.values()) {
            if(pair.left != null) {
                if(pair.left.equals(loc.clone().add(0, 1, 0))) {
                    event.setCancelled(true);
                    continue;
                }
            }
            if(pair.right != null) {
                if(pair.right.equals(loc.clone().add(0, 1, 0))) {
                    event.setCancelled(true);
                    continue;
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!(event.getBlock().getType().equals(Material.CRIMSON_DOOR) || event.getBlock().getType().equals(Material.WARPED_DOOR)))
            return;

        Location loc = event.getBlock().getLocation();

        ArrayList<String> list = new ArrayList<>(doorMap.keySet());

        for(String doorId : list) {
            Pair<Location, Location> pair = doorMap.get(doorId);
            if(pair.left != null && pair.right == null) {
                if(pair.left.equals(loc) || pair.left.equals(loc.clone().add(0, -1, 0))) {
                    Location holoLocA;

                    if(pair.left.equals(loc)) {
                        holoLocA = holoMap.get(pair.left);
                        holoMap.remove(pair.left);
                    } else {
                        holoLocA = holoMap.get(loc.clone().add(0, -1, 0));
                        holoMap.remove(loc.clone().add(0, -1, 0));
                    }

                    despawnHologram(holoLocA);

                    HashMap<Enchantment, Integer> map = new HashMap<>();
                    map.put(Enchantment.LUCK, 1);

                    ItemStack doorA = new RouletteShop_Item(Material.WARPED_DOOR, 1, map, "§b어디로든 문 A | " + doorId,
                            "§f이 문과 '§c어디로든 문 B | " + doorId + "§7'§f을(를) 설치 후",
                            "§f문을 우클릭시 다른 문으로 이동합니다.").getItemStack();
                    event.setDropItems(false);
                    loc.getWorld().dropItemNaturally(loc, doorA);

                    pair.left = null;
                    doorMap.remove(doorId);
                    return;
                }
            }
            if(pair.left == null && pair.right != null) {
                if(pair.right.equals(loc) || pair.right.equals(loc.clone().add(0, -1, 0))) {
                    Location holoLocB;

                    if(pair.right.equals(loc)) {
                        holoLocB = holoMap.get(pair.right);
                        holoMap.remove(pair.right);
                    } else {
                        holoLocB = holoMap.get(loc.clone().add(0, -1, 0));
                        holoMap.remove(loc.clone().add(0, -1, 0));
                    }

                    despawnHologram(holoLocB);

                    HashMap<Enchantment, Integer> map = new HashMap<>();
                    map.put(Enchantment.LUCK, 1);

                    ItemStack doorB = new RouletteShop_Item(Material.CRIMSON_DOOR, 1, map, "§c어디로든 문 B | " + doorId,
                            "§f이 문과 '§b어디로든 문 A | " + doorId + "§7'§f을(를) 설치 후",
                            "§f문을 우클릭시 다른 문으로 이동합니다.").getItemStack();
                    event.setDropItems(false);
                    loc.getWorld().dropItemNaturally(loc, doorB);

                    pair.right = null;
                    doorMap.remove(doorId);
                    return;
                }
            }
            if(pair.left != null && pair.right != null) {
                if(pair.left.equals(loc) || pair.left.equals(loc.clone().add(0, -1, 0))) {
                    Location holoLocA;
                    Location holoLocB;

                    if(pair.left.equals(loc)) {
                        holoLocA = holoMap.get(pair.left);
                        holoLocB = holoMap.get(pair.right);
                        holoMap.remove(pair.left);
                    } else {
                        holoLocA = holoMap.get(loc.clone().add(0, -1, 0));
                        holoLocB = holoMap.get(pair.right);
                        holoMap.remove(loc.clone().add(0, -1, 0));
                    }

                    despawnHologram(holoLocA);
                    despawnHologram(holoLocB);

                    spawnHologram(holoLocB, "§c어디로든 문 B §4§l(연결 X) " + "§e" + doorId);

                    HashMap<Enchantment, Integer> map = new HashMap<>();
                    map.put(Enchantment.LUCK, 1);

                    ItemStack doorA = new RouletteShop_Item(Material.WARPED_DOOR, 1, map, "§b어디로든 문 A | " + doorId,
                            "§f이 문과 '§c어디로든 문 B | " + doorId + "§7'§f을(를) 설치 후",
                            "§f문을 우클릭시 다른 문으로 이동합니다.").getItemStack();
                    event.setDropItems(false);
                    loc.getWorld().dropItemNaturally(loc, doorA);

                    pair.left = null;
                    doorMap.put(doorId, pair);
                    return;
                }
                if(pair.right.equals(loc)|| pair.right.equals(loc.clone().add(0, -1, 0))) {
                    Location holoLocA;
                    Location holoLocB;

                    if(pair.right.equals(loc)) {
                        holoLocA = holoMap.get(pair.left);
                        holoLocB = holoMap.get(pair.right);
                        holoMap.remove(pair.right);
                    } else {
                        holoLocA = holoMap.get(pair.left);
                        holoLocB = holoMap.get(loc.clone().add(0, -1, 0));
                        holoMap.remove(loc.clone().add(0, -1, 0));
                    }

                    despawnHologram(holoLocA);
                    despawnHologram(holoLocB);

                    spawnHologram(holoLocA, "§b어디로든 문 A §4§l(연결 X) " + "§e" + doorId);

                    HashMap<Enchantment, Integer> map = new HashMap<>();
                    map.put(Enchantment.LUCK, 1);

                    ItemStack doorB = new RouletteShop_Item(Material.CRIMSON_DOOR, 1, map, "§c어디로든 문 B | " + doorId,
                            "§f이 문과 '§b어디로든 문 A | " + doorId + "§7'§f을(를) 설치 후",
                            "§f문을 우클릭시 다른 문으로 이동합니다.").getItemStack();
                    event.setDropItems(false);
                    loc.getWorld().dropItemNaturally(loc, doorB);

                    pair.right = null;
                    doorMap.put(doorId, pair);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        ArrayList<Block> list = new ArrayList<>(event.blockList());
        for(Block block : list) {
            Location loc = block.getLocation();

            for(Pair<Location, Location> pair : doorMap.values()) {
                if(pair.left != null) {
                    if(pair.left.equals(loc) || pair.left.equals(loc.clone().add(0, -1, 0)) || pair.left.equals(loc.clone().add(0, 1, 0))) {
                        event.blockList().remove(block);
                        continue;
                    }
                }
                if(pair.right != null) {
                    if(pair.right.equals(loc) || pair.right.equals(loc.clone().add(0, -1, 0)) || pair.right.equals(loc.clone().add(0, 1, 0))) {
                        event.blockList().remove(block);
                        continue;
                    }
                }
            }
        }
    }

    public void spawnHologram(Location loc, String name) {
        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
        stand.setInvisible(true);
        stand.setCustomNameVisible(true);
        stand.setInvulnerable(true);
        stand.setMarker(false);
        stand.setGravity(false);
        stand.setCustomName(name);
    }

    public void despawnHologram(Location loc) {
        for(Entity entity : loc.getNearbyEntitiesByType(ArmorStand.class, 1)) {
            entity.remove();
            return;
        }
    }
}

