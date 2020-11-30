package com.yuhtin.minecraft.wiclowpickaxes.sql.provider.document.parser.impl;

import com.yuhtin.minecraft.wiclowpickaxes.api.player.PlayerData;
import com.yuhtin.minecraft.wiclowpickaxes.sql.provider.document.Document;
import com.yuhtin.minecraft.wiclowpickaxes.sql.provider.document.parser.DocumentParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerDataDocumentParser implements DocumentParser<PlayerData> {

    @Getter private static final PlayerDataDocumentParser instance = new PlayerDataDocumentParser();

    @Override
    @Nullable
    public PlayerData parse(Document document) {

        if (document.isEmpty()) return null;

        List<String> usedMines = new ArrayList<>();

        String mines = document.getString("mines");
        if (!mines.equalsIgnoreCase("")) {

            if (mines.contains(", ")) Collections.addAll(usedMines, mines.split(", "));
            else usedMines.add(mines);

        }

        return PlayerData.builder()
                .gemas(document.getNumber("gemas").doubleValue())
                .usedMines(usedMines)
                .build();
    }

}
