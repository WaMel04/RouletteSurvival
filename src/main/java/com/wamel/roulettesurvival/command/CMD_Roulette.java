package com.wamel.roulettesurvival.command;

import com.wamel.roulettesurvival.command.roulette.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CMD_Roulette implements CommandExecutor {

    protected static final String PREFIX = ChatColor.of("#8A2BE2") + "[RouletteSurvival] §f";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        switch(cmd.getName().toLowerCase()) {
            case "roulette":
            case "roul":
                break;
            default:
                return false;
        }

        if(!(sender.isOp())) {
            sender.sendMessage("§c권한이 부족합니다.");
            return false;
        }
        if(args.length == 0) {
            sender.sendMessage("");
            sender.sendMessage("§5   /" + label + " start §7- 룰렛 서바이벌을 시작합니다.");
            sender.sendMessage("§5   /" + label + " stop §7- 룰렛 서바이벌을 종료합니다.");
            sender.sendMessage("");
            sender.sendMessage("§5   /" + label + " play [닉네임] §7- 룰렛을 실행시킵니다.");
            sender.sendMessage("§5   /" + label + " splay [닉네임] §7- 황금 룰렛을 실행시킵니다.");
            sender.sendMessage("");
            sender.sendMessage("§5   /" + label + " reload §7- config를 리로드합니다.");
            sender.sendMessage("§5   /" + label + " select [닉네임] [이름] §7- 해당 이름을 포함한 룰렛 이벤트 발동! §c#개발자 명령어");
            sender.sendMessage("§5   /" + label + " test §7 테스트!! §c#개발자 명령어");
            sender.sendMessage("");
            return false;
        }
        switch(args[0].toLowerCase()) {
            case "start":
                CMD_Roulette_Start.run();
                return false;
            case "stop":
                CMD_Roulette_Stop.run();
                return false;
            case "play":
                CMD_Roulette_Play.run(sender, args);
                return false;
            case "splay":
                CMD_Roulette_SPlay.run(sender, args);
                return false;
            case "reload":
                CMD_Roulette_Reload.run(sender);
                return false;
            case "select":
                CMD_Roulette_Select.run(sender, args);
                return false;
            case "test":
                CMD_Roulette_Test.run(sender);
                return false;
            default:
                return false;
        }

    }

}
