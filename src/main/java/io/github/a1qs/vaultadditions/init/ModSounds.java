package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, VaultAdditions.MOD_ID);

    public static final RegistryObject<SoundEvent> BLADE_FRENZY = SOUNDS.register("blade_frenzy",
            () -> new SoundEvent(new ResourceLocation(VaultAdditions.MOD_ID, "blade_frenzy")));
}
