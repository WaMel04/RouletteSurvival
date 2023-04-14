package com.wamel.roulettesurvival.data;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.rouletteevent.REventBase;

import java.util.ArrayList;
import java.util.HashMap;

public class DataManager_REvent {

    public static ArrayList<REventBase> registeredREvents = new ArrayList<>();
    public static HashMap<RoulettePlayer, ArrayList<REventBase>> enabledREvents = new HashMap<>();
}
