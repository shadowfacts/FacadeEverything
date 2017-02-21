package net.shadowfacts.facadeeverything.compat.jei

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.facadeeverything.util.getState
import net.shadowfacts.facadeeverything.util.isItemBlock

/**
 * @author shadowfacts
 */
object RecipeWrapperPaintedFacade: BlankRecipeWrapper() {

	val BLOCKS by lazy {
		FEJEIPlugin.INGREDIENTS.getIngredients(ItemStack::class.java).filter {
			if (it.isItemBlock) {
				val state = it.getState()!!
				!state.block.hasTileEntity(state)
			} else {
				false
			}
		}
	}
	val FACADES by lazy {
		BLOCKS.map { ItemFacade.forState(it.getState()!!) }
	}

	override fun getIngredients(ingredients: IIngredients) {
		ingredients.setInputLists(ItemStack::class.java, listOf(listOf(ItemStack(ModItems.facade)), BLOCKS))
		ingredients.setOutputLists(ItemStack::class.java, listOf(FACADES))
	}

}