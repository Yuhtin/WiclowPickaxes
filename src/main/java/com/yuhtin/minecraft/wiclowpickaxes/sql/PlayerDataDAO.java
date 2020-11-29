package com.yuhtin.minecraft.wiclowpickaxes.sql;

import com.google.inject.Singleton;
import com.yuhtin.minecraft.wiclowpickaxes.api.player.PlayerData;
import com.yuhtin.minecraft.wiclowpickaxes.sql.provider.DatabaseProvider;
import com.yuhtin.minecraft.wiclowpickaxes.sql.provider.document.parser.impl.PlayerDataDocumentParser;

import javax.annotation.Nullable;

@Singleton
public class PlayerDataDAO extends DatabaseProvider {

    public void createTable() {
        update("create table if not exists `wiclow_gemas` ("
                + "`name` char(16) not null, "
                + "`gemas` double not null"
                + ");");
    }

    @Nullable
    public PlayerData findByName(String name) {
        return query("select * from `wiclow_gemas` where `name` = ?", name)
                .parse(PlayerDataDocumentParser.getInstance());
    }

    public void insertPlayer(String name, PlayerData playerData) {
        update("insert into `wiclow_gemas` values (?, ?);",
                name,
                playerData.getGemas()
        );
    }

    public void updatePlayer(String name, PlayerData playerData) {
        update("update `wiclow_gemas` set `gemas` = ? where `name` = ?",
                playerData.getGemas(),
                name
        );

    }

}
