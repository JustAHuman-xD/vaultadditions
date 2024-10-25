package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.init.ModNetwork;
import io.github.a1qs.vaultadditions.network.ServerboundOpenSpecialExpertisesMessage;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.screen.player.element.SkillTabContainerElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SkillTabContainerElement.class, remap = false)
public class MixinSkillTabContainerElement {
    @ModifyVariable(
            method = "<init>",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private TextureAtlasRegion[] modifyIcons(TextureAtlasRegion[] icons) {
        TextureAtlasRegion[] newIcons = new TextureAtlasRegion[icons.length + 1];
        System.arraycopy(icons, 0, newIcons, 0, icons.length);
        newIcons[icons.length] = ScreenTextures.TAB_ICON_ARCHETYPES;

        return newIcons;
    }

    // dude what the fuck
    @Inject(method = "lambda$new$0(II)V", at = @At("TAIL"))
    private static void b(int selectedIndex, int index, CallbackInfo ci) {
        if (selectedIndex != index) {
            if(index == 5) {
                ModNetwork.CHANNEL.sendToServer(ServerboundOpenSpecialExpertisesMessage.INSTANCE);
            }
        }
    }


}
