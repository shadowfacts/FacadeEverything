package net.shadowfacts.facadeeverything.compat.jei

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.shadowfacts.facadeeverything.recipe.RecipePaintFacade

/**
 * @author shadowfacts
 */
object RecipeHandlerPaintedFacade: IRecipeHandler<RecipePaintFacade> {

	override fun isRecipeValid(recipe: RecipePaintFacade?): Boolean {
		return true
	}

	override fun getRecipeCategoryUid(recipe: RecipePaintFacade?): String {
		return VanillaRecipeCategoryUid.CRAFTING
	}

	override fun getRecipeClass(): Class<RecipePaintFacade> {
		return RecipePaintFacade::class.java
	}

	override fun getRecipeWrapper(recipe: RecipePaintFacade): IRecipeWrapper {
		return RecipeWrapperPaintedFacade
	}

}