package io.github.a1qs.vaultadditions.events;


import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.commands.CrystalCommands;
import io.github.a1qs.vaultadditions.commands.EventCommands;
import io.github.a1qs.vaultadditions.commands.PowerSkillCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = VaultAdditions.MOD_ID)
public class CommandEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new io.github.a1qs.vaultadditions.commands.ConfigCommand(event.getDispatcher());
        new PowerSkillCommands(event.getDispatcher());
        new CrystalCommands(event.getDispatcher());
        new EventCommands(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

}
