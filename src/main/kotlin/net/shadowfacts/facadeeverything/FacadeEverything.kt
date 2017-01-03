package net.shadowfacts.facadeeverything

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.event.ClientEventHandler
import net.shadowfacts.facadeeverything.gui.GUIHandler
import net.shadowfacts.facadeeverything.network.PacketRequestUpdate
import net.shadowfacts.facadeeverything.network.PacketUpdate
import net.shadowfacts.facadeeverything.util.CommandStateId

/**
 * @author shadowfacts
 */
@Mod(modid = MOD_ID, name = NAME, version = "@VERSION@", dependencies = "required-after:shadowmc;", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object FacadeEverything {

	lateinit var network: SimpleNetworkWrapper
		private set

//	Content
	val blocks = ModBlocks

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		blocks.init()

		NetworkRegistry.INSTANCE.registerGuiHandler(this, GUIHandler)

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID)
		network.registerMessage(PacketRequestUpdate.Handler, PacketRequestUpdate::class.java, 0, Side.SERVER)
		network.registerMessage(PacketUpdate.Handler, PacketUpdate::class.java, 1, Side.CLIENT)
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	fun preInitClient(event: FMLPreInitializationEvent) {
		MinecraftForge.EVENT_BUS.register(ClientEventHandler)
	}

	@Mod.EventHandler
	fun serverStarting(event: FMLServerStartingEvent) {
		event.registerServerCommand(CommandStateId)
	}

}