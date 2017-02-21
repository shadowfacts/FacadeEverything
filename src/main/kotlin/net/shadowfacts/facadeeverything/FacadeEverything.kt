package net.shadowfacts.facadeeverything

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.ShapedOreRecipe
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.event.ClientEventHandler
import net.shadowfacts.facadeeverything.gui.GUIHandler
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.facadeeverything.model.FEModelLoader
import net.shadowfacts.facadeeverything.network.PacketRequestUpdate
import net.shadowfacts.facadeeverything.network.PacketUpdate
import net.shadowfacts.facadeeverything.recipe.RecipePaintedFacade
import net.shadowfacts.facadeeverything.util.CommandStateId

/**
 * @author shadowfacts
 */
@Mod(modid = MOD_ID, name = NAME, version = "@VERSION@", dependencies = "required-after:shadowmc;required-after:forgelin@[1.3.1,);", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object FacadeEverything {

	lateinit var network: SimpleNetworkWrapper
		private set

//	Content
	val blocks = ModBlocks
	val items = ModItems

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		blocks.init()
		items.init()

		GameRegistry.addRecipe(RecipePaintedFacade)
		GameRegistry.addShapedRecipe(ItemStack(blocks.table), " F ", "FCF", " F ", 'F', items.facade, 'C', Blocks.CRAFTING_TABLE)
		GameRegistry.addRecipe(ShapedOreRecipe(items.applicator, "FF ", "FS ", "  S", 'F', items.facade, 'S', "stickWood"))

		NetworkRegistry.INSTANCE.registerGuiHandler(this, GUIHandler)

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID)
		network.registerMessage(PacketRequestUpdate.Handler, PacketRequestUpdate::class.java, 0, Side.SERVER)
		network.registerMessage(PacketUpdate.Handler, PacketUpdate::class.java, 1, Side.CLIENT)
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	fun preInitClient(event: FMLPreInitializationEvent) {
		MinecraftForge.EVENT_BUS.register(ClientEventHandler)
		ModelLoaderRegistry.registerLoader(FEModelLoader)
	}

	@Mod.EventHandler
	fun serverStarting(event: FMLServerStartingEvent) {
		event.registerServerCommand(CommandStateId)
	}

}