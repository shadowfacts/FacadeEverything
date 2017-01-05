package net.shadowfacts.facadeeverything.model.item

import com.google.common.collect.ImmutableList
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormatElement
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad
import net.shadowfacts.facadeeverything.model.QuadCache
import net.shadowfacts.facadeeverything.util.getFacadeState

/**
 * @author shadowfacts
 */
class ModelFacadeItem(val facade: IBlockState? = null): IBakedModel {

	object Overrides: ItemOverrideList(listOf()) {
		override fun handleItemState(originalModel: IBakedModel, stack: ItemStack, world: World?, entity: EntityLivingBase?): IBakedModel {
			return ModelFacadeItem(stack.getFacadeState())
		}
	}

	private fun putVertex(builder: UnpackedBakedQuad.Builder, normal: Vec3d, x: Double, y: Double, z: Double, u: Float, v: Float, sprite: TextureAtlasSprite) {
		val format = DefaultVertexFormats.ITEM
		var u = u
		var v = v
		for (e in 0..format.elementCount - 1) {
			when (format.getElement(e).usage) {
				VertexFormatElement.EnumUsage.POSITION -> builder.put(e, x.toFloat(), y.toFloat(), z.toFloat(), 1.0f)
				VertexFormatElement.EnumUsage.COLOR -> builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f)
				VertexFormatElement.EnumUsage.UV -> {
					if (format.getElement(e).index === 0) {
						u = sprite.getInterpolatedU(u.toDouble())
						v = sprite.getInterpolatedV(v.toDouble())
						builder.put(e, u, v, 0f, 1f)
					} else {
						builder.put(e, normal.xCoord.toFloat(), normal.yCoord.toFloat(), normal.zCoord.toFloat(), 0f)
					}
				}
				VertexFormatElement.EnumUsage.NORMAL -> builder.put(e, normal.xCoord.toFloat(), normal.yCoord.toFloat(), normal.zCoord.toFloat(), 0f)
				else -> builder.put(e)
			}
		}
	}

	private fun createQuad(v1: Vec3d, v2: Vec3d, v3: Vec3d, v4: Vec3d, sprite: TextureAtlasSprite): BakedQuad {
		val normal = v1.subtract(v2).crossProduct(v3.subtract(v2))

		val builder = UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM)
		builder.setTexture(sprite)
		putVertex(builder, normal, v1.xCoord, v1.yCoord, v1.zCoord, 0f, 0f, sprite)
		putVertex(builder, normal, v2.xCoord, v2.yCoord, v2.zCoord, 0f, 16f, sprite)
		putVertex(builder, normal, v3.xCoord, v3.yCoord, v3.zCoord, 16f, 16f, sprite)
		putVertex(builder, normal, v4.xCoord, v4.yCoord, v4.zCoord, 16f, 0f, sprite)
		return builder.build()
	}

	override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
		if (facade != null) {
			if (side == null || side == EnumFacing.SOUTH) {
				return QuadCache[facade, side, rand]
			}
		} else {
			if (side == EnumFacing.SOUTH) {
				return ImmutableList.of(createQuad(Vec3d(0.0, 0.0, 1.0), Vec3d(1.0, 0.0, 1.0), Vec3d(1.0, 1.0, 1.0), Vec3d(0.0, 1.0, 1.0), ModelLoader.White.INSTANCE))
			}
		}
		return ImmutableList.of()
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
		return Overrides
	}

	override fun getParticleTexture(): TextureAtlasSprite {
		return Minecraft.getMinecraft().textureMapBlocks.missingSprite
	}

	@Deprecated("")
	override fun getItemCameraTransforms(): ItemCameraTransforms {
		return ItemCameraTransforms.DEFAULT
	}

}