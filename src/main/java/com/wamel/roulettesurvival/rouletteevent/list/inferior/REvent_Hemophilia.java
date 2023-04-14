package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class REvent_Hemophilia extends REventBase {

    private final double BLOOD_DAMAGE = 0.5;

    public REvent_Hemophilia(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#DC143C") + "§l혈우병", "피해를 입을시 출혈 상태에 걸립니다.", 300, REventRank.INFERIOR);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name + " §7(지속시간: " + duration + "초)");
        player.sendMessage("");
        player.sendMessage("   §f야생을 좀 더 현실적으로 바꿉니다.");
        player.sendMessage("   §7물리 피해를 입을시 " + ChatColor.of("#DC143C") + "출혈§f에 걸리게 되며, 4초 동안 " + "§4♥ x " + "0.5의 §4고정 피해§f를 입습니다.");
        player.sendMessage("   §7* 이미 출혈 중인 상태에선 다시 출혈 상태에 걸리지 않습니다.");
        player.sendMessage("");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        if(event.getDamage() == 0 || event.isCancelled())
            return;

        Player player = (Player) event.getEntity();

        if(getIsEnabled(player) == false)
            return;
        if(player.hasMetadata("roul_isBlooding"))
            return;
        switch (event.getCause()) {
            case ENTITY_ATTACK:
            case ENTITY_SWEEP_ATTACK:
            case FALL:
            case PROJECTILE:
            case THORNS:
                break;
            default:
                return;
        }

        player.setMetadata("roul_isBlooding", new FixedMetadataValue(plugin, true));
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null)
            sendRedScreen(player, 0.99, 20*4);

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 50, 0, 1, 0, 40, Material.REDSTONE_BLOCK.createBlockData());
                player.getWorld().playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT, 5, 1);
                count++;
                if(count>=4) {
                    if(player.getHealth() > 1)
                        player.setHealth(player.getHealth()-1);
                    else
                        player.damage(999999);

                    player.removeMetadata("roul_isBlooding", plugin);
                    super.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    private void setRedScreen(Player player, double x, double z, Double oldDiam, Double newDiam, long speed, int warnDis, int warnTime) {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.INITIALIZE_BORDER);
        packet.getWorldBorderActions().writeSafely(0, EnumWrappers.WorldBorderAction.INITIALIZE);
        packet.getDoubles().write(0, x);
        packet.getDoubles().write(1, z);
        packet.getDoubles().write(2, oldDiam);
        packet.getDoubles().write(3, newDiam);
        packet.getLongs().write(0, speed);
        packet.getIntegers().write(0, 29999984); // 고정값
        packet.getIntegers().write(1, warnDis);
        packet.getIntegers().write(2, warnTime);

        try {
            pm.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 플레이어의 화면을 붉게 물들인다.
    // percentage에 따라 세기 변경 가능. 0.01 ~ 0.99 사이의 값을 가짐.
    private void sendRedScreen(Player player, Double percentage, int tick) {
        setRedScreen(player, 0, 0, 0D, 30000000D, 0,
                (int) (30000000/(2-2*percentage)), 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                removeRedScreen(player);
            }
        }.runTaskLater(plugin, tick);
    }

    private void removeRedScreen(Player player) {
        setRedScreen(player, 0, 0, 0D, 30000000D, 0, 5, 0);
    }

}
