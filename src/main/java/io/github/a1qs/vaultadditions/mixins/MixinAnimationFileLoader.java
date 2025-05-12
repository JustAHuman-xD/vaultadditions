package io.github.a1qs.vaultadditions.mixins;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.a1qs.vaultadditions.VaultAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.file.AnimationFileLoader;
import software.bernie.geckolib3.util.json.JsonAnimationUtils;

import java.util.Map;

@Mixin(value = AnimationFileLoader.class, remap = false)
public abstract class MixinAnimationFileLoader {
    @Shadow protected abstract JsonObject loadFile(ResourceLocation location, ResourceManager manager);

    /**
     * @author JustAHuman
     * @reason For some reason geckolib doesn't listen for any exception and instead ONLY for chained json, wtf
     */
    @Overwrite
    public AnimationFile loadAllAnimations(MolangParser parser, ResourceLocation location, ResourceManager manager) {
        VaultAdditions.LOGGER.info("HAS OVERWRITTEN LOAD ALL");
        AnimationFile animationFile = new AnimationFile();
        GeckoLib.LOGGER.info("Attempting to load animation file {}...", location.toString());
        JsonObject jsonRepresentation = this.loadFile(location, manager);

        for(Map.Entry<String, JsonElement> entry : JsonAnimationUtils.getAnimations(jsonRepresentation)) {
            String animationName = entry.getKey();
            GeckoLib.LOGGER.info("Attempting to load animation {}...", animationName);
            try {
                Animation animation = JsonAnimationUtils.deserializeJsonToAnimation(JsonAnimationUtils.getAnimation(jsonRepresentation, animationName), parser);
                animationFile.putAnimation(animationName, animation);
            } catch (Exception e) {
                GeckoLib.LOGGER.error("Could not load animation: {}", animationName, e);
                throw new RuntimeException(e);
            }
        }

        GeckoLib.LOGGER.info("Animation file {} processed", location.toString());
        return animationFile;
    }
}
