package com.Da_Technomancer.crossroads.API.effects.goggles;

import java.util.ArrayList;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.MiscOp;
import com.Da_Technomancer.crossroads.API.Properties;
import com.Da_Technomancer.crossroads.API.packets.ModPackets;
import com.Da_Technomancer.crossroads.API.packets.SendFieldsToClient;
import com.Da_Technomancer.crossroads.API.rotary.IAxleHandler;
import com.Da_Technomancer.crossroads.API.technomancy.FieldWorldSavedData;
import com.Da_Technomancer.crossroads.blocks.ModBlocks;
import com.Da_Technomancer.crossroads.tileentities.technomancy.GatewayFrameTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EmeraldGoggleEffect implements IGoggleEffect{

	@Override
	public void armorTick(World world, EntityPlayer player, ArrayList<String> chat, RayTraceResult ray){
		if(ray != null){
			TileEntity te = world.getTileEntity(ray.getBlockPos());
			if(te != null){

				if(te.hasCapability(Capabilities.AXLE_HANDLER_CAPABILITY, ray.sideHit.getOpposite())){
					IAxleHandler axle = te.getCapability(Capabilities.AXLE_HANDLER_CAPABILITY, ray.sideHit.getOpposite());
					chat.add("Speed: " + axle.getMotionData()[0]);
					chat.add("Energy: " + axle.getMotionData()[1]);
					chat.add("Power: " + axle.getMotionData()[2]);
					chat.add("I: " + axle.getPhysData()[1] + ", Rotation Ratio: " + axle.getRotationRatio());
				}else if(te.hasCapability(Capabilities.AXLE_HANDLER_CAPABILITY, ray.sideHit)){
					IAxleHandler axle = te.getCapability(Capabilities.AXLE_HANDLER_CAPABILITY, ray.sideHit);
					chat.add("Speed: " + axle.getMotionData()[0]);
					chat.add("Energy: " + axle.getMotionData()[1]);
					chat.add("Power: " + axle.getMotionData()[2]);
					chat.add("I: " + axle.getPhysData()[1] + ", Rotation Ratio: " + axle.getRotationRatio());
				}else if(te.hasCapability(Capabilities.AXIS_HANDLER_CAPABILITY, null)){
					chat.add("Total Energy: " + te.getCapability(Capabilities.AXIS_HANDLER_CAPABILITY, null).getTotalEnergy());
				}else if(world.getBlockState(ray.getBlockPos()) == ModBlocks.gatewayFrame.getDefaultState().withProperty(Properties.FACING, EnumFacing.UP)){
					GatewayFrameTileEntity frame = (GatewayFrameTileEntity) te;
					if(frame.dialedCoord(Axis.X) != null){
						chat.add("Dialed: " + frame.dialedCoord(Axis.X).getCoord() + ", " + frame.getCoord() + ", " + frame.dialedCoord(Axis.Z).getCoord());
					}
				}
			}
		}

		if(world.getTotalWorldTime() % 5 == 1){
			if(FieldWorldSavedData.get(world).fieldNodes.containsKey(MiscOp.getLongFromChunkPos(new ChunkPos(player.getPosition())))){
				ModPackets.network.sendTo(new SendFieldsToClient(FieldWorldSavedData.get(world).fieldNodes.get(MiscOp.getLongFromChunkPos(new ChunkPos(player.getPosition())))[1], (byte) 1, MiscOp.getLongFromChunkPos(new ChunkPos(player.getPosition()))), (EntityPlayerMP) player);
			}else{
				ModPackets.network.sendTo(new SendFieldsToClient(new byte[1][1], (byte) -1, MiscOp.getLongFromChunkPos(new ChunkPos(player.getPosition()))), (EntityPlayerMP) player);
			}
		}
	}
}