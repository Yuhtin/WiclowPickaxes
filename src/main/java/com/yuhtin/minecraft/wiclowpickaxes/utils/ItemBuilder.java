package com.yuhtin.minecraft.wiclowpickaxes.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(String url) {
        itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        if (!url.contains("http://textures.minecraft.net/texture/")) meta.setOwner(url);
        else {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = Base64.getEncoder()
                    .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        itemStack.setItemMeta(meta);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int quantity, int data) {
        this.itemStack = new ItemStack(material, quantity, (short) data);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        itemMeta.setDisplayName(ColorUtils.colored(name));
        return this;
    }

    public ItemBuilder changeItem(Consumer<ItemStack> consumer) {

        consumer.accept(itemStack);
        return this;

    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        itemMeta.setLore(ColorUtils.colored(lore));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreRestrictions) {
        itemMeta.addEnchant(enchantment, level, ignoreRestrictions);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder glow() {
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder applyMeta() {

        itemStack.setItemMeta(itemMeta);
        return this;

    }

    public ItemStack result() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack wrap() {
        return itemStack;
    }
}
