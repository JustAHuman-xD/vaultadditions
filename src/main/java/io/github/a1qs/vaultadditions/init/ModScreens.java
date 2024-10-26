package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.vault.powermenu.PowerElementContainerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModScreens {
    public static void register() {
        MenuScreens.register(ModContainers.POWERS_TAB_CONTAINER, PowerElementContainerScreen::new);
    }
}
