package com.yuhtin.minecraft.wiclowpickaxes.utils;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class PickaxeLoreUpdater {

    public static void updateItemStack(ItemStack itemStack) {

        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getEnchants().forEach((key, value) -> {
            String numural = RomanNumbers.numural(value);
            if (numural.equalsIgnoreCase("")) numural = "0";

            lore.add(ColorUtils.colored(" &f" + key.getName()
                    .replace("DIG_SPEED", "Eficiência").replace("LOOT_BONUS_BLOCKS", "Fortuna")
                    + ": &7" + numural));
        });

        meta.setLore(lore);
        itemStack.setItemMeta(meta);

    }
}
