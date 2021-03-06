package com.Da_Technomancer.crossroads.tileentities.technomancy;

import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.API.beams.BeamUnit;
import com.Da_Technomancer.crossroads.API.beams.BeamUnitStorage;
import com.Da_Technomancer.crossroads.API.beams.BeamUtil;
import com.Da_Technomancer.crossroads.API.templates.BeamRenderTE;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Crossroads.MODID)
public class ClockworkStabilizerTileEntity extends BeamRenderTE{

	@ObjectHolder("clock_stab")
	public static TileEntityType<ClockworkStabilizerTileEntity> type = null;

	public static final double RATE = 0.2D;
	private BeamUnitStorage storage = new BeamUnitStorage();
	private Direction dir = null;

	public ClockworkStabilizerTileEntity(){
		super(type);
	}

	@Override
	protected int getLimit(){
		return (int) (BeamUtil.POWER_LIMIT / RATE);//This block can store 5 times as much so it emits a full power beam at capacity
	}

	private Direction getDir(){
		if(dir == null){
			BlockState state = getBlockState();
			if(state.getBlock() != CRBlocks.clockworkStabilizer){
				return Direction.NORTH;
			}
			dir = state.getValue(ESProperties.FACING);
		}
		return dir;
	}

	@Override
	public void clearCache(){
		super.clearCache();
		dir = null;
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt){
		super.save(nbt);
		storage.writeToNBT("stab_mag", nbt);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt){
		super.load(state, nbt);
		storage = BeamUnitStorage.readFromNBT("stab_mag", nbt);
	}

	@Override
	protected void doEmit(BeamUnit toEmit){
		storage.addBeam(toEmit);

		//Enforce LIMIT
		if(storage.getPower() > getLimit()){
			BeamUnit stored = storage.getOutput();
			storage.clear();
			storage.addBeam(stored.mult((double) getLimit() / (double) stored.getPower(), true));
		}

		Direction dir = getDir();

		if(!storage.isEmpty()){
			double toWithdraw = RATE * storage.getPower();
			if(toWithdraw < 1){
				toWithdraw = 1;//Withdraw a minimum of 1, to prevent a small quantity getting 'stuck'
			}else{
				toWithdraw = Math.round(toWithdraw);
			}
			BeamUnit output = new BeamUnit(MiscUtil.withdrawExact(storage.getOutput().getValues(), (int) toWithdraw));
			storage.subtractBeam(output);
			if(beamer[dir.get3DDataValue()].emit(output, level)){
				refreshBeam(dir.get3DDataValue());
			}
		}else if(beamer[dir.get3DDataValue()].emit(BeamUnit.EMPTY, level)){
			refreshBeam(dir.get3DDataValue());
		}
	}

	@Override
	protected boolean[] inputSides(){
		boolean[] out = {true, true, true, true, true, true};
		out[getDir().get3DDataValue()] = false;
		return out;
	}

	@Override
	protected boolean[] outputSides(){
		boolean[] out = new boolean[6];
		out[getDir().get3DDataValue()] = true;
		return out;
	}

	public int getRedstone(){
		return storage.getPower();
	}
} 
