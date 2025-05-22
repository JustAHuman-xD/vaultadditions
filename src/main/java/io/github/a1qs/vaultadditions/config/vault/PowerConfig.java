package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.config.Config;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public class PowerConfig extends Config {
    @Expose
    private PowerTree tree;

    @Override
    protected void onLoad(@Nullable Config oldConfigInstance) {
        if (this.tree == null) {
            VaultAdditions.LOGGER.warn("[PowerConfig] No tree found, creating a new one.");
            this.tree = new PowerTree();
        }

        DistExecutor.safeCallWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            PlayerPowersData.getServer().scheduleMerge();
            return null;
        });
    }

    public String getName() {
        return "vaultadditions_power";
    }

    public PowerTree getTree() {
        return this.tree;
    }

    @Override
    protected boolean isValid() {
        return this.tree != null;
    }

    @Override
    protected void reset() {}
}

