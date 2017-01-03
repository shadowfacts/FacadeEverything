package net.shadowfacts.facadeeverything.block.table

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerTable(pos: BlockPos, playerInv: InventoryPlayer, table: TileEntityTable): ContainerBase(pos) {

	init {
		addSlotToContainer(SlotTable(table, table.input, 0, 48, 35))
		addSlotToContainer(SlotTable(table, table.facades, EnumFacing.DOWN.ordinal, 48, 62))
		addSlotToContainer(SlotTable(table, table.facades, EnumFacing.UP.ordinal, 48, 8))
		addSlotToContainer(SlotTable(table, table.facades, EnumFacing.NORTH.ordinal, 21, 26))
		addSlotToContainer(SlotTable(table, table.facades, EnumFacing.SOUTH.ordinal, 75, 44))
		addSlotToContainer(SlotTable(table, table.facades, EnumFacing.WEST.ordinal, 21, 44))
		addSlotToContainer(SlotTable(table, table.facades, EnumFacing.EAST.ordinal, 75, 26))

		addSlotToContainer(SlotTableOutput(table, table.output, 0, 132, 34))

		for (i in 0..2) {
			for (j in 0..8) {
				this.addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (k in 0..8) {
			this.addSlotToContainer(Slot(playerInv, k, 8 + k * 18, 142))
		}
	}

	open class SlotTable(val table: TileEntityTable, handler: IItemHandler, id: Int, x: Int, y: Int): SlotItemHandler(handler, id, x, y) {
		override fun onSlotChanged() {
			table.updateOutput()
			table.markDirty()
		}
	}

	class SlotTableOutput(table: TileEntityTable, handler: IItemHandler, id: Int, x: Int, y: Int): SlotTable(table, handler, id, x, y) {
		override fun onSlotChanged() {
			super.onSlotChanged()
			table.removeInput()
		}
	}

}