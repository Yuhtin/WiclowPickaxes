package com.yuhtin.minecraft.wiclowpickaxes.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public final class NBTUtils {

    private NBTUtils() { throw new IllegalStateException("Utility class"); }

    public static void updateNbtTag(ItemStack itemStack, Consumer<NBTTagCompound> compoundConsumer) {
        final net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound tagCompound = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();

        compoundConsumer.accept(tagCompound);
        nmsItemStack.setTag(tagCompound);

        final ItemMeta itemMeta = CraftItemStack.getItemMeta(nmsItemStack);
        itemStack.setItemMeta(itemMeta);
    }

    public static Number getNumberFromNbt(ItemStack itemStack, String field) {

        final net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItemStack == null) return 0;

        NBTTagCompound tagCompound = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();

        if (!tagCompound.hasKey(field)) return 0;

        return NumberUtils.createNumber(tagCompound.getString(field));
    }

}
