package io.github.a1qs.vaultadditions.mixins;


import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.registry.ArmorPieceModelRegistry;
import iskallia.vault.dynamodel.registry.DynamicModelRegistry;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.woldsvaults.models.Battlestaffs;

@Restriction(require = @Condition("woldsvaults"))
@Mixin(value = Battlestaffs.class, remap = false)
public class MixinBattlestaffs {
    @Shadow
    @Final
    public static DynamicModelRegistry<HandHeldModel> REGISTRY;

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void injectArmorModels(CallbackInfo ci) {
        REGISTRY.register(ModModels.WoldsBattleStaffs.DARKSABER);
    }
}
