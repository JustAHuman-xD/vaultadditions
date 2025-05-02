package io.github.a1qs.vaultadditions.vault.gear.gecko;

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

    public ResourceLocation getTextureLocation(ItemStack itemStack) {
        this.itemStack = itemStack;
        return ((VaultGeckoModelProvider<?>) getGeoModelProvider()).getTextureLocation(this.itemStack);
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return ((VaultGeckoModelProvider<?>) getGeoModelProvider()).getTextureLocation(this.itemStack);
    }
}
