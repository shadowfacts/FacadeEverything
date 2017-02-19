package net.shadowfacts.facadeeverything.item.applicator

import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.facadeeverything.FacadeEverything
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.gui.GUIHandler
import net.shadowfacts.facadeeverything.item.ItemBase
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.util.getFacadeState

/**
 * @author shadowfacts
 */
class ItemApplicator: ItemBase("applicator") {

	companion object {
		fun ItemStack.getItemHandler(): IItemHandler {
			return getCapability(ITEM_HANDLER_CAPABILITY, null)!!
		}
	}

	override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
		val stack = player.getHeldItem(hand)
		if (player.isSneaking) {
			player.openGui(FacadeEverything, GUIHandler.APPLICATOR, world, hand.ordinal, 0, 0)
			return ActionResult(EnumActionResult.SUCCESS, stack)
		}
		return ActionResult(EnumActionResult.PASS, stack)
	}

	override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
		if (!player.isSneaking) {
			val stack = player.getHeldItem(hand)
			if (stack.getItemHandler().getStackInSlot(0).isEmpty) {
				return EnumActionResult.FAIL
			}


			val state = world.getBlockState(pos)
			if (state.block !== ModBlocks.facade) {
				return EnumActionResult.FAIL
			}

			val tile = ModBlocks.facade.getTileEntity(world, pos)

			val current = tile.facades[facing]
			if (current != null) {
				player.inventory.addItemStackToInventory(ItemFacade.forState(current))
			}
			tile.facades[facing] = stack.getItemHandler().extractItem(0, 1, false).getFacadeState()
			tile.markDirty()
			world.markBlockRangeForRenderUpdate(pos, pos)

			return EnumActionResult.SUCCESS
		}
		return EnumActionResult.PASS
	}

	override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
		val facade = stack.getItemHandler().getStackInSlot(0)
		if (facade.isEmpty) {
			tooltip.add(I18n.format("$MOD_ID.applicator.none"))
		} else {
			tooltip.add(facade.displayName)
		}
	}

	override fun initCapabilities(stack: ItemStack, nbt: NBTTagCompound?): ICapabilityProvider? {
		return ApplicatorCapProvider()
	}

	class ApplicatorCapProvider: ICapabilitySerializable<NBTTagCompound> {

		val inventory = object: ItemStackHandler(1) {
			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				return if (stack.item !is ItemFacade || stack.getFacadeState() === null) {
					stack
				} else {
					super.insertItem(slot, stack, simulate)
				}
			}
		}

		override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
			return capability == ITEM_HANDLER_CAPABILITY
		}

		override fun <T: Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
			return if (capability == ITEM_HANDLER_CAPABILITY) inventory as T else null
		}

		override fun deserializeNBT(tag: NBTTagCompound) {
			inventory.deserializeNBT(tag)
		}

		override fun serializeNBT(): NBTTagCompound {
			return inventory.serializeNBT()
		}

	}

}