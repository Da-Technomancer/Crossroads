package com.Da_Technomancer.crossroads.tileentities.technomancy;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.IInfoTE;
import com.Da_Technomancer.essentials.shared.IAxleHandler;
import com.Da_Technomancer.essentials.blocks.EssentialsProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class HamsterWheelTileEntity extends TileEntity implements ITickable, IInfoTE{

	public float angle = 0;
	public float nextAngle = 0;

	@Override
	public void update(){
		EnumFacing facing = world.getBlockState(pos).getValue(EssentialsProperties.FACING);
		TileEntity te = world.getTileEntity(pos.offset(facing));
		if(te != null && te.hasCapability(Capabilities.AXLE_HANDLER_CAPABILITY, facing.getOpposite())){
			IAxleHandler axle = te.getCapability(Capabilities.AXLE_HANDLER_CAPABILITY, facing.getOpposite());
			if(world.isRemote){
				angle = axle.getAngle();
				nextAngle = axle.getNextAngle();
				return;
			}
			axle.addEnergy(2 * facing.getAxisDirection().getOffset(), true, true);
		}else if(world.isRemote){
			nextAngle = angle;//
		}
	}
}