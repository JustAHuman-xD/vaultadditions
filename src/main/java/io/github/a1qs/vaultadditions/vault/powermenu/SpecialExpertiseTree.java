package io.github.a1qs.vaultadditions.vault.powermenu;


import io.github.a1qs.vaultadditions.init.ModNetwork;
import io.github.a1qs.vaultadditions.network.KnownSpecialExpertiseMessage;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.tree.SkillTree;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

public class SpecialExpertiseTree extends SkillTree {
    public SpecialExpertiseTree() {
    }

    public void sync(SkillContext context) {
        this.syncTree(context);
    }

    public void syncTree(SkillContext context) {
        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            ModNetwork.CHANNEL.sendTo(new KnownSpecialExpertiseMessage(this), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        });
    }
}
