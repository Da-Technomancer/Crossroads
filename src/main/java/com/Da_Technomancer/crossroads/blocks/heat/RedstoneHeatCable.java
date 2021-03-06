package com.Da_Technomancer.crossroads.blocks.heat;

import com.Da_Technomancer.crossroads.API.alchemy.EnumTransferMode;
import com.Da_Technomancer.crossroads.API.heat.HeatInsulators;
import com.Da_Technomancer.crossroads.tileentities.heat.RedstoneHeatCableTileEntity;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import com.Da_Technomancer.essentials.blocks.redstone.IReadable;
import com.Da_Technomancer.essentials.blocks.redstone.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RedstoneHeatCable extends HeatCable implements IReadable{

	public RedstoneHeatCable(HeatInsulators insulator){
		super(insulator, "redstone_heat_cable_" + insulator.toString().toLowerCase());
		registerDefaultState(defaultBlockState().setValue(ESProperties.REDSTONE_BOOL, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		if(state.getValue(ESProperties.REDSTONE_BOOL)){
			return super.getShape(state, worldIn, pos, context);
		}else{
			return SHAPES[0];//Core only
		}
	}

	@Override
	protected boolean evaluate(EnumTransferMode value, BlockState state, @Nullable IConduitTE<EnumTransferMode> te){
		return super.evaluate(value, state, te) && state.getValue(ESProperties.REDSTONE_BOOL);
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
		neighborChanged(state, world, pos, this, pos, false);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
		super.createBlockStateDefinition(builder);
		builder.add(ESProperties.REDSTONE_BOOL);
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving){
		if(worldIn.hasNeighborSignal(pos)){
			if(!state.getValue(ESProperties.REDSTONE_BOOL)){
				worldIn.setBlockAndUpdate(pos, state.setValue(ESProperties.REDSTONE_BOOL, true));
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}
		}else if(state.getValue(ESProperties.REDSTONE_BOOL)){
			worldIn.setBlockAndUpdate(pos, state.setValue(ESProperties.REDSTONE_BOOL, false));
			worldIn.updateNeighbourForOutputSignal(pos, this);
		}
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn){
		return new RedstoneHeatCableTileEntity(insulator);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context).setValue(ESProperties.REDSTONE_BOOL, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		super.appendHoverText(stack, world, tooltip, advanced);

		tooltip.add(new TranslationTextComponent("tt.crossroads.redstone_heat_cable.reader"));

		/*
		tooltip.add("");

		tooltip.add("Comparators read: ");
		for(int i = 0; i < 5; i++){
			StringBuilder line = new StringBuilder(50);
			for(int j = 3*i + 1; j <= 3*i + 3; j++){
				line.append("  ").append(j).append(" above ").append(Integer.toString((int) Math.round(HeatUtil.toCelcius(j * HeatUtil.toKelvin(insulator.getLimit()) / 16D)))).append("°C ");
			}
			tooltip.add(line.toString());
		}
		*/
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state){
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos){
		return RedstoneUtil.clampToVanilla(read(worldIn, pos, blockState));//A rather pointless output- ranging from 0 to 15 degrees C
	}

	@Override
	public float read(World world, BlockPos pos, BlockState blockState){
		TileEntity te = world.getBlockEntity(pos);
		if(te instanceof RedstoneHeatCableTileEntity){
			return ((RedstoneHeatCableTileEntity) te).getTemp();
		}
		return 0;
	}
}
