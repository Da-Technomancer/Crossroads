package com.Da_Technomancer.crossroads.blocks.alchemy;

import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.items.CRItems;
import com.Da_Technomancer.crossroads.tileentities.alchemy.MaxwellDemonTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MaxwellDemon extends ContainerBlock{

	public MaxwellDemon(){
		super(CRBlocks.getRockProperty());
		String name = "maxwell_demon";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this, new Item.Properties().tab(CRItems.TAB_CROSSROADS).rarity(CRItems.BOBO_RARITY));
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn){
		return new MaxwellDemonTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state){
		return BlockRenderType.MODEL;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		tooltip.add(new TranslationTextComponent("tt.crossroads.maxwell_demon.top", MaxwellDemonTileEntity.MAX_TEMP, MaxwellDemonTileEntity.RATE));
		tooltip.add(new TranslationTextComponent("tt.crossroads.maxwell_demon.bottom", MaxwellDemonTileEntity.MIN_TEMP, MaxwellDemonTileEntity.RATE));
		tooltip.add(new TranslationTextComponent("tt.crossroads.maxwell_demon.quip").setStyle(MiscUtil.TT_QUIP));
	}
}
