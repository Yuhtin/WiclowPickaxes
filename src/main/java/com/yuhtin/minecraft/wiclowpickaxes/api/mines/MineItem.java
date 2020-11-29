package com.yuhtin.minecraft.wiclowpickaxes.api.mines;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
@Builder
public class MineItem {

    private ItemStack itemStack;
    private String permission;
    private List<String> commands;
}
