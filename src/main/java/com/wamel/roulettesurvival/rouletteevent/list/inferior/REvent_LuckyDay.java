package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.listener.DropMultipleEvent;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.shop.RouletteShop_Item;
import com.wamel.roulettesurvival.util.Pair;
import com.wamel.roulettesurvival.util.WeightedRandom;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class REvent_LuckyDay extends REventBase {

    private List<Pair<RouletteShop_Item, Integer>> items = new ArrayList<>(); // Pair<아이템, 가중치>

    private static final Integer BLOCK_CHANCE = 3;
    private static final Integer MOB_CHANCE = 20;

    public REvent_LuckyDay(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#00FF7F") + "§l오늘은 럭키데이!", "행운이 찾아옵니다.", 120, REventRank.INFERIOR);

        items.add(new Pair<>(new RouletteShop_Item(Material.COOKED_BEEF, 2, null, null, null), 110));
        items.add(new Pair<>(new RouletteShop_Item(Material.COOKED_PORKCHOP, 2, null, null, null), 110));

        items.add(new Pair<>(new RouletteShop_Item(Material.OBSIDIAN, 1, null, null, null), 80));
        items.add(new Pair<>(new RouletteShop_Item(Material.OBSIDIAN, 3, null, null, null), 50));
        items.add(new Pair<>(new RouletteShop_Item(Material.OBSIDIAN, 5, null, null, null), 20));

        items.add(new Pair<>(new RouletteShop_Item(Material.ENDER_PEARL, 1, null, null, null), 50));
        items.add(new Pair<>(new RouletteShop_Item(Material.ENDER_PEARL, 3, null, null, null), 30));

        items.add(new Pair<>(new RouletteShop_Item(Material.BLAZE_ROD, 1, null, null, null), 20));

        items.add(new Pair<>(new RouletteShop_Item(Material.ENCHANTING_TABLE, 1, null, null, null), 10));

        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_APPLE, 1, null, null, null), 50));
        items.add(new Pair<>(new RouletteShop_Item(Material.ENCHANTED_GOLDEN_APPLE, 1, null, null, null), 10));

        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_INGOT, 3, null, null, null), 100));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_INGOT, 5, null, null, null), 80));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_INGOT, 7, null, null, null), 50));
        items.add(new Pair<>(new RouletteShop_Item(Material.DIAMOND, 1, null, null, null), 70));
        items.add(new Pair<>(new RouletteShop_Item(Material.DIAMOND, 5, null, null, null), 50));
        items.add(new Pair<>(new RouletteShop_Item(Material.DIAMOND, 6, null, null, null), 30));

        HashMap<Enchantment, Integer> map = new HashMap<>();
        map.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_HELMET, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_CHESTPLATE, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_LEGGINGS, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_BOOTS, 1, map, null, null), 20));

        map = new HashMap<>();
        map.put(Enchantment.DAMAGE_ALL, 2);
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_SWORD, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_AXE, 1, map, null, null), 20));

        map = new HashMap<>();
        map.put(Enchantment.DIG_SPEED, 2);
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_PICKAXE, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.IRON_SHOVEL, 1, map, null, null), 20));


        map = new HashMap<>();
        map.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_HELMET, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_CHESTPLATE, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_LEGGINGS, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_BOOTS, 1, map, null, null), 20));

        map = new HashMap<>();
        map.put(Enchantment.DAMAGE_ALL, 3);
        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_SWORD, 1, map, null, null), 20));
        items.add(new Pair<>(new RouletteShop_Item(Material.GOLDEN_AXE, 1, map, null, null), 20));

        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*60*5, 0, true), true);
        meta.setColor(Color.ORANGE);
        meta.setDisplayName(Color.YELLOW + "오렌지 주스 (화염저항 I 5분)");
        potion.setItemMeta(meta);
        items.add(new Pair<>(new RouletteShop_Item(potion), 30));

        potion = new ItemStack(Material.POTION, 1);
        meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*60*3, 3, true), true);
        meta.setColor(Color.YELLOW);
        meta.setDisplayName(Color.YELLOW + "게토레이 (성급함 IV 3분)");
        potion.setItemMeta(meta);
        items.add(new Pair<>(new RouletteShop_Item(potion), 30));

        potion = new ItemStack(Material.POTION, 1);
        meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*3, 0, true), true);
        meta.setColor(Color.PURPLE);
        meta.setDisplayName(Color.PURPLE + "포도 주스 (힘 I 3분)");
        potion.setItemMeta(meta);
        items.add(new Pair<>(new RouletteShop_Item(potion), 30));

        potion = new ItemStack(Material.POTION, 1);
        meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*5, 1, true), true);
        meta.setColor(Color.fromRGB(0, 191, 255));
        meta.setDisplayName(Color.fromRGB(0, 191, 255) + "포카리스웨트 (신속 II 5분)");
        potion.setItemMeta(meta);
        items.add(new Pair<>(new RouletteShop_Item(potion), 30));

        potion = new ItemStack(Material.POTION, 1);
        meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*60*1, 0, true), true);
        meta.setColor(Color.WHITE);
        meta.setDisplayName(Color.WHITE + "우유 (느린 낙하 I 1분)");
        potion.setItemMeta(meta);
        items.add(new Pair<>(new RouletteShop_Item(potion), 30));

        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_INGOT, 1, null, null, null), 10));

        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_HELMET, 1, null, null, null), 3));
        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_CHESTPLATE, 1, null, null, null), 3));
        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_LEGGINGS, 1, null, null, null), 3));
        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_BOOTS, 1, null, null, null), 3));

        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_SWORD, 1, null, null, null), 5));
        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_AXE, 1, null, null, null), 5));
        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_PICKAXE, 1, null, null, null), 5));
        items.add(new Pair<>(new RouletteShop_Item(Material.NETHERITE_SHOVEL, 1, null, null, null), 5));

    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f오늘따라 운이 좋은 것 같습니다.");
        player.sendMessage("   §7블럭을 캘 때 " + BLOCK_CHANCE + "%의 확률로 유용한 아이템을 얻을 수 있습니다.");
        player.sendMessage("   §7몬스터를 처치 시 " + MOB_CHANCE + "%의 확률로 유용한 아이템을 얻을 수 있습니다.");
        player.sendMessage("   §e다른 플레이어가 설치한 블럭에는 적용되지 않습니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(getIsEnabled(event.getPlayer()) == false)
            return;

        Location loc = event.getBlock().getLocation();

        if(DropMultipleEvent.blocks.contains(loc)) // 드랍률 이벤트에서 남이 설치한 블럭을 담은 list
            return;

        Random random = new Random();
        if(random.nextInt(100) <= BLOCK_CHANCE) {
            ItemStack item = WeightedRandom.getRandomElement(items).getItemStack();
            loc.getWorld().dropItemNaturally(loc, item);
            spawnFirework(loc);
        }
    }

    @EventHandler
    public void onEntityKill(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;
        if(!(event.getEntity() instanceof LivingEntity))
            return;
        if(event.getEntity() instanceof Player)
            return;
        if(event.isCancelled())
            return;

        Player attacker = (Player) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if(getIsEnabled(attacker) == false)
            return;
        if(victim.getHealth() > event.getFinalDamage())
            return;

        Location loc = victim.getLocation();

        Random random = new Random();
        if(random.nextInt(100) <= MOB_CHANCE) {
            ItemStack item = WeightedRandom.getRandomElement(items).getItemStack();
            loc.getWorld().dropItemNaturally(loc, item);
            spawnFirework(loc);
        }
    }

    private void spawnFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwMeta = fw.getFireworkMeta();

        fwMeta.setPower(10);

        Random random = new Random();

        Color randColor = Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        fwMeta.addEffect(FireworkEffect.builder().withColor(randColor).flicker(true).with(FireworkEffect.Type.BALL).build());
        fwMeta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).flicker(true).with(FireworkEffect.Type.BALL).build());

        fw.setFireworkMeta(fwMeta);
        fw.setMetadata("roul_nodamage", new FixedMetadataValue(plugin, true));
        fw.detonate();
    }

}
