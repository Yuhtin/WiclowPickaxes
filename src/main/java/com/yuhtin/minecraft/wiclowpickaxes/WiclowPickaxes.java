package com.yuhtin.minecraft.wiclowpickaxes;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.yuhtin.minecraft.wiclowpickaxes.command.GemasCommand;
import com.yuhtin.minecraft.wiclowpickaxes.listener.PlaceholderRegister;
import com.yuhtin.minecraft.wiclowpickaxes.listener.PlayerJoinQuitListener;
import com.yuhtin.minecraft.wiclowpickaxes.listener.SelectPickaxeListener;
import com.yuhtin.minecraft.wiclowpickaxes.manager.EnchantmentController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.MineController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.sql.PlayerDataDAO;
import com.yuhtin.minecraft.wiclowpickaxes.sql.connection.SQLConnection;
import com.yuhtin.minecraft.wiclowpickaxes.sql.connection.mysql.MySQLConnection;
import com.yuhtin.minecraft.wiclowpickaxes.sql.connection.sqlite.SQLiteConnection;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class WiclowPickaxes extends JavaPlugin {

    private Injector injector;
    private SQLConnection sqlConnection;

    @Inject private PlayerDataDAO playerDataDAO;
    @Inject private PlayerDataController playerDataController;
    @Inject private MineController mineController;
    @Inject private EnchantmentController enchantmentController;

    public static WiclowPickaxes getInstance() {
        return JavaPlugin.getPlugin(WiclowPickaxes.class);
    }

    @Override
    public void onLoad() {

        saveDefaultConfig();

    }

    @Override
    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();
        if (!configureSQLConnection()) {

            pluginManager.disablePlugin(this);
            return;

        }

        this.injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {

                bind(WiclowPickaxes.class).toInstance(getInstance());
                bind(Logger.class).annotatedWith(Names.named("main")).toInstance(getLogger());
                bind(SQLConnection.class).toInstance(sqlConnection);

            }
        });

        this.injector.injectMembers(this);
        this.getLogger().info("Membros registrados com sucesso");

        this.playerDataDAO.createTable();

        BukkitFrame bukkitFrame = new BukkitFrame(this);

        GemasCommand gemasCommand = new GemasCommand(
                playerDataController,
                mineController
        );

        bukkitFrame.registerCommands(gemasCommand);

        this.getLogger().info("Comandos registrados com sucesso");

        PlayerJoinQuitListener playerJoinQuitListener = new PlayerJoinQuitListener(playerDataController);
        SelectPickaxeListener selectPickaxeListener = new SelectPickaxeListener(
                playerDataController,
                enchantmentController,
                this.getConfig().getDouble("PercentagePerLevel")
        );

        pluginManager.registerEvents(playerJoinQuitListener, this);
        pluginManager.registerEvents(selectPickaxeListener, this);

        this.getLogger().info("Eventos registrados com sucesso");

        InventoryManager.enable(this);

        this.getLogger().info("Inventários registrados com sucesso");

        this.mineController.register(this.getConfig().getConfigurationSection("mines"));
        this.enchantmentController.register(this.getConfig().getConfigurationSection("enchantment"));

        this.getLogger().info("Minas e encantamentos registrados com sucesso");

        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {

            PlaceholderRegister placeholderRegister = new PlaceholderRegister(playerDataController);
            placeholderRegister.register();

            this.getLogger().info("Placeholder registrada com sucesso");

        }

    }

    @Override
    public void onDisable() {

        this.playerDataController.getPlayers().keySet().forEach(this.playerDataController::unload);
        this.getLogger().info("Plugin desligado com sucesso");

    }

    private boolean configureSQLConnection() {
        ConfigurationSection connectionSection = getConfig().getConfigurationSection("connection");
        this.sqlConnection = new MySQLConnection();

        boolean sucess = sqlConnection.configure(connectionSection.getConfigurationSection("mysql"));
        if (!sucess) {

            this.sqlConnection = new SQLiteConnection();
            return this.sqlConnection.configure(connectionSection.getConfigurationSection("sqlite"));

        }

        return true;
    }
}
