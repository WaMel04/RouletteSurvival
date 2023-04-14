package com.wamel.roulettesurvival;

import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.listener.DropMultipleEvent;
import com.wamel.roulettesurvival.listener.MinecraftEvent;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventFactory;
import com.wamel.roulettesurvival.rouletteevent.REventScoreboardManager;
import com.wamel.roulettesurvival.rouletteevent.REventTimerManager;
import com.wamel.roulettesurvival.rouletteevent.list.superior.REvent_Xray;
import com.wamel.roulettesurvival.shop.RouletteShop;
import com.wamel.roulettesurvival.shop.RouletteShop_DisplayGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class RouletteSurvival extends JavaPlugin {

    private static RouletteSurvival instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        REventFactory.start();
        registerCommands();
        getServer().getPluginManager().registerEvents(new MinecraftEvent(), this);
        getServer().getPluginManager().registerEvents(new DropMultipleEvent(), this);

        getServer().getPluginManager().registerEvents(new RouletteShop(), this);
        getServer().getPluginManager().registerEvents(new RouletteShop_DisplayGUI(), this);

        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            Bukkit.getConsoleSender().sendMessage("§c주의: ProtocolLib 플러그인이 존재하지 않아 패킷 효과가 적용되지 않습니다.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§eProtocolLib 플러그인과의 연동에 성공했습니다.");
        }

    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            REventTimerManager.disApplyAll(player);
            REventScoreboardManager.unRegister(player);

            // 추가 체력
            player.setMaxHealth(20);

            // 다크 템플러
            player.setWalkSpeed(0.2f);

            // x-ray
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
            if(REvent_Xray.xrayMap.containsKey(rPlayer)) {
                HashMap<Location, Integer> blockMap = REvent_Xray.xrayMap.get(rPlayer);
                for(Integer entityId : blockMap.values()) {
                    REvent_Xray.unsetPersonalGlowingBlock(entityId, player);
                }
            }
        }
    }

    public static RouletteSurvival getInstance() {
        return instance;
    }

    private void registerCommands() {
        getCommand("roulette").setExecutor(new CMD_Roulette());
        getCommand("roul").setExecutor(new CMD_Roulette());
    }

    public static void registerREvent(Class base) {
        REventFactory.registerREvent(base);
    }
}
