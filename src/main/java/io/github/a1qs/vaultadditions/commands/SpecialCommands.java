package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.a1qs.vaultadditions.init.ModBlocks;
import io.github.a1qs.vaultadditions.item.LootStatueBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SpecialCommands {
    public SpecialCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("grantLootStatue")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.argument("PlayerName", StringArgumentType.string())
                        .executes(this::grantLootStatue)
                )

        );
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    private int grantLootStatue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "PlayerName");
        ItemStack statue = new ItemStack(ModBlocks.LOOT_STATUE.get());
        LootStatueBlockItem.setStatueName(statue, playerName);
        ServerPlayer player = context.getSource().getPlayerOrException();

        boolean added = player.getInventory().add(statue);

        if (!added || !statue.isEmpty()) {
            // Drop the item if it couldn't be fully added
            player.drop(statue, false);
        }
        context.getSource().sendSuccess(new TextComponent("You've been granted a Loot Statue").withStyle(ChatFormatting.GREEN), true);
        return 0;
    }
}
