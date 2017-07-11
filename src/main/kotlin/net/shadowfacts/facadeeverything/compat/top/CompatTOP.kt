package net.shadowfacts.facadeeverything.compat.top

import mcjty.theoneprobe.api.ITheOneProbe
import net.minecraftforge.fml.common.event.FMLInterModComms

/**
 * @author shadowfacts
 */
object CompatTOP {

	fun init() {
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "net.shadowfacts.facadeeverything.compat.top.CompatTOP\$Handler")
	}

	class Handler: java.util.function.Function<ITheOneProbe, Void?> {

		override fun apply(top: ITheOneProbe): Void? {
			top.registerBlockDisplayOverride(FacadeBlockDisplayOverride)
			return null
		}

	}

}