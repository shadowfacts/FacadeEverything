package net.shadowfacts.facadeeverything.block.assembly

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.gui.element.UIFacadePreview
import net.shadowfacts.shadowmc.ui.dsl.container

/**
 * @author shadowfacts
 */
object GUIAssemblyTable {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/assembly_table.png")
	private val PREVIEW_BG = ResourceLocation(MOD_ID, "textures/gui/preview.png")

	fun create(container: Container, table: TileEntityAssemblyTable): GuiContainer {
		return container(container) {
			fixed {
				id = "root"
				width = 176 + 83 * 2
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
						text = ModBlocks.assembly.localizedName
					}

					label {
						id = "inventory"
						text = Minecraft.getMinecraft().player.inventory.displayName.unformattedText
					}
				}

				fixed {
					id = "preview-container"
					width = 83
					height = 86

					image {
						id = "preivew-bg"
						width = 83
						height = 86
						texture = PREVIEW_BG
					}

					add(UIFacadePreview(table, "preview"))
				}
			}
			style("$MOD_ID:assembly_table")
		}
	}

}