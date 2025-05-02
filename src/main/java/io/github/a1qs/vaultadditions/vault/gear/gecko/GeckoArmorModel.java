package io.github.a1qs.vaultadditions.vault.gear.gecko;

import io.github.a1qs.vaultadditions.vault.gear.model.armor.AdditionalArmorModel;
import iskallia.vault.VaultMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;

public class GeckoArmorModel extends AdditionalArmorModel {
    private final ResourceLocation modelPath;
    private final ResourceLocation texturePath;
    private final ResourceLocation animationPath;

    private final AnimationBuilder armorAnimation;
    private final float transitionTicks;

    public GeckoArmorModel(String id, String displayName, String animationName, float transitionTicks, boolean hidesElytra) {
        super(VaultMod.id("gear/armor/" + id), displayName, hidesElytra);

        this.modelPath = VaultMod.id("geo/" + id + ".geo.json");
        this.texturePath = VaultMod.id("textures/item/gear/armor/" + id + "/texture.png");
        this.animationPath = VaultMod.id("animations/" + id + ".animation.json");

        this.armorAnimation = new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP);
        this.transitionTicks = transitionTicks;
    }

    public ResourceLocation getModelPath() {
        return this.modelPath;
    }

    public ResourceLocation getTexturePath() {
        return texturePath;
    }

    public ResourceLocation getAnimationPath() {
        return animationPath;
    }

    public AnimationBuilder getArmorAnimation() {
        return this.armorAnimation;
    }

    public float getTransitionTicks() {
        return this.transitionTicks;
    }
}
