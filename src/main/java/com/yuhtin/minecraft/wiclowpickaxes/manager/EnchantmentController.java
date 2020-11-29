package com.yuhtin.minecraft.wiclowpickaxes.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yuhtin.minecraft.wiclowpickaxes.api.enchantment.CustomEnchantment;
import com.yuhtin.minecraft.wiclowpickaxes.parser.EnchantmentParser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class EnchantmentController {

    private final Map<Enchantment, CustomEnchantment> enchantments = new HashMap<>();

    @Inject private EnchantmentParser enchantmentParser;

    public CustomEnchantment get(Enchantment enchantment) {
        return enchantments.get(enchantment);
    }

    public void register(ConfigurationSection section) {

        for (String key : section.getKeys(false)) {

            enchantments.put(
                    Enchantment.getByName(key),
                    enchantmentParser.parseEnchantmentSection(section.getConfigurationSection(key))
            );
        }

    }

}
