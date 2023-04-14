package com.wamel.roulettesurvival.command.roulette;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Roulette_Select extends CMD_Roulette {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    public static void run(CommandSender sender, String[] args) {
        if(args.length == 1) {
            sender.sendMessage(PREFIX + "닉네임을 입력하세요.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if(target == null) {
            sender.sendMessage(PREFIX + "§c" + args[1] + "§f(이)라는 이름을 가진 플레이어가 존재하지 않습니다.");
            return;
        }

        if(args.length == 2) {
            sender.sendMessage(PREFIX + "룰렛 이벤트의 이름을 입력하세요.");
            return;
        }

        for(REventBase revent : DataManager_REvent.registeredREvents) {
            if(ChatColor.stripColor(revent.getName()).toLowerCase().contains(args[2].toLowerCase())) {
                revent.onChosenByPlayer(target);

                sender.sendMessage(PREFIX + "§a" + args[1] + "§f님에게 §e" + revent.getName() + " §f이벤트를 실행했습니다.");
                return;
            }
        }

        sender.sendMessage(PREFIX + "§c" + args[2] + "§f(이)라는 이름을 가진 룰렛 이벤트가 존재하지 않습니다.");
        return;
    }

}
