package com.yuhtin.minecraft.wiclowpickaxes.api.player;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
@Builder
public class PlayerData {

    private double gemas;
    private List<String> usedMines;
}
