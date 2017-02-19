package net.shadowfacts.facadeeverything.block.painter

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.facadeeverything.util.getFacadeState
import net.shadowfacts.facadeeverything.util.getState
import net.shadowfacts.facadeeverything.util.isItemBlock
import net.shadowfacts.shadowmc.capability.CapHolder
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity

/**
 * @author shadowfacts
 */
class TileEntityPainter: BaseTileEntity() {

	companion object {
		val SLOT_BLANK = 0 // blank facades
		val SLOT_TEMPLATE = 1 // template block
		val SLOT_OUTPUT = 2 // output
	}

	@CapHolder(capabilities = arrayOf(IItemHandler::class))
	val inventory = object: ItemStackHandler(3) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			when (slot) {
				SLOT_BLANK -> if (stack.item !== ModItems.facade || stack.getFacadeState() !== null) return stack
				SLOT_TEMPLATE -> if (!stack.isItemBlock) return stack
				SLOT_OUTPUT -> return stack
			}
			return super.insertItem(slot, stack, simulate)
		}
	}

	fun updateOutput() {
		val blank = inventory.getStackInSlot(SLOT_BLANK)
		val template = inventory.getStackInSlot(SLOT_TEMPLATE)
		inventory.setStackInSlot(SLOT_OUTPUT, if (blank.isEmpty || template.isEmpty) ItemStack.EMPTY else getOutput())
		markDirty()
	}

	fun removeInput() {
		inventory.extractItem(SLOT_BLANK, 1, false)
	}

	private fun getOutput(): ItemStack {
		return ItemFacade.forState(inventory.getStackInSlot(SLOT_TEMPLATE).getState()!!)
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setTag("inventory", inventory.serializeNBT())
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inventory.deserializeNBT(tag.getCompoundTag("inventory"))
		super.readFromNBT(tag)
	}

}