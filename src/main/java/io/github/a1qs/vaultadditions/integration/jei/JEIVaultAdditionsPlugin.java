package io.github.a1qs.vaultadditions.integration.jei;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.client.menu.PlayerTraderMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIVaultAdditionsPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return VaultAdditions.id("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(PlayerTraderMenu.class, new PlayerTraderGhost());
    }
}
