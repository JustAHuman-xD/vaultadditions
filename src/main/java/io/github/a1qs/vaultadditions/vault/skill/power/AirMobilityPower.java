package io.github.a1qs.vaultadditions.vault.skill.power;

import com.google.gson.JsonObject;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.client.ClientPowerData;
import io.github.a1qs.vaultadditions.events.KeybindEvents;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.Skill;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(value = Dist.CLIENT,  modid = VaultAdditions.MOD_ID)
public class AirMobilityPower extends LearnableSkill {
    private float playerBaseSpeed;
    private float playerBaseAirMovement;

    public AirMobilityPower(int unlockLevel, int learnPointCost, int regretPointCost, float playerBaseSpeed, float playerBaseAirMovement) {
        super(unlockLevel, learnPointCost, regretPointCost);
        this.playerBaseSpeed = playerBaseSpeed;
        this.playerBaseAirMovement = playerBaseAirMovement;
    }

    public AirMobilityPower() {
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAirMobilityTick(TickEvent.PlayerTickEvent event) {
        if(KeybindEvents.isZephyrToggled) return;

        PowerTree tree = ClientPowerData.getPowerTree();
        for(AirMobilityPower power : tree.getAll(AirMobilityPower.class, Skill::isUnlocked)) {
            event.player.setSpeed(power.playerBaseSpeed);
            event.player.flyingSpeed = power.playerBaseAirMovement;
        }
    }





    public void writeBits(BitBuffer buffer) {
        super.writeBits(buffer);
        Adapters.FLOAT.writeBits(this.playerBaseSpeed, buffer);
        Adapters.FLOAT.writeBits(this.playerBaseAirMovement, buffer);
    }

    public void readBits(BitBuffer buffer) {
        super.readBits(buffer);
        this.playerBaseSpeed = Adapters.FLOAT.readBits(buffer).orElseThrow();
        this.playerBaseAirMovement = Adapters.FLOAT.readBits(buffer).orElseThrow();
    }

    public Optional<CompoundTag> writeNbt() {
        return super.writeNbt().map((nbt) -> {
            Adapters.FLOAT.writeNbt(this.playerBaseSpeed).ifPresent((tag) -> {
                nbt.put("playerBaseSpeed", tag);
            });
            Adapters.FLOAT.writeNbt(this.playerBaseAirMovement).ifPresent((tag) -> {
                nbt.put("playerBaseAirMovement", tag);
            });
            return nbt;
        });
    }

    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        this.playerBaseSpeed = Adapters.FLOAT.readNbt(nbt.get("playerBaseSpeed")).orElseThrow(() -> {
            return new IllegalStateException("Unknown attribute in " + nbt);
        });
        this.playerBaseAirMovement = Adapters.FLOAT.readNbt(nbt.get("playerBaseAirMovement")).orElseThrow();
    }

    public Optional<JsonObject> writeJson() {
        return super.writeJson().map((json) -> {
            Adapters.FLOAT.writeJson(this.playerBaseSpeed).ifPresent((element) -> {
                json.add("playerBaseSpeed", element);
            });
            Adapters.FLOAT.writeJson(this.playerBaseAirMovement).ifPresent((element) -> {
                json.add("playerBaseAirMovement", element);
            });
            return json;
        });
    }

    public void readJson(JsonObject json) {
        super.readJson(json);
        this.playerBaseSpeed = Adapters.FLOAT.readJson(json.get("playerBaseSpeed")).orElseThrow();
        this.playerBaseAirMovement = Adapters.FLOAT.readJson(json.get("playerBaseAirMovement")).orElseThrow();
    }


}
