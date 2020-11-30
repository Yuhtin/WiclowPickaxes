package com.yuhtin.minecraft.wiclowpickaxes.inventories;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.minecraft.wiclowpickaxes.api.mines.MineItem;
import com.yuhtin.minecraft.wiclowpickaxes.manager.MineController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ItemBuilder;
import com.yuhtin.minecraft.wiclowpickaxes.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                        .lore("&fGemas: &d" + MathUtils.format(playerDataController.get(viewer.getPlayer()).getGemas()))
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

        List<MineItem> mines = new ArrayList<>(mineController.getMines())
                .stream()
                .filter(mineItem -> !playerDataController.get(viewer.getPlayer()).getUsedMines().contains(mineItem.getName()))
                .collect(Collectors.toList());

        for (MineItem mine : mines) {

            if (!viewer.getPlayer().hasPermission(mine.getPermission())) continue;

            items.add(() -> InventoryItem.of(mine.getItemStack())
                    .defaultCallback(callBack -> {
                        
                        for (String command : mine.getCommands()) {

                            Bukkit.dispatchCommand(
                                    Bukkit.getConsoleSender(),
                                    command.replace("%player%", viewer.getPlayer().getName())
                            );

                        }

                        this.playerDataController.get(viewer.getPlayer()).getUsedMines().add(mine.getName());

                        callBack.updateInventory();

                    }));
        }

        return items;
    }
}
