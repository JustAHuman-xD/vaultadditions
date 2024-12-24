package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.mana.Mana;
import iskallia.vault.mana.ManaAction;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.WorldBorderCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;

import java.util.List;
import java.util.Locale;

public class DebugCommands {
    public DebugCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultadditions")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("debug")
                        .then(Commands.literal("giveMeMyMana")
                                .executes(this::giveMeMyMana)
                        )
                        .then(Commands.literal("configureWorldBorders")
                                .then(Commands.argument("worldBorderSize", DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
                                        .executes(this::configureWorldBorders)
                                )
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

    private int configureWorldBorders(CommandContext<CommandSourceStack> context) {
        double amount = DoubleArgumentType.getDouble(context, "worldBorderSize");

        List<ResourceKey<Level>> levels = List.of(Level.OVERWORLD, Level.NETHER, Level.END);

        for(ResourceKey<Level> level : levels) {
            WorldBorder border = context.getSource().getServer().getLevel(level).getWorldBorder();
            border.setSize(amount);
        }

        context.getSource().sendSuccess(new TranslatableComponent("commands.worldborder.set.immediate", String.format(Locale.ROOT, "%.1f", amount)), true);
        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
