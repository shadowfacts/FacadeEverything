package net.shadowfacts.facadeeverything.event

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.model.block.ModelFacadeBlock
import net.shadowfacts.facadeeverything.model.QuadCache
import net.shadowfacts.facadeeverything.model.item.ModelFacadeItem

/**
 * @author shadowfacts
 */
object ClientEventHandler {

	@SubscribeEvent
	fun onModelBake(event: ModelBakeEvent) {
		event.modelRegistry.putObject(ModelResourceLocation(ResourceLocation(MOD_ID, "facade_block"), "normal"), ModelFacadeBlock)
		event.modelRegistry.putObject(ModelResourceLocation(ResourceLocation(MOD_ID, "facade"), "inventory"), ModelFacadeItem(null))
	}

	@SubscribeEvent
	fun onTextureStitch(event: TextureStitchEvent.Pre) {
		QuadCache.clear()
	}

}