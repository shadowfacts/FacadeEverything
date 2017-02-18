package net.shadowfacts.facadeeverything.event

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.model.block.BakedModelFacadeBlock
import net.shadowfacts.facadeeverything.model.QuadCache
import net.shadowfacts.facadeeverything.model.item.BakedModelFacadeBlockItem
import net.shadowfacts.facadeeverything.model.item.BakedModelFacadeItem

/**
 * @author shadowfacts
 */
object ClientEventHandler {

	@SubscribeEvent
	fun onModelBake(event: ModelBakeEvent) {
//		event.modelRegistry.putObject(ModelResourceLocation(ResourceLocation(MOD_ID, "facade_block"), "normal"), BakedModelFacadeBlock)
//		event.modelRegistry.putObject(ModelResourceLocation(ResourceLocation(MOD_ID, "facade_block_item"), "inventory"), BakedModelFacadeBlockItem())
//		event.modelRegistry.putObject(ModelResourceLocation(ResourceLocation(MOD_ID, "facade"), "inventory"), BakedModelFacadeItem())
	}

	@SubscribeEvent
	fun onTextureStitch(event: TextureStitchEvent.Pre) {
		QuadCache.clear()
		BakedModelFacadeItem.Cache.clear()
		BakedModelFacadeBlockItem.Cache.clear()
		event.map.registerSprite(ResourceLocation(MOD_ID, "misc/blank"))
	}

}