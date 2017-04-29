package net.shadowfacts.facadeeverything.block.disassembly

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.shadowmc.ui.dsl.*

/**
 * @author shadowfacts
 */
object GUIDisassemblyTable {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/disassembly_table.png")

	fun create(container: Container, table: TileEntityDisassemblyTable): GuiContainer {
		return container(container) {
			fixed {
				id = "root"
				width = 176
				height = 166

				image {
					id = "bg"
					width = 176
					height = 166
					texture = BG
				}

				fixed {
					id = "top"
					width = 176
					height = 166

					label {
						id = "table"
						text = ModBlocks.disassembly.localizedName
					}

					label {
						id = "inventory"
						text = Minecraft.getMinecraft().player.inventory.displayName.unformattedText
					}
				}
			}

			style("$MOD_ID:disassembly_table")
		}
	}

}