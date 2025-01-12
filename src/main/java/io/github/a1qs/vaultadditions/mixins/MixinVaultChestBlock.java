package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.init.vault.ModGearAttributes;
import iskallia.vault.block.VaultChestBlock;
import iskallia.vault.block.entity.VaultChestTileEntity;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = VaultChestBlock.class, remap = false, priority = 100)
public class MixinVaultChestBlock extends ChestBlock {

    public MixinVaultChestBlock(Properties p_51490_, Supplier<BlockEntityType<? extends ChestBlockEntity>> p_51491_) {
        super(p_51490_, p_51491_);
    }

    @Redirect(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"), remap = true)
    private boolean cancelDefaultBehaviour(ItemStack instance) {
        return false;
    }

    @Inject(method = "playerDestroy", at = @At(value = "INVOKE", target = "Liskallia/vault/block/entity/VaultChestTileEntity;getItem(I)Lnet/minecraft/world/item/ItemStack;"), remap = true)
    private void injectBreachingBehaviour(Level world, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack, CallbackInfo ci) {
        if (te instanceof VaultChestTileEntity chest) {
            boolean hasBreach = false;

            if(player.getMainHandItem().getItem() instanceof VaultGearItem) {
                VaultGearData data = VaultGearData.read(player.getMainHandItem().copy());
                hasBreach = data.hasAttribute(ModGearAttributes.BREACHING);
            }

            for (int slot = 0; slot < chest.getContainerSize(); ++slot) {
                ItemStack invStack = chest.getItem(slot);
                if (!invStack.isEmpty()) {
                    Block.popResource(world, pos, invStack);
                    chest.setItem(slot, ItemStack.EMPTY);

                    if (hasBreach) {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    } else {
                        break;
                    }
                }
            }
        }
    }


    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if(pState.getBlock() == ModBlocks.WOODEN_CHEST || pState.getBlock() == ModBlocks.WOODEN_BARREL) return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);

        if(!(pPlayer.getMainHandItem().getItem() instanceof VaultGearItem)) return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);

        VaultGearData data = VaultGearData.read(pPlayer.getMainHandItem().copy());

        boolean hasBreach = data.hasAttribute(ModGearAttributes.BREACHING);

        float f = pState.getDestroySpeed(pLevel, pPos);

        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = net.minecraftforge.common.ForgeHooks.isCorrectToolForDrops(pState, pPlayer) ? 30 : 100;
            i += hasBreach ? 1500 : 3;
            return pPlayer.getDigSpeed(pState, pPos) / f / (float)i;
        }
    }
}
