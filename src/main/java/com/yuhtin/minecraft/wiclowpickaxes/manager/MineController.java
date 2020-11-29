package com.yuhtin.minecraft.wiclowpickaxes.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yuhtin.minecraft.wiclowpickaxes.api.mines.MineItem;
import com.yuhtin.minecraft.wiclowpickaxes.parser.MineParser;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class MineController {

    @Inject private MineParser mineParser;

    @Getter private final List<MineItem> mines = new ArrayList<>();

    public void register(ConfigurationSection section) {
        mines.addAll(mineParser.parseMineSection(section));
    }
}
