package com.wamel.roulettesurvival.listener;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.command.roulette.CMD_Roulette_Stop;
import com.wamel.roulettesurvival.data.DataManager_Config;
import com.wamel.roulettesurvival.data.DataManager_Game;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.data.DataManager_Roulette;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import com.wamel.roulettesurvival.rouletteevent.REventScoreboardManager;
import com.wamel.roulettesurvival.rouletteevent.REventTimerManager;
import com.wamel.roulettesurvival.shop.RouletteShop;
import com.wamel.roulettesurvival.util.BarTimer;
import com.wamel.roulettesurvival.util.Roulette;
import com.wamel.roulettesurvival.util.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MinecraftEvent implements Listener {

    private RouletteSurvival plugin = RouletteSurvival.getInstance();

    // 기본 설정
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        REventTimerManager.applyAll(player);
        REventScoreboardManager.refresh(player);
        BarTimer.applyAll(player);

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        // 초기 설정
        player.setWalkSpeed(0.2f);
        if(DataManager_REvent.enabledREvents.get(rPlayer) == null)
            player.setMaxHealth(20);
        else if(!(Util.hasREvent(DataManager_REvent.enabledREvents.get(rPlayer), "§d§l추가 체력")))
            player.setMaxHealth(20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
    }

    // 상점 열기
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if(!(event.getPlayer().isSneaking()))
            return;

        event.setCancelled(true);
        RouletteShop.open(event.getPlayer());
    }

    // 폭죽 피해 제거
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager().hasMetadata("roul_nodamage")) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }

    // 게임 관련
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(DataManager_Game.isEnabled == false)
            return;

        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.getDrops().clear();
        event.setDroppedExp(0);

        Player player = event.getPlayer();
        player.getWorld().getChunk
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        Integer lifes;

        if(!(DataManager_Game.playerLifesMap.containsKey(rPlayer))) {
            DataManager_Game.playerLifesMap.put(rPlayer, DataManager_Config.PLAYER_LIFES - 1);
            lifes = DataManager_Config.PLAYER_LIFES - 1;
        } else {
            lifes = DataManager_Game.playerLifesMap.get(rPlayer) - 1;
            DataManager_Game.playerLifesMap.put(rPlayer, lifes);
        }

        if(lifes <= 0) {
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §b" + player.getName() + "§c님이 탈락했습니다!");
            return;
        } else {
            Bukkit.broadcastMessage(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §b" + player.getName() + "§f님의 남은 목숨: §c" + lifes);
            return;
        }
    }

    @EventHandler
    public void onConquerDragon(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof EnderDragon))
            return;
        if(!(event.getDamager() instanceof Player))
            return;
        if(DataManager_Game.isEnabled == false)
            return;

        Player player = (Player) event.getDamager();

        if(((EnderDragon) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                Util.sendTitleWithSound(player1, ChatColor.of("#8A2BE2") + "Roulette Survival", "§b" + player.getName() + "§f님이 게임에서 승리하셨습니다!",
                        0, 100, 20, Sound.ITEM_TOTEM_USE, 100, 1);
            }

            CMD_Roulette_Stop.run();

            new BukkitRunnable() {
                int tick = 0;
                @Override
                public void run() {
                    if(tick >= 15*10) {
                        this.cancel();
                        return;
                    }

                    Util.spreadFirework(player);

                    tick = tick + 15;
                }
            }.runTaskTimer(plugin, 0, 15);
            return;
        }
    }

    // 룰렛 사용권
    public static final String ROULETTE_ITEM_NAME = ChatColor.of("#8A2BE2") + "§l룰렛 사용권";
    public static final String SUPERIOR_ROULETTE_ITEM_NAME = "§e§l황금 룰렛 사용권";

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(!(event.getAction().isRightClick()))
            return;
        if(event.getItem() == null)
            return;
        if(event.getItem().getItemMeta().getDisplayName() == null)
            return;

        Player player = event.getPlayer();

        if(event.getItem().getItemMeta().getDisplayName().equals(ROULETTE_ITEM_NAME)) {
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

            if(DataManager_Roulette.enabledRoulettes.containsKey(rPlayer)) {
                player.sendActionBar("이미 룰렛을 사용 중입니다");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 100, 0.5f);
                return;
            }

            Roulette roulette = new Roulette(player);

            if(roulette.canStart(REventRank.INFERIOR) == false) {
                player.sendActionBar("§c모든 룰렛 이벤트가 활성화되어 룰렛을 사용할 수 없습니다!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 100, 0.5f);
                DataManager_Roulette.enabledRoulettes.remove(rPlayer);
                return;
            }

            ItemStack stack = event.getItem().clone();
            stack.setAmount(1);

            player.getInventory().removeItem(stack);

            roulette.start();
            return;
        } else if(event.getItem().getItemMeta().getDisplayName().equals(SUPERIOR_ROULETTE_ITEM_NAME)) {
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

            if(DataManager_Roulette.enabledRoulettes.containsKey(rPlayer)) {
                player.sendActionBar("이미 룰렛을 사용 중입니다");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 100, 0.5f);
                return;
            }

            Roulette roulette = new Roulette(player);
            if(roulette.canStart(REventRank.SUPERIOR) == false) {
                player.sendActionBar("§c모든 룰렛 이벤트가 활성화되어 룰렛을 사용할 수 없습니다!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 100, 0.5f);
                DataManager_Roulette.enabledRoulettes.remove(rPlayer);
                return;
            }

            ItemStack stack = event.getItem().clone();
            stack.setAmount(1);

            player.getInventory().removeItem(stack);

            roulette.superiorStart();
            return;
        }
    }
}
