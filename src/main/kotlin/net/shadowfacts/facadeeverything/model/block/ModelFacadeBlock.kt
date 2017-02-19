package net.shadowfacts.facadeeverything.model.block

import com.google.common.base.Function
import com.google.common.base.Optional
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModel
import net.minecraftforge.common.model.IModelState

/**
 * @author shadowfacts
 */
object ModelFacadeBlock: IModel {

	override fun bake(state: IModelState?, format: VertexFormat?, bakedTextureGetter: Function<ResourceLocation, TextureAtlasSprite>?): IBakedModel {
		return BakedModelFacadeBlock
	}

	override fun getTextures(): Collection<ResourceLocation> {
		return listOf()
	}

	override fun getDefaultState(): IModelState {
		return IModelState { Optional.absent() }
	}

	override fun getDependencies(): Collection<ResourceLocation> {
		return listOf()
	}

}