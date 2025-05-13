package io.github.a1qs.vaultadditions.mixins.vault_gecko_compat;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.init.ModDynamicModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(ModDynamicModels.class)
public abstract class MixinModDynamicModels {
    @Redirect(method = "lambda$bakeModels$4", at = @At(value = "INVOKE", target = "Liskallia/vault/init/ModDynamicModels;jsonModelExists(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/ResourceLocation;)Z"))
    private static boolean forceBakeGeckoModels(ResourceManager manager, ResourceLocation id, @Local(argsOnly = true) DynamicModel<?> dynamicModel) {
        if (ModelUtil.getGeckoModel(dynamicModel) != null) {
            ResourceLocation model = DynamicModel.prependToId("models/item/", dynamicModel.getId());
            VaultAdditions.LOGGER.info("Looking for resources DIRECTLY under : {}", model);
            for (ResourceLocation resource : manager.listResources(model.getPath(), s -> true)) {
                VaultAdditions.LOGGER.info("Found : {}", resource);
            }

            String string = model.getPath();
            for (int i = 0; i < 2; i++) {
                if (string.lastIndexOf("/") == -1) {
                    break;
                }
                string = string.substring(0, string.lastIndexOf("/"));
                VaultAdditions.LOGGER.info("Looking for resources under : {}", string);
                for (ResourceLocation resource : manager.listResources(string, s -> true)) {
                    VaultAdditions.LOGGER.info("Found : {}", resource);
                }
            }
        }
        return jsonModelExists(manager, id);
    }

    @Shadow
    public static boolean jsonModelExists(ResourceManager manager, ResourceLocation id) {
        throw new AssertionError("Mixin failed to inject");
    }
}
