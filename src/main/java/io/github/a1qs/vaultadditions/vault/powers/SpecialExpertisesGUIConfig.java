package io.github.a1qs.vaultadditions.vault.powers;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.SkillStyle;

import java.util.HashMap;

public class SpecialExpertisesGUIConfig extends Config {
    @Expose
    private HashMap<String, SkillStyle> styles;

    public SpecialExpertisesGUIConfig() {
    }

    public String getName() {
        return "vaultadditions_special_expertises_gui_styles";
    }

    public HashMap<String, SkillStyle> getStyles() {
        return this.styles;
    }

    protected void reset() {
        this.styles = new HashMap<>();
    }
}
