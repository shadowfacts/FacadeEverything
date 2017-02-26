package net.shadowfacts.facadeeverything.block.facade

import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.text.TextFormatting
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.getStateForSide
import net.shadowfacts.shadowmc.util.KeyboardHelper
import net.shadowfacts.shadowmc.util.RelativeSide

/**
 * @author shadowfacts
 */
class ItemBlockFacade: ItemBlock(ModBlocks.facade) {

	init {
		registryName = block.registryName
	}

	override fun getMetadata(damage: Int): Int {
		return EnumFacing.NORTH.ordinal
	}

	override fun getMetadata(stack: ItemStack?): Int {
		return EnumFacing.NORTH.ordinal
	}

	override fun getItemStackDisplayName(stack: ItemStack): String {
		return I18n.format("$unlocalizedName.specific.name", stack.base.block.localizedName)
	}

	override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
		if (KeyboardHelper.isShiftPressed()) {
			RelativeSide.values().forEach {
				val state = stack.getStateForSide(it)
				if (state != null) {
					tooltip.add(I18n.format("$unlocalizedName.tooltip", I18n.format("side.${it.name.toLowerCase()}"), state.block.localizedName))
				}
			}
		} else {
			tooltip.add("${TextFormatting.ITALIC}Press [Shift] for facades")
		}
	}

}