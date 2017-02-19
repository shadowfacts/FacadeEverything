package net.shadowfacts.facadeeverything.block.table

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemBlockSpecial
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.getFacadeState
import net.shadowfacts.facadeeverything.util.getState
import net.shadowfacts.facadeeverything.util.setStateForSide
import net.shadowfacts.shadowmc.capability.CapHolder
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity

/**
 * @author shadowfacts
 */
class TileEntityTable: BaseTileEntity() {

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.UP))
	val input = object: ItemStackHandler(1) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if (stack.item !is ItemBlock && stack.item !is ItemBlockSpecial) return stack
			return super.insertItem(slot, stack, simulate)
		}
	}

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.DOWN))
	val output = object: ItemStackHandler(1) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			return stack
		}
	}

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST))
	val facades = object: ItemStackHandler(6) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if (stack.item !is ItemFacade || stack.getFacadeState() == null) return stack
			return super.insertItem(slot, stack, simulate)
		}
	}

	fun getOutput(): ItemStack {
		val base = input.getStackInSlot(0).getState()
		val stack: ItemStack
		if (base != null) {
			stack = ItemStack(ModBlocks.facade)
			stack.base = base
			EnumFacing.VALUES.forEachIndexed { i, side ->
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

//	fun updateOutput() {
//		if (input.getStackInSlot(0).isEmpty) {
//			output.setStackInSlot(0, ItemStack.EMPTY)
//			return
//		}
//		val stack = ItemStack(ModBlocks.facade)
//		stack.base = input.getStackInSlot(0).getState() ?: return
//		EnumFacing.VALUES.forEachIndexed { i, side ->
//			stack.setStateForSide(side, facades.getStackInSlot(i).getFacadeState())
//		}
//		output.setStackInSlot(0, stack)
//	}
//
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