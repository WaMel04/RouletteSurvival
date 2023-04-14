package com.wamel.roulettesurvival.rouletteevent.list.inferior;

import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventManager;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class REvent_MonsterGatcha extends REventBase {

    private ArrayList<EntityType> mobs = new ArrayList<>();

    public REvent_MonsterGatcha(String name, String description, Integer duration, REventRank rank) {
        super(ChatColor.of("#DC143C") + "§l몬스터 가챠", "랜덤한 몬스터를 소환합니다.", -1, REventRank.INFERIOR);

        mobs.add(EntityType.PIG);
        mobs.add(EntityType.COW);
        mobs.add(EntityType.SHEEP);
        mobs.add(EntityType.ZOMBIE);
        mobs.add(EntityType.SKELETON);
        mobs.add(EntityType.HORSE);
        mobs.add(EntityType.BEE);
        mobs.add(EntityType.SLIME);
        mobs.add(EntityType.PIGLIN_BRUTE);
        mobs.add(EntityType.ZOMBIFIED_PIGLIN);
        mobs.add(EntityType.ENDERMAN);
        mobs.add(EntityType.SILVERFISH);
        mobs.add(EntityType.RABBIT);
        mobs.add(EntityType.TURTLE);
        mobs.add(EntityType.RAVAGER);
        mobs.add(EntityType.VINDICATOR);
        mobs.add(EntityType.ILLUSIONER);
        mobs.add(EntityType.ALLAY);
        mobs.add(EntityType.BLAZE);
        mobs.add(EntityType.VILLAGER);
        mobs.add(EntityType.ZOMBIE_VILLAGER);
        mobs.add(EntityType.AXOLOTL);
        mobs.add(EntityType.IRON_GOLEM);
        mobs.add(EntityType.MULE);
        mobs.add(EntityType.GHAST);
        mobs.add(EntityType.LIGHTNING);
        mobs.add(EntityType.MAGMA_CUBE);
        mobs.add(EntityType.POLAR_BEAR);
        mobs.add(EntityType.ENDERMITE);
        mobs.add(EntityType.WITHER_SKELETON);
        mobs.add(EntityType.CAVE_SPIDER);
        mobs.add(EntityType.SPIDER);
        mobs.add(EntityType.ALLAY);
        mobs.add(EntityType.LLAMA);
        mobs.add(EntityType.WOLF);
        mobs.add(EntityType.CAT);
        mobs.add(EntityType.TURTLE);
        mobs.add(EntityType.DOLPHIN);
        mobs.add(EntityType.CHICKEN);
        mobs.add(EntityType.MUSHROOM_COW);
        mobs.add(EntityType.DOLPHIN);
        mobs.add(EntityType.GUARDIAN);
        mobs.add(EntityType.ELDER_GUARDIAN);
        mobs.add(EntityType.ZOGLIN);
    }

    @Override
    public void onChosenByPlayer(Player player) {
        REventManager.register(player, this);

        player.sendMessage("");
        player.sendMessage("     " + name);
        player.sendMessage("");
        player.sendMessage("   §f몬스터를 가챠로 뽑습니다! 당신의 운을 시험해보세요!");
        player.sendMessage("   §73초 후 행운의 주인공이 결정됩니다.");
        player.sendMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                Random random = new Random();
                EntityType mob = mobs.get(random.nextInt(mobs.size()));

                player.getWorld().spawnEntity(player.getLocation(), mob);
                player.getWorld().strikeLightningEffect(player.getLocation());

                Util.sendTitleWithSound(player, "§e§l축하합니다", "§c" + mob.getName() + "§f을(를) 소환했습니다!", 0, 60, 20, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 1);
                Bukkit.broadcastMessage(PREFIX + "§a" + player.getName() + "§f님이 " + "§c" + mob.getName() + "§f을(를) 소환했습니다!");

                //1초 후 소리 재생
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 100, 1);
                }, 20);
            }
        }.runTaskLater(plugin, 60);
    }

}
