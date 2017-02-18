package net.shadowfacts.facadeeverything.model.block

import com.google.common.collect.ImmutableList
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.property.IExtendedBlockState
import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.model.QuadCache

/**
 * @author shadowfacts
 */
object BakedModelFacadeBlock: IBakedModel {

	override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
		if (state is IExtendedBlockState) {
			val base = state.getValue(BlockFacade.BASE)
			if (side == null) {
				if (base.block.canRenderInLayer(base, MinecraftForgeClient.getRenderLayer())) {
					return QuadCache[base, null, rand]
				} else {
					return ImmutableList.of()
				}
			} else {
				val facade = state.getValue(BlockFacade.SIDE_PROPS[side])
				if (facade != null) {
					if (facade.block.canRenderInLayer(facade, MinecraftForgeClient.getRenderLayer())) {
						return QuadCache[facade, side, rand]
					} else {
						return ImmutableList.of()
					}
				} else {
					if (base.block.canRenderInLayer(base, MinecraftForgeClient.getRenderLayer())) {
						return QuadCache[base, side, rand]
					} else {
						return ImmutableList.of()
					}
				}
			}
		} else {
			return ImmutableList.of()
		}
	}

	override fun isBuiltInRenderer(): Boolean {
		return false
	}

	override fun isAmbientOcclusion(): Boolean {
		return false
	}

	override fun isGui3d(): Boolean {
		return false
	}

	override fun getOverrides(): ItemOverrideList {
		return ItemOverrideList.NONE
	}

	override fun getParticleTexture(): TextureAtlasSprite {
		return Minecraft.getMinecraft().textureMapBlocks.missingSprite
	}

	@Deprecated("")
	override fun getItemCameraTransforms(): ItemCameraTransforms {
		return ItemCameraTransforms.DEFAULT
	}

}