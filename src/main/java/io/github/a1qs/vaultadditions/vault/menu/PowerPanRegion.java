package io.github.a1qs.vaultadditions.vault.menu;

import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.pan.SkillPanRegion;
import iskallia.vault.client.gui.screen.player.legacy.widget.TalentGroupWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.skill.base.TieredSkill;
import net.minecraft.network.chat.TextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerPanRegion extends SkillPanRegion<PowerTree, PowerElementContainerScreen, PowerWidget> {

    public PowerPanRegion(PowerDialog powerDialog, PowerElementContainerScreen parentScreen) {
        super(parentScreen, new TextComponent("Talents Tab"), powerDialog);
    }

    protected void initSkillWidget(PowerTree skillTree, String skillName, SkillStyle style, Map<String, PowerWidget> widgets, List<TalentGroupWidget> groups) {
        widgets.put(skillName, new PowerWidget((TieredSkill)skillTree.getForId(skillName).orElse(null), skillTree, style));
    }

    protected HashMap<String, SkillStyle> getStyles() {
        return CustomVaultConfigRegistry.POWERS_GUI.getStyles();
    }


}
