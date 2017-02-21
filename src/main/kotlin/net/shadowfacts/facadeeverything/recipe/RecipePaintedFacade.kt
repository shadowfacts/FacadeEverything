package net.shadowfacts.facadeeverything.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.facadeeverything.util.getFacadeState
import net.shadowfacts.facadeeverything.util.getState
import net.shadowfacts.facadeeverything.util.isItemBlock
import net.shadowfacts.forgelin.extensions.first
import net.shadowfacts.forgelin.extensions.forEach
import net.shadowfacts.forgelin.extensions.forEachIndexed

/**
 * @author shadowfacts
 */
object RecipePaintedFacade: IRecipe {

	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		var blanks = 0
		var blocks = 0
		var others = 0

		inv.forEach {
			if (it.isItemBlock && it.getState()!!.block !== ModBlocks.facade) blocks++
			else if (it.item == ModItems.facade && it.getFacadeState() == null) blanks++
			else if (!it.isEmpty) others++
		}

		return blanks == 1 && blocks == 1 && others == 0
	}

	override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
		val block = inv.first { it.isItemBlock }
		return ItemFacade.forState(block.getState()!!)
	}

	override fun getRecipeSize(): Int {
		return 2
	}

	override fun getRecipeOutput(): ItemStack {
		return ItemStack(ModItems.facade)
	}

	override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
		val list = NonNullList.withSize(inv.sizeInventory, ItemStack.EMPTY)

		inv.forEachIndexed { i, it ->
			if (it.isItemBlock) {
				list[i] = it.copy()
			}
		}

		return list
	}

}