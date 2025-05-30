package io.github.a1qs.vaultadditions.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.a1qs.vaultadditions.mixins.accessors.AccessorIdPalette;
import io.github.a1qs.vaultadditions.mixins.accessors.AccessorStructureTemplate;
import io.github.a1qs.vaultadditions.vault.gear.effect.AttributeTransmogEffect;
import iskallia.vault.core.Version;
import iskallia.vault.core.data.key.TemplateKey;
import iskallia.vault.core.util.ThemeBlockRetriever;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.world.data.tile.PartialBlock;
import iskallia.vault.core.world.data.tile.PartialBlockState;
import iskallia.vault.core.world.template.StructureTemplate;
import iskallia.vault.core.world.template.Template;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.mana.Mana;
import iskallia.vault.mana.ManaAction;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DebugCommands {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public DebugCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultadditions")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("debug")
                        .then(Commands.literal("giveMeMyMana")
                                .executes(this::giveMeMyMana)
                        )
                        .then(Commands.literal("resetCooldowns")
                                .executes(this::resetCooldowns)
                        )
                        .then(Commands.literal("configureWorldBorders")
                                .then(Commands.argument("worldBorderSize", DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
                                        .executes(this::configureWorldBorders)
                                )
                        )
                        .then(Commands.literal("serializeHeldModifiers")
                                .executes(this::serializeHeldModifiers))
                        .then(Commands.literal("createCrucibleListsFromStructures")
                                .executes(this::createCrucibleListsFromStructures))
                )
        );
    }

    private int createCrucibleListsFromStructures(CommandContext<CommandSourceStack> context) {
        JsonObject crucibleJson = new JsonObject();
        JsonArray explicitIds = new JsonArray();
        for (TemplateKey templateKey : VaultRegistry.TEMPLATE.getKeys()) {
            for (Map.Entry<Version, Template> entry : templateKey.getMap().entrySet()) {
                if (!(entry.getValue() instanceof StructureTemplate template)) {
                    continue;
                }

                StructureTemplate.IdPalette palette = ((AccessorStructureTemplate) template).getPalette();
                if (palette == null) {
                    context.getSource().sendFailure(new TextComponent("Template " + templateKey.getName() + " has no palette!"));
                    continue;
                }

                String fileName = templateKey.getName() + "_" + entry.getKey().getName() + ".json";
                int nullBlocks = 0;
                JsonObject templateJson = new JsonObject();
                JsonArray ids = new JsonArray();
                for (PartialBlockState state : ((AccessorIdPalette) palette).getIds()) {
                    PartialBlock block = state.getBlock();
                    if (block == null) {
                        nullBlocks++;
                        continue;
                    }
                    ResourceLocation id = ResourceLocation.tryParse(block.toString());
                    if (id != null) {
                        ids.add(id.toString());
                        if (!ThemeBlockRetriever.allowVaultBlock(id)) {
                            explicitIds.add(id.toString());
                        }
                    }
                }
                if (nullBlocks > 0) {
                    context.getSource().sendFailure(new TextComponent("Template " + templateKey.getName() + " has " + nullBlocks + " null blocks!"));
                }
                templateJson.add("ids", ids);

                File file = new File("exported/vaultadditions/" + fileName);
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    context.getSource().sendFailure(new TextComponent("Failed to create directory for " + file.getName()));
                    continue;
                } else if (!file.exists()) {
                    try {
                        if (!file.createNewFile()) {
                            context.getSource().sendFailure(new TextComponent("Failed to create file: " + file.getName()));
                            continue;
                        }
                    } catch (Exception e) {
                        context.getSource().sendFailure(new TextComponent("Failed to create file: " + file.getName()));
                        e.printStackTrace();
                        continue;
                    }
                }

                try(FileWriter writer = new FileWriter(file)) {
                    GSON.toJson(templateJson, writer);
                    context.getSource().sendSuccess(new TextComponent("Successfully wrote " + file.getName() + "!"), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(new TextComponent("Failed to write template: " + file.getName()));
                    e.printStackTrace();
                }
            }
        }

        File crucibleFile = new File("exported/vaultadditions/parent_crucible.json");
        if (!crucibleFile.getParentFile().exists() && !crucibleFile.getParentFile().mkdirs()) {
            context.getSource().sendFailure(new TextComponent("Failed to create directory for parent_crucible.json"));
            return 0;
        } else if (!crucibleFile.exists()) {
            try {
                if (!crucibleFile.createNewFile()) {
                    context.getSource().sendFailure(new TextComponent("Failed to create file: parent_crucible.json"));
                    return 0;
                }
            } catch (Exception e) {
                context.getSource().sendFailure(new TextComponent("Failed to create file: parent_crucible.json"));
                e.printStackTrace();
                return 0;
            }
        }

        try(FileWriter writer = new FileWriter(crucibleFile)) {
            crucibleJson.add("ids", explicitIds);
            GSON.toJson(crucibleJson, writer);
            context.getSource().sendSuccess(new TextComponent("Successfully wrote exported/vaultadditions/parent_crucible.json!"), true);
        } catch (Exception e) {
            context.getSource().sendFailure(new TextComponent("Failed to write parent_crucible.json"));
            e.printStackTrace();
        }
        return Command.SINGLE_SUCCESS;
    }

    private int giveMeMyMana(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Mana.increase(player, ManaAction.PLAYER_ACTION, Integer.MAX_VALUE);
        context.getSource().sendSuccess(new TextComponent("Added " + Integer.MAX_VALUE + " Mana!").withStyle(ChatFormatting.AQUA), true);
        return Command.SINGLE_SUCCESS;
    }

    private int resetCooldowns(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get(player.getLevel());
        AbilityTree abilityTree = abilitiesData.getAbilities(player);

        abilityTree.iterate(Ability.class, (ability) ->
                ability.getCooldown().ifPresent(cooldown -> {
                    ability.reduceCooldownBy(1000000);
                })
        );

        abilityTree.sync(SkillContext.of(player));
        context.getSource().sendSuccess(new TextComponent("Reset the Cooldown of player: " + player.getName().getString() + "!"), true);
        return Command.SINGLE_SUCCESS;
    }

    private int configureWorldBorders(CommandContext<CommandSourceStack> context) {
        double amount = DoubleArgumentType.getDouble(context, "worldBorderSize");

        List<ResourceKey<Level>> levels = List.of(Level.OVERWORLD, Level.NETHER, Level.END);

        for(ResourceKey<Level> level : levels) {
            WorldBorder border = context.getSource().getServer().getLevel(level).getWorldBorder();
            border.setSize(amount);
        }

        context.getSource().sendSuccess(new TranslatableComponent("commands.worldborder.set.immediate", String.format(Locale.ROOT, "%.1f", amount)), true);
        return Command.SINGLE_SUCCESS;
    }

    private int serializeHeldModifiers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty() || !VaultGearData.hasData(stack)) {
            context.getSource().sendFailure(new TextComponent("Held item has no modifiers!"));
            return 0;
        }
        context.getSource().sendSuccess(new TextComponent("Serialized Modifiers:"), true);
        VaultGearData data = VaultGearData.read(stack);
        for (VaultGearModifier.AffixType type : VaultGearModifier.AffixType.values()) {
            for (VaultGearModifier<?> modifier : data.getModifiers(type)) {
                context.getSource().sendSuccess(new TextComponent(" "), true);
                context.getSource().sendSuccess(new TextComponent(new AttributeTransmogEffect<>(modifier).toString()), true);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
