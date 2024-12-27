package io.github.a1qs.vaultadditions.block.blockentity;

import io.github.a1qs.vaultadditions.init.ModBlockEntities;
import iskallia.vault.container.oversized.OverSizedItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class PlayerTraderBlockEntity extends BlockEntity {
    private ItemStack offer = ItemStack.EMPTY;
    private OverSizedItemStack currency = OverSizedItemStack.EMPTY;

    public PlayerTraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLAYER_TRADER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition, worldPosition.offset(1, 2, 1));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.offer = ItemStack.of(tag.getCompound("offerStack"));
        this.currency = OverSizedItemStack.deserialize(tag.getCompound("currencyStack"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("offerStack", this.offer.serializeNBT());
        tag.put("currencyStack", this.currency.serialize());
    }



    public ItemStack getOfferStack() {
        return this.offer.copy();
    }

    public ItemStack getCurrencyStack() {
        return this.currency.overSizedStack().copy();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void clear() {
        this.offer = ItemStack.EMPTY;
        this.currency = OverSizedItemStack.EMPTY;
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public void setOffer(ItemStack offer, OverSizedItemStack currency) {
        this.offer = offer;
        this.currency = currency;
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
}
