package com.yuhtin.minecraft.wiclowpickaxes.inventories;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.yuhtin.minecraft.wiclowpickaxes.api.enchantment.CustomEnchantment;
import com.yuhtin.minecraft.wiclowpickaxes.api.player.PlayerData;
import com.yuhtin.minecraft.wiclowpickaxes.manager.EnchantmentController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ColorUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ItemBuilder;
import com.yuhtin.minecraft.wiclowpickaxes.utils.MathUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.PickaxeLoreUpdater;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class EvolvePickaxeInventory extends SimpleInventory {

    private final PlayerDataController playerDataController;
    private final EnchantmentController enchantmentController;
    private final double percentagePerLevel;

    public EvolvePickaxeInventory(PlayerDataController playerDataController,
                                  EnchantmentController enchantmentController,
                                  double percentagePerLevel) {
        super(
                "mine.evolvepick",
                "&8Evoluindo picareta",
                3 * 9
        );

        this.playerDataController = playerDataController;
        this.enchantmentController = enchantmentController;
        this.percentagePerLevel = percentagePerLevel;
    }

    private static String translate(String name) {
        switch (name) {
            case "DIG_SPEED":
                return "Eficiência";
            case "LOOT_BONUS_BLOCKS":
                return "Fortuna";
            default:
                return name;
        }
    }

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {

        configureInventory(viewer, editor);

    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        ItemStack pickaxe = viewer.getPropertyMap().get("pickaxe");

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);

        int i = 12;
        for (Enchantment enchantment : pickaxe.getEnchantments().keySet()) {
            CustomEnchantment customEnchantment = enchantmentController.get(enchantment);
            if (customEnchantment == null || customEnchantment.getMaxLevel() == 0) continue;

            Player player = viewer.getPlayer();
            PlayerData playerData = this.playerDataController.get(player);

            int level = pickaxe.getEnchantments().get(enchantment);
            double evolve = customEnchantment.getMaxLevel() < level + 1
                    ? 0
                    : customEnchantment.getPrice() * (1 + (level * percentagePerLevel));

            if (evolve == 0) {

                editor.setItem(i, InventoryItem.of(
                        new ItemBuilder("http://textures.minecraft.net/texture/e9e4bdcf172d5dc77c2bd4e37ad985399a9f2cdebf72463929ea4b666ef6f80")
                                .name("&a" + translate(enchantment.getName()))
                                .lore("&fVocê chegou no level máximo deste encantamento &a(" + customEnchantment.getMaxLevel() + ")")
                                .result()
                ));

            } else if (evolve > playerData.getGemas()) {

                editor.setItem(i, InventoryItem.of(
                        new ItemBuilder(Material.BARRIER)
                                .name("&c" + translate(enchantment.getName()))
                                .lore("&fVocê precisa de &c" + MathUtils.format(evolve) + " gemas &fpara isto")
                                .result()
                ));

            } else {

                editor.setItem(i, InventoryItem.of(
                        new ItemBuilder(customEnchantment.getItemStack())
                                .name(ColorUtils.colored("&3" + translate(enchantment.getName())))
                                .lore(" &f" + customEnchantment.getDescription(),
                                        "",
                                        " &b Nível atual: &f" + level,
                                        " &b Próximo nível: &f" + (evolve == 0 ? "&cMáximo" : level + 1),
                                        " &b Nível máximo: &f" + customEnchantment.getMaxLevel(),
                                        "",
                                        " &bCustará &f" + MathUtils.format(evolve) + " &bgemas.",
                                        " &bClique para evoluir.")
                                .result()
                        ).defaultCallback(action -> {

                            if (viewer.getPropertyMap().get("inHand") != null && !pickaxe.isSimilar(player.getItemInHand()))
                                return;

                            pickaxe.addUnsafeEnchantment(enchantment, level + 1);
                            PickaxeLoreUpdater.updateItemStack(pickaxe);

                            player.sendMessage(ColorUtils.colored(
                                    "&aVocê evoluiu este encantamento com sucesso"
                            ));

                            playerData.setGemas(playerData.getGemas() - evolve);

                            if (viewer.getPropertyMap().get("inHand") != null) player.setItemInHand(pickaxe);

                            action.updateInventory();

                        })
                );
            }

            i = 14;
        }

    }
}
