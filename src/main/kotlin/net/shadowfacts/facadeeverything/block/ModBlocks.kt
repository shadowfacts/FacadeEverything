package net.shadowfacts.facadeeverything.block

import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.block.facade.ItemBlockFacade
import net.shadowfacts.facadeeverything.block.painter.BlockPainter
import net.shadowfacts.facadeeverything.block.table.BlockTable
import net.shadowfacts.shadowmc.block.ModBlocks

/**
 * @author shadowfacts
 */
object ModBlocks: ModBlocks() {

	val facade = BlockFacade()
	val table = BlockTable()
	val painter = BlockPainter()

	override fun init() {
		register(facade, ItemBlockFacade())
		register(table)
		register(painter)
	}

}