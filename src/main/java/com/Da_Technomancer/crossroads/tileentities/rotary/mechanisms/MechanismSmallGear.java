package com.Da_Technomancer.crossroads.tileentities.rotary.mechanisms;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.API.rotary.*;
import com.Da_Technomancer.crossroads.items.itemSets.GearFactory;
import com.Da_Technomancer.crossroads.render.TESR.models.ModelGearOctagon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MechanismSmallGear implements IMechanism{

	protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[6];
	static{
		BOUNDING_BOXES[0] = new AxisAlignedBB(0D, 0D, 0D, 1D, .125D, 1D);//DOWN
		BOUNDING_BOXES[1] = new AxisAlignedBB(0D, .875D, 0D, 1D, 1D, 1D);//UP
		BOUNDING_BOXES[2] = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, .125D);//NORTH
		BOUNDING_BOXES[3] = new AxisAlignedBB(0D, 0D, .875D, 1D, 1D, 1D);//SOUTH
		BOUNDING_BOXES[4] = new AxisAlignedBB(0D, 0D, 0D, .125D, 1D, 1D);//WEST
		BOUNDING_BOXES[5] = new AxisAlignedBB(.875D, 0D, 0D, 1D, 1D, 1D);//EAST
	}

	@Override
	public double getInertia(GearFactory.GearMaterial mat, @Nullable EnumFacing side, @Nullable EnumFacing.Axis axis){
		// assume each gear is 1/8 of a cubic meter and has a radius of 1/2 meter.
		// mass is rounded to make things nicer for everyone
		return 0.125D * MiscUtil.betterRound(mat.getDensity() / 8, 1);// .125 because r*r/2 so .5*.5/2
	}

	@Override
	public boolean hasCap(Capability<?> cap, EnumFacing capSide, GearFactory.GearMaterial mat, @Nullable EnumFacing side, @Nullable EnumFacing.Axis axis, MechanismTileEntity te){
		return (cap == Capabilities.COG_CAPABILITY || cap == Capabilities.AXLE_CAPABILITY) && side == capSide;
	}

	@Override
	public void propogate(GearFactory.GearMaterial mat, @Nullable EnumFacing side, @Nullable EnumFacing.Axis axis, MechanismTileEntity te, MechanismTileEntity.SidedAxleHandler handler, IAxisHandler masterIn, byte key, double rotRatioIn, double lastRadius){
		//This mechanism should never be in the axle slot
		if(side == null){
			return;
		}

		if(lastRadius != 0){
			rotRatioIn *= lastRadius * 2D;
		}

		//If true, this has already been checked.
		if(key == handler.updateKey){
			//If true, there is rotation conflict.
			if(handler.rotRatio != rotRatioIn){
				masterIn.lock();
			}
			return;
		}

		if(masterIn.addToList(handler)){
			return;
		}

		handler.rotRatio = rotRatioIn;
		handler.updateKey = key;

		//Other internal gears
		for(int i = 0; i < 6; i++){
			if(i != side.getIndex() && i != side.getOpposite().getIndex() && te.members[i] != null && te.members[i].hasCap(Capabilities.COG_CAPABILITY, EnumFacing.byIndex(i), te.mats[i], EnumFacing.byIndex(i), te.axleAxis, te)){
				te.axleHandlers[i].propogate(masterIn, key, RotaryUtil.getDirSign(side, EnumFacing.byIndex(i)) * handler.rotRatio, .5D, !handler.renderOffset);
			}
		}

		TileEntity sideTE = te.getWorld().getTileEntity(te.getPos().offset(side));
		for(int i = 0; i < 6; i++){
			if(i != side.getIndex() && i != side.getOpposite().getIndex()){
				EnumFacing facing = EnumFacing.byIndex(i);
				// Adjacent gears
				TileEntity adjTE = te.getWorld().getTileEntity(te.getPos().offset(facing));
				if(adjTE != null){
					ICogHandler cogHandler;
					if((cogHandler = adjTE.getCapability(Capabilities.COG_CAPABILITY, side)) != null){
						cogHandler.connect(masterIn, key, -handler.rotRatio, .5D, facing.getOpposite(), handler.renderOffset);
					}else if((cogHandler = adjTE.getCapability(Capabilities.COG_CAPABILITY, facing.getOpposite())) != null){
						//Check for large gears
						cogHandler.connect(masterIn, key, RotaryUtil.getDirSign(side, facing) * handler.rotRatio, .5D, side, handler.renderOffset);
					}
				}

				// Diagonal gears
				TileEntity diagTE = te.getWorld().getTileEntity(te.getPos().offset(facing).offset(side));
				ICogHandler cogHandler;
				if(diagTE != null && (cogHandler = diagTE.getCapability(Capabilities.COG_CAPABILITY, facing.getOpposite())) != null && RotaryUtil.canConnectThrough(te.getWorld(), te.getPos().offset(facing), facing.getOpposite(), side)){
					cogHandler.connect(masterIn, key, -RotaryUtil.getDirSign(side, facing) * handler.rotRatio, .5D, side.getOpposite(), handler.renderOffset);
				}

				if(sideTE != null && (cogHandler = sideTE.getCapability(Capabilities.COG_CAPABILITY, facing)) != null){
					cogHandler.connect(masterIn, key, -RotaryUtil.getDirSign(side, facing) * rotRatioIn, .5D, side.getOpposite(), handler.renderOffset);
				}
			}
		}

		//Connected block
		if(sideTE != null){
			IAxisHandler axisHandler;
			if((axisHandler = sideTE.getCapability(Capabilities.AXIS_CAPABILITY, side.getOpposite())) != null){
				axisHandler.trigger(masterIn, key);
			}
			ISlaveAxisHandler slaveHandler;
			if((slaveHandler = sideTE.getCapability(Capabilities.SLAVE_AXIS_CAPABILITY, side.getOpposite())) != null){
				masterIn.addAxisToList(slaveHandler, side.getOpposite());
			}
			IAxleHandler axleHandler;
			if((axleHandler = sideTE.getCapability(Capabilities.AXLE_CAPABILITY, side.getOpposite())) != null){
				axleHandler.propogate(masterIn, key, handler.rotRatio, 0, handler.renderOffset);
			}
		}

		//Axle slot
		if(te.axleAxis == side.getAxis() && te.members[6] != null && te.members[6].hasCap(Capabilities.AXLE_CAPABILITY, side, te.mats[6], null, te.axleAxis, te)){
			te.axleHandlers[6].propogate(masterIn, key, handler.rotRatio, 0, handler.renderOffset);
		}
	}

	@Nonnull
	@Override
	public ItemStack getDrop(GearFactory.GearMaterial mat){
		return new ItemStack(GearFactory.gearTypes.get(mat).getSmallGear(), 1);
	}

	@Override
	public AxisAlignedBB getBoundingBox(@Nullable EnumFacing side, @Nullable EnumFacing.Axis axis){
		return side == null ? Block.NULL_AABB : BOUNDING_BOXES[side.getIndex()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRender(MechanismTileEntity te, float partialTicks, GearFactory.GearMaterial mat, @Nullable EnumFacing side, @Nullable EnumFacing.Axis axis){
		if(side == null){
			return;
		}

		MechanismTileEntity.SidedAxleHandler handler = te.axleHandlers[side.getIndex()];

		GlStateManager.pushMatrix();
		GlStateManager.rotate(side == EnumFacing.DOWN ? 0 : side == EnumFacing.UP ? 180F : side == EnumFacing.NORTH || side == EnumFacing.EAST ? 90F : -90F, side.getAxis() == EnumFacing.Axis.Z ? 1 : 0, 0, side.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
		float angle = handler.getAngle(partialTicks);
		GlStateManager.translate(0, -0.4375F, 0);
		GlStateManager.rotate((float) -side.getAxisDirection().getOffset() * angle, 0F, 1F, 0F);
		ModelGearOctagon.render(mat.getColor());
		GlStateManager.popMatrix();
	}
}
