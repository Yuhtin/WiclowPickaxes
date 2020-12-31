package com.yuhtin.minecraft.wiclowpickaxes.inventories;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ColorUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ItemBuilder;
import com.yuhtin.minecraft.wiclowpickaxes.utils.NBTUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.PickaxeLoreUpdater;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class SelectingPickaxeInventory extends SimpleInventory {

    private static final HashMap<String, Long> playerDelay = new HashMap<>();

    public SelectingPickaxeInventory() {
        super(
                "mine.selectpick",
                "&8Selecione a picareta",
                3 * 9
        );

    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        editor.setItem(13, InventoryItem.of(
                new ItemBuilder(Material.DIAMOND_PICKAXE)
                        .name("&aEvolução de Picareta")
                        .lore("&7Clique na sua picareta para evoluir-la", "&7Caso não tenha uma, clique aqui para conseguir")
                        .result()
        ).defaultCallback(action -> {

            Player player = action.getPlayer();
            if (player.getInventory().firstEmpty() == -1) {

                player.sendMessage(ColorUtils.colored("&cVocê não tem espaço no inventário"));
                return;

            }

            if (playerDelay.containsKey(player.getName()) && playerDelay.get(player.getName()) > System.currentTimeMillis()) {

                player.sendMessage(ColorUtils.colored(
                        "&cVocê precisa aguardar um tempo antes de pegar outra picareta"
                ));
                return;

            }

            ItemStack pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE)
                    .name("&eSuper Picareta")
                    .setUnbreakable(true)
                    .applyMeta()
                    .changeItem(item -> {

                        item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 0);
                        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 0);

                        NBTUtils.updateNbtTag(item, consumer -> consumer.setString("superPickaxe", "1"));

                    })
                    .wrap();

            PickaxeLoreUpdater.updateItemStack(pickaxe);
            player.getInventory().addItem(pickaxe);
            playerDelay.put(player.getName(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3));

        }));

    }
}
