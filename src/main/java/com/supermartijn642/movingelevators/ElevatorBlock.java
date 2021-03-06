package com.supermartijn642.movingelevators;

import com.supermartijn642.movingelevators.base.ElevatorInputBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created 3/28/2020 by SuperMartijn642
 */
public class ElevatorBlock extends ElevatorInputBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public ElevatorBlock(){
        super("elevator_block", ElevatorBlockTile::new);
    }

    @Override
    protected void onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult){
        if(player != null && player.getHeldItem(handIn).getItem() instanceof ButtonBlockItem){
            if(!worldIn.isRemote){
                ItemStack stack = player.getHeldItem(handIn);
                CompoundNBT tag = stack.getOrCreateTag();
                tag.putInt("controllerDim", worldIn.dimension.getType().getId());
                tag.putInt("controllerX", pos.getX());
                tag.putInt("controllerY", pos.getY());
                tag.putInt("controllerZ", pos.getZ());
                player.sendMessage(new TranslationTextComponent("block.movingelevators.button_block.bind").applyTextStyle(TextFormatting.YELLOW));
            }
        }else if(state.get(FACING) != rayTraceResult.getFace()){
            if(worldIn.isRemote)
                ClientProxy.openElevatorScreen(pos);
        }else
            super.onRightClick(state, worldIn, pos, player, handIn, rayTraceResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block,BlockState> builder){
        builder.add(FACING);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving){
        if(state.getBlock() != newState.getBlock()){
            TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof ElevatorBlockTile)
                ((ElevatorBlockTile)tile).onBreak();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state){
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World worldIn, BlockPos pos){
        if(!state.has(FACING))
            return 0;
        return worldIn.isAirBlock(pos.offset(state.get(FACING)).down()) ? 0 : 15;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving){
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof ElevatorBlockTile)
            ((ElevatorBlockTile)tile).redstone = world.isBlockPowered(pos) || world.isBlockPowered(pos.up());
    }
}
