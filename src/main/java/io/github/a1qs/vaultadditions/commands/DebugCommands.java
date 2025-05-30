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
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.mixins.crucible_export.AccessorPartialBlock;
import io.github.a1qs.vaultadditions.mixins.crucible_export.AccessorTargetTileProcessor;
import io.github.a1qs.vaultadditions.vault.gear.effect.AttributeTransmogEffect;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.data.key.PaletteKey;
import iskallia.vault.core.util.ThemeBlockRetriever;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.world.data.entity.PartialCompoundNbt;
import iskallia.vault.core.world.data.tile.PartialBlock;
import iskallia.vault.core.world.data.tile.PartialBlockProperties;
import iskallia.vault.core.world.data.tile.PartialBlockState;
import iskallia.vault.core.world.data.tile.PartialTile;
import iskallia.vault.core.world.data.tile.TilePredicate;
import iskallia.vault.core.world.processor.Palette;
import iskallia.vault.core.world.processor.tile.BernoulliWeightedTileProcessor;
import iskallia.vault.core.world.processor.tile.LeveledTileProcessor;
import iskallia.vault.core.world.processor.tile.ReferenceTileProcessor;
import iskallia.vault.core.world.processor.tile.TileProcessor;
import iskallia.vault.core.world.processor.tile.WeightedTileProcessor;
import iskallia.vault.core.world.template.data.TemplatePool;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

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
        File structuresDir = new File("config/the_vault/gen/1.0/structures");
        if (!structuresDir.exists()) {
            context.getSource().sendFailure(new TextComponent("The vault hunters structures directory does not exist: " + structuresDir.getPath()));
            return 0;
        }

        File exportDir = new File("exported/vaultadditions");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            context.getSource().sendFailure(new TextComponent("Failed to create directory for exported jsons"));
            return 0;
        }

        Set<ResourceLocation> explicitIds = new HashSet<>();
        exportStructures(context, explicitIds, structuresDir);
        exportJson(context, explicitIds, new File(exportDir, "parent_crucible.json"));
        return Command.SINGLE_SUCCESS;
    }

    private void exportStructures(CommandContext<CommandSourceStack> context, Set<ResourceLocation> explicitIds, File file) {
        if (file == null || !file.exists()) {
            return;
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    exportStructures(context, explicitIds, subFile);
                }
            }
            return;
        } else if (!file.getName().endsWith(".nbt")) {
            context.getSource().sendFailure(new TextComponent("Skipping file: " + file.getName() + " (not a .nbt file)"));
            return;
        }

        try {
            CompoundTag nbt = NbtIo.readCompressed(new FileInputStream(file));
            if (nbt.contains("palettes", Tag.TAG_LIST)) {
                VaultAdditions.LOGGER.info("Found multiple palettes in file: {}", file.getName());
                for (Tag paletteTag : nbt.getList("palettes", Tag.TAG_LIST)) {
                    if (paletteTag instanceof ListTag palette && palette.getElementType() == Tag.TAG_COMPOUND) {
                        exportPalette(context, explicitIds, file, palette);
                    }
                }
            } else {
                VaultAdditions.LOGGER.info("Found single palette in file: {}", file.getName());
                exportPalette(context, explicitIds, file, nbt.getList("palette", Tag.TAG_COMPOUND));
            }
        } catch (Exception e) {
            context.getSource().sendFailure(new TextComponent("Failed to read NBT from file: " + file.getName()));
            VaultAdditions.LOGGER.error("Failed to read NBT from file: " + file.getName(), e);
        }
    }

    private void exportPalette(CommandContext<CommandSourceStack> context, Set<ResourceLocation> explicitIds, File file, ListTag palette) {
        int nullBlocks = 0;
        Set<ResourceLocation> ids = new HashSet<>();
        for (Tag tag : palette) {
            if (!(tag instanceof CompoundTag compoundTag)) {
                context.getSource().sendFailure(new TextComponent("Skipping non-CompoundTag in palette: " + tag));
                continue;
            }
            ResourceLocation id = Adapters.IDENTIFIER.readNbt(compoundTag.get("Name")).orElse(null);
            if (id == null) {
                context.getSource().sendFailure(new TextComponent("Skipping null name in palette: " + compoundTag));
                nullBlocks++;
                continue;
            }
            ids.add(id);
            if (!ThemeBlockRetriever.allowVaultBlock(id)) {
                explicitIds.add(id);
            }
        }
        if (nullBlocks > 0) {
            context.getSource().sendFailure(new TextComponent("Structure " + file.getName() + " has " + nullBlocks + " null blocks!"));
        }

        String relativePath = file.getPath().replace("config/the_vault/gen/1.0/structures/", "");
        File poolFile = new File("config/the_vault/gen/1.0/template_pools/" + relativePath.replace(".nbt", ".json"));
        if (poolFile.exists()) {
            TemplatePool pool = TemplatePool.fromPath(poolFile.getPath());
            if (pool != null) {
                List<PartialTile> tiles = new ArrayList<>();
                for (ResourceLocation id : ids) {
                    tiles.add(PartialTile.of(PartialBlockState.of(PartialBlock.of(id), PartialBlockProperties.empty()), PartialCompoundNbt.empty()));
                }
                Supplier<List<PartialTile>> tileSupplier = () -> {
                    List<PartialTile> tileList = new ArrayList<>();
                    for (PartialTile tile : tiles) {
                        tileList.add(tile.copy());
                    }
                    return tileList;
                };

                pool.iterate(entry -> {
                    for (PaletteKey key : entry.getPalettes()) {
                        for (Palette paletteEntry : key.getMap().values()) {
                            for (TileProcessor processor : paletteEntry.getTileProcessors()) {
                                appendProcessedIds(explicitIds, ids, processor, tileSupplier);
                            }
                        }
                    }
                    return true;
                });
            } else {
                context.getSource().sendFailure(new TextComponent("Failed to create template pool from file: " + poolFile.getName()));
            }
        } else {
            context.getSource().sendFailure(new TextComponent("No template pool found for structure: " + file.getName()));
        }
        exportJson(context, ids, new File("exported/vaultadditions/" + file.getName().replace(".nbt", ".json")));
    }

    private void appendProcessedIds(Set<ResourceLocation> explicitIds, Set<ResourceLocation> ids, TileProcessor processor, Supplier<List<PartialTile>> tileSupplier) {
        if (processor instanceof LeveledTileProcessor leveled) {
            for (TileProcessor child : leveled.levels.values()) {
                appendProcessedIds(explicitIds, ids, child, tileSupplier);
            }
        } else if (processor instanceof ReferenceTileProcessor reference) {
            for (ResourceLocation id : reference.getPool().keySet()) {
                for (Palette palette : VaultRegistry.PALETTE.getKey(id).getMap().values()) {
                    for (TileProcessor tileProcessor : palette.getTileProcessors()) {
                        appendProcessedIds(explicitIds, ids, tileProcessor, tileSupplier);
                    }
                }
            }
        } else if (processor instanceof BernoulliWeightedTileProcessor weighted) {
            for (PartialTile tile : tileSupplier.get()) {
                if (weighted.target.test(tile)) {
                    appendTiles(explicitIds, ids, weighted.success.keySet());
                    appendTiles(explicitIds, ids, weighted.failure.keySet());
                    break;
                }
            }
        } else if (processor instanceof WeightedTileProcessor weighted) {
            TilePredicate predicate = ((AccessorTargetTileProcessor) weighted).getPredicate();
            for (PartialTile tile : tileSupplier.get()) {
                if (predicate.test(tile)) {
                    appendTiles(explicitIds, ids, weighted.getOutput().keySet());
                    break;
                }
            }
        }
    }

    private void appendTiles(Set<ResourceLocation> explicitIds, Set<ResourceLocation> ids, Iterable<PartialTile> tiles) {
        for (PartialTile success : tiles) {
            ResourceLocation id = ((AccessorPartialBlock) success.getState().getBlock()).getId();
            if (id != null) {
                ids.add(id);
                if (!ThemeBlockRetriever.allowVaultBlock(id)) {
                    explicitIds.add(id);
                }
            }
        }
    }

    private void exportJson(CommandContext<CommandSourceStack> context, Set<ResourceLocation> ids, File file) {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    context.getSource().sendFailure(new TextComponent("Failed to create file: " + file.getName()));
                    return;
                }
            } catch (Exception e) {
                context.getSource().sendFailure(new TextComponent("Failed to create file: " + file.getName()));
                e.printStackTrace();
                return;
            }
        }

        try(FileWriter writer = new FileWriter(file)) {
            JsonObject json = new JsonObject();
            JsonArray jsonIds = new JsonArray();
            for (ResourceLocation id : ids) {
                jsonIds.add(id.toString());
            }
            json.add("ids", jsonIds);
            GSON.toJson(json, writer);
            context.getSource().sendSuccess(new TextComponent("Successfully wrote " + file.getName() + "!"), true);
        } catch (Exception e) {
            context.getSource().sendFailure(new TextComponent("Failed to write template: " + file.getName()));
            e.printStackTrace();
        }
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
