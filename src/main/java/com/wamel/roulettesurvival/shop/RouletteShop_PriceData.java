package com.wamel.roulettesurvival.shop;

import org.bukkit.inventory.ItemStack;

public class RouletteShop_PriceData {

    private RouletteShop_Item item;
    private Integer amount;

    public RouletteShop_PriceData(RouletteShop_Item item, Integer amount) {
        this.item = item;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        ItemStack stack = item.getItemStack();
        return stack;
    }

    public Integer getAmount() {
        return amount;
    }
}
