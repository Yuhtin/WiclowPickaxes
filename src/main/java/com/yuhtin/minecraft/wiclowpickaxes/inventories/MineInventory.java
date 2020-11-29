package com.yuhtin.minecraft.wiclowpickaxes.inventories;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.minecraft.wiclowpickaxes.api.mines.MineItem;
import com.yuhtin.minecraft.wiclowpickaxes.manager.EnchantmentController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.MineController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ItemBuilder;
import com.yuhtin.minecraft.wiclowpickaxes.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class MineInventory extends PagedInventory {

    private final PlayerDataController playerDataController;
    private final MineController mineController;

    public MineInventory(PlayerDataController playerDataController,
                         MineController mineController) {
        super(
                "mine.main",
                "&8Minas",
                3 * 9
        );

        this.playerDataController = playerDataController;
        this.mineController = mineController;

    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        editor.setItem(0, InventoryItem.of(
                new ItemBuilder(viewer.getPlayer().getName())
                        .name("&eSeu perfil")
                        .lore("&fGemas: &6" + MathUtils.format(playerDataController.get(viewer.getPlayer()).getGemas()))
                        .result()
                )
        );

        editor.setItem(8, InventoryItem.of(
                new ItemBuilder(Material.DIAMOND_PICKAXE)
                        .name("&eEvoluir picareta")
                        .lore("&7Clique aqui para evoluir sua picareta")
                        .result()
        ).defaultCallback(action -> {

            SelectingPickaxeInventory selectingPickaxeInventory = new SelectingPickaxeInventory().init();
            selectingPickaxeInventory.openInventory(action.getPlayer());

        }));

    }

    @Override
    protected void configureViewer(PagedViewer viewer) {

        ViewerConfigurationImpl.Paged configuration = viewer.getConfiguration();
        configuration.itemPageLimit(7);
        configuration.nextPageSlot(17);
        configuration.previousPageSlot(9);
        configuration.emptyPageSlot(13);

    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        List<InventoryItemSupplier> items = new ArrayList<>();

        for (MineItem mine : mineController.getMines()) {

            if (!viewer.getPlayer().hasPermission(mine.getPermission())) continue;

            items.add(() -> InventoryItem.of(mine.getItemStack())
                    .defaultCallback(callBack -> {

                        viewer.getPlayer().closeInventory();
                        for (String command : mine.getCommands()) {

                            Bukkit.dispatchCommand(
                                    Bukkit.getConsoleSender(),
                                    command.replace("%player%", viewer.getPlayer().getName())
                            );

                        }

                    }));
        }

        return items;
    }
}
