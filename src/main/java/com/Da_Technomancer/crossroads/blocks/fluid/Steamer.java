package com.Da_Technomancer.crossroads.blocks.fluid;

import com.Da_Technomancer.crossroads.API.CircuitUtil;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.tileentities.fluid.SteamerTileEntity;
import com.Da_Technomancer.essentials.blocks.redstone.IReadable;
import com.Da_Technomancer.essentials.blocks.redstone.RedstoneUtil;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
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

public class Steamer extends ContainerBlock implements IReadable{

	public Steamer(){
		super(CRBlocks.getMetalProperty());
		String name = "steamer";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this);
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn){
		return new SteamerTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state){
		return BlockRenderType.MODEL;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		InventoryHelper.dropContents(world, pos, (IInventory) world.getBlockEntity(pos));
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		TileEntity te;
		if(!worldIn.isClientSide && (te = worldIn.getBlockEntity(pos)) instanceof INamedContainerProvider){
			NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) te, pos);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		tooltip.add(new TranslationTextComponent("tt.crossroads.steamer.desc", SteamerTileEntity.REQUIRED));
		tooltip.add(new TranslationTextComponent("tt.crossroads.steamer.water", SteamerTileEntity.FLUID_USE));
		tooltip.add(new TranslationTextComponent("tt.crossroads.steamer.steam", SteamerTileEntity.FLUID_USE));
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state){
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, World worldIn, BlockPos pos){
		return RedstoneUtil.clampToVanilla(read(worldIn, pos, state));
	}

	@Override
	public float read(World world, BlockPos pos, BlockState state){
		TileEntity te = world.getBlockEntity(pos);
		if(te instanceof IInventory){
			return CircuitUtil.getRedstoneFromSlots((IInventory) te, 0);
		}else{
			return 0;
		}
	}
}
