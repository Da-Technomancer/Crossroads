package com.Da_Technomancer.crossroads.items.itemSets;

import com.Da_Technomancer.crossroads.API.rotary.RotaryUtil;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.tileentities.rotary.mechanisms.IMechanism;
import com.Da_Technomancer.crossroads.tileentities.rotary.mechanisms.MechanismTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicGear extends GearMatItem{

	public BasicGear(){
		this("gear_base");
	}

	protected BasicGear(String name){
		super();
		setRegistryName(name);
	}

	@Override
	protected double shapeFactor(){
		return 0.125D / 8D;
	}

	protected IMechanism mechanismToPlace(){
		return MechanismTileEntity.MECHANISMS.get(0);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context){
		GearFactory.GearMaterial type = getMaterial(context.getItemInHand());
		if(type == null){
			return ActionResultType.SUCCESS;
		}
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();//The position of the block clicked
		Direction side = context.getClickedFace();
		BlockPos placePos = pos.relative(side);//Where the gear will be placed
		PlayerEntity playerIn = context.getPlayer();
		BlockState stateAtPlacement = world.getBlockState(placePos);
		TileEntity teAtPlacement = world.getBlockEntity(placePos);

		//Must be able to place against a solid surface
		if(RotaryUtil.solidToGears(world, pos, side)){
			int mechInd = side.getOpposite().get3DDataValue();//Index this gear would be placed within the mechanism
			if(teAtPlacement instanceof MechanismTileEntity){
				//Existing mechanism TE to expand
				MechanismTileEntity mte = (MechanismTileEntity) teAtPlacement;
				if(mte.members[mechInd] != null){
					//This spot is already taken
					return ActionResultType.SUCCESS;
				}

				mte.setMechanism(mechInd, mechanismToPlace(), type, null, false);

				//Consume an item
				if(!world.isClientSide && (playerIn == null || !playerIn.isCreative())){
					context.getItemInHand().shrink(1);
				}

				RotaryUtil.increaseMasterKey(!world.isClientSide);
			}else if(stateAtPlacement.canBeReplaced(new BlockItemUseContext(context))){
				//No existing mechanism- we will create a new one
				world.setBlock(placePos, CRBlocks.mechanism.defaultBlockState(), 3);

				teAtPlacement = world.getBlockEntity(placePos);
				if(teAtPlacement instanceof MechanismTileEntity){
					((MechanismTileEntity) teAtPlacement).setMechanism(mechInd, mechanismToPlace(), type, null, true);
				}else{
					//Log an error
					Crossroads.logger.error("Mechanism TileEntity did not exist at gear placement; Report to mod author");
				}

				//Consume an item
				if(!world.isClientSide && (playerIn == null || !playerIn.isCreative())){
					context.getItemInHand().shrink(1);
				}

				RotaryUtil.increaseMasterKey(!world.isClientSide);
			}
		}

		return ActionResultType.SUCCESS;
	}
}
