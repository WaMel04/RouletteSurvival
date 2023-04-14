package com.wamel.roulettesurvival.rouletteevent.list.superior;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.rouletteevent.list.inferior.REvent_JumpLock;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class REvent_FallAttack extends REventBase {

    private static final String METADATA_KEY = "roul_isFalling";

    public REvent_FallAttack(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#7FFFD4") + "§l낙하 공격", "낙하 공격을 할 수 있게 됩니다.", -2, REventRank.SUPERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(영구 지속)");
        player.sendMessage("");
        player.sendMessage("   §f낙하 공격을 할 수 있다니, 얼마나 멋진가요!");
        player.sendMessage("   §f쉬프트를 누르면서 점프 시 강화된 점프를 할 수 있습니다.");
        player.sendMessage("   §7점프 후 좌클릭 시 " + ChatColor.of("#7FFFD4") + "낙하§7를 시작합니다.");
        player.sendMessage("   " + ChatColor.of("#7FFFD4") + "낙하§7가 지속된 시간에 비례해 공격의 파괴력이 증가합니다.");
        player.sendMessage("   " + ChatColor.of("#7FFFD4") + "낙하§7가 지속된 시간에 비례해 고정 피해를 입습니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                break;
            default:
                return;
        }

        if(getIsEnabled(event.getPlayer()) == false)
            return;

        Player player = event.getPlayer();

        if(player.hasMetadata(METADATA_KEY))
            return;
        if(getDistanceFromGround(player) <= 5)
            return;

        player.setMetadata(METADATA_KEY, new FixedMetadataValue(plugin, true));
        new BukkitRunnable() {
            int tick;
            int radius = 4;
            @Override
            public void run() {
                player.setVelocity(new Vector(0, -5, 0).multiply(0.5f));
                if(player.isOnline() == false) {
                    player.removeMetadata(METADATA_KEY, plugin);

                    super.cancel();
                    return;
                }
                // 낙공 해제
                if(player.isOnGround()) {
                    // 이펙트
                    //Location cLoc = player.getLocation();
                    //int radius = 10;

                    //for(int x=cLoc.getBlockX() - radius; x<cLoc.getBlockX() + radius; x++) {
                    //    for(int z=cLoc.getBlockZ() - radius; z<cLoc.getBlockZ() + radius; z++) {
                    //        Location loc = new Location(player.getWorld(), x, cLoc.getBlockY()-1, z);

                    //        if(!(loc.getBlock().equals(Material.AIR)))
                    //            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc,
                    //                3, 0, 1, 0, 40, loc.getBlock().getType().createBlockData());
                    //    }
                    //}

                    //// 엔티티 띄우기
                    //for(Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), radius, 3, radius)) {
                    //    if (entity instanceof LivingEntity) {
                    //        if (entity instanceof Player) {
                    //            if (((Player) entity).equals(player))
                    //                continue;
                    //        }
                    //        entity.setVelocity(new Vector(0, 3, 0).multiply(tick/20f));
                    //    }
                    //}

                    // 폭파
                    player.getWorld().createExplosion(player.getLocation(), 4*(tick/10f));

                    // 낙하 피해
                    Double damage = (double) 2*(tick/20f);

                    if(player.getHealth() > damage)
                        player.setHealth(player.getHealth() - damage);
                    else
                        player.damage(999999);

                    // 메타데이터 삭제
                    player.removeMetadata(METADATA_KEY, plugin);

                    // 소용돌이 모션 해제
                    if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null)
                        makeRiptide(player, false, Bukkit.getOnlinePlayers());

                    super.cancel();
                    return;
                }

                // 소용돌이 모션 사용
                if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null)
                    makeRiptide(player, true, Bukkit.getOnlinePlayers());

                player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 3);
                player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 1);

                if(tick <= 20*2.5)
                    tick++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(getIsEnabled(player) == false)
            return;
        switch (event.getCause()) {
            case FALL:
            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
                break;
            default:
                return;
        }
        if(!(player.hasMetadata(METADATA_KEY)))
            return;

        event.setCancelled(true);
        event.setDamage(0);
        makeRiptide(player, true, Bukkit.getOnlinePlayers());
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();

        if(getIsEnabled(player) == false)
            return;
        if(player.isSneaking() == false)
            return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.setVelocity(new Vector(0, 2, 0));
        }, 5);
    }

    private void makeRiptide(Player player, Boolean state, Collection<? extends Player> targets) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher(player);

        if(state == true)
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(8, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x04);
        else
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(8, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x01);
        packet.getEntityModifier(player.getWorld()).write(0, player);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        try {
            for(Player target : targets) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDistanceFromGround(Player player) {
        Location loc = player.getLocation().clone();
        int distance = 0;

        while (!loc.getBlock().getType().isSolid()) {
            loc.subtract(0, 1, 0);
            distance++;
        }

        return distance;
    }

}
