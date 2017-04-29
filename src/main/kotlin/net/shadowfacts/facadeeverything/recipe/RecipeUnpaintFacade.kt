package net.shadowfacts.facadeeverything.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.facadeeverything.util.getFacadeState
import net.shadowfacts.forgelin.extensions.first
import net.shadowfacts.forgelin.extensions.forEach

/**
 * @author shadowfacts
 */
object RecipeUnpaintFacade: IRecipe {

	override fun matches(inv: InventoryCrafting, world: World): Boolean {
		var facades = 0
		var others = 0

		inv.forEach {
			if (it.item == ModItems.facade && it.getFacadeState() != null) facades++
			else if (!it.isEmpty) others++
		}

		return facades == 1 && others == 0
	}

	override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
		return recipeOutput
	}

	override fun getRecipeSize(): Int {
		return 1
	}

	override fun getRecipeOutput(): ItemStack {
		return ItemStack(ModItems.facade)
	}

	override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
		return NonNullList.withSize(inv.sizeInventory, ItemStack.EMPTY)
	}

}