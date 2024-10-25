package io.github.a1qs.vaultadditions.vault.powermenu;

import io.github.a1qs.vaultadditions.init.ModNetwork;
import io.github.a1qs.vaultadditions.network.SpecialExpertiseLevelMessage;
import io.github.a1qs.vaultadditions.vault.powers.PowerConfigs;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.dialog.SkillDialog;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.source.SkillSource;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

public class SpecialExpertiseDialog extends SkillDialog<SpecialExpertiseTree, SpecialExpertiseElementContainerScreen> {

    public SpecialExpertiseDialog(SpecialExpertiseTree talentTree, SpecialExpertiseElementContainerScreen expertisesElementContainerScreen) {
        super(talentTree, expertisesElementContainerScreen);
    }

    protected int getUnspentSkillPoints() {
        return VaultBarOverlay.unspentArchetypePoints;
    }

    protected void updateRegretButton() {
        this.regretButton = null;
    }

    protected HashMap<String, SkillStyle> getStyles() {
        return PowerConfigs.SPECIAL_EXPERTISES_GUI.getStyles();
    }

    protected SkillContext getSkillContext() {
        return new SkillContext(VaultBarOverlay.vaultLevel, VaultBarOverlay.unspentArchetypePoints, 0, SkillSource.of(Minecraft.getInstance().player));
    }

    protected void sendUpgradeMessage() {
        ModNetwork.CHANNEL.sendToServer(new SpecialExpertiseLevelMessage(this.skillGroup.getId(), true));
    }

}
