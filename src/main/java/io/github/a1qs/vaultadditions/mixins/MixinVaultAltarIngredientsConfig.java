package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.expertise.FortunateVaultarExpertise;
import iskallia.vault.altar.RequiredItems;
import iskallia.vault.config.Config;
import iskallia.vault.config.altar.VaultAltarIngredientsConfig;
import iskallia.vault.config.altar.entry.AltarIngredientEntry;
import iskallia.vault.init.ModGameRules;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.skill.expertise.type.LuckyAltarExpertise;
import iskallia.vault.skill.tree.ExpertiseTree;
import iskallia.vault.world.VaultCrystalMode;
import iskallia.vault.world.data.PlayerExpertisesData;
import iskallia.vault.world.data.PlayerStatsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(value = VaultAltarIngredientsConfig.class)
public abstract class MixinVaultAltarIngredientsConfig {

    @Shadow(remap = false)
    protected abstract double getScale(String poolId, int crystalsCrafted);

    @Shadow(remap = false)
    protected abstract void spawnLuckyEffects(Level world, BlockPos pos);

    @Shadow(remap = false)
    protected abstract Map<String, AltarIngredientEntry> getEntries(int vaultLevel);

    /**
     * @author a1qs
     * @reason add expertise, overwrite cause lazy oh well
     */
    @Overwrite(remap = false)
    public List<RequiredItems> getIngredients(ServerPlayer player, BlockPos pos) {
        ServerLevel level = player.getLevel();
        boolean isLucky = false;
        int altarLevel = PlayerVaultStatsData.get(level).getVaultStats(player).getVaultLevel();
        int crystalsCrafted = PlayerStatsData.get(level.getServer()).get(player.getUUID()).getCrystals().size();
        VaultCrystalMode mode = player.getLevel().getGameRules().getRule(ModGameRules.CRYSTAL_MODE).get();
        float amtMultiplier = mode.getMultiplier();
        float luckyAltarChance = 0.0F;
        ExpertiseTree expertises = PlayerExpertisesData.get(level).getExpertises(player);

        LuckyAltarExpertise expertise;
        for (Iterator<LuckyAltarExpertise> var11 = expertises.getAll(LuckyAltarExpertise.class, Skill::isUnlocked).iterator(); var11.hasNext(); luckyAltarChance += expertise.getLuckyAltarChance()) {
            expertise = var11.next();
        }

        /* Addition */
        float reductionCost = 0.0F;
        FortunateVaultarExpertise fortunateVaultarExpertise;
        for (Iterator<FortunateVaultarExpertise> var11 = expertises.getAll(FortunateVaultarExpertise.class, Skill::isUnlocked).iterator(); var11.hasNext(); reductionCost += fortunateVaultarExpertise.getAltarCostReduction()) {
            fortunateVaultarExpertise = var11.next();
        }
        /* End of Addition */

        if (Config.rand.nextFloat() < luckyAltarChance) {
            this.spawnLuckyEffects(level, pos);
            isLucky = true;
        }

        List<RequiredItems> requiredItems = new ArrayList<>();
        Map<String, AltarIngredientEntry> entries = this.getEntries(altarLevel);

        String poolId;
        List<ItemStack> items;
        int amount;
        for (Iterator<Map.Entry<String, AltarIngredientEntry>> var13 = entries.entrySet().iterator(); var13.hasNext(); requiredItems.add(new RequiredItems(poolId, items, amount))) {
            Map.Entry<String, AltarIngredientEntry> entry = var13.next();
            poolId = entry.getKey();
            AltarIngredientEntry ingredientEntry = entry.getValue();
            items = ingredientEntry.getItems().stream().map(ItemStack::copy).peek((itemStack) -> itemStack.setCount(1)).toList();
            amount = ingredientEntry.getAmount();
            if (isLucky) {
                amount = 0;
            } else if (ingredientEntry.getScale() != 0.0) {
                double scale = this.getScale(poolId, crystalsCrafted);
                amount = Math.max((int) ((double) Math.round((double) amount * scale * (double) amtMultiplier) * ingredientEntry.getScale()), mode.getMinCost());
                /* Addition */
                amount = (int) (amount * (1.0F - reductionCost));
                if(amount == 0 ) {
                    amount = 1;
                }
                /* End of Addition */
            } else {
                amount = Math.max(Math.round((float) amount * amtMultiplier), mode.getMinCost());
                /* Addition */
                amount = (int) (amount * (1.0F - reductionCost));
                if(amount == 0 ) {
                    amount = 1;
                }
                /* End of Addition */
            }
        }

        return requiredItems;
    }

}
