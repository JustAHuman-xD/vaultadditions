package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.vault.skill.ability.ShieldWallAbility;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.RegistryEvent;

import java.awt.*;

public class ModEffects {
    public static final MobEffect SHIELD_WALL;

    public static void registerEffects(RegistryEvent.Register<MobEffect> event) {
        event.getRegistry().register(SHIELD_WALL);
    }

    static {
        SHIELD_WALL = new ShieldWallAbility.ShieldWallEffect(MobEffectCategory.BENEFICIAL, Color.GRAY.getRGB(), VaultAdditions.id("shield_wall"));
    }
}
