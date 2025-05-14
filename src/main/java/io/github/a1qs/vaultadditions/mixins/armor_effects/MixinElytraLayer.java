package io.github.a1qs.vaultadditions.mixins.armor_effects;

import io.github.a1qs.vaultadditions.config.Configs;
import io.github.a1qs.vaultadditions.vault.gear.effect.HideElytraEffect;
import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ElytraLayer.class, priority = 900)
public abstract class MixinElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public MixinElytraLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, remap = false)
    public void shouldRender(ItemStack stack, T entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player) {
            ItemStack chest = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
            if (chest.getItem() instanceof VaultArmorItem && Configs.TRANSMOG_EFFECTS_CONFIG.hasEffect(chest, HideElytraEffect.INSTANCE)) {
                cir.setReturnValue(false);
            }
        }
    }
}
