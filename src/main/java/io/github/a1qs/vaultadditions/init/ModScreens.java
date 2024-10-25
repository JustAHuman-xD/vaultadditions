package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseElementContainerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModScreens {
    public static void register() {
        MenuScreens.register(ModContainers.SPECIAL_EXPERTISE_TAB_CONTAINER, SpecialExpertiseElementContainerScreen::new);
    }
}
