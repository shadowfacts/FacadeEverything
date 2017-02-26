package net.shadowfacts.facadeeverything.block.facade

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.shadowfacts.facadeeverything.FacadeEverything
import net.shadowfacts.facadeeverything.network.PacketRequestUpdate
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity
import net.shadowfacts.shadowmc.util.RelativeSide
import java.util.*

/**
 * @author shadowfacts
 */
class TileEntityFacade: BaseTileEntity() {

	var base: IBlockState = Blocks.STONE.defaultState
	val facades: MutableMap<RelativeSide, IBlockState?> = EnumMap(RelativeSide::class.java)

	fun getFacadeForSide(side: EnumFacing): IBlockState {
		val front = world.getBlockState(pos).getValue(BlockFacade.FRONT)
		return facades[RelativeSide.forFacing(front, side)] ?: base
	}

	override fun onLoad() {
		if (world.isRemote) {
			FacadeEverything.network.sendToServer(PacketRequestUpdate(this))
		}
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setInteger("base", Block.getStateId(base))
		facades.forEach {
			tag.setInteger(it.key.name.toLowerCase(), if (it.value == null) -1 else Block.getStateId(it.value))
		}
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		base = Block.getStateById(tag.getInteger("base"))
		facades.clear()
		RelativeSide.values().forEach {
			val id = tag.getInteger(it.name.toLowerCase())
			facades[it] = if (id <= 0) null else Block.getStateById(id)
		}
		super.readFromNBT(tag)
	}

}