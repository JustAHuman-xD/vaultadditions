package io.github.a1qs.vaultadditions.integration.jei;

import io.github.a1qs.vaultadditions.client.menu.PlayerTraderMenu;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class PlayerTraderGhost implements IGhostIngredientHandler<PlayerTraderMenu> {

    @Override
    public <I> List<Target<I>> getTargets(PlayerTraderMenu gui, I ingredient, boolean doStart) {
        List<Target<I>> res = new ArrayList<>();
        Slot s = gui.getMenu().getGhostSlot();
        res.add(new GhostTarget<>(gui, s));
        return res;
    }

    @Override
    public void onComplete() { }
}
