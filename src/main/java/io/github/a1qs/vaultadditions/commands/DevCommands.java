package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import io.github.a1qs.vaultadditions.data.EventData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Map;

public class DevCommands {
    public DevCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultadditions")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("dev")
                        .then(Commands.literal("stopEvent")
                                .executes(this::testConfig)
                        )

                )
        );
    }

    private int testConfig(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(0).value.getParsedMessage(), true);
        return 0;
    }

    private int getRequiredPermissionLevel() {
        return 2;
    }
}
