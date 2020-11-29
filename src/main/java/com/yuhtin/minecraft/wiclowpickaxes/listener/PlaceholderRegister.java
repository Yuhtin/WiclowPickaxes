package com.yuhtin.minecraft.wiclowpickaxes.listener;

import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.utils.MathUtils;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public class PlaceholderRegister extends PlaceholderExpansion {

    private final PlayerDataController playerDataController;

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "wiclowpickaxes";
    }

    @Override
    public String getAuthor() {
        return "Yuhtin";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (params.equalsIgnoreCase("gemas")) return MathUtils.format(playerDataController.get(player).getGemas());

        return "";
    }
}
