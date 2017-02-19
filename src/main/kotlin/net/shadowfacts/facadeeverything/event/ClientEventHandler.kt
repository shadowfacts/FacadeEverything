package net.shadowfacts.facadeeverything.event

import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.model.QuadCache
import net.shadowfacts.facadeeverything.model.item.BakedModelFacadeBlockItem
import net.shadowfacts.facadeeverything.model.item.BakedModelFacadeItem

/**
 * @author shadowfacts
 */
object ClientEventHandler {

	@SubscribeEvent
	fun onTextureStitch(event: TextureStitchEvent.Pre) {
		QuadCache.clear()
		BakedModelFacadeItem.Cache.clear()
		BakedModelFacadeBlockItem.Cache.clear()
		event.map.registerSprite(ResourceLocation(MOD_ID, "misc/blank"))
	}

}