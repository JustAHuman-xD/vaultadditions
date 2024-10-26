package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.vault.powermenu.PowerTree;
import iskallia.vault.container.NBTElementContainer;
import iskallia.vault.core.net.ArrayBitBuffer;
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

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MenuType<?>> event) {
        POWERS_TAB_CONTAINER = IForgeMenuType.create((windowId, inventory, buffer) -> {
            PowerTree expertiseTree = new PowerTree();
            expertiseTree.readBits(ArrayBitBuffer.backing(buffer.readLongArray(), 0));
            return new NBTElementContainer(() -> {
                return POWERS_TAB_CONTAINER;
            }, windowId, inventory.player, expertiseTree);
        });


        event.getRegistry().register(POWERS_TAB_CONTAINER.setRegistryName("power_tab"));
    }
}
