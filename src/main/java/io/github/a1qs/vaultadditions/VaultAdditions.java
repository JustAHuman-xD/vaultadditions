package io.github.a1qs.vaultadditions;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(VaultAdditions.MOD_ID)
public class VaultAdditions {
    public static final String MOD_ID = "vaultadditions";

    public VaultAdditions() {
        MinecraftForge.EVENT_BUS.register(this);
    }


}
