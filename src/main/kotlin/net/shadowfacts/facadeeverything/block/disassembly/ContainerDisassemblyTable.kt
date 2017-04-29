package net.shadowfacts.facadeeverything.block.disassembly

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.shadowmc.inventory.ContainerBase
import net.shadowfacts.shadowmc.util.RelativeSide

/**
 * @author shadowfacts
 */
class ContainerDisassemblyTable(pos: BlockPos, playerInv: InventoryPlayer, table: TileEntityDisassemblyTable): ContainerBase(pos) {

	init {
		addSlotToContainer(SlotTable(table, table.output, 0, 48, 35))
		addSlotToContainer(SlotTable(table, table.facades, RelativeSide.BOTTOM.ordinal, 48, 62))
		addSlotToContainer(SlotTable(table, table.facades, RelativeSide.TOP.ordinal, 48, 8))
		addSlotToContainer(SlotTable(table, table.facades, RelativeSide.FRONT.ordinal, 21, 26))
		addSlotToContainer(SlotTable(table, table.facades, RelativeSide.BACK.ordinal, 75, 44))
		addSlotToContainer(SlotTable(table, table.facades, RelativeSide.RIGHT.ordinal, 21, 44))
		addSlotToContainer(SlotTable(table, table.facades, RelativeSide.LEFT.ordinal, 75, 26))

		addSlotToContainer(SlotTableInput(table, table.input, 0, 133, 35))

		for (i in 0..2) {
			for (j in 0..8) {
				this.addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (k in 0..8) {
			this.addSlotToContainer(Slot(playerInv, k, 8 + k * 18, 142))
		}
	}

	open class SlotTable(val table: TileEntityDisassemblyTable, handler: IItemHandler, id: Int, x: Int, y: Int): SlotItemHandler(handler, id, x, y)

	class SlotTableInput(table: TileEntityDisassemblyTable, handler: IItemHandler, id: Int, x: Int, y: Int): SlotTable(table, handler, id, x, y) {
		override fun onSlotChanged() {
			table.updateOutput()
		}
	}

}