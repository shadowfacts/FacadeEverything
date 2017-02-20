package net.shadowfacts.facadeeverything.model

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.util.EnumFacing
import java.util.concurrent.TimeUnit

/**
 * @author shadowfacts
 */
object QuadCache {

	private val cache: Cache<Pair<IBlockState, EnumFacing?>, List<BakedQuad>> = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build()

	fun clear() {
		cache.invalidateAll()
	}

	operator fun get(state: IBlockState, side: EnumFacing?, rand: Long): List<BakedQuad> {
		return cache.get(state to side) {
			val model = Minecraft.getMinecraft().blockRendererDispatcher.blockModelShapes.getModelForState(state)
			if (side != null) {
				model.getQuads(state, side, rand)
			} else {
				val quads = model.getQuads(state, null, rand).filter {
					it.face == side
				}
				quads
			}
		}
	}

}