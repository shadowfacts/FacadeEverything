package net.shadowfacts.facadeeverything.compat.jei

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.ingredients.IIngredientRegistry
import net.minecraft.item.ItemStack
import net.shadowfacts.facadeeverything.block.ModBlocks

/**
 * @author shadowfacts
 */
@JEIPlugin
class FEJEIPlugin: BlankModPlugin() {

	companion object {
		lateinit var INGREDIENTS: IIngredientRegistry
			private set
	}

	override fun register(registry: IModRegistry) {
		INGREDIENTS = registry.ingredientRegistry
		registry.addRecipeHandlers(RecipeHandlerPaintedFacade)
		registry.jeiHelpers.ingredientBlacklist.addIngredientToBlacklist(ItemStack(ModBlocks.facade))
	}

}