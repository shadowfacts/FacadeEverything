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
import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.gui.GUIHandler
import net.shadowfacts.facadeeverything.item.ItemBase
import net.shadowfacts.facadeeverything.item.ItemFacade
import net.shadowfacts.facadeeverything.util.getFacadeState
import net.shadowfacts.shadowmc.util.RelativeSide

/**
 * @author shadowfacts
 */
class ItemApplicator: ItemBase("applicator") {

	companion object {
		fun ItemStack.getItemHandler(): IItemHandler {
			return getCapability(ITEM_HANDLER_CAPABILITY, null)!!
		}
	}

	fun createFacade(world: World, pos: BlockPos, facing: EnumFacing): Boolean {
		if (world.getTileEntity(pos) !== null) {
			return false
		}

		val state = world.getBlockState(pos)

		if (state.getBlockHardness(world, pos) == -1f) {
			return false
		}

		world.setBlockState(pos, ModBlocks.facade.defaultState.withProperty(BlockFacade.FRONT, if (!EnumFacing.Plane.HORIZONTAL.apply(facing)) EnumFacing.NORTH else facing))
		val tile = ModBlocks.facade.getTileEntity(world, pos)
		tile.base = state
		return true
	}

	fun applyFacade(world: World, pos: BlockPos, side: EnumFacing, player: EntityPlayer, stack: ItemStack): EnumActionResult {
		val front = world.getBlockState(pos).getValue(BlockFacade.FRONT)
		val tile = ModBlocks.facade.getTileEntity(world, pos)

		val new = stack.getItemHandler().getStackInSlot(0).getFacadeState()
		val relative = RelativeSide.forFacing(front, side)
		val current = tile.facades[relative]
		if (current != null) {
			if (current == new) return EnumActionResult.FAIL
			else player.inventory.addItemStackToInventory(ItemFacade.forState(current))
		}
		stack.getItemHandler().extractItem(0, 1, false)
		tile.facades[relative] = new
		tile.markDirty()
		world.markBlockRangeForRenderUpdate(pos, pos)

		return EnumActionResult.SUCCESS
	}

	fun removeFacade(world: World, pos: BlockPos, side: EnumFacing, player: EntityPlayer, stack: ItemStack): EnumActionResult {
		val front = world.getBlockState(pos).getValue(BlockFacade.FRONT)
		val tile = ModBlocks.facade.getTileEntity(world, pos)

		val relative = RelativeSide.forFacing(front, side)
		val current = tile.facades[relative]
		if (current != null) {
			player.inventory.addItemStackToInventory(ItemFacade.forState(current))
			tile.facades[relative] = null

			if (tile.facades.all { it.value == null }) {
				world.setBlockState(pos, tile.base)
			}

			world.markBlockRangeForRenderUpdate(pos, pos)

			return EnumActionResult.SUCCESS
		}

		return EnumActionResult.FAIL
	}

	override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
		if (!player.isSneaking) {
			val state = world.getBlockState(pos)
			val stack = player.getHeldItem(hand)

			if (!stack.getItemHandler().getStackInSlot(0).isEmpty) {
				if (state.block !== ModBlocks.facade && !createFacade(world, pos, facing)) {
					return EnumActionResult.FAIL
				}
				return applyFacade(world, pos, facing, player, stack)
			} else {
				if (state.block !== ModBlocks.facade) {
					return EnumActionResult.FAIL
				}
				return removeFacade(world, pos, facing, player, stack)
			}
		}
		return EnumActionResult.PASS
	}

	override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
		val stack = player.getHeldItem(hand)
		if (player.isSneaking) {
			player.openGui(FacadeEverything, GUIHandler.APPLICATOR, world, hand.ordinal, 0, 0)
			return ActionResult(EnumActionResult.SUCCESS, stack)
		}
		return ActionResult(EnumActionResult.PASS, stack)
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