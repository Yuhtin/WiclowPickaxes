package com.yuhtin.minecraft.wiclowpickaxes.api.enchantment;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
@Builder
public class CustomEnchantment {

    private final ItemStack itemStack;
    private final String description;
    private final int maxLevel;
    private final double price;

}
