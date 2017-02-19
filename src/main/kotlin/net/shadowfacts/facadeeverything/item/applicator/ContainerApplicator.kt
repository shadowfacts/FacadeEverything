package net.shadowfacts.facadeeverything.item.applicator

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerApplicator(pos: BlockPos, playerInv: InventoryPlayer, applicator: ItemStack): ContainerBase(pos) {

	init {
		addSlotToContainer(SlotApplicator(applicator, 0, 80, 35))

		for (i in 0..2) {
			for (j in 0..8) {
				this.addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (k in 0..8) {
			this.addSlotToContainer(Slot(playerInv, k, 8 + k * 18, 142))
		}
	}

	class SlotApplicator(applicator: ItemStack, id: Int, x: Int, y: Int): SlotItemHandler(applicator.getCapability(ITEM_HANDLER_CAPABILITY, null), id, x, y)

}