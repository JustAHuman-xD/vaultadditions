package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.gear.gecko.VaultGeckoModelProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mixin(GeoArmorRenderer.class)
public class MixinGeoArmorRenderer {
    @Shadow(remap = false) @Final private AnimatedGeoModel<?> modelProvider;
    @Shadow(remap = false) protected ItemStack itemStack;

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib3/model/AnimatedGeoModel;getModel(Lnet/minecraft/resources/ResourceLocation;)Lsoftware/bernie/geckolib3/geo/render/built/GeoModel;"), remap = false)
    public ResourceLocation getRenderModelLocation(ResourceLocation location) {
        if (this.modelProvider instanceof VaultGeckoModelProvider<?> provider) {
            return provider.getModelLocation(this.itemStack);
        }
        return location;
    }
}
