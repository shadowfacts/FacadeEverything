package net.shadowfacts.facadeeverything.compat.waila

import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import mcp.mobius.waila.api.IWailaDataProvider
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.shadowfacts.facadeeverything.FEConfig
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.block.facade.TileEntityFacade

/**
 * @author shadowfacts
 */
object FacadeDataProvider: IWailaDataProvider {

	override fun getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler?): ItemStack {
		if (FEConfig.inWorldCamoTooltip) {
			val tile = accessor.tileEntity as TileEntityFacade
			val side = tile.facades[accessor.side]
			val stack: ItemStack
			if (side != null) {
				stack = ItemStack(side.block, 1, side.block.getMetaFromState(side))
			} else {
				stack = ItemStack(tile.base.block, 1, tile.base.block.getMetaFromState(tile.base))
			}
			if (!stack.isEmpty) return stack
		}
		return accessor.block.getPickBlock(accessor.blockState, accessor.mop, accessor.world, accessor.position, accessor.player)
	}

	override fun getWailaHead(itemStack: ItemStack?, currenttip: MutableList<String>, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String> {
		return currenttip
	}

	override fun getWailaBody(itemStack: ItemStack?, currenttip: MutableList<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler?): MutableList<String> {
		if (!FEConfig.inWorldCamoTooltip) {
			if (accessor.player.isSneaking) {
				val tile = accessor.tileEntity as TileEntityFacade
				EnumFacing.VALUES.forEach {
					val state = tile.facades[it]
					if (state != null) {
						currenttip.add(I18n.format("${ModBlocks.facade.unlocalizedName}.tooltip", I18n.format("direction.${it.name.toLowerCase()}"), state.block.localizedName))
					}
				}
			} else {
				currenttip.add(TextFormatting.ITALIC.toString() + I18n.format("${ModBlocks.facade.unlocalizedName}.tooltip.sneak"))
			}
		}
		return currenttip
	}

	override fun getWailaTail(itemStack: ItemStack?, currenttip: MutableList<String>, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String> {
		return currenttip
	}

	override fun getNBTData(player: EntityPlayerMP?, te: TileEntity?, tag: NBTTagCompound, world: World?, pos: BlockPos?): NBTTagCompound {
		return tag
	}

}