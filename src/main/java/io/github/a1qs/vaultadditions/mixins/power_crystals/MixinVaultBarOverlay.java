package io.github.a1qs.vaultadditions.mixins.power_crystals;

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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VaultBarOverlay.class, remap = false)
public class MixinVaultBarOverlay {

    @Inject(method = "renderPointText", at = @At(value = "INVOKE", target = "Liskallia/vault/client/render/IVaultOptions;showPointMessages()Z"))
    private static void renderPowerPoints(Minecraft minecraft, LocalPlayer player, PoseStack matrixStack, int right, MultiBufferSource.BufferSource buffer, CallbackInfo ci) {
        minecraft.getProfiler().popPush("batchPowerPointText");
        IVaultOptions options = (IVaultOptions) Minecraft.getInstance().options;
        if (!options.showPointMessages() || MiscUtil.unspentPowerPoints == 0 || VaultBarOverlay.vaultLevel < 100) {
            return;
        }

        MiscUtil.POWER_POINT_SUPPLIER.ifChanged(MixinVaultBarOverlay::vaultadditions$onUnspentPowerPointsChanged);
        float x = right - MiscUtil.unspentPowerPointComponentWidth - 5;
        minecraft.font.drawInBatch(MiscUtil.unspentPowerPointComponent, x, 18.0F, 16777215, true, matrixStack.last().pose(), buffer, false, 0, 15728880);
        matrixStack.translate(0.0, 12.0, 0.0);
    }

    @Unique
    private static void vaultadditions$onUnspentPowerPointsChanged(int unspentPowerPoints) {
        MutableComponent cmp = new TextComponent(String.valueOf(unspentPowerPoints)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414)));
        int absUnspentPowerPoint = Math.abs(unspentPowerPoints);
        MiscUtil.unspentPowerPointComponent = cmp.append((new TextComponent(" unspent power point" + (absUnspentPowerPoint == 1 ? "" : "s"))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16777215))));
        MiscUtil.unspentPowerPointComponentWidth = Minecraft.getInstance().font.width(MiscUtil.unspentPowerPointComponent);
    }
}
