package net.shadowfacts.facadeeverything.compat.waila

import mcp.mobius.waila.api.IWailaPlugin
import mcp.mobius.waila.api.IWailaRegistrar
import mcp.mobius.waila.api.WailaPlugin
import net.shadowfacts.facadeeverything.block.facade.BlockFacade

/**
 * @author shadowfacts
 */
@WailaPlugin
class FEWailaPlugin: IWailaPlugin {

	override fun register(registrar: IWailaRegistrar) {
		registrar.registerStackProvider(FacadeDataProvider, BlockFacade::class.java)
		registrar.registerBodyProvider(FacadeDataProvider, BlockFacade::class.java)
	}

}