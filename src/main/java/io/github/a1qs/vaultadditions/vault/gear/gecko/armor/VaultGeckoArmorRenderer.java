package io.github.a1qs.vaultadditions.vault.gear.gecko.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.a1qs.vaultadditions.vault.gear.gecko.VaultGeckoModelProvider;
import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class VaultGeckoArmorRenderer<T extends VaultArmorItem & IAnimatable> extends GeoArmorRenderer<T> {
    public VaultGeckoArmorRenderer() {
        super(new VaultGeckoModelProvider<>());
        this.headBone = "Head";
        this.bodyBone = "Body";
        this.rightArmBone = "RightArm";
        this.leftArmBone = "LeftArm";
        this.rightLegBone = "RightLeg";
        this.leftLegBone = "LeftLeg";
        this.rightBootBone = "RightBoot";
        this.leftBootBone = "LeftBoot";
    }

    @Override
    public void render(float partialTick, PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        ((VaultGeckoModelProvider<?>) getGeoModelProvider()).using(itemStack);
        super.render(partialTick, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(ItemStack itemStack) {
        this.itemStack = itemStack;
        return ((VaultGeckoModelProvider<?>) getGeoModelProvider()).getTextureLocation(this.itemStack);
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return ((VaultGeckoModelProvider<?>) getGeoModelProvider()).getTextureLocation(this.itemStack);
    }
}
