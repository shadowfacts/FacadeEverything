package net.shadowfacts.facadeeverything.block.painter

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.facadeeverything.block.ModBlocks
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
	}

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST))
	val inventory = object: ItemStackHandler(2) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			val res = if (slot === SLOT_BLANK && (stack.item !== ModItems.facade || stack.getFacadeState() !== null)) {
				stack
			} else if (slot === SLOT_TEMPLATE && (!stack.isItemBlock || stack.getState()!!.block == ModBlocks.facade)) {
				stack
			} else {
				super.insertItem(slot, stack, simulate)
			}

			if (!simulate) {
				updateOutput()
			}

			return res
		}

		override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
			val res = super.extractItem(slot, amount, simulate)

			if (!simulate) {
				updateOutput()
			}

			return res
		}
	}

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.DOWN))
	val output = object: ItemStackHandler(1) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			return stack
		}

		override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
			val res = super.extractItem(slot, amount, simulate)

			if (!simulate) {
				removeInput()
				updateOutput()
			}

			return res
		}
	}

	fun updateOutput() {
		val blank = inventory.getStackInSlot(SLOT_BLANK)
		val template = inventory.getStackInSlot(SLOT_TEMPLATE)
		output.setStackInSlot(0, if (blank.isEmpty || template.isEmpty) ItemStack.EMPTY else getOutput())
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
		tag.setTag("output", output.serializeNBT())
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inventory.deserializeNBT(tag.getCompoundTag("inventory"))
		output.deserializeNBT(tag.getCompoundTag("output"))
		super.readFromNBT(tag)
	}

}