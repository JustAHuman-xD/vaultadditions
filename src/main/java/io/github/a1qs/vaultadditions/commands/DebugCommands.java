package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.mana.Mana;
import iskallia.vault.mana.ManaAction;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class DebugCommands {
    public DebugCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultadditions")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("debug")
                        .then(Commands.literal("giveMeMyMana")
                                .executes(this::giveMeMyMana)
                        )

                )
        );
    }

    private int giveMeMyMana(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Mana.increase(player, ManaAction.PLAYER_ACTION, Integer.MAX_VALUE);
        context.getSource().sendSuccess(new TextComponent("Added " + Integer.MAX_VALUE + " Mana!").withStyle(ChatFormatting.AQUA), true);



        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
