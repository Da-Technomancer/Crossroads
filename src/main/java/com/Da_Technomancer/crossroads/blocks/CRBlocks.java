package com.Da_Technomancer.crossroads.blocks;

import com.Da_Technomancer.crossroads.blocks.alchemy.*;
import com.Da_Technomancer.crossroads.blocks.beams.*;
import com.Da_Technomancer.crossroads.blocks.electric.Dynamo;
import com.Da_Technomancer.crossroads.blocks.electric.TeslaCoil;
import com.Da_Technomancer.crossroads.blocks.electric.TeslaCoilTop;
import com.Da_Technomancer.crossroads.blocks.fluid.*;
import com.Da_Technomancer.crossroads.blocks.heat.*;
import com.Da_Technomancer.crossroads.blocks.rotary.*;
import com.Da_Technomancer.crossroads.blocks.technomancy.*;
import com.Da_Technomancer.crossroads.fluids.CRFluids;
import com.Da_Technomancer.crossroads.fluids.GenericFluid;
import com.Da_Technomancer.crossroads.items.CRItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CRBlocks{

	public static Mechanism mechanism;
	public static MasterAxis masterAxis;
	public static FluidTube fluidTube;
	public static HeatingCrucible heatingCrucible;
	public static Millstone millstone;
	public static SteamBoiler steamBoiler;
	public static BlockSalt blockSalt;
	public static FluidVoid fluidVoid;
	public static RotaryPump rotaryPump;
	public static SteamTurbine steamTurbine;
	public static HeatSink heatSink;
	public static FluidTank fluidTank;
	public static Firebox firebox;
	public static Smelter smelter;
	public static SaltReactor saltReactor;
	public static FluidCoolingChamber fluidCoolingChamber;
	public static LargeGearMaster largeGearMaster;
	public static LargeGearSlave largeGearSlave;
	public static Radiator radiator;
	public static RotaryDrill rotaryDrill;
	public static RotaryDrill rotaryDrillGold;
	public static FatCollector fatCollector;
	public static FatCongealer fatCongealer;
	public static RedstoneFluidTube redstoneFluidTube;
	public static WaterCentrifuge waterCentrifuge;
	public static BeamExtractor beamExtractor;
	public static QuartzStabilizer quartzStabilizer;
	public static CrystallinePrism crystallinePrism;
	public static BeamReflector beamReflector;
	public static LensFrame lensFrame;
	public static BasicBlock blockPureQuartz;
	public static BasicBlock blockBrightQuartz;
	public static BeamSiphon beamSiphon;
	public static BeamSplitter beamSplitter;
	public static ColorChart colorChart;
	public static LightCluster lightCluster;
	public static CrystalMasterAxis crystalMasterAxis;
	public static BeaconHarness beaconHarness;
	public static FatFeeder fatFeeder;
	public static RedstoneAxis redstoneAxis;
	public static CageCharger cageCharger;
	public static HamsterWheel hamsterWheel;
	public static CopshowiumCreationChamber copshowiumCreationChamber;
	public static GatewayController gatewayController;
	public static GatewayFrameEdge gatewayEdge;
	public static DetailedCrafter detailedCrafter;
	//	public static PrototypingTable prototypingTable;
//	public static Prototype prototype;
//	public static PrototypePort prototypePort;
//	public static MechanicalArm mechanicalArm;
//	public static RedstoneRegistry redstoneRegistry;
	public static AlchemicalTube alchemicalTubeGlass;
	public static RedsAlchemicalTube redsAlchemicalTubeGlass;
	public static FluidInjector fluidInjectorGlass;
	public static FlowLimiter flowLimiterGlass;
	public static HeatedTube heatedTubeGlass;
	public static CoolingCoil coolingCoilGlass;
	public static ReactionChamber reactionChamberGlass;
	public static ReagentTank reagentTankGlass;
	public static ReagentPump reagentPumpGlass;
	public static AlchemicalTube alchemicalTubeCrystal;
	public static RedsAlchemicalTube redsAlchemicalTubeCrystal;
	public static FluidInjector fluidInjectorCrystal;
	public static FlowLimiter flowLimiterCrystal;
	public static HeatedTube heatedTubeCrystal;
	public static CoolingCoil coolingCoilCrystal;
	public static ReactionChamber reactionChamberCrystal;
	public static ReagentTank reagentTankCrystal;
	public static ReagentPump reagentPumpCrystal;
	public static ChemicalVent chemicalVent;
	public static HeatLimiterBasic heatLimiterBasic;
	public static HeatLimiterRedstone heatLimiterRedstone;
	public static ReagentFilter reagentFilterGlass;
	public static ReagentFilter reagentFilterCrystal;
	public static Dynamo dynamo;
	public static TeslaCoil teslaCoil;
	public static TeslaCoilTop teslaCoilTopNormal;
	public static TeslaCoilTop teslaCoilTopDistance;
	public static TeslaCoilTop teslaCoilTopIntensity;
	public static TeslaCoilTop teslaCoilTopAttack;
	public static TeslaCoilTop teslaCoilTopEfficiency;
	public static TeslaCoilTop teslaCoilTopDecorative;
	public static MaxwellDemon maxwellDemon;
	public static GlasswareHolder glasswareHolder;
	public static DensusPlate densusPlate;
	public static DensusPlate antiDensusPlate;
	public static BasicBlock cavorite;
	public static ChargingStand chargingStand;
	public static AtmosCharger atmosCharger;
	public static VoltusGenerator voltusGenerator;
	public static ReactiveSpot reactiveSpot;
	public static ClockworkStabilizer clockworkStabilizer;
	public static WindTurbine windTurbine;
	public static SolarHeater solarHeater;
	public static HeatReservoir heatReservoir;
	public static StirlingEngine stirlingEngine;
	public static Icebox icebox;
	public static StampMill stampMill;
	public static StampMillTop stampMillTop;
	public static OreCleanser oreCleanser;
	public static BlastFurnace blastFurnace;
	public static BeamRedirector beamRedirector;
	public static PermeableGlass permeableGlass;
	public static PermeableQuartz permeableQuartz;
	public static PermeableObsidian permeableObsidian;
	public static FluxNode fluxNode;
	public static TemporalAccelerator temporalAccelerator;
	public static ChronoHarness chronoHarness;
	public static FluxSink fluxSink;
	public static Steamer steamer;
	public static WindingTable windingTable;
	public static BasicBlock redstoneCrystal;
	public static DetailedAutoCrafter detailedAutoCrafter;
	public static LodestoneTurbine lodestoneTurbine;
	public static LodestoneDynamo lodestoneDynamo;
	public static SequenceBox sequenceBox;
	public static ChunkAccelerator chunkAccelerator;
	public static GatewayControllerDestination gatewayControllerDestination;
	public static BeamCannon beamCannon;

	public static AbstractBlock.Properties getRockProperty(){
		return AbstractBlock.Properties.of(Material.STONE).strength(3).requiresCorrectToolForDrops().sound(SoundType.STONE);
	}

	public static AbstractBlock.Properties getMetalProperty(){
		return AbstractBlock.Properties.of(Material.METAL).strength(3).requiresCorrectToolForDrops().sound(SoundType.METAL);
	}

	public static AbstractBlock.Properties getGlassProperty(){
		return AbstractBlock.Properties.of(Material.GLASS).strength(0.5F).sound(SoundType.GLASS);
	}

	private static final Item.Properties itemBlockProp = new Item.Properties().tab(CRItems.TAB_CROSSROADS);
	public static final ArrayList<Block> toRegister = new ArrayList<>();

	/**
	 * Registers the item form of a block
	 * @param block The block to register
	 * @return The passed block for convenience. 
	 */
	public static <T extends Block> T blockAddQue(T block){
		return blockAddQue(block, itemBlockProp);
	}

	public static <T extends Block> T blockAddQue(T block, Item.Properties itemProp){
		assert block.getRegistryName() != null;
		Item item = new BlockItem(block, itemProp).setRegistryName(block.getRegistryName());
		CRItems.toRegister.add(item);
		return block;
	}

	public static void init(){
		masterAxis = new MasterAxis();
		millstone = new Millstone();
		mechanism = new Mechanism();
		largeGearMaster = new LargeGearMaster();
		largeGearSlave = new LargeGearSlave();
		heatingCrucible = new HeatingCrucible();
		fluidTube = new FluidTube();
		steamBoiler = new SteamBoiler();
		rotaryPump = new RotaryPump();
		steamTurbine = new SteamTurbine();
		blockSalt = new BlockSalt();
		fluidVoid = new FluidVoid();
		heatSink = new HeatSink();
		fluidTank = new FluidTank();
		firebox = new Firebox();
		smelter = new Smelter();
		saltReactor = new SaltReactor();
		fluidCoolingChamber = new FluidCoolingChamber();
		radiator = new Radiator();
		rotaryDrill = new RotaryDrill(false);
		rotaryDrillGold = new RotaryDrill(true);
		fatCollector = new FatCollector();
		fatCongealer = new FatCongealer();
		redstoneFluidTube = new RedstoneFluidTube();
		waterCentrifuge = new WaterCentrifuge();
		beamExtractor = new BeamExtractor();
		quartzStabilizer = new QuartzStabilizer();
		crystallinePrism = new CrystallinePrism();
		beamReflector = new BeamReflector();
		lensFrame = new LensFrame();
		blockPureQuartz = new BasicBlock("block_pure_quartz", CRBlocks.getRockProperty());
		blockBrightQuartz = new BasicBlock("block_bright_quartz", CRBlocks.getRockProperty().lightLevel(state -> 15));
		beamSiphon = new BeamSiphon();
		beamSplitter = new BeamSplitter();
		colorChart = new ColorChart();
		lightCluster = new LightCluster();
		crystalMasterAxis = new CrystalMasterAxis();
//		ratiator = new Ratiator();
		beaconHarness = new BeaconHarness();
		fatFeeder = new FatFeeder();
		redstoneAxis = new RedstoneAxis();
		cageCharger = new CageCharger();
		hamsterWheel = new HamsterWheel();
		copshowiumCreationChamber = new CopshowiumCreationChamber();
//		mathAxis = new MathAxis();
		gatewayController = new GatewayController();
		gatewayEdge = new GatewayFrameEdge();
//		redstoneKeyboard = new RedstoneKeyboard();
		detailedCrafter = new DetailedCrafter();
//		prototypingTable = new PrototypingTable();
//		prototype = new Prototype();
//		prototypePort = new PrototypePort();
//		mechanicalArm = new MechanicalArm();
//		redstoneRegistry = new RedstoneRegistry();
		alchemicalTubeGlass = new AlchemicalTube(false);
		redsAlchemicalTubeGlass = new RedsAlchemicalTube(false);
		fluidInjectorGlass = new FluidInjector(false);
		flowLimiterGlass = new FlowLimiter(false);
		heatedTubeGlass = new HeatedTube(false);
		coolingCoilGlass = new CoolingCoil(false);
		reactionChamberGlass = new ReactionChamber(false);
		reagentPumpGlass = new ReagentPump(false);
		reagentTankGlass = new ReagentTank(false);
		alchemicalTubeCrystal = new AlchemicalTube(true);
		redsAlchemicalTubeCrystal = new RedsAlchemicalTube(true);
		fluidInjectorCrystal = new FluidInjector(true);
		flowLimiterCrystal = new FlowLimiter(true);
		heatedTubeCrystal = new HeatedTube(true);
		coolingCoilCrystal = new CoolingCoil(true);
		reactionChamberCrystal = new ReactionChamber(true);
		reagentPumpCrystal = new ReagentPump(true);
		reagentTankCrystal = new ReagentTank(true);
		chemicalVent = new ChemicalVent();
		heatLimiterBasic = new HeatLimiterBasic();
		heatLimiterRedstone = new HeatLimiterRedstone();
		reagentFilterGlass = new ReagentFilter(false);
		reagentFilterCrystal = new ReagentFilter(true);
		dynamo = new Dynamo();
		teslaCoil = new TeslaCoil();
		teslaCoilTopNormal = new TeslaCoilTop(TeslaCoilTop.TeslaCoilVariants.NORMAL);
		teslaCoilTopDistance = new TeslaCoilTop(TeslaCoilTop.TeslaCoilVariants.DISTANCE);
		teslaCoilTopIntensity = new TeslaCoilTop(TeslaCoilTop.TeslaCoilVariants.INTENSITY);
		teslaCoilTopAttack = new TeslaCoilTop(TeslaCoilTop.TeslaCoilVariants.ATTACK);
		teslaCoilTopEfficiency = new TeslaCoilTop(TeslaCoilTop.TeslaCoilVariants.EFFICIENCY);
		teslaCoilTopDecorative = new TeslaCoilTop(TeslaCoilTop.TeslaCoilVariants.DECORATIVE);
		maxwellDemon = new MaxwellDemon();
		glasswareHolder = new GlasswareHolder();
		densusPlate = new DensusPlate(false);
		antiDensusPlate = new DensusPlate(true);
		cavorite = new BasicBlock("block_cavorite", CRBlocks.getRockProperty()){
			@Override
			public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
				tooltip.add(new TranslationTextComponent("tt.crossroads.cavorite"));
				tooltip.add(new TranslationTextComponent("tt.crossroads.decoration"));
			}
		};
		chargingStand = new ChargingStand();
		atmosCharger = new AtmosCharger();
		voltusGenerator = new VoltusGenerator();
		reactiveSpot = new ReactiveSpot();
		clockworkStabilizer = new ClockworkStabilizer();
		windTurbine = new WindTurbine();
		solarHeater = new SolarHeater();
		heatReservoir = new HeatReservoir();
		stirlingEngine = new StirlingEngine();
		icebox = new Icebox();
		stampMill = new StampMill();
		stampMillTop = new StampMillTop();
		oreCleanser = new OreCleanser();
		blastFurnace = new BlastFurnace();
		beamRedirector = new BeamRedirector();
		permeableGlass = new PermeableGlass();
		permeableQuartz = new PermeableQuartz();
		permeableObsidian = new PermeableObsidian();
		fluxNode = new FluxNode();
		temporalAccelerator = new TemporalAccelerator();
		chronoHarness = new ChronoHarness();
		fluxSink = new FluxSink();
		steamer = new Steamer();
		windingTable = new WindingTable();
		redstoneCrystal = new BasicBlock("redstone_crystal", CRBlocks.getGlassProperty().strength(0.3F)){
			@Override
			public boolean isSignalSource(BlockState state){
				return true;
			}

			@Override
			public int getSignal(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
				return 15;
			}

			@Override
			public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
				tooltip.add(new TranslationTextComponent("tt.crossroads.redstone_crystal.drops"));
				tooltip.add(new TranslationTextComponent("tt.crossroads.redstone_crystal.power"));
			}
		};
		detailedAutoCrafter = new DetailedAutoCrafter();
		lodestoneTurbine = new LodestoneTurbine();
		lodestoneDynamo = new LodestoneDynamo();
		sequenceBox = new SequenceBox();
		chunkAccelerator = new ChunkAccelerator();
		gatewayControllerDestination = new GatewayControllerDestination();
		beamCannon = new BeamCannon();
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientInit(){
		setCutout(permeableGlass, rotaryPump, steamTurbine, alchemicalTubeGlass, alchemicalTubeCrystal, redsAlchemicalTubeGlass, redsAlchemicalTubeCrystal, fluidInjectorGlass, fluidInjectorCrystal, flowLimiterGlass, flowLimiterCrystal, heatedTubeGlass, heatedTubeCrystal, coolingCoilGlass, coolingCoilCrystal, reactionChamberGlass, reactionChamberCrystal, reagentTankGlass, reagentTankCrystal, reagentPumpGlass, reagentPumpCrystal, glasswareHolder, reagentFilterGlass, reagentFilterCrystal, chargingStand);
		setFluidTrans(CRFluids.distilledWater, CRFluids.steam);
	}

	@OnlyIn(Dist.CLIENT)
	private static void setCutout(Block... blocks){
		RenderType cutout = RenderType.cutout();
		for(Block block : blocks){
			RenderTypeLookup.setRenderLayer(block, cutout);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void setFluidTrans(GenericFluid.FluidData...fluids){
		RenderType type = RenderType.translucent();
		for(GenericFluid.FluidData f : fluids){
			RenderTypeLookup.setRenderLayer(f.still, type);
			RenderTypeLookup.setRenderLayer(f.flowing, type);
		}
	}
}
