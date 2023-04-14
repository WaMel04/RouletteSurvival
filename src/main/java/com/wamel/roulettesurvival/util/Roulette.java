package com.wamel.roulettesurvival.util;

import com.wamel.roulettesurvival.RouletteSurvival;
import com.wamel.roulettesurvival.data.DataManager_REvent;
import com.wamel.roulettesurvival.data.DataManager_Roulette;
import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import com.wamel.roulettesurvival.rouletteevent.REventBase;
import com.wamel.roulettesurvival.rouletteevent.REventRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class Roulette {

    private static RouletteSurvival plugin = RouletteSurvival.getInstance();

    // 룰렛을 작동시키는 플레이어
    private Player player;
    // 룰렛의 작동 중 여부 | 활성화 상태 중에 다른 룰렛 요청이 들어올시 무시합니다.
    // 일반 룰렛이 돌아가는 속도(주기) | 단위: Tick
    private final Integer INFERIOR_PERIOD = 4;    // 룰렛이 정지할때 까지 걸리는 시간 | 단위: Tick
    // 룰렛이 정지할때 까지 걸리는 시간 | 단위: Tick
    private final Integer INFERIOR_STOP_TIME = 20*5;
    // 특급 룰렛이 돌아가는 속도(주기) | 단위: Tick
    private final Integer SUPERIOR_PERIOD = 2;
    // 룰렛이 정지할때 까지 걸리는 시간 | 단위: Tick
    private final Integer SUPERIOR_STOP_TIME = 20*5;

    // 특급 룰렛이 당첨될 확률 (0~100)
    private final Integer SUPERIOR_CHANCE = 10;

    public Roulette(Player player) {
        this.player = player;
    }

    public void start() {
        if(isEnabled())
            return;

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
        ArrayList<REventBase> list;

        if(DataManager_REvent.enabledREvents.get(rPlayer) == null)
            list = new ArrayList<>();
        else
            list = DataManager_REvent.enabledREvents.get(rPlayer);

        DataManager_Roulette.enabledRoulettes.put(rPlayer, this);

        ArrayList<REventBase> inferiorREvents = new ArrayList<>();

        for(REventBase base : DataManager_REvent.registeredREvents) {
            if(base.getRank().equals(REventRank.INFERIOR))
                inferiorREvents.add(base);
        }

        if(inferiorREvents.size() == list.size()) {
            player.sendMessage(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §e에러: 등록된 모든 룰렛 이벤트가 활성화되어 룰렛을 사용할 수 없습니다!");
            DataManager_Roulette.enabledRoulettes.remove(rPlayer);
            return;
        }

        int inferior_max = inferiorREvents.size();

        Random random = new Random();

        new BukkitRunnable() {
            int inferior_index = random.nextInt(inferior_max);
            int inferior_tick = 0;
            @Override
            public void run() {
                if(inferior_tick < INFERIOR_STOP_TIME) {
                    if(inferior_index >= inferior_max)
                        inferior_index = 0;

                    REventBase displayedREvent = inferiorREvents.get(inferior_index);

                    Util.sendTitleWithSound(player, displayedREvent.getName(), displayedREvent.getDescription(), 0, 10, 0,
                            Sound.BLOCK_NOTE_BLOCK_BIT, 100, 1);

                    inferior_index++;
                } else {
                    if(random.nextInt(100) <= SUPERIOR_CHANCE) { // SUPERIOR
                        DataManager_Roulette.enabledRoulettes.remove(rPlayer);

                        player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 1);
                        superiorStart();

                        super.cancel();
                        return;
                    } else {
                        int select;

                        do {
                            select = random.nextInt(inferior_max);
                        } while(Util.hasREvent(list, inferiorREvents.get(select)) == true);

                        REventBase chosenREvent = inferiorREvents.get(select);

                        //3초 후 룰렛 이벤트 적용
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

                            chosenREvent.onChosenByPlayer(player);
                            DataManager_Roulette.enabledRoulettes.remove(rPlayer);
                        }, 60L);

                        Util.sendTitleWithSound(player, chosenREvent.getName(), chosenREvent.getDescription(), 0, 40, 20,
                                Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                        Util.broadcast(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §a" + player.getName() + "§f님이 " + chosenREvent.getName() + " §f이벤트에 당첨되었습니다!",
                                player);
                        Util.broadcast(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §7설명: " + chosenREvent.getDescription(),
                                player);

                        super.cancel();
                        return;
                    }
                }

                inferior_tick = inferior_tick + INFERIOR_PERIOD;
            }
        }.runTaskTimer(plugin, 0, INFERIOR_PERIOD);
    }

    public void superiorStart() {
        if(isEnabled())
            return;

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
        ArrayList<REventBase> list;

        if(DataManager_REvent.enabledREvents.get(rPlayer) == null)
            list = new ArrayList<>();
        else
            list = DataManager_REvent.enabledREvents.get(rPlayer);

        DataManager_Roulette.enabledRoulettes.put(rPlayer, this);

        ArrayList<REventBase> superiorREvents = new ArrayList<>();

        for(REventBase base : DataManager_REvent.registeredREvents) {
            if(base.getRank().equals(REventRank.SUPERIOR))
                superiorREvents.add(base);
        }

        if(superiorREvents.size() == list.size()) {
            player.sendMessage(ChatColor.of("#8A2BE2") + "[RouletteSurvival] §e에러: 등록된 모든 룰렛 이벤트가 활성화되어 룰렛을 사용할 수 없습니다!");
            DataManager_Roulette.enabledRoulettes.remove(rPlayer);
            return;
        }

        int superior_max = superiorREvents.size();

        new BukkitRunnable() {
            Random random = new Random();
            int superior_index = random.nextInt(superior_max);
            int superior_tick = 0;
            @Override
            public void run() {
                if(superior_tick < SUPERIOR_STOP_TIME) {
                    if(superior_index >= superior_max)
                        superior_index = 0;

                    REventBase displayedREvent = superiorREvents.get(superior_index);

                    Util.sendTitleWithSound(player, "§e§l" + ChatColor.stripColor(displayedREvent.getName()), displayedREvent.getDescription(), 0, 10, 0,
                            Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 2);

                    superior_index++;
                } else {
                    Random random = new Random();
                    int select;

                    do {
                        select = random.nextInt(superior_max);
                    } while(Util.hasREvent(list, superiorREvents.get(select)) == true);

                    REventBase chosenREvent = superiorREvents.get(select);

                    //3초 후 룰렛 이벤트 적용
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

                        chosenREvent.onChosenByPlayer(player);
                        DataManager_Roulette.enabledRoulettes.remove(rPlayer);
                    }, 60L);

                    player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 100, 1);
                    Util.sendTitleWithSound(player, chosenREvent.getName(), chosenREvent.getDescription(), 0, 40, 20,
                            Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
                    Util.broadcast("§e§l[RouletteSurvival] §a" + player.getName() + "§f님이 " + chosenREvent.getName() + " §f이벤트에 당첨되었습니다!",
                            player);
                    Util.broadcast("§e§l[RouletteSurvival] §7설명: " + chosenREvent.getDescription(),
                            player);

                    super.cancel();
                    return;
                }

                superior_tick = superior_tick + SUPERIOR_PERIOD;
            }
        }.runTaskTimer(plugin, 0, SUPERIOR_PERIOD);
    }

    public Boolean isEnabled() {
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(DataManager_Roulette.enabledRoulettes.get(rPlayer) == null)
            return false;
        else
            return true;
    }

    public Player getPlayer() {
        return player;
    }

    public Boolean canStart(REventRank rank) {
        if(rank.equals(REventRank.INFERIOR)) {
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
            ArrayList<REventBase> list;

            if(DataManager_REvent.enabledREvents.get(rPlayer) == null)
                list = new ArrayList<>();
            else
                list = DataManager_REvent.enabledREvents.get(rPlayer);

            ArrayList<REventBase> inferiorREvents = new ArrayList<>();

            for(REventBase base : DataManager_REvent.registeredREvents) {
                if(base.getRank().equals(REventRank.INFERIOR))
                    inferiorREvents.add(base);
            }

            if(inferiorREvents.size() == list.size()) {
                return false;
            }

            return true;
        } else if(rank.equals(REventRank.SUPERIOR)) {
            RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);
            ArrayList<REventBase> list;

            if(DataManager_REvent.enabledREvents.get(rPlayer) == null)
                list = new ArrayList<>();
            else
                list = DataManager_REvent.enabledREvents.get(rPlayer);

            ArrayList<REventBase> superiorREvents = new ArrayList<>();

            for(REventBase base : DataManager_REvent.registeredREvents) {
                if(base.getRank().equals(REventRank.SUPERIOR))
                    superiorREvents.add(base);
            }

            if(superiorREvents.size() == list.size()) {
                return false;
            }

            return true;

        }

        return true;
    }

}
