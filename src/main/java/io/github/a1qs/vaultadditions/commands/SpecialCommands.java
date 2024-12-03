package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.init.ModBlocks;
import io.github.a1qs.vaultadditions.item.LootStatueBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;


public class SpecialCommands {
    private static final List<ItemStack> STATUES = List.of(
            new ItemStack(ModBlocks.LOOT_STATUE_VAULT.get()),
            new ItemStack(ModBlocks.LOOT_STATUE_GIFT.get()),
            new ItemStack(ModBlocks.LOOT_STATUE_GIFT_MEGA.get()),
            new ItemStack(ModBlocks.LOOT_STATUE_ARENA.get())
    );

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_STATUES = (context, builder) -> {
        List<ResourceLocation> itemIdentifiers = STATUES.stream()
                .map(itemStack -> itemStack.getItem().getRegistryName())
                .toList();

        return SharedSuggestionProvider.suggestResource(itemIdentifiers.stream(), builder);
    };

    public SpecialCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("grantLootStatue")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.argument("ItemId", ResourceLocationArgument.id())
                        .suggests(SUGGEST_STATUES)
                        .then(Commands.argument("PlayerName", StringArgumentType.string())
                                .executes(this::grantLootStatue)
                        )
                )


        );
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    private int grantLootStatue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "PlayerName");

        ItemStack statue = new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocationArgument.getId(context, "ItemId")));
        LootStatueBlockItem.setStatueName(statue, playerName);
        ServerPlayer player = context.getSource().getPlayerOrException();

        boolean added = player.getInventory().add(statue);

        if (!added || !statue.isEmpty()) {
            // Drop the item if it couldn't be fully added
            player.drop(statue, false);
        }
        VaultAdditions.LOGGER.info("{} has been granted a Loot Statue of type {} with statue_name {}", player.getName().getString(), ModBlocks.LOOT_STATUE_VAULT.getId(), playerName);
        context.getSource().sendSuccess(new TextComponent("You've been granted a Loot Statue").withStyle(ChatFormatting.GREEN), true);
        return 0;
    }


}
