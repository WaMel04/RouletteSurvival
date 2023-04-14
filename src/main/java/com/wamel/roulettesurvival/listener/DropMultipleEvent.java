package com.wamel.roulettesurvival.listener;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.data.DataManager_Config;
import com.wamel.roulettesurvival.data.DataManager_Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DropMultipleEvent implements Listener {

    private RouletteSurvival plugin = RouletteSurvival.getInstance();
    public static ArrayList<Location> blocks = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getPlayer() == null)
            return;
        if(DataManager_Game.isEnabled == false)
            return;
        if(blocks.contains(event.getBlock().getLocation()))
            return;

        blocks.add(event.getBlock().getLocation());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer() == null)
            return;
        if(DataManager_Game.isEnabled == false)
            return;
        if(blocks.contains(event.getBlock().getLocation())) {
            blocks.remove(event.getBlock().getLocation());
            return;
        }

        Collection<ItemStack> drops = event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand());
        ArrayList<ItemStack> results = new ArrayList<>();

        for(ItemStack drop : drops) {
            drop.setAmount(drop.getAmount() * DataManager_Config.DROP_MULTIPLE);
            results.add(drop);
        }

        Integer exp = event.getExpToDrop();

        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);

        for(ItemStack drop : results) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
        }

        if(exp != 0) {
            ExperienceOrb orb = event.getPlayer().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class);
            orb.setExperience(exp);
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if(event.getEntity().getKiller() == null)
            return;
        if(DataManager_Game.isEnabled == false)
            return;

        Collection<ItemStack> drops = event.getDrops();

        for(ItemStack drop : drops) {
            drop.setAmount(drop.getAmount() * (DataManager_Config.DROP_MULTIPLE-1));
            event.getEntity().getWorld().dropItemNaturally(
                    event.getEntity().getLocation(), drop
                    );
        }

    }

}
