package net.shadowfacts.facadeeverything.block

import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.block.facade.ItemBlockFacade
import net.shadowfacts.facadeeverything.block.assembly.BlockAssemblyTable
import net.shadowfacts.facadeeverything.block.disassembly.BlockDisassemblyTable
import net.shadowfacts.shadowmc.block.ModBlocks

/**
 * @author shadowfacts
 */
object ModBlocks: ModBlocks() {

	val facade = BlockFacade()
	val assembly = BlockAssemblyTable()
	val disassembly = BlockDisassemblyTable()

	override fun init() {
		register(facade, ItemBlockFacade())
		register(assembly)
		register(disassembly)
	}

}