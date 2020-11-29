package com.yuhtin.minecraft.wiclowpickaxes.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yuhtin.minecraft.wiclowpickaxes.api.enchantment.CustomEnchantment;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class EnchantmentParser {

    @Inject private ItemParser itemParser;

    public CustomEnchantment parseEnchantmentSection(ConfigurationSection section) {

        return CustomEnchantment.builder()
                .itemStack(itemParser.parseItemSection(section.getConfigurationSection("icon")))
                .description(section.getString("description"))
                .maxLevel(section.getInt("maxLevel"))
                .price(section.getDouble("price"))
                .build();

    }

}
