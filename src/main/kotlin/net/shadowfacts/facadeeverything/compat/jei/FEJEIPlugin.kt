package net.shadowfacts.facadeeverything.compat.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.ingredients.IIngredientRegistry
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.minecraft.item.ItemStack
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.recipe.RecipePaintFacade

/**
 * @author shadowfacts
 */
@JEIPlugin
class FEJEIPlugin: IModPlugin {

	companion object {
		lateinit var INGREDIENTS: IIngredientRegistry
			private set
	}

	override fun register(registry: IModRegistry) {
		INGREDIENTS = registry.ingredientRegistry
		registry.handleRecipes(RecipePaintFacade::class.java, { RecipeWrapperPaintedFacade }, VanillaRecipeCategoryUid.CRAFTING)
		registry.jeiHelpers.ingredientBlacklist.addIngredientToBlacklist(ItemStack(ModBlocks.facade))
	}

}