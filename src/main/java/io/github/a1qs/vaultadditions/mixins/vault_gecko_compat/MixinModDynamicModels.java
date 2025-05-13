package io.github.a1qs.vaultadditions.mixins.vault_gecko_compat;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.vault.gear.gecko.VaultGeckoModel;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.dynamodel.registry.DynamicModelRegistry;
import iskallia.vault.init.ModDynamicModels;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.ForgeModelBakery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModDynamicModels.class)
public class MixinModDynamicModels {
    @Inject(method = "lambda$bakeModels$4", at = @At(value = "INVOKE", target = "Liskallia/vault/init/ModDynamicModels;jsonModelExists(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/ResourceLocation;)Z", shift = At.Shift.AFTER))
    private static void onBakeExistingModel(ForgeModelBakery modelLoader, ResourceManager resourceManager, ResourceLocation modelId, DynamicModel<?> dynamicModel, DynamicModelRegistry<?> registry, ModelResourceLocation modelLocation, CallbackInfo ci) {
        if (getGeckoModel(dynamicModel) != null) {
            VaultAdditions.LOGGER.info("Baked PRIOR existing GECKO model {}", modelId.toString());
        } else {
            VaultAdditions.LOGGER.info("Baked PRIOR existing VAULT model {}", modelId.toString());
        }
    }

    @Inject(method = "lambda$bakeModels$4", at = @At(value = "INVOKE", target = "Liskallia/vault/dynamodel/DynamicModel;bakeModel(Lnet/minecraft/client/resources/model/ModelResourceLocation;Lnet/minecraftforge/client/model/ForgeModelBakery;Lnet/minecraft/client/renderer/block/model/BlockModel;)Lnet/minecraft/client/resources/model/BakedModel;", shift = At.Shift.AFTER))
    private static void onCreateRuntimeModel(ForgeModelBakery modelLoader, ResourceManager resourceManager, ResourceLocation modelId, DynamicModel<?> dynamicModel, DynamicModelRegistry<?> registry, ModelResourceLocation modelLocation, CallbackInfo ci) {
        if (getGeckoModel(dynamicModel) != null) {
            VaultAdditions.LOGGER.info("Creating runtime GECKO model {}", modelId.toString());
        } else {
            VaultAdditions.LOGGER.info("Creating runtime VAULT model {}", modelId.toString());
        }
    }

    @Unique
    private static VaultGeckoModel getGeckoModel(DynamicModel<?> model) {
        if (model instanceof VaultGeckoModel gecko) {
            return gecko;
        } else if (model instanceof ArmorPieceModel piece && piece.getArmorModel() instanceof VaultGeckoModel gecko) {
            return gecko;
        }
        return null;
    }
}
