package io.github.a1qs.vaultadditions.mixins.vault_gecko_compat;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.gecko.VaultGeckoModel;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.dynamodel.registry.DynamicModelRegistry;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModDynamicModels;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@Mixin(VaultGearItem.class)
public interface MixinVaultGearItem extends VaultGearItem, IAnimatable {
    @Override
    default void registerControllers(AnimationData data) {
        for (DynamicModelRegistry<? extends DynamicModel<?>> registry : ModDynamicModels.REGISTRIES.getUniqueRegistries()) {
            registry.forEach((id, model) -> {
                VaultGeckoModel gecko = model instanceof VaultGeckoModel geckoModel ? geckoModel : null;
                if (model instanceof ArmorPieceModel piece && piece.getArmorModel() instanceof VaultGeckoModel geckoModel) {
                    gecko = geckoModel;
                }
                if (gecko == null) {
                    return;
                }

                VaultAdditions.LOGGER.info("Registering AnimationController for {} ({})", model.getId(), getClass().getSimpleName());

                VaultGeckoModel finalGecko = gecko;
                data.addAnimationController(new AnimationController<>(this, model.getId() + " Animation Controller", gecko.getTransitionTicks(), event -> {
                    DynamicModel<?> eventModel = ModelUtil.getDynamicModel(event.getExtraDataOfType(ItemStack.class).get(0));
                    if (Screen.hasShiftDown()) {
                        VaultAdditions.LOGGER.info("Animation Event Fired for {}, this controller for {}, matched : {}",
                                eventModel == null ? "null" : eventModel.getId().toString(),
                                id.toString(),
                                model == eventModel
                        );
                    }
                    if (eventModel == model) {
                        event.getController().setAnimation(finalGecko.getAnimation());
                        return PlayState.CONTINUE;
                    } else {
                        return PlayState.STOP;
                    }
                }));
            });
        }
    }

    @Override
    default AnimationFactory getFactory() {
        return ModelUtil.getAnimationFactory(this);
    }
}
