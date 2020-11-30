package com.yuhtin.minecraft.wiclowpickaxes.command;

import com.henryfabio.minecraft.inventoryapi.inventory.CustomInventory;
import com.yuhtin.minecraft.wiclowpickaxes.api.player.PlayerData;
import com.yuhtin.minecraft.wiclowpickaxes.inventories.MineInventory;
import com.yuhtin.minecraft.wiclowpickaxes.manager.EnchantmentController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.MineController;
import com.yuhtin.minecraft.wiclowpickaxes.manager.PlayerDataController;
import com.yuhtin.minecraft.wiclowpickaxes.utils.ColorUtils;
import com.yuhtin.minecraft.wiclowpickaxes.utils.MathUtils;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public class GemasCommand {

    private final PlayerDataController playerDataController;
    private final MineController mineController;

    @Command(name = "gemas", target = CommandTarget.PLAYER)
    public void gemasCommand(Context<Player> context) {

        Player player = context.getSender();
        player.sendMessage(ColorUtils.colored(
                "&fVocê possui &d" + MathUtils.format(this.playerDataController.get(player).getGemas()) + " gemas"
        ));

    }

    @Command(name = "picareta", aliases = {"mina", "pick", "minas"}, target = CommandTarget.PLAYER)
    public void pickaxeCommand(Context<Player> context) {

        CustomInventory inventory = new MineInventory(
                playerDataController,
                mineController
        ).init();

        inventory.openInventory(context.getSender());

    }

    @Command(name = "givegemas", permission = "wiclowpickaxes.givegemas", target = CommandTarget.ALL)
    public void giveGemasCommand(Context<CommandSender> context,
                                 Player target,
                                 double quantity) {

        PlayerData playerData = playerDataController.get(target);
        playerData.setGemas(playerData.getGemas() + quantity);

        context.getSender().sendMessage(ColorUtils.colored(
                "&aVocê adicionou &2" + MathUtils.format(quantity) + " gemas &apara o jogador " + target.getName()
        ));

    }
}
