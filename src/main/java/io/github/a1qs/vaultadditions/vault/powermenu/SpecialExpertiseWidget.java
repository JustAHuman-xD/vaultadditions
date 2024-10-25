package io.github.a1qs.vaultadditions.vault.powermenu;

import iskallia.vault.client.gui.screen.player.legacy.widget.SkillWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.skill.base.TieredSkill;
import net.minecraft.network.chat.TextComponent;

public class SpecialExpertiseWidget extends SkillWidget<SpecialExpertiseTree> {

    public SpecialExpertiseWidget(TieredSkill skill, SpecialExpertiseTree expertiseTree, SkillStyle style) {
        super(expertiseTree, new TextComponent("the_vault.widgets.special_expertise"), skill, style); //todo: change to own modname?
    }

}
