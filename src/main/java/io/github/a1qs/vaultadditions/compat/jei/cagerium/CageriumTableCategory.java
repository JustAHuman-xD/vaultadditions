package io.github.a1qs.vaultadditions.compat.jei.cagerium;

import iskallia.vault.VaultMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings({"removal"})
public class CageriumTableCategory<T extends CageriumJeiPlugin.LootRecipe> implements IRecipeCategory<T> {
    private static final ResourceLocation TEXTURE = VaultMod.id("textures/gui/jei/loot_info.png");
    private final RecipeType<T> recipeType;
    private final Component title;
    private final IDrawable icon;
    private final IDrawableStatic background;
    private final T type;

    public CageriumTableCategory(IGuiHelper helper, RecipeType<T> recipeType, T type, ItemStack icon) {
        this.recipeType = recipeType;
        this.type = type;
        this.title = icon.getDisplayName();
        this.icon = helper.createDrawableItemStack(icon);
        this.background = helper.createDrawable(TEXTURE, 0, 0, 162, 108);
    }

    @Override @ParametersAreNonnullByDefault
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        List<ItemStack> itemList = recipe.lootTable.();
        int count = itemList.size();

        for(int i = 0; i < count; ++i) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + 18 * (i % 9), 1 + 18 * (i / 9)).addItemStack((ItemStack)itemList.get(i));
        }
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return type.getId();
    }

    @Override
    public @NotNull Class<? extends T> getRecipeClass() {
        return (Class<? extends T>) type.getClass();
    }
}
