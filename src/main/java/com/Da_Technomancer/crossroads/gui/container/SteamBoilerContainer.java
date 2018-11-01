package com.Da_Technomancer.crossroads.gui.container;

import com.Da_Technomancer.crossroads.API.templates.InventoryTE;
import com.Da_Technomancer.crossroads.API.templates.MachineContainer;
import net.minecraft.inventory.IInventory;

public class SteamBoilerContainer extends MachineContainer{

	public SteamBoilerContainer(IInventory playerInv, InventoryTE te){
		super(playerInv, te);
	}

	@Override
	protected void addSlots(){
		addSlotToContainer(new OutputSlot(te, 0, 40, 54));//Salt
		addSlotToContainer(new FluidSlot(this, 100, 19, 100, 54));

		//TODO FLUID STUFF
	}

	@Override
	protected int slotCount(){
		return 3;
	}
}