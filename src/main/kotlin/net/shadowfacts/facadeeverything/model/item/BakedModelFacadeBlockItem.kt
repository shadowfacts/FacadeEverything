package net.shadowfacts.facadeeverything.model.item

import com.google.common.cache.CacheBuilder
import com.google.common.collect.ImmutableList
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import net.shadowfacts.facadeeverything.model.BakedModelBase
import net.shadowfacts.facadeeverything.model.QuadCache
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.sides
import java.util.concurrent.TimeUnit

/**
 * @author shadowfacts
 */
class BakedModelFacadeBlockItem(val base: IBlockState? = null, val facades: Array<IBlockState?> = arrayOfNulls(6)): BakedModelBase() {

	init {
		setTransform(ItemCameraTransforms.TransformType.GUI, getTransform(-15f, -3.5f, 0f, 30f, 225f, 0f, 0.625f))
		setTransform(ItemCameraTransforms.TransformType.GROUND, getTransform(-6f, -4f, -6f, 0f, 0f, 0f, 0.25f))
		setTransform(ItemCameraTransforms.TransformType.FIXED, getTransform(0f, 0f, 0f, 0f, 0f, 0f, 0.5f))
		addThirdPersonTransform(getTransform(-4f, -4f, -6f, 75f, 45f, 0f, 0.375f))
		addFirstPersonTransform(getTransform(-6f, -4f, -6f, 0f, 45f, 0f, 0.4f))
	}

	override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
		if (side == null) {
			return if (base != null) QuadCache[base, null, rand] else ImmutableList.of()
		} else {
			val facade = facades[side.ordinal]
			if (facade != null) {
				return QuadCache[facade, side, rand]
			} else {
				return if (base != null) QuadCache[base, side, rand] else ImmutableList.of()
			}
		}
	}

	override fun getOverrides(): ItemOverrideList {
		return Overrides
	}

	override fun getParticleTexture(): TextureAtlasSprite {
		return Minecraft.getMinecraft().textureMapBlocks.missingSprite
	}

	object Overrides: ItemOverrideList(listOf()) {
		override fun handleItemState(originalModel: IBakedModel, stack: ItemStack, world: World?, entity: EntityLivingBase?): IBakedModel {
			return Cache[stack.base, stack.sides.values.toTypedArray()]
		}
	}

	object Cache {

		private val cache: com.google.common.cache.Cache<Int, BakedModelFacadeBlockItem> = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build()

		fun clear() {
			cache.invalidateAll()
		}

		fun hash(base: IBlockState, facades: Array<IBlockState?>): Int {
			var result = 1
			facades.forEach {
				result = 31 * result + (if (it == null) 0 else Block.getStateId(it))
			}
			return 31 * Block.getStateId(base) + result
		}

		operator fun get(base: IBlockState, facades: Array<IBlockState?>): BakedModelFacadeBlockItem {
			val hash = hash(base, facades)
			return cache.get(hash) {
				BakedModelFacadeBlockItem(base, facades)
			}
		}

	}

}