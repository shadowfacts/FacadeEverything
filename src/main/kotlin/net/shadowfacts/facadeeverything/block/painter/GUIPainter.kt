package net.shadowfacts.facadeeverything.block.painter

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.shadowmc.ui.dsl.container

/**
 * @author shadowfacts
 */
object GUIPainter {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/painter.png")

	fun create(container: Container): GuiContainer {
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

				label {
					id = "painter"
					text = ModBlocks.painter.localizedName
				}

				label {
					id = "inventory"
					text = Minecraft.getMinecraft().player.inventory.displayName.unformattedText
				}
			}

			style("$MOD_ID:painter")
		}
	}

}