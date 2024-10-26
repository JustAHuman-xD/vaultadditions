package io.github.a1qs.vaultadditions.vault.powermenu;

import io.github.a1qs.vaultadditions.util.MiscUtil;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.pan.SkillPanRegion;
import iskallia.vault.client.gui.screen.player.legacy.widget.TalentGroupWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.skill.base.TieredSkill;
import net.minecraft.network.chat.TextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialExpertisePanRegion extends SkillPanRegion<SpecialExpertiseTree, SpecialExpertiseElementContainerScreen, SpecialExpertiseWidget> {

    public SpecialExpertisePanRegion(SpecialExpertiseDialog talentDialog, SpecialExpertiseElementContainerScreen parentScreen) {
        super(parentScreen, new TextComponent("Talents Tab"), talentDialog);
    }

    protected void initSkillWidget(SpecialExpertiseTree skillTree, String skillName, SkillStyle style, Map<String, SpecialExpertiseWidget> widgets, List<TalentGroupWidget> groups) {
        widgets.put(skillName, new SpecialExpertiseWidget((TieredSkill)skillTree.getForId(skillName).orElse(null), skillTree, style));
    }

    protected HashMap<String, SkillStyle> getStyles() {
        return MiscUtil.SPECIAL_EXPERTISES_GUI.getStyles();
    }

}
