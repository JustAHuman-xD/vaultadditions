package io.github.a1qs.vaultadditions.integration.jei;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public record GhostTarget<I>(AbstractContainerScreen<?> gui, Slot slot) implements IGhostIngredientHandler.Target<I> {
    @Override
    public Rect2i getArea() {
        return new Rect2i(slot.x + gui.getGuiLeft(), slot.y + gui.getGuiTop(), 16, 16);
    }

    @Override
    public void accept(I i) {
        if(i instanceof ItemStack) {
            // TODO Send network message to server to update the player trader tile entity and make it reflect on the slot.
        }
    }
}
