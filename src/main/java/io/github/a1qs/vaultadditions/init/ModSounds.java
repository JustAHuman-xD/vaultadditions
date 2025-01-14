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

    public static final RegistryObject<SoundEvent> HOY_ACTIVATE_SMITE_ARCHON = SOUNDS.register("hoy_activate_smite_archon",
            () -> new SoundEvent(new ResourceLocation(VaultAdditions.MOD_ID, "hoy_activate_smite_archon")));

    public static final RegistryObject<SoundEvent> HOY_ARCHON_BOLT = SOUNDS.register("hoy_archon_bolt",
            () -> new SoundEvent(new ResourceLocation(VaultAdditions.MOD_ID, "hoy_archon_bolt")));

    public static final RegistryObject<SoundEvent> HOY_DASH = SOUNDS.register("hoy_dash",
            () -> new SoundEvent(new ResourceLocation(VaultAdditions.MOD_ID, "hoy_dash")));

    public static final RegistryObject<SoundEvent> HOY_ENABLE_MANASHIELD = SOUNDS.register("hoy_enable_manashield",
            () -> new SoundEvent(new ResourceLocation(VaultAdditions.MOD_ID, "hoy_enable_manashield")));
}
