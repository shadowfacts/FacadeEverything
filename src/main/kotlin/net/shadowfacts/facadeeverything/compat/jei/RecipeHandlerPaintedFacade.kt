package net.shadowfacts.facadeeverything.compat.jei

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.shadowfacts.facadeeverything.recipe.RecipePaintedFacade

/**
 * @author shadowfacts
 */
object RecipeHandlerPaintedFacade: IRecipeHandler<RecipePaintedFacade> {

	override fun isRecipeValid(recipe: RecipePaintedFacade?): Boolean {
		return true
	}

	override fun getRecipeCategoryUid(recipe: RecipePaintedFacade?): String {
		return VanillaRecipeCategoryUid.CRAFTING
	}

	override fun getRecipeClass(): Class<RecipePaintedFacade> {
		return RecipePaintedFacade::class.java
	}

	override fun getRecipeWrapper(recipe: RecipePaintedFacade): IRecipeWrapper {
		return RecipeWrapperPaintedFacade
	}

}