package net.shadowfacts.facadeeverything

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.ShapedOreRecipe
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.compat.top.CompatTOP
import net.shadowfacts.facadeeverything.event.ClientEventHandler
import net.shadowfacts.facadeeverything.gui.GUIHandler
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.facadeeverything.model.FEModelLoader
import net.shadowfacts.facadeeverything.network.PacketRequestUpdate
import net.shadowfacts.facadeeverything.network.PacketUpdate
import net.shadowfacts.facadeeverything.recipe.RecipePaintFacade
import net.shadowfacts.facadeeverything.recipe.RecipeUnpaintFacade
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
		FEConfig.init(event.modConfigurationDirectory)

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
	fun init(event: FMLInitializationEvent) {
		if (Loader.isModLoaded("theoneprobe")) {
			CompatTOP.init()
		}
	}

	@Mod.EventHandler
	fun serverStarting(event: FMLServerStartingEvent) {
		event.registerServerCommand(CommandStateId)
	}

	@Mod.EventBusSubscriber
	object RegistrationHandler {

		@JvmStatic
		@SubscribeEvent
		fun registerBlocks(event: RegistryEvent.Register<Block>) {
			event.registry.registerAll(
					blocks.facade,
					blocks.assembly,
					blocks.disassembly
			)
		}

		@JvmStatic
		@SubscribeEvent
		fun registerItems(event: RegistryEvent.Register<Item>) {
			event.registry.registerAll(
					blocks.facade.createItemBlock(),
					blocks.assembly.createItemBlock(),
					blocks.disassembly.createItemBlock(),
					items.facade,
					items.applicator
			)
		}

		@JvmStatic
		@SubscribeEvent
		fun registerRecipes(event: RegistryEvent.Register<IRecipe>) {
			event.registry.registerAll(
					RecipePaintFacade,
					RecipeUnpaintFacade
			)
		}

		@JvmStatic
		@SubscribeEvent
		fun registerModels(event: ModelRegistryEvent) {
			blocks.facade.initItemModel()
			blocks.assembly.initItemModel()
			blocks.disassembly.initItemModel()
			items.facade.initItemModel()
			items.applicator.initItemModel()
		}

	}

}