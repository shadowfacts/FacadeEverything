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

	private val models: MutableMap<Triple<String, String, String>, IModel> = mutableMapOf()

	private fun init() {
		models[Triple(MOD_ID, "facade_block", "normal")] = ModelFacadeBlock
		models[Triple(MOD_ID, "facade_block", "inventory")] = ModelFacadeBlockItem
		models[Triple(MOD_ID, "facade", "inventory")] = ModelFacadeItem
	}

	override fun accepts(modelLocation: ResourceLocation): Boolean {
		if (modelLocation is ModelResourceLocation) {
			return models.keys.firstOrNull {
				it.first == modelLocation.resourceDomain && it.second == modelLocation.resourcePath && it.third == modelLocation.variant
			} != null
		} else {
			return models.keys.firstOrNull {
				it.first == modelLocation.resourceDomain && it.second == modelLocation.resourcePath
			} != null
		}
	}

	override fun loadModel(modelLocation: ResourceLocation): IModel {
		if (modelLocation is ModelResourceLocation) {
			return models.toList().first {
				it.first.first == modelLocation.resourceDomain && it.first.second == modelLocation.resourcePath && it.first.third == modelLocation.variant
			}.second
		} else {
			return models.toList().first {
				it.first.first == modelLocation.resourceDomain && it.first.second == modelLocation.resourcePath
			}.second
		}
	}

	override fun onResourceManagerReload(resourceManager: IResourceManager) {
		models.clear()
		init()
	}


}