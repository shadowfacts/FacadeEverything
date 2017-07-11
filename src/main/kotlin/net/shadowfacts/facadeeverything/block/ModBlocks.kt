package net.shadowfacts.facadeeverything.block

import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.block.assembly.BlockAssemblyTable
import net.shadowfacts.facadeeverything.block.disassembly.BlockDisassemblyTable

/**
 * @author shadowfacts
 */
object ModBlocks {

	val facade = BlockFacade()
	val assembly = BlockAssemblyTable()
	val disassembly = BlockDisassemblyTable()

}