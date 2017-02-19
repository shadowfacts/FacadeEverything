package net.shadowfacts.facadeeverything.item.applicator

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.resources.I18n
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.item.ModItems
import net.shadowfacts.shadowmc.ui.dsl.container

/**
 * @author shadowfacts
 */
object GUIApplicator {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/applicator.png")

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
					id = "applicator"
					text = I18n.format("${ModItems.applicator.unlocalizedName}.name")
				}

				label {
					id = "inventory"
					text = Minecraft.getMinecraft().player.inventory.displayName.unformattedText
				}
			}

			style("$MOD_ID:applicator")
		}
	}

}