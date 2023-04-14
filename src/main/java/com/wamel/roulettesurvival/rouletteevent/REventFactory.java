package com.wamel.roulettesurvival.rouletteevent;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.util.ClassFinder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class REventFactory {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    // com.wamel.roulettesurvival.rouletteevent.list에 생성시 자동으로 등록됨
    public static void start() {
        try {
            JavaPlugin pluginObject = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin(plugin.getName());
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File jarFile = (File) getFileMethod.invoke(pluginObject);

            int i = 0;
            for (Class<?> clazz : ClassFinder.getClasses(jarFile, "com.wamel.roulettesurvival.rouletteevent.list.superior")) {
                registerREvent((Class<? extends REventBase>) clazz);
                i++;
            }
            int j = 0;
            for (Class<?> clazz : ClassFinder.getClasses(jarFile, "com.wamel.roulettesurvival.rouletteevent.list.inferior")) {
                registerREvent((Class<? extends REventBase>) clazz);
                j++;
            }
            Bukkit.broadcastMessage(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §f성공적으로 " + (i + j) + "개의 이벤트를 로드했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void registerREvent(Class<? extends REventBase> rEvent) {
        try {
            Constructor constructor = rEvent.getConstructor(String.class, String.class, Integer.class, REventRank.class);

            Object instance = constructor.newInstance(null, null, null, null);

            if(!(DataManager_REvent.registeredREvents.contains(instance))) {
                DataManager_REvent.registeredREvents.add((REventBase) instance);
                plugin.getServer().getPluginManager().registerEvents((Listener) instance, plugin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
