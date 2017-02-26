package net.shadowfacts.facadeeverything.block.table

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.util.*
import net.shadowfacts.forgelin.extensions.forEach
import net.shadowfacts.shadowmc.capability.CapHolder
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity
import net.shadowfacts.shadowmc.util.RelativeSide

/**
 * @author shadowfacts
 */
class TileEntityTable: BaseTileEntity() {

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.UP))
	val input = object: ItemStackHandler(1) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			val res = if (!stack.isItemBlock || stack.getState()!!.block == ModBlocks.facade) {
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

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST))
	val facades = object: ItemStackHandler(6) {

		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			val res = if (stack.item !is ItemFacade || stack.getFacadeState() === null) {
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

	fun hasAtLeastOneFacade(): Boolean {
		facades.forEach {
			if (!it.isEmpty) return true
		}
		return false
	}

	fun getOutput(): ItemStack {
		val base = input.getStackInSlot(0).getState()
		val stack: ItemStack
		if (base != null && hasAtLeastOneFacade()) {
			stack = ItemStack(ModBlocks.facade)
			stack.base = base
			RelativeSide.values().forEachIndexed { i, side ->
				stack.setStateForSide(side, facades.getStackInSlot(i).getFacadeState())
			}
		} else {
			stack = ItemStack.EMPTY
		}
		return stack
	}

	fun updateOutput() {
		output.setStackInSlot(0, getOutput())
		markDirty()
	}

	fun removeInput() {
		input.extractItem(0, 1, false)
		for (i in 0..facades.slots - 1) {
			facades.extractItem(i, 1, false)
		}
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setTag("input", input.serializeNBT())
		tag.setTag("output", output.serializeNBT())
		tag.setTag("facades", facades.serializeNBT())
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		input.deserializeNBT(tag.getCompoundTag("input"))
		output.deserializeNBT(tag.getCompoundTag("output"))
		facades.deserializeNBT(tag.getCompoundTag("facades"))
		super.readFromNBT(tag)
	}

}