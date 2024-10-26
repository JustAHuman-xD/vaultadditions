package io.github.a1qs.vaultadditions.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.render.IVaultOptions;
import iskallia.vault.util.function.ObservableSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VaultBarOverlay.class, remap = false)
public class MixinVaultBarOverlay {
    /*public*/ private static int unspentPowerPoints;
    private static final ObservableSupplier<Integer> POWER_POINT_SUPPLIER = ObservableSupplier.of(() -> unspentPowerPoints, Integer::equals);
    private static Component unspentPowerPointComponent;
    private static int unspentPowerPointComponentWidth;

    @Inject(method = "renderPointText", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE))
    private static void s(Minecraft minecraft, LocalPlayer player, PoseStack matrixStack, int right, MultiBufferSource.BufferSource buffer, CallbackInfo ci) {
        IVaultOptions options = (IVaultOptions)Minecraft.getInstance().options;
        if (options.showPointMessages()) {
            int x;
            int gap = 5;

            minecraft.getProfiler().popPush("batchPowerPointText");
            if (unspentPowerPoints != 0) {
                POWER_POINT_SUPPLIER.ifChanged(MixinVaultBarOverlay::onUnspentPowerPointsChanged);
                x = right - unspentPowerPointComponentWidth - gap;
                minecraft.font.drawInBatch(unspentPowerPointComponent, (float)x, 18.0F, 16777215, true, matrixStack.last().pose(), buffer, false, 0, 15728880);
                matrixStack.translate(0.0, 12.0, 0.0);
            }
        }
    }

    private static void onUnspentPowerPointsChanged(int unspentPowerPoints) {
        MutableComponent cmp = new TextComponent(String.valueOf(unspentPowerPoints)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414)));
        int absUnspentPowerPoint = Math.abs(unspentPowerPoints);
        unspentPowerPointComponent = cmp.append((new TextComponent(" unspent power point" + (absUnspentPowerPoint == 1 ? "" : "s"))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16777215))));
        unspentPowerPointComponentWidth = Minecraft.getInstance().font.width(unspentPowerPointComponent);
    }
}
