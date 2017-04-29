package net.shadowfacts.facadeeverything.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.block.assembly.ContainerAssemblyTable
import net.shadowfacts.facadeeverything.block.assembly.GUIAssemblyTable
import net.shadowfacts.facadeeverything.block.disassembly.ContainerDisassemblyTable
import net.shadowfacts.facadeeverything.block.disassembly.GUIDisassemblyTable
import net.shadowfacts.facadeeverything.item.applicator.ContainerApplicator
import net.shadowfacts.facadeeverything.item.applicator.GUIApplicator

/**
 * @author shadowfacts
 */
object GUIHandler: IGuiHandler {

	val ASSEMBLY_TABLE = 0
	val DISASSEMBLY_TABLE = 1
	val APPLICATOR = 2

	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			ASSEMBLY_TABLE -> GUIAssemblyTable.create(getServerGuiElement(ID, player, world, x, y, z)!!, ModBlocks.assembly.getTileEntity(world, pos))
			DISASSEMBLY_TABLE -> GUIDisassemblyTable.create(getServerGuiElement(ID, player, world, x, y, z)!!, ModBlocks.disassembly.getTileEntity(world, pos))
			APPLICATOR -> GUIApplicator.create(getServerGuiElement(ID, player, world, x, y, z)!!)
			else -> null
		}
	}

	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Container? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			ASSEMBLY_TABLE -> ContainerAssemblyTable(pos, player.inventory, ModBlocks.assembly.getTileEntity(world, pos))
			DISASSEMBLY_TABLE -> ContainerDisassemblyTable(pos, player.inventory, ModBlocks.disassembly.getTileEntity(world, pos))
			APPLICATOR -> ContainerApplicator(player.position, player.inventory, player.getHeldItem(EnumHand.values()[x]))
			else -> null
		}
	}

}