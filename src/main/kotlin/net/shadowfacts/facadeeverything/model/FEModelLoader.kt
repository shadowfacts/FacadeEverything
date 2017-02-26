package net.shadowfacts.facadeeverything.model

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ICustomModelLoader
import net.minecraftforge.client.model.IModel
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.model.block.ModelFacadeBlock
import net.shadowfacts.facadeeverything.model.item.ModelFacadeBlockItem
import net.shadowfacts.facadeeverything.model.item.ModelFacadeItem

/**
 * @author shadowfacts
 */
object FEModelLoader: ICustomModelLoader {

	private val models: MutableMap<Pair<String, String?>, (String?) -> IModel> = mutableMapOf()

	private fun init() {
		models["facade_block" to null] = {
			when (it) {
				"inventory" -> ModelFacadeBlockItem
				else -> ModelFacadeBlock
			}
		}
		models["facade" to "inventory"] = { ModelFacadeItem }
	}

	override fun accepts(modelLocation: ResourceLocation): Boolean {
		if (modelLocation.resourceDomain == MOD_ID) {
			if (modelLocation is ModelResourceLocation) {
				for ((key) in models) {
					if (key.first == modelLocation.resourcePath && (key.second == null || key.second == modelLocation.variant)) {
						return true
					}
				}
			} else {
				for ((key) in models) {
					if (key.first == modelLocation.resourcePath) {
						return true
					}
				}
			}
		}
		return false
	}

	override fun loadModel(modelLocation: ResourceLocation): IModel {
		if (modelLocation is ModelResourceLocation) {
			for ((key, value) in models) {
				if (key.first == modelLocation.resourcePath && (key.second == null || key.second == modelLocation.variant)) {
					return value(modelLocation.variant)
				}
			}
		} else {
			for ((key, value) in models) {
				if (key.first == modelLocation.resourcePath) {
					return value(null)
				}
			}
		}
		throw RuntimeException("Unable to handle model $modelLocation")
	}

	override fun onResourceManagerReload(resourceManager: IResourceManager) {
		models.clear()
		init()
	}


}