package io.github.a1qs.vaultadditions.vault.gear.gecko.item;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

public class GeckoItemRenderProperties implements IItemRenderProperties {
    private final IItemRenderProperties defaultProperties;
    BlockEntityWithoutLevelRenderer renderer = null;

    public GeckoItemRenderProperties(IItemRenderProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
        VaultAdditions.LOGGER.info("GeckoItemRenderProperties created with default properties {}",
                defaultProperties == null ? "null" : defaultProperties.getClass().getName());
    }

    @Override
    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        MiscUtil.limitedLog("GeckoItemRenderProperties getItemStackRenderer");
        if (renderer == null) {
            renderer = new VaultGeckoItemRenderer<>(defaultProperties);
        }
        return renderer;
    }
}