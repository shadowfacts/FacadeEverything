package net.shadowfacts.facadeeverything.util

import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

/**
 * @author shadowfacts
 */
class InsertOverrideItemHandler(size: Int): ItemStackHandler(size) {

	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		return stack
	}

	fun insertForce(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		return super.insertItem(slot, stack, simulate)
	}

}