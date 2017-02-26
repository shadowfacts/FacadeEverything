package net.shadowfacts.facadeeverything.compat.top

import mcjty.theoneprobe.Tools
import mcjty.theoneprobe.api.*
import mcjty.theoneprobe.config.Config
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.shadowfacts.facadeeverything.block.ModBlocks

/**
 * @author shadowfacts
 */
object FacadeBlockDisplayOverride: IBlockDisplayOverride {

	override fun overrideStandardInfo(mode: ProbeMode, probeInfo: IProbeInfo, player: EntityPlayer, world: World, state: IBlockState, data: IProbeHitData): Boolean {
		if (state.block == ModBlocks.facade) {
			val tile = ModBlocks.facade.getTileEntity(world, data.pos)
			val facadeState = tile.facades[data.sideHit] ?: tile.base
			val stack = ItemStack(facadeState.block, 1, facadeState.block.getMetaFromState(facadeState))
			if (!stack.isEmpty) {
				if (Tools.show(mode, Config.getRealConfig().showModName)) {
					probeInfo.horizontal()
							.item(stack)
							.vertical()
							.itemLabel(stack)
							.text(TextStyleClass.MODNAME.toString() + Tools.getModName(facadeState.block))
				} else {
					probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
							.item(stack)
							.itemLabel(stack)
				}
				return true
			}
		}
		return false
	}

}