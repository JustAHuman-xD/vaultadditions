package io.github.a1qs.vaultadditions.mixins.skillaltarfix;

import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.data.SkillAltarData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(SkillAltarData.class)
public class SkillAltarDataMixin {
    @Redirect(method = "save",at = @At(value = "INVOKE", target = "Liskallia/vault/util/nbt/NBTHelper;writeMap(Lnet/minecraft/nbt/CompoundTag;Ljava/lang/String;Ljava/util/Map;Ljava/util/function/Function;Ljava/util/function/Function;)V"))
    public <K extends Integer, V extends SkillAltarData.SkillTemplate> void saveNPE(CompoundTag tag, String key, Map<K, V> map, Function<K, String> getStringKey, Function<V, Tag> getNbtValue) {
        var nullSafeMap = new HashMap<K, V>();
        if (map != null) {
            for (var entry : map.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    nullSafeMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        NBTHelper.writeMap(tag, key, nullSafeMap, getStringKey, getNbtValue);
    }
}
