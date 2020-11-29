package com.yuhtin.minecraft.wiclowpickaxes.listener;

import com.henryfabio.minecraft.inventoryapi.event.impl.CustomInventoryClickEvent;
import com.yuhtin.minecraft.wiclowpickaxes.inventories.EvolvePickaxeInventory;
import com.yuhtin.minecraft.wiclowpickaxes.manager.EnchantmentController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ColorUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.NBTUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public class SelectPickaxeListener implements Listener {

    private final PlayerDataController playerDataController;
    private final EnchantmentController enchantmentController;
    private final double percentagePerLevel;

    @EventHandler
    public void onSelectPickaxe(CustomInventoryClickEvent event) {

        ItemStack itemStack = event.getItemStack();
        if (!event.getCustomInventory().getId().equalsIgnoreCase("mine.selectpick")
                || event.getClickedInventory().getType() != InventoryType.PLAYER
                || itemStack == null
                || itemStack.getType() != Material.DIAMOND_PICKAXE) return;

        Player player = event.getPlayer();

        Number superPickaxe = NBTUtils.getNumberFromNbt(itemStack, "superPickaxe");
        if (superPickaxe.intValue() == 0) {

            player.sendMessage(ColorUtils.colored("&cEste item não é uma super picareta."));
            return;

        }

        EvolvePickaxeInventory evolvePickaxeInventory = new EvolvePickaxeInventory(
                playerDataController,
                enchantmentController,
                percentagePerLevel
        ).init();

        evolvePickaxeInventory.openInventory(
                player,
                editor -> editor.getPropertyMap().set("pickaxe", itemStack)
        );

    }
}
