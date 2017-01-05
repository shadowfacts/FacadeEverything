package net.shadowfacts.facadeeverything.util

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemBlockSpecial
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.util.Constants
import net.shadowfacts.facadeeverything.item.ItemFacade
import java.util.*

/**
 * @author shadowfacts
 */
private fun ItemStack.initDefaults() {
	tagCompound = NBTTagCompound().apply {
		setInteger("base", Block.getStateId(Blocks.STONE.defaultState))
		EnumFacing.VALUES.forEach {
			setInteger(it.name.toLowerCase(), -1)
		}
	}
}

var ItemStack.baseId: Int
	get() {
		if (!hasTagCompound()) initDefaults()
		return tagCompound!!.getInteger("base")
	}
	set(value) {
		if (!hasTagCompound()) initDefaults()
		tagCompound!!.setInteger("base", value)
	}

var ItemStack.base: IBlockState
	get() {
		return Block.getStateById(baseId)
	}
	set(value) {
		baseId = Block.getStateId(value)
	}

fun ItemStack.getStateIdForSide(side: EnumFacing): Int {
	if (!hasTagCompound()) initDefaults()
	return if (tagCompound!!.hasKey(side.name.toLowerCase())) tagCompound!!.getInteger(side.name.toLowerCase()) else -1
}

fun ItemStack.setStateIdForSide(side: EnumFacing, id: Int) {
	if (!hasTagCompound()) initDefaults()
	tagCompound!!.setInteger(side.name.toLowerCase(), id)
}

fun ItemStack.getStateForSide(side: EnumFacing): IBlockState? {
	val id = getStateIdForSide(side)
	return if (id == -1) null else Block.getStateById(id)
}

fun ItemStack.setStateForSide(side: EnumFacing, state: IBlockState?) {
	setStateIdForSide(side, if (state == null) -1 else Block.getStateId(state))
}

val ItemStack.sideIds: Map<EnumFacing, Int>
	get() {
		val map = mutableMapOf<EnumFacing, Int>()
		EnumFacing.VALUES.forEach {
			map.put(it, getStateIdForSide(it))
		}
		return map
	}

val ItemStack.sides: Map<EnumFacing, IBlockState?>
	get() {
		val map = EnumMap<EnumFacing, IBlockState?>(EnumFacing::class.java)
		EnumFacing.VALUES.forEach {
			map.put(it, getStateForSide(it))
		}
		return map
	}

val ItemStack.isItemBlock: Boolean
	get() = item is ItemBlock || item is ItemBlockSpecial

fun ItemStack.getState(): IBlockState? {
	var block: Block? = null
	if (item is ItemBlock) {
		block = (item as ItemBlock).block
	} else if (item is ItemBlockSpecial) {
		block = (item as ItemBlockSpecial).block
	}

	return block?.getStateFromMeta(metadata)
}

fun ItemStack.getFacadeState(): IBlockState? {
	if (tagCompound?.hasKey(ItemFacade.TAG_FACADE, Constants.NBT.TAG_INT) ?: false) {
		return Block.getStateById(tagCompound!!.getInteger(ItemFacade.TAG_FACADE))
	} else {
		return null
	}
}