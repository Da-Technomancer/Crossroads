package com.Da_Technomancer.crossroads.blocks.fluid;

import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.tileentities.fluid.FatCongealerTileEntity;
import com.Da_Technomancer.essentials.ESConfig;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class FatCongealer extends ContainerBlock{

	public FatCongealer(){
		super(CRBlocks.getMetalProperty());
		String name = "fat_congealer";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this);
		registerDefaultState(defaultBlockState().setValue(ESProperties.HORIZ_FACING, Direction.NORTH));
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn){
		return new FatCongealerTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state){
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
		builder.add(ESProperties.HORIZ_FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return defaultBlockState().setValue(ESProperties.HORIZ_FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		if(newState.getBlock() != this){
			InventoryHelper.dropContents(world, pos, (IInventory) world.getBlockEntity(pos));
		}
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		TileEntity te;
		if(ESConfig.isWrench(playerIn.getItemInHand(hand))){
			if(!worldIn.isClientSide){
				worldIn.setBlockAndUpdate(pos, state.cycle(ESProperties.HORIZ_FACING));
			}
		}else if(!worldIn.isClientSide && (te = worldIn.getBlockEntity(pos)) instanceof INamedContainerProvider){
			NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) te, pos);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		tooltip.add(new TranslationTextComponent("tt.crossroads.fat_congealer.desc"));
		tooltip.add(new TranslationTextComponent("tt.crossroads.fat_congealer.hun", FatCongealerTileEntity.HUN_PER_SPD));
		tooltip.add(new TranslationTextComponent("tt.crossroads.fat_congealer.sat", FatCongealerTileEntity.SAT_PER_SPD));
		tooltip.add(new TranslationTextComponent("tt.crossroads.fat_congealer.quip").setStyle(MiscUtil.TT_QUIP));
	}
}
