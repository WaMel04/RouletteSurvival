package com.wamel.roulettesurvival.util;

import com.wamel.roulettesurvival.shop.RouletteShop_Item;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WeightedRandom {


    public static RouletteShop_Item getRandomElement(List<Pair<RouletteShop_Item, Integer>> target) {
        // 총 가중치 합산
        double totalWeight = 0;

        for(Pair<RouletteShop_Item, Integer> pair : target) {
            totalWeight = totalWeight + pair.right;
        }

        // 가중치 -> 백분율
        List<Pair<RouletteShop_Item, Double>> candidates = new ArrayList<>();
        for(Pair<RouletteShop_Item, Integer> pair : target) {
            Double value = pair.right / totalWeight;
            candidates.add(new Pair<>(pair.left, value));
        }

        // 정렬
        candidates.sort(Comparator.comparingDouble(p -> p.right));

        // 0.0 ~ 1.0 사이의 랜덤 기준점 설정
        final double pivot = Math.random();

        double acc = 0;
        for (Pair<RouletteShop_Item, Double> pair : candidates) {
            acc = acc + pair.right;

            // 누적 가중치 값이 기준점 이상이면 종료
            if (pivot <= acc) {
                return pair.left;
            }
        }

        return null;
    }


}
