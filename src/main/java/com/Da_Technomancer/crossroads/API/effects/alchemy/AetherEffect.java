package com.Da_Technomancer.crossroads.API.effects.alchemy;

import com.Da_Technomancer.crossroads.API.CRReflection;
import com.Da_Technomancer.crossroads.API.alchemy.EnumMatterPhase;
import com.Da_Technomancer.crossroads.API.alchemy.EnumReagents;
import com.Da_Technomancer.crossroads.API.alchemy.ReagentMap;
import com.Da_Technomancer.crossroads.API.packets.CRPackets;
import com.Da_Technomancer.crossroads.API.packets.SendBiomeUpdateToClient;
import com.Da_Technomancer.crossroads.CRConfig;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.tileentities.alchemy.ReactiveSpotTileEntity;
import com.Da_Technomancer.essentials.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.FastRandom;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBiomeReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.Biomes;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class AetherEffect implements IAlchEffect{

	private static final ITag<Block> SOIL_GROUP = BlockTags.bind(Crossroads.MODID + ":alchemy_soil");
	private static final ITag<Block> ROCK_GROUP = BlockTags.bind(Crossroads.MODID + ":alchemy_rock");
	private static final ITag<Block> FLUD_GROUP = BlockTags.bind(Crossroads.MODID + ":alchemy_fluid");//Was going to be named FLUID_GROUP, but the other two fields had the same name lengths and I couldn't resist
	private static final ITag<Block> CRYS_GROUP = BlockTags.bind(Crossroads.MODID + ":alchemy_crystal");
	private static final ITag<Block> WOOD_GROUP = BlockTags.bind(Crossroads.MODID + ":alchemy_wood");
	private static final ITag<Block> FOLI_GROUP = BlockTags.bind(Crossroads.MODID + ":alchemy_foliage");
	private static final Field biomeField = ReflectionUtil.reflectField(CRReflection.BIOME_ARRAY);

	protected Block soilBlock(){
		return Blocks.GRASS_BLOCK;
	}

	protected Block rockBlock(){
		return Blocks.STONE;
	}

	protected Block fluidBlock(){
		return Blocks.WATER;
	}

	protected Block crystalBlock(){
		return Blocks.GLASS;
	}

	protected Block woodBlock(){
		return Blocks.OAK_LOG;
	}

	protected Block foliageBlock(){
		return Blocks.OAK_LEAVES;
	}

	protected RegistryKey<Biome> biome(){
		return Biomes.PLAINS;
	}

	@Nullable
	public static Biome lookupBiome(RegistryKey<Biome> biomeKey, IBiomeReader world){
		//Gets the biome associated with a key
		return world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(biomeKey);
	}

	@Override
	public void doEffect(World world, BlockPos pos, int amount, EnumMatterPhase phase, ReagentMap contents){
		BlockState oldState = world.getBlockState(pos);

		//quicksilver makes it create a block instead of transmuting blocks
		if(contents.getQty(EnumReagents.QUICKSILVER.id()) != 0 && oldState.getBlock().isAir(oldState, world, pos)){
			world.setBlockAndUpdate(pos, soilBlock().defaultBlockState());
			return;
		}

		//sulfur dioxide prevents biome changing
		if(contents.getQty(EnumReagents.SULFUR_DIOXIDE.id()) == 0){
			RegistryKey<Biome> biomeKey = biome();
			Biome biome = lookupBiome(biomeKey, world);
			if(biome != null && world.getBiome(pos) != biome){
				setBiomeAtPos(world, pos, biome);
				CRPackets.sendPacketToDimension(world, new SendBiomeUpdateToClient(pos, biomeKey.location()));
			}
		}

		//cavorite prevents block transmutation
		if(oldState.isAir(world, pos) || oldState.getDestroySpeed(world, pos) < 0 || contents.getQty(EnumReagents.CAVORITE.id()) != 0){
			return;
		}

		if(CRYS_GROUP.contains(oldState.getBlock())){
			if(oldState != crystalBlock().defaultBlockState()){
				world.setBlockAndUpdate(pos, crystalBlock().defaultBlockState());
			}
		}else if(FLUD_GROUP.contains(oldState.getBlock())){
			if(oldState != fluidBlock().defaultBlockState() && oldState.getBlock() != CRBlocks.reactiveSpot){
				world.setBlockAndUpdate(pos, CRBlocks.reactiveSpot.defaultBlockState());
				TileEntity te = world.getBlockEntity(pos);
				if(te instanceof ReactiveSpotTileEntity){
					((ReactiveSpotTileEntity) te).setTarget(fluidBlock().defaultBlockState());
				}
			}
		}else if(ROCK_GROUP.contains(oldState.getBlock())){
			if(oldState != rockBlock().defaultBlockState()){
				world.setBlockAndUpdate(pos, rockBlock().defaultBlockState());
			}
		}else if(SOIL_GROUP.contains(oldState.getBlock())){
			//Special case for grass vs dirt
			BlockPos upPos = pos.above();
			if((soilBlock() == Blocks.GRASS_BLOCK || soilBlock() == Blocks.MYCELIUM) && !world.getBlockState(upPos).isAir(world, upPos)){
				if(oldState != Blocks.DIRT.defaultBlockState()){
					world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
				}
			}else if(oldState != soilBlock().defaultBlockState()){
				world.setBlockAndUpdate(pos, soilBlock().defaultBlockState());
			}
		}else if(WOOD_GROUP.contains(oldState.getBlock())){
			if(oldState != woodBlock().defaultBlockState()){
				world.setBlockAndUpdate(pos, woodBlock().defaultBlockState());
			}
		}else if(FOLI_GROUP.contains(oldState.getBlock())){
			if(oldState != foliageBlock().defaultBlockState()){
				world.setBlockAndUpdate(pos, foliageBlock().defaultBlockState());
			}
		}
	}

	@Override
	public ITextComponent getName(){
		return new TranslationTextComponent("effect.terraform_plains");
	}

	/**
	 * Sets the biome at a position in a way that will be saved to disk
	 * Does not handle packets, should be called on both sides
	 * @param world The world
	 * @param pos The position to set the position of. Y-coord is irrelevant, will set the biome in an entire column
	 * @param biome The biome to set it to
	 */
	public static void setBiomeAtPos(World world, BlockPos pos, Biome biome){
		if(biome == null){
			return;
		}

		//As of MC1.15, we have to reflect in as the biome array is private and the int array won't save to disk
		BiomeContainer bc = world.getChunk(pos).getBiomes();
		if(biomeField != null && bc != null){
			Object o;
			try{
				o = biomeField.get(bc);
				Biome[] biomeArray = (Biome[]) o;
				long seed = 0L;//TODO don't know how to get seed on the client
				if(CRConfig.verticalBiomes.get()){
					int y = 0;
					do{
						//We set the biome in a column from bedrock to world height
						biomeArray[getBiomeIndex(pos.getX(), y, pos.getZ(), seed)] = biome;
					}while(!World.isOutsideBuildHeight(++y));
				}else{
					biomeArray[getBiomeIndex(pos.getX(), pos.getY(), pos.getZ(), seed)] = biome;
				}
			}catch(IllegalAccessException | NullPointerException | IndexOutOfBoundsException e){
				e.printStackTrace();
				Crossroads.logger.error(String.format("Failed to set biome at pos: %s; to biome: %s", pos, biome), e);
			}
		}
	}

	//Copied from BiomeContainer to copy its biome array ordering
	private static final int WIDTH_BITS = (int)Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;

	/**
	 * Vanilla biomes are packed in an elaborate format involving biomes being stored in 4x4x4 segments
	 * This method gets the index corresponding to a world position in the chunk's biome array
	 * Multiple positions will share an index
	 * @param x The x position
	 * @param y The y position- does actually matter
	 * @param z The z position
	 * @param seed The world seed
	 * @return The biome index at this position
	 */
	private static int getBiomeIndex(int x, int y, int z, long seed){
		//This incredibly elaborate formula (and associated methods) were copied from FuzzedBiomeMagnifier and BiomeContainer.
		//If this ever breaks, we're doomed
		int i = x - 2;
		int j = y - 2;
		int k = z - 2;
		int l = i >> 2;
		int i1 = j >> 2;
		int j1 = k >> 2;
		double d0 = (double)(i & 3) / 4.0D;
		double d1 = (double)(j & 3) / 4.0D;
		double d2 = (double)(k & 3) / 4.0D;
		double[] adouble = new double[8];

		for(int k1 = 0; k1 < 8; ++k1) {
			boolean flag = (k1 & 4) == 0;
			boolean flag1 = (k1 & 2) == 0;
			boolean flag2 = (k1 & 1) == 0;
			int l1 = flag ? l : l + 1;
			int i2 = flag1 ? i1 : i1 + 1;
			int j2 = flag2 ? j1 : j1 + 1;
			double d3 = flag ? d0 : d0 - 1.0D;
			double d4 = flag1 ? d1 : d1 - 1.0D;
			double d5 = flag2 ? d2 : d2 - 1.0D;
			adouble[k1] = getFiddledDistance(seed, l1, i2, j2, d3, d4, d5);
		}

		int k2 = 0;
		double d6 = adouble[0];

		for(int l2 = 1; l2 < 8; ++l2) {
			if (d6 > adouble[l2]) {
				k2 = l2;
				d6 = adouble[l2];
			}
		}

		int i3 = (k2 & 4) == 0 ? l : l + 1;
		int j3 = (k2 & 2) == 0 ? i1 : i1 + 1;
		int k3 = (k2 & 1) == 0 ? j1 : j1 + 1;

		//Copied from BiomeContainer.getNoiseBiome(i3, j3, k3)
		int arrayIndex = i3 & BiomeContainer.HORIZONTAL_MASK;//X
		arrayIndex |= (k3 & BiomeContainer.HORIZONTAL_MASK) << WIDTH_BITS;//Z
		return arrayIndex | MathHelper.clamp(j3, 0, BiomeContainer.VERTICAL_MASK) << WIDTH_BITS + WIDTH_BITS;//Y
	}

	private static double getFiddledDistance(long p_226845_0_, int p_226845_2_, int p_226845_3_, int p_226845_4_, double p_226845_5_, double p_226845_7_, double p_226845_9_) {
		long lvt_11_1_ = FastRandom.next(p_226845_0_, (long)p_226845_2_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, (long)p_226845_3_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, (long)p_226845_4_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, (long)p_226845_2_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, (long)p_226845_3_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, (long)p_226845_4_);
		double d0 = getFiddle(lvt_11_1_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_0_);
		double d1 = getFiddle(lvt_11_1_);
		lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_0_);
		double d2 = getFiddle(lvt_11_1_);
		return sqr(p_226845_9_ + d2) + sqr(p_226845_7_ + d1) + sqr(p_226845_5_ + d0);
	}

	private static double getFiddle(long p_226844_0_) {
		double d0 = (double)((int)Math.floorMod(p_226844_0_ >> 24, 1024L)) / 1024.0D;
		return (d0 - 0.5D) * 0.9D;
	}

	private static double sqr(double p_226843_0_) {
		return p_226843_0_ * p_226843_0_;
	}
}
