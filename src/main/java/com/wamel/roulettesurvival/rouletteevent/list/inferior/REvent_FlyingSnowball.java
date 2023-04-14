package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.BarTimer;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class REvent_FlyingSnowball extends REventBase {

    public REvent_FlyingSnowball(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#F0FFFF") + "§l눈덩이와 함께 날아봐", "눈덩이를 타고 날아갈 수 있게됩니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(600초 지속)");
        player.sendMessage("");
        player.sendMessage("   §f눈덩이와 함께 날아봐요~!");
        player.sendMessage("   §7눈덩이를 우클릭시 날아가는 눈덩이에 탑승하게 됩니다.");
        player.sendMessage("   §b눈덩이 5개를 지급받습니다.");
        player.sendMessage("");

        if(Util.getEmptySlotInStorage(player.getInventory()) < 1) {
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.SNOWBALL, 5));
        } else {
            player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 5));
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getType().equals(EntityType.SNOWBALL)))
            return;
        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();

        if (getIsEnabled(player) == false)
            return;

        event.getEntity().setPassenger(player);
    }

}
