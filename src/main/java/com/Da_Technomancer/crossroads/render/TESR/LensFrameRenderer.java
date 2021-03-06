package com.Da_Technomancer.crossroads.render.TESR;

import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.render.CRRenderUtil;
import com.Da_Technomancer.crossroads.tileentities.beams.LensFrameTileEntity;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class LensFrameRenderer extends BeamRenderer<LensFrameTileEntity>{

	//All of these textures are associated with blocks, and don't need to be stitched
	private static final ResourceLocation[] LENS_FRAME_TEXTURES = new ResourceLocation[6];

	static{
		LENS_FRAME_TEXTURES[0] = new ResourceLocation(Crossroads.MODID, "item/gem_ruby");
		LENS_FRAME_TEXTURES[1] = new ResourceLocation("item/emerald");
		LENS_FRAME_TEXTURES[2] = new ResourceLocation("item/diamond");
		LENS_FRAME_TEXTURES[3] = new ResourceLocation(Crossroads.MODID, "item/pure_quartz");
		LENS_FRAME_TEXTURES[4] = new ResourceLocation(Crossroads.MODID, "item/glow_quartz");
		LENS_FRAME_TEXTURES[5] = new ResourceLocation(Crossroads.MODID, "item/void_crystal");
	}

	protected LensFrameRenderer(TileEntityRendererDispatcher dispatcher){
		super(dispatcher);
	}

	@Override
	public void render(LensFrameTileEntity beam, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		super.render(beam, partialTicks, matrix, buffer, combinedLight, combinedOverlay);

		//Render the item in the frame
		BlockState state = beam.getBlockState();
		int content = beam.getContents();

		if(content != 0 && state.getBlock() == CRBlocks.lensFrame){
			Direction.Axis axis = state.getValue(ESProperties.AXIS);

			matrix.pushPose();
			matrix.translate(0.5F, 0.5F, 0.5F);
			switch(axis){
				case X:
					matrix.mulPose(Direction.WEST.getRotation());
					break;
				case Z:
					matrix.mulPose(Direction.NORTH.getRotation());
					break;
			}

			IVertexBuilder builder = buffer.getBuffer(RenderType.cutout());
			float scale = 0.5F;
			float height = 0.1F;
			TextureAtlasSprite sprite = CRRenderUtil.getTextureSprite(LENS_FRAME_TEXTURES[content - 1]);

			CRRenderUtil.addVertexBlock(builder, matrix, -scale, height, -scale, sprite.getU0(), sprite.getV0(), 0, 1, 0, combinedLight);
			CRRenderUtil.addVertexBlock(builder, matrix, -scale, height, scale, sprite.getU0(), sprite.getV1(), 0, 1, 0, combinedLight);
			CRRenderUtil.addVertexBlock(builder, matrix, scale, height, scale, sprite.getU1(), sprite.getV1(), 0, 1, 0, combinedLight);
			CRRenderUtil.addVertexBlock(builder, matrix, scale, height, -scale, sprite.getU1(), sprite.getV0(), 0, 1, 0, combinedLight);

			CRRenderUtil.addVertexBlock(builder, matrix, -scale, -height, -scale, sprite.getU0(), sprite.getV0(), 0, 1, 0, combinedLight);
			CRRenderUtil.addVertexBlock(builder, matrix, scale, -height, -scale, sprite.getU1(), sprite.getV0(), 0, 1, 0, combinedLight);
			CRRenderUtil.addVertexBlock(builder, matrix, scale, -height, scale, sprite.getU1(), sprite.getV1(), 0, 1, 0, combinedLight);
			CRRenderUtil.addVertexBlock(builder, matrix, -scale, -height, scale, sprite.getU0(), sprite.getV1(), 0, 1, 0, combinedLight);

			matrix.popPose();
		}
	}
}
