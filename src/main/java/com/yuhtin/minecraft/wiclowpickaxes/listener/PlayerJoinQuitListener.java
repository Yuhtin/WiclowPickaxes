package com.yuhtin.minecraft.wiclowpickaxes.listener;

import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public class PlayerJoinQuitListener implements Listener {

    private final PlayerDataController playerDataController;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        this.playerDataController.get(event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        this.playerDataController.unload(event.getPlayer());

    }

}

