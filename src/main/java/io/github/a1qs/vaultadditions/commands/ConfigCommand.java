package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ConfigCommand {
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public ConfigCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultadditions")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("config")
                        .then(Commands.literal("setBorderShardIncrease")
                                .then(Commands.argument("increaseAmount", IntegerArgumentType.integer())
                                        .executes(this::setShardIncrease)
                                )
                        )
                        .then(Commands.literal("setNetherBorderMultiplier")
                                .then(Commands.argument("increaseAmount", IntegerArgumentType.integer())
                                        .executes(this::setNetherBorderMultiplier)
                                )
                        )
                        .then(Commands.literal("setEndBorderMultiplier")
                                .then(Commands.argument("increaseAmount", IntegerArgumentType.integer())
                                        .executes(this::setEndBorderMultiplier)
                                )
                        )
                        .then(Commands.literal("setWorldBorderTimeLimit")
                                .then(Commands.argument("timeFormat", StringArgumentType.string())
                                        .executes(this::setWorldBorderTimeLimit)
                                )
                        )
                        .then(Commands.literal("useTimeLimit")
                                .then(Commands.argument("boolean", BoolArgumentType.bool())
                                        .executes(this::useTimeLimit)
                                )
                        )
                        .then(Commands.literal("growPlayersOnGemstoneSubmit")
                                .then(Commands.argument("boolean", BoolArgumentType.bool())
                                        .executes(this::growPlayersOnGemstoneSubmit)
                                )
                        )
                        .then(Commands.literal("showPowerMenu")
                                .then(Commands.argument("boolean", BoolArgumentType.bool())
                                        .executes(this::showPowerMenu)
                                )
                        )
                        .then(Commands.literal("growPlayerByAmount")
                                .then(Commands.argument("double", DoubleArgumentType.doubleArg())
                                        .executes(this::growPlayerByAmount)
                                )
                        )
                        .then(Commands.literal("growPlayerCap")
                                .then(Commands.argument("double", DoubleArgumentType.doubleArg())
                                        .executes(this::growPlayerCap)
                                )
                        )

                )
        );
    }



    private int setShardIncrease(CommandContext<CommandSourceStack> context) {
        ServerConfigs.POWER_CRYSTAL_INCREASE.set(IntegerArgumentType.getInteger(context, "increaseAmount"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int setNetherBorderMultiplier(CommandContext<CommandSourceStack> context) {
        ServerConfigs.NETHER_BORDER_INCREASE.set(IntegerArgumentType.getInteger(context, "increaseAmount"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int setEndBorderMultiplier(CommandContext<CommandSourceStack> context) {
        ServerConfigs.END_BORDER_INCREASE.set(IntegerArgumentType.getInteger(context, "increaseAmount"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int setWorldBorderTimeLimit(CommandContext<CommandSourceStack> context) {
        ServerConfigs.STOP_ACCEPTING_GEMSTONES_DATE.set(StringArgumentType.getString(context, "timeFormat"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int useTimeLimit(CommandContext<CommandSourceStack> context) {
        ServerConfigs.LIMIT_TIME_FOR_EXPANSION.set(BoolArgumentType.getBool(context, "boolean"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int growPlayersOnGemstoneSubmit(CommandContext<CommandSourceStack> context) {
        ServerConfigs.GROW_PLAYER_ON_GEMSTONE_SUBMIT.set(BoolArgumentType.getBool(context, "boolean"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int showPowerMenu(CommandContext<CommandSourceStack> context) {
        ServerConfigs.SHOW_POWER_MENU.set(BoolArgumentType.getBool(context, "boolean"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int growPlayerByAmount(CommandContext<CommandSourceStack> context) {
        ServerConfigs.GROW_PLAYER_AMOUNT.set(DoubleArgumentType.getDouble(context, "double"));
        ServerConfigs.SPEC.save();
        return 0;
    }

    private int growPlayerCap(CommandContext<CommandSourceStack> context) {
        ServerConfigs.GROW_PLAYER_CAP.set(DoubleArgumentType.getDouble(context, "double"));
        ServerConfigs.SPEC.save();
        return 0;
    }

}
