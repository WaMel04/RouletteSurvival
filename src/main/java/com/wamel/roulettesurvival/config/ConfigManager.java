package com.wamel.roulettesurvival.config;

import com.wamel.roulettesurvival.RouletteSurvival;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private static final RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static Object get(String key) {
        File file = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        return yaml.get(key);
    }

}
