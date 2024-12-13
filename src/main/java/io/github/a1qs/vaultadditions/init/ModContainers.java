package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.container.LootStatueContainer;
import io.github.a1qs.vaultadditions.container.RenameContainer;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.container.NBTElementContainer;
import iskallia.vault.core.net.ArrayBitBuffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModContainers {
    public static MenuType<NBTElementContainer<PowerTree>> POWERS_TAB_CONTAINER;
    public static MenuType<LootStatueContainer> LOOT_STATUE_CONTAINER;
    public static MenuType<RenameContainer> RENAMING_CONTAINER;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MenuType<?>> event) {
        POWERS_TAB_CONTAINER = IForgeMenuType.create((windowId, inventory, buffer) -> {
            PowerTree expertiseTree = new PowerTree();
            expertiseTree.readBits(ArrayBitBuffer.backing(buffer.readLongArray(), 0));
            return new NBTElementContainer(() -> {
                return POWERS_TAB_CONTAINER;
            }, windowId, inventory.player, expertiseTree);
        });

        LOOT_STATUE_CONTAINER = IForgeMenuType.create((windowId, inventory, buffer) -> {
            CompoundTag nbt = buffer.readNbt();
            return new LootStatueContainer(windowId, nbt == null ? new CompoundTag() : nbt);
        });

        RENAMING_CONTAINER = IForgeMenuType.create((windowId, inventory, buffer) -> {
            CompoundTag nbt = buffer.readNbt();
            return new RenameContainer(windowId, nbt == null ? new CompoundTag() : nbt);
        });


        event.getRegistry().register(POWERS_TAB_CONTAINER.setRegistryName("power_tab"));
        event.getRegistry().register(LOOT_STATUE_CONTAINER.setRegistryName("loot_statue_container"));
        event.getRegistry().register(RENAMING_CONTAINER.setRegistryName("renaming_container"));
    }
}
