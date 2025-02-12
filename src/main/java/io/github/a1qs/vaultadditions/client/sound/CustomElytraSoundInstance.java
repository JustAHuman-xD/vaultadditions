package io.github.a1qs.vaultadditions.client.sound;

import io.github.a1qs.vaultadditions.init.ModSounds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomElytraSoundInstance extends AbstractTickableSoundInstance {
    public static final int DELAY = 20;
    private final LocalPlayer player;
    private int time;

    public CustomElytraSoundInstance(LocalPlayer pPlayer) {
        super(ModSounds.HOY_ELYTRA_GLIDE.get(), SoundSource.PLAYERS);
        this.player = pPlayer;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1F;
    }

    public void tick() {
        ++this.time;
        if (!this.player.isRemoved() && (this.time <= DELAY || this.player.isFallFlying())) {
            this.x = (float)this.player.getX();
            this.y = (float)this.player.getY();
            this.z = (float)this.player.getZ();
            float f = (float)this.player.getDeltaMovement().lengthSqr();
            if ((double)f >= 1.0E-7D) {
                this.volume = Mth.clamp(f / 32.0F, 0.0F, 0.4F);
            } else {
                this.volume = 0.0F;
            }

            if (this.time < DELAY) {
                this.volume = 0.0F;
            } else if (this.time < 40) {
                this.volume *= (float)(this.time - DELAY) / 40.0F;
            }

            float f1 = 0.8F;
            if (this.volume > 0.8F) {
                this.pitch = 1.0F + (this.volume - 0.8F);
            } else {
                this.pitch = 1.0F;
            }

            this.volume *= 0.2F; // Reduce to 20% of current calculated volume
        } else {
            this.stop();
        }
    }

}
