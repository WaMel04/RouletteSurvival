package com.wamel.roulettesurvival.command.roulette;

import com.wamel.roulettesurvival.command.CMD_Roulette;
import com.wamel.roulettesurvival.util.Roulette;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Roulette_SPlay extends CMD_Roulette {

    public static void run(CommandSender sender, String[] args) {
        if(args.length == 1) {
            sender.sendMessage(PREFIX + "닉네임을 입력하세요.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if(target == null) {
            sender.sendMessage(PREFIX + "§a" + args[1] + "§f(이)라는 이름을 가진 플레이어가 존재하지 않습니다.");
            return;
        }

        Roulette roulette = new Roulette(target);
        roulette.superiorStart();

        sender.sendMessage(PREFIX + "§a" + args[1] + "§f님에게 황금 룰렛을 실행시켰습니다!");
        return;
    }

}
