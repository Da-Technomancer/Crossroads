package com.Da_Technomancer.crossroads.tileentities.heat;

import com.Da_Technomancer.crossroads.API.CRProperties;
import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.templates.InventoryTE;
import com.Da_Technomancer.crossroads.CRConfig;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.gui.container.FireboxContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

@ObjectHolder(Crossroads.MODID)
public class FireboxTileEntity extends InventoryTE{

	@ObjectHolder("firebox")
	private static TileEntityType<FireboxTileEntity> TYPE = null;

	public static final int POWER = 10;
	private static final int MAX_TEMP = 15_000;

	private int burnTime;
	private int maxBurnTime = 0;

	public FireboxTileEntity(){
		super(TYPE, 1);
	}

	@Override
	protected boolean useHeat(){
		return true;
	}

	public int getBurnProg(){
		return maxBurnTime == 0 ? 0 : 100 * burnTime / maxBurnTime;
	}

	@Override
	public void tick(){
		super.tick();
		if(level.isClientSide){
			return;
		}

		if(burnTime != 0){
			temp = Math.min(MAX_TEMP, temp + POWER);
			if(--burnTime == 0){
				level.setBlock(worldPosition, CRBlocks.firebox.defaultBlockState(), 18);
			}
			setChanged();
		}

		int fuelBurn;
		if(burnTime == 0 && (fuelBurn = ForgeHooks.getBurnTime(inventory[0])) != 0){
			int configLimit = CRConfig.fireboxCap.get();
			if(configLimit >= 0){
				fuelBurn = Math.min(fuelBurn, configLimit);
			}
			burnTime = fuelBurn;
			maxBurnTime = burnTime;
			Item item = inventory[0].getItem();
			inventory[0].shrink(1);
			if(inventory[0].isEmpty() && item.hasContainerItem(inventory[0])){
				inventory[0] = item.getContainerItem(inventory[0]);
			}
			level.setBlock(worldPosition, CRBlocks.firebox.defaultBlockState().setValue(CRProperties.ACTIVE, true), 18);
			setChanged();
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt){
		super.load(state, nbt);
		burnTime = nbt.getInt("burn");
		maxBurnTime = nbt.getInt("max_burn");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt){
		super.save(nbt);
		nbt.putInt("burn", burnTime);
		nbt.putInt("max_burn", maxBurnTime);
		return nbt;
	}

	private LazyOptional<ItemHandler> itemOpt = LazyOptional.of(() -> new ItemHandler(null));

	@Override
	public void setRemoved(){
		super.setRemoved();
		itemOpt.invalidate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing){
		if(capability == Capabilities.HEAT_CAPABILITY && (facing == Direction.UP || facing == null)){
			return (LazyOptional<T>) heatOpt;
		}
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (LazyOptional<T>) itemOpt;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack){
		return index == 0 && ForgeHooks.getBurnTime(stack) != 0;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction){
		return index == 0 && !canPlaceItem(index, stack);//Allow removing empty buckets
	}

	@Override
	public ITextComponent getDisplayName(){
		return new TranslationTextComponent("container.firebox");
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player){
		return new FireboxContainer(id, playerInv, createContainerBuf());
	}
}
