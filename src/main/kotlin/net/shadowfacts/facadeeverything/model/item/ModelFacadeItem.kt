package net.shadowfacts.facadeeverything.model.item

import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModel
import net.minecraftforge.common.model.IModelState
import net.shadowfacts.facadeeverything.MOD_ID
import java.util.*

/**
 * @author shadowfacts
 */
object ModelFacadeItem: IModel {

	override fun bake(state: IModelState, format: VertexFormat, bakedTextureGetter: java.util.function.Function<ResourceLocation, TextureAtlasSprite>): IBakedModel {
		return BakedModelFacadeItem(format, bakedTextureGetter.apply(ResourceLocation(MOD_ID, "misc/blank")))
	}

	override fun getTextures(): Collection<ResourceLocation> {
		return listOf(ResourceLocation(MOD_ID, "misc/blank"))
	}

	override fun getDefaultState(): IModelState {
		return IModelState { Optional.empty() }
	}

	override fun getDependencies(): Collection<ResourceLocation> {
		return listOf()
	}

}