package io.github.a1qs.vaultadditions.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.render.IVaultOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VaultBarOverlay.class)
public class MixinVaultBarOverlay {

    @Debug(export = true)
    @Inject(method = "renderPointText", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE))
    private static void renderPowerPoints(Minecraft minecraft, LocalPlayer player, PoseStack matrixStack, int right, MultiBufferSource.BufferSource buffer, CallbackInfo ci) {
        minecraft.getProfiler().popPush("batchPowerPointText");
        IVaultOptions options = (IVaultOptions) Minecraft.getInstance().options;
        if (!options.showPointMessages() || MiscUtil.unspentPowerPoints == 0 || VaultBarOverlay.vaultLevel < 100) {
            return;
        }

        int gap = 5;
        int x = right - MiscUtil.unspentPowerPointComponentWidth - gap;
        minecraft.font.drawInBatch(MiscUtil.unspentPowerPointComponent, (float)x, 18.0F, 16777215, true, matrixStack.last().pose(), buffer, false, 0, 15728880);
        matrixStack.translate(0.0, 12.0, 0.0);
        MiscUtil.POWER_POINT_SUPPLIER.ifChanged(MixinVaultBarOverlay::vaultadditions$onUnspentPowerPointsChanged);
    }

    @Unique
    private static void vaultadditions$onUnspentPowerPointsChanged(int unspentPowerPoints) {
        MutableComponent cmp = new TextComponent(String.valueOf(unspentPowerPoints)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414)));
        int absUnspentPowerPoint = Math.abs(unspentPowerPoints);
        MiscUtil.unspentPowerPointComponent = cmp.append((new TextComponent(" unspent power point" + (absUnspentPowerPoint == 1 ? "" : "s"))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16777215))));
        MiscUtil.unspentPowerPointComponentWidth = Minecraft.getInstance().font.width(MiscUtil.unspentPowerPointComponent);
    }
}
