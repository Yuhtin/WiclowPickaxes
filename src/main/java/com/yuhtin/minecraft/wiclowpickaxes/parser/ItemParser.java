package com.yuhtin.minecraft.wiclowpickaxes.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ColorUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class ItemParser {

    @Inject @Named("main") private Logger logger;

    public List<ItemStack> parseItemCollection(ConfigurationSection section) {

        List<ItemStack> items = new ArrayList<>();
        for (String key : section.getKeys(false)) {

            items.add(
                    parseItemSection(section.getConfigurationSection(key))
            );

        }

        return items;
    }

    public ItemStack parseItemSection(ConfigurationSection section) {

        try {

            ItemBuilder itemBuilder;

            if (section.contains("head")) itemBuilder = new ItemBuilder(section.getString("head"));

            else {

                String material = section.getString("material");

                itemBuilder = new ItemBuilder(
                        Material.valueOf(material),
                        1,
                        (short) section.getInt("data")
                );
            }

            if (section.contains("quantity")) itemBuilder.setAmount(section.getInt("quantity"));
            if (section.contains("glow") && section.getBoolean("glow")) itemBuilder.glow();
            if (section.contains("unbreakable") && section.getBoolean("unbreakable")) itemBuilder.setUnbreakable(true);
            if (section.contains("hideFlags") && section.getBoolean("hideFlags"))
                itemBuilder.addItemFlags(ItemFlag.values());
            if (section.contains("displayName")) itemBuilder.name(ColorUtils.colored(section.getString("displayName")));

            if (section.contains("description")) {

                final List<String> lore = new ArrayList<>();
                for (String description : section.getStringList("description")) {
                    lore.add(ColorUtils.colored(description));
                }

                itemBuilder.lore(lore);
            }

            if (section.contains("enchantments")) {

                for (String enchantments : section.getStringList("enchantments")) {

                    String[] splited = enchantments.split(":");

                    int id = Integer.parseInt(splited[0]);
                    int level = Integer.parseInt(splited[1]);

                    itemBuilder.addEnchantment(Enchantment.getById(id), level, true);

                }

            }

            return itemBuilder.result();

        } catch (Throwable throwable) {

            throwable.printStackTrace();

            this.logger.warning(section.getName() + " is invalid!");
            return null;

        }
    }
}
