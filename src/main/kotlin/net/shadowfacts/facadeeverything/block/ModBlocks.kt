package net.shadowfacts.facadeeverything.block

import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.block.table.BlockTable
import net.shadowfacts.shadowmc.block.ModBlocks

/**
 * @author shadowfacts
 */
object ModBlocks: ModBlocks() {

	val facade = BlockFacade()
	val table = BlockTable()

	override fun init() {
		register(facade)
		register(table)
	}

}