package io.github.a1qs.vaultadditions.vault.powermenu;

import com.mojang.blaze3d.vertex.PoseStack;
import iskallia.vault.client.gui.framework.ScreenRenderers;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.screen.player.SkillsElementContainerScreen;
import iskallia.vault.client.gui.screen.player.legacy.SplitTabContent;
import iskallia.vault.client.gui.screen.player.legacy.TabContent;
import iskallia.vault.container.NBTElementContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;

public class PowerElementContainerScreen extends SkillsElementContainerScreen<PowerTree> {
    public static final int TAB_INDEX = 5;

    public PowerElementContainerScreen(NBTElementContainer<PowerTree> container, Inventory inventory, Component title) {
        super(container, inventory, title, ScreenRenderers.getImmediate());
    }

    public int getTabIndex() {
        return TAB_INDEX;
    }
    public MutableComponent getTabTitle() {
        return new TextComponent("Power");
    }

    public TabContent getTabContent() {
        PowerDialog borderDialog = new PowerDialog(this.getSkillTree(), this);
        PowerPanRegion talentPanningContent = new PowerPanRegion(borderDialog, this);
        return new SplitTabContent(this, borderDialog, talentPanningContent);
    }

    protected void renderPointOverlay(PoseStack matrixStack) {
        this.renderPointOverlay(matrixStack, VaultBarOverlay.unspentArchetypePoints, TextColor.fromRgb(16724414), " unspent archetype point" + (VaultBarOverlay.unspentArchetypePoints == 1 ? "" : "s"));
    }

}
