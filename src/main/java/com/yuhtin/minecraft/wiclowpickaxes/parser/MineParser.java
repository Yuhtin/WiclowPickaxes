package com.yuhtin.minecraft.wiclowpickaxes.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yuhtin.minecraft.wiclowpickaxes.api.mines.MineItem;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class MineParser {

    @Inject private ItemParser itemParser;

    public List<MineItem> parseMineSection(ConfigurationSection section) {
        List<MineItem> items = new ArrayList<>();

        for (String key : section.getKeys(false)) {

            ConfigurationSection keySection = section.getConfigurationSection(key);

            items.add(
                    MineItem.builder()
                            .name(key)
                            .itemStack(itemParser.parseItemSection(keySection.getConfigurationSection("icon")))
                            .permission(keySection.getString("permission"))
                            .commands(keySection.getStringList("executeCommands"))
                            .build()
            );

        }

        return items;
    }


}
