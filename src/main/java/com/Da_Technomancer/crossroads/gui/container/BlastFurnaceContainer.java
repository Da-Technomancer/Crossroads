package com.Da_Technomancer.crossroads.gui.container;

import com.Da_Technomancer.crossroads.API.templates.MachineContainer;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.tileentities.rotary.BlastFurnaceTileEntity;
import com.Da_Technomancer.essentials.gui.container.FluidSlotManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;

@ObjectHolder(Crossroads.MODID)
public class BlastFurnaceContainer extends MachineContainer<BlastFurnaceTileEntity>{

	@ObjectHolder("ind_blast_furnace")
	private static ContainerType<BlastFurnaceContainer> type = null;

	public BlastFurnaceContainer(int id, PlayerInventory playerInv, PacketBuffer buf){
		super(type, id, playerInv, buf);
		trackInt(te.carbRef);
		trackInt(te.progRef);
	}

	@Override
	protected void addSlots(){
		addSlot(new StrictSlot(te, 0, 8, 35));//Gravel/Clumps
		addSlot(new StrictSlot(te, 1, 29, 20));//Carbon
		addSlot(new OutputSlot(te, 2, 44, 53));//Slag
		Pair<Slot, Slot> fluidSlots = FluidSlotManager.createFluidSlots(new FluidSlotManager.FakeInventory(this), 0, 98, 18, 98, 53, te, new int[] {0});
		addSlot(fluidSlots.getLeft());
		addSlot(fluidSlots.getRight());
	}

	@Override
	protected int slotCount(){
		return 5;
	}
}