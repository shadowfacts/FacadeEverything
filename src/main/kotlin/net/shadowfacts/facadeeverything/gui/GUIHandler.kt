package net.shadowfacts.facadeeverything.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.block.table.ContainerTable
import net.shadowfacts.facadeeverything.block.table.GUITable
import net.shadowfacts.facadeeverything.item.applicator.ContainerApplicator
import net.shadowfacts.facadeeverything.item.applicator.GUIApplicator

/**
 * @author shadowfacts
 */
object GUIHandler: IGuiHandler {

	val ASSEMBLY_TABLE = 0
	val APPLICATOR = 2

	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			ASSEMBLY_TABLE -> GUITable.create(getServerGuiElement(ID, player, world, x, y, z)!!, ModBlocks.table.getTileEntity(world, pos))
			APPLICATOR -> GUIApplicator.create(getServerGuiElement(ID, player, world, x, y, z)!!)
			else -> null
		}
	}

	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Container? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			ASSEMBLY_TABLE -> ContainerTable(pos, player.inventory, ModBlocks.table.getTileEntity(world, pos))
			APPLICATOR -> ContainerApplicator(player.position, player.inventory, player.getHeldItem(EnumHand.values()[x]))
			else -> null
		}
	}

}