package io.github.a1qs.vaultadditions.vault.powers;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import iskallia.vault.config.Config;

public class SpecialExpertisesConfig extends Config {
    @Expose
    public SpecialExpertiseTree tree;

    public SpecialExpertisesConfig() {
    }

    public String getName() {
        return "vaultadditions_expertises";
    }

    public SpecialExpertiseTree getAll() {
        return this.tree == null ? new SpecialExpertiseTree() : this.tree;
    }

    protected boolean isValid() {
        return this.tree != null;
    }

    protected void reset() {

    }

}

