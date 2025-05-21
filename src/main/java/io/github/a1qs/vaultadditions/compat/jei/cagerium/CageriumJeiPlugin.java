package io.github.a1qs.vaultadditions.compat.jei.cagerium;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mehvahdjukaar.cagerium.Cagerium;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

@JeiPlugin
public class CageriumJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = Cagerium.res("jei_plugin");

    private static final RecipeType<BossLootRecipe> BOSS_LOOT = RecipeType.create(Cagerium.MOD_ID, "boss_loot", BossLootRecipe.class);
    private static final RecipeType<HostileLootRecipe> HOSTILE_LOOT = RecipeType.create(Cagerium.MOD_ID, "hostile_loot", HostileLootRecipe.class);
    private static final RecipeType<PassiveLootRecipe> PASSIVE_LOOT = RecipeType.create(Cagerium.MOD_ID, "passive_loot", PassiveLootRecipe.class);


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories()
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(Cagerium.PLATE_ITEM.get().getDefaultInstance(), BOSS_LOOT);
        registration.addRecipeCatalyst(Cagerium.CAGE_ITEM.get().getDefaultInstance(), HOSTILE_LOOT);
        registration.addRecipeCatalyst(Cagerium.TERRARIUM_ITEM.get().getDefaultInstance(), PASSIVE_LOOT);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes();
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    public abstract static class LootRecipe {
        public final LootTable lootTable;
        public LootRecipe(LootTable lootTable) {
            this.lootTable = lootTable;
        }
         public abstract ResourceLocation getId();
    }

    static class BossLootRecipe extends LootRecipe {
        public static final ResourceLocation ID = Cagerium.res("boss_loot");
        public BossLootRecipe(LootTable lootTable) { super(lootTable); }
        @Override public ResourceLocation getId() { return ID; }
    }

    static class HostileLootRecipe extends LootRecipe {
        public static final ResourceLocation ID = Cagerium.res("hostile_loot");
        public HostileLootRecipe(LootTable lootTable) { super(lootTable); }
        @Override public ResourceLocation getId() { return ID; }
    }

    static class PassiveLootRecipe extends LootRecipe {
        public static final ResourceLocation ID = Cagerium.res("passive_loot");
        public PassiveLootRecipe(LootTable lootTable) { super(lootTable); }
        @Override public ResourceLocation getId() { return ID; }
    }
}
