package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.config.Config;

public class PowerConfig extends Config {
    @Expose
    public PowerTree tree;

    public PowerConfig() {
    }

    public String getName() {
        return "vaultadditions_power";
    }

    public PowerTree getAll() {
        return this.tree == null ? new PowerTree() : this.tree;
    }

    protected boolean isValid() {
        return this.tree != null;
    }

    protected void reset() {

    }

}

