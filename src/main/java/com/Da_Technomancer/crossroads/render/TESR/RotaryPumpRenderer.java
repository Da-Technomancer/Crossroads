package com.Da_Technomancer.crossroads.render.TESR;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.rotary.IAxleHandler;
import com.Da_Technomancer.crossroads.render.CRRenderUtil;
import com.Da_Technomancer.crossroads.tileentities.fluid.RotaryPumpTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.LazyOptional;

import java.awt.*;

public class RotaryPumpRenderer extends TileEntityRenderer<RotaryPumpTileEntity>{

	protected RotaryPumpRenderer(TileEntityRendererDispatcher dispatcher){
		super(dispatcher);
	}

	@Override
	public void render(RotaryPumpTileEntity te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		//Render the screw
		matrix.pushPose();
		matrix.translate(0.5D, 0, 0.5D);
		LazyOptional<IAxleHandler> opt = te.getCapability(Capabilities.AXLE_CAPABILITY, null);
		if(opt.isPresent()){
			matrix.mulPose(Vector3f.YP.rotationDegrees(opt.orElseThrow(NullPointerException::new).getAngle(partialTicks)));
		}
		CRModels.renderScrew(matrix, buffer, combinedLight);
		matrix.popPose();

		//Render the liquid
		if(te.getCompletion() != 0){
			BlockPos fPos = te.getBlockPos().relative(Direction.DOWN);
			FluidState state = te.getLevel().getFluidState(fPos);
			TextureAtlasSprite lText;
			Color fCol;
			if(!state.isEmpty() && state.isSource()){
				ResourceLocation textLoc = state.getType().getAttributes().getStillTexture();
				lText = CRRenderUtil.getTextureSprite(textLoc);
				fCol = new Color(state.getType().getAttributes().getColor(te.getLevel(), fPos));
			}else{
				return;
			}

			int[] cols = {fCol.getRed(), fCol.getGreen(), fCol.getBlue(), fCol.getAlpha()};

			float xSt = 3F / 16F;
			float ySt = 0;
			float zSt = 3F / 16F;
			float xEn = 13F / 16F;
			float yEn = 7F / 16F * te.getCompletion();
			float zEn = 13F / 16F;
			float uSt = lText.getU(xSt * 16);
			float uEn = lText.getU(xEn * 16);
			float vSt = lText.getV(16 - (ySt * 16));
			float vEn = lText.getV(16 - (yEn * 16));

			//Draw liquid layer
			IVertexBuilder builder = buffer.getBuffer(RenderType.translucentNoCrumbling());

			CRRenderUtil.addVertexBlock(builder, matrix, xEn, ySt, zSt, uEn, vSt, 0, 0, -1, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, ySt, zSt, uSt, vSt, 0, 0, -1, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, yEn, zSt, uSt, vEn, 0, 0, -1, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, yEn, zSt, uEn, vEn, 0, 0, -1, combinedLight, cols);

			CRRenderUtil.addVertexBlock(builder, matrix, xSt, ySt, zEn, uSt, vSt, 0, 0, 1, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, ySt, zEn, uEn, vSt, 0, 0, 1, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, yEn, zEn, uEn, vEn, 0, 0, 1, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, yEn, zEn, uSt, vEn, 0, 0, 1, combinedLight, cols);

			CRRenderUtil.addVertexBlock(builder, matrix, xSt, ySt, zSt, uSt, vSt, -1, 0, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, ySt, zEn, uEn, vSt, -1, 0, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, yEn, zEn, uEn, vEn, -1, 0, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, yEn, zSt, uSt, vEn, -1, 0, 0, combinedLight, cols);

			CRRenderUtil.addVertexBlock(builder, matrix, xEn, yEn, zSt, uEn, vSt, 1, 0, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, yEn, zEn, uEn, vEn, 1, 0, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, ySt, zEn, uSt, vEn, 1, 0, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, ySt, zSt, uSt, vSt, 1, 0, 0, combinedLight, cols);

			CRRenderUtil.addVertexBlock(builder, matrix, xSt, yEn, zSt, lText.getU0(), lText.getV0(), 0, 1, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xSt, yEn, zEn, lText.getU0(), lText.getV1(), 0, 1, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, yEn, zEn, lText.getU1(), lText.getV1(), 0, 1, 0, combinedLight, cols);
			CRRenderUtil.addVertexBlock(builder, matrix, xEn, yEn, zSt, lText.getU1(), lText.getV0(), 0, 1, 0, combinedLight, cols);
		}
	}
}
