package io.github.a1qs.vaultadditions.mixins.accessors;

import iskallia.vault.snapshot.AttributeSnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AttributeSnapshot.AttributeValue.class)
public interface InvokeAttributeSnapshotAttributeValue {
    @Invoker("<init>")
    static AttributeSnapshot.AttributeValue invokeConstructor() {
        throw new AssertionError();
    }

    @Invoker(value = "addCachedValue", remap = false)
    void invokeAddCachedValue(Object object);
}
