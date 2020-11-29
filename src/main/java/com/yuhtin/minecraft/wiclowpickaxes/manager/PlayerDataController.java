package com.yuhtin.minecraft.wiclowpickaxes.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yuhtin.minecraft.wiclowpickaxes.api.player.PlayerData;
import com.yuhtin.minecraft.wiclowpickaxes.sql.PlayerDataDAO;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
@Data
public class PlayerDataController {

    @Inject private PlayerDataDAO playerDataDAO;

    private final Map<String, PlayerData> players = new HashMap<>();

    public PlayerData get(Player player) {
        PlayerData playerData = this.players.getOrDefault(player.getName(), null);
        if (playerData != null) return playerData;

        playerData = playerDataDAO.findByName(player.getName());
        if (playerData == null) {

            playerData = PlayerData.builder().gemas(0).build();
            this.playerDataDAO.insertPlayer(player.getName(), playerData);

        }

        this.players.put(player.getName(), playerData);
        return playerData;

    }

    public void unload(String name) {
        this.playerDataDAO.updatePlayer(name, this.players.get(name));
    }

    public void unload(Player player) {
        unload(player.getName());
    }

}
