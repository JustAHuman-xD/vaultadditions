package io.github.a1qs.vaultadditions.init.vault;

import io.github.a1qs.vaultadditions.VaultAdditions;
import iskallia.vault.VaultMod;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.config.ConfigurableAttributeGenerator;
import iskallia.vault.gear.attribute.type.VaultGearAttributeType;
import iskallia.vault.gear.comparator.VaultGearAttributeComparator;
import iskallia.vault.gear.reader.VaultGearModifierReader;
import iskallia.vault.init.ModGearAttributeGenerators;
import iskallia.vault.init.ModGearAttributeReaders;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = VaultAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModGearAttributes {
    public static final VaultGearAttribute<Boolean> BREACHING = attr("breaching",
            VaultGearAttributeType.booleanType(), ModGearAttributeGenerators.booleanFlag(),
            ModGearAttributeReaders.booleanReader("Breaching", 10031431),
            VaultGearAttributeComparator.booleanComparator()
    );

    @SubscribeEvent
    public static void init(RegistryEvent.Register<VaultGearAttribute<?>> event) {
        IForgeRegistry<VaultGearAttribute<?>> registry = event.getRegistry();

        registry.register(BREACHING);

    }

    private static <T> VaultGearAttribute<T> attr(String name,
                                                  VaultGearAttributeType<T> type,
                                                  ConfigurableAttributeGenerator<T, ?> generator,
                                                  VaultGearModifierReader<T> reader,
                                                  @Nullable VaultGearAttributeComparator<T> comparator) {
        return new VaultGearAttribute<>(VaultMod.id(name), type, generator, reader, comparator);
    }

}
