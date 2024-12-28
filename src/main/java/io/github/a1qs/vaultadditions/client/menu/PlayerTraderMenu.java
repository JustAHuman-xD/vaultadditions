package io.github.a1qs.vaultadditions.client.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.container.PlayerTraderContainer;
import io.github.a1qs.vaultadditions.container.RenameContainer;
import iskallia.vault.VaultMod;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.framework.ScreenRenderers;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.framework.element.*;
import iskallia.vault.client.gui.framework.render.ScreenTooltipRenderer;
import iskallia.vault.client.gui.framework.render.TooltipDirection;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.text.LabelTextStyle;
import iskallia.vault.init.ModTextureAtlases;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Inventory;

public class PlayerTraderMenu extends AbstractElementContainerScreen<PlayerTraderContainer> {
    private static final TextureAtlasRegion TEXTURE = TextureAtlasRegion.of(ModTextureAtlases.SCREEN, VaultMod.id("gui/screen/player_trader"));
    private final TextInputElement<?> levelInput;
    private EditBox nameField;
    private String name;

    public PlayerTraderMenu(PlayerTraderContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, ScreenRenderers.getBuffered(), ScreenTooltipRenderer::create);
        this.imageWidth = 176;
        this.imageHeight = 176;

        // Background Texture
        this.addElement(
                new TextureAtlasElement<>(
                        Spatials.positionXY(0, -10).size(imageWidth, imageHeight),
                        TEXTURE
                ).layout((screen, gui, parent, world) -> world
                        .translateXY(gui)
                        .size(Spatials.copy(gui).add(Spatials.size(0, 10))))
        );

        // Title
        this.addElement(
                new LabelElement<>(
                        Spatials.positionXY(7, -4),
                        ((MutableComponent)this.title).withStyle(Style.EMPTY.withColor(0xFF_3f3f3f)),
                        LabelTextStyle.defaultStyle()
                ).layout((screen, gui, parent, world) -> world.translateXY(gui))
        );

        // Slots? idk
        this.addElement(
                new SlotsElement<>(
                        Spatials.zero(),
                        this.getMenu().slots,
                        ScreenTextures.INSET_ITEM_SLOT_BACKGROUND
                ).layout((screen, gui, parent, world) -> world.positionXY(gui))
        );


        // something something input of items idk
        this.levelInput = this.addElement(
                new TextInputElement<>(
                        Spatials.copy(Spatials.positionXY(143, 20)).size(26, 12),
                        Minecraft.getInstance().font
                ).layout((screen, gui, parent, world) -> world.translateXY(gui))
        ).adjustEditBox(editBox -> {
            editBox.setFilter(input -> {
                if (input.isEmpty()) return true;
                int parsedLevel;
                try {
                    parsedLevel = Integer.parseInt(input);
                } catch (NumberFormatException exc) {
                    return false;
                }
                return parsedLevel <= 64;
            });
            editBox.setMaxLength(2);
            editBox.setValue("1");
        });
        this.levelInput.setVisible(false);
        this.levelInput.tooltip((tooltipRenderer, poseStack, mouseX, mouseY, tooltipFlag) -> {
            if (!this.levelInput.isVisible()) {
                return false;
            }
            Component cmp = new TextComponent("Level of crafted gear");
            tooltipRenderer.renderTooltip(poseStack, cmp, mouseX, mouseY, TooltipDirection.RIGHT);
            return true;
        });
        this.name = "dev";
    }

    @Override
    protected void init() {
        super.init();
        this.initFields();
    }

    private void initFields() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.nameField = new EditBox(this.font, i + 34, j + 26, 103, 12, new TextComponent(this.name));
        this.nameField.setCanLoseFocus(false);
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(16);
        this.nameField.setResponder(this::rename);
        this.addWidget(this.nameField);
        this.setInitialFocus(this.nameField);
        this.nameField.setValue(this.name);
    }

    private void addBackgroundElement() {
        this.addElement(
                new NineSliceElement<>(
                        this.getGuiSpatial(),
                        ScreenTextures.DEFAULT_WINDOW_BACKGROUND
                ).layout((screen, gui, parent, world) -> world
                        .translateXY(gui)
                        .size(Spatials.copy(gui))));
    }

    private void rename(String name) {
        this.name = name;
    }
}
