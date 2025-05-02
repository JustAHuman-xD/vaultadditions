package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.gecko.GeckoArmorModel;
import io.github.a1qs.vaultadditions.vault.gear.gecko.VaultGeckoArmorRenderer;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

@Mixin(VaultArmorItem.class)
public abstract class MixinVaultArmorItem extends DyeableArmorItem implements VaultGearItem, IAnimatable {
    private MixinVaultArmorItem() {
        super(null, null, null);
    }

    @Unique
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Inject(at = @At("HEAD"), method = "getArmorTexture", cancellable = true, remap = false)
    public void getArmorTexture(ItemStack itemStack, Entity entity, EquipmentSlot slot, String type, CallbackInfoReturnable<String> cir) {
        if (entity != null && ModelUtil.getArmorModel(itemStack) instanceof GeckoArmorModel model
                && GeoArmorRenderer.getRenderer(getClass(), entity) instanceof VaultGeckoArmorRenderer<?>) {
            cir.setReturnValue(model.getTexturePath().toString());
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        for (ModModels.GeckoArmor armor : ModModels.GeckoArmor.values()) {
            GeckoArmorModel model = armor.getModel();
            data.addAnimationController(new AnimationController<>(this, model.getId() + " Animation Controller", model.getTransitionTicks(), event -> {
                ItemStack itemStack = event.getExtraDataOfType(ItemStack.class).get(0);
                if (ModelUtil.getArmorModel(itemStack) == model) {
                    event.getController().setAnimation(model.getArmorAnimation());
                    return PlayState.CONTINUE;
                } else {
                    return PlayState.STOP;
                }
            }));
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
