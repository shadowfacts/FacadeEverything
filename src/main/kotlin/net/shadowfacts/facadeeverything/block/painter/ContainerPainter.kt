package net.shadowfacts.facadeeverything.block.painter

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerPainter(pos: BlockPos, playerInv: InventoryPlayer, painter: TileEntityPainter): ContainerBase(pos) {

	init {
		addSlotToContainer(SlotPainter(painter, TileEntityPainter.SLOT_BLANK, 30, 35)) // blank facades
		addSlotToContainer(SlotPainter(painter, TileEntityPainter.SLOT_TEMPLATE, 66, 35)) // template blocks
		addSlotToContainer(SlotPainter(painter, TileEntityPainter.SLOT_OUTPUT, 123, 34)) // output


		for (i in 0..2) {
			for (j in 0..8) {
				this.addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (k in 0..8) {
			this.addSlotToContainer(Slot(playerInv, k, 8 + k * 18, 142))
		}
	}

	class SlotPainter(val painter: TileEntityPainter, id: Int, x: Int, y: Int): SlotItemHandler(painter.inventory, id, x, y) {
		override fun onSlotChanged() {
			super.onSlotChanged()
			painter.markDirty()
		}
	}

}