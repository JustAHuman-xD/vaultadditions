package io.github.a1qs.vaultadditions.vault.expertise;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.skill.base.LearnableSkill;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class FortunateVaultarExpertise extends LearnableSkill {
    @Expose
    private float altarCostReduction;

    public FortunateVaultarExpertise(int unlockLevel, int learnPointCost, int regretPointCost, float luckyAltarChance) {
        super(unlockLevel, learnPointCost, regretPointCost);
        this.altarCostReduction = luckyAltarChance;
    }

    public FortunateVaultarExpertise() {
    }

    public float getAltarCostReduction() {
        return this.altarCostReduction;
    }

    public void writeBits(BitBuffer buffer) {
        super.writeBits(buffer);
        Adapters.FLOAT.writeBits(this.altarCostReduction, buffer);
    }

    public void readBits(BitBuffer buffer) {
        super.readBits(buffer);
        this.altarCostReduction = Adapters.FLOAT.readBits(buffer).orElseThrow();
    }

    public Optional<CompoundTag> writeNbt() {
        return super.writeNbt().map((nbt) -> {
            Adapters.FLOAT.writeNbt(this.altarCostReduction).ifPresent((tag) -> {
                nbt.put("altarCostReduction", tag);
            });
            return nbt;
        });
    }

    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        this.altarCostReduction = Adapters.FLOAT.readNbt(nbt.get("altarCostReduction")).orElseThrow();
    }

    public Optional<JsonObject> writeJson() {
        return super.writeJson().map((json) -> {
            Adapters.FLOAT.writeJson(this.altarCostReduction).ifPresent((element) -> {
                json.add("altarCostReduction", element);
            });
            return json;
        });
    }

    public void readJson(JsonObject json) {
        super.readJson(json);
        this.altarCostReduction = Adapters.FLOAT.readJson(json.get("altarCostReduction")).orElseThrow();
    }
}
