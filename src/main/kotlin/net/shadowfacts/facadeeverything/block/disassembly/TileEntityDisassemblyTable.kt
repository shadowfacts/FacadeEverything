package net.shadowfacts.facadeeverything.block.disassembly

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.facadeeverything.block.facade.ItemBlockFacade
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.util.InsertOverrideItemHandler
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.getStateForSide
import net.shadowfacts.forgelin.extensions.firstOrEmpty
import net.shadowfacts.forgelin.extensions.get
import net.shadowfacts.forgelin.extensions.isEmpty
import net.shadowfacts.shadowmc.capability.CapHolder
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity
import net.shadowfacts.shadowmc.util.RelativeSide

/**
 * @author shadowfacts
 */
class TileEntityDisassemblyTable: BaseTileEntity() {

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.UP))
	val input = object: ItemStackHandler(1) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			val res = if (stack.item !is ItemBlockFacade) {
				stack
			} else {
				super.insertItem(slot, stack, simulate)
			}

			if (!simulate) {
				updateOutput()
			}

			return res
		}
	}

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST))
	val facades = InsertOverrideItemHandler(6)

	@CapHolder(capabilities = arrayOf(IItemHandler::class), sides = arrayOf(EnumFacing.DOWN))
	val output = InsertOverrideItemHandler(1)

	fun updateOutput() {
		val input = input[0]
		if (input.isEmpty) return

		val baseMeta = input.base.block.getMetaFromState(input.base)
		val base = ItemStack(input.base.block, 1, baseMeta)

		if (!output.insertForce(0, base, true).isEmpty) return

		val facades = RelativeSide.values().mapIndexed { i: Int, it: RelativeSide ->
			val facade = input.getStateForSide(it)
			if (facade == null) null else ItemFacade.forState(facade)
		}

		facades.forEachIndexed { i, it ->
			if (it != null && !this.facades.insertForce(i, it, true).isEmpty) {
				return
			}
		}

		this.input.extractItem(0, 1, false)
		output.insertForce(0, base, false)
		facades.forEachIndexed { i, it ->
			if (it != null) {
				this.facades.insertForce(i, it, false)
			}
		}

//		if (!output.getStackInSlot(0).isEmpty && output.getStackInSlot(0).count == output.getStackInSlot(0).maxStackSize) return
//		if (!facades.isEmpty && !facades.firstOrEmpty { it.count == it.maxStackSize }.isEmpty) return
//
//		val input = input.getStackInSlot(0)
//		if (input.isEmpty) return
//
//		RelativeSide.values().forEachIndexed { i, it ->
//			val facade = input.getStateForSide(it)
//			if (facade != null) {
//				facades.insertItem(i, ItemFacade.forState(facade), false)
//			}
//		}
//		output.insertItem(0, ItemStack(input.base.block, 1, input.base.block.getMetaFromState(input.base)), false)
//
//		this.input.extractItem(0, 1, false)
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