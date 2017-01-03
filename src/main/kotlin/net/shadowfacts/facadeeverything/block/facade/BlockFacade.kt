package net.shadowfacts.facadeeverything.block.facade

import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IExtendedBlockState
import net.shadowfacts.facadeeverything.property.UnlistedPropertyState
import net.shadowfacts.facadeeverything.util.*
import net.shadowfacts.shadowmc.block.BlockTE
import net.shadowfacts.shadowmc.util.KeyboardHelper

/**
 * @author shadowfacts
 */
class BlockFacade: BlockTE<TileEntityFacade>(Material.ROCK, "facade") {

	companion object {
		val BASE = UnlistedPropertyState("base")
		val SIDE_PROPS: Map<EnumFacing, UnlistedPropertyState> = mutableMapOf<EnumFacing, UnlistedPropertyState>().apply {
			EnumFacing.VALUES.forEach {
				put(it, UnlistedPropertyState(it.name.toLowerCase()))
			}
		}
	}

	init {
		unlocalizedName = registryName.toString()
	}

	override fun createBlockState(): BlockStateContainer {
		val list = mutableListOf(BASE)
		list.addAll(SIDE_PROPS.values)
		return ExtendedBlockState(this, arrayOf<IProperty<*>>(), list.toTypedArray())
	}

	override fun getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState {
		if (state is IExtendedBlockState) {
			val te = getTileEntity(world, pos)
			@Suppress("NAME_SHADOWING")
			var state: IExtendedBlockState = state.withProperty(BASE, te.base)
			SIDE_PROPS.forEach {
				state = state.withProperty(it.value, te.facades[it.key])
			}
			return state
		}
		return state
	}

	override fun initItemModel() {
//		TODO: fixme, find a way of doing this without needing 65536^7 MRLs
//		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this)) { stack ->
//			var variant = "base=${stack.baseId}"
//			stack.sideIds.forEach {
//				variant += ",${it.key.name.toLowerCase()}=${it.value}"
//			}
//			ModelResourceLocation(registryName, variant)
//		}
	}

	override fun onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
		val te = getTileEntity(world, pos)
		te.base = stack.base
		stack.sides.forEach {
			te.facades[it.key] = it.value
		}
		te.markDirty()
	}

	override fun canRenderInLayer(state: IBlockState, layer: BlockRenderLayer): Boolean {
		return true
	}

	override fun isOpaqueCube(state: IBlockState?): Boolean {
		return false
	}

	override fun isFullCube(state: IBlockState?): Boolean {
		return false
	}

	override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
		if (KeyboardHelper.isShiftPressed()) {
			tooltip.add("Base: ${stack.base.block.localizedName}")
			EnumFacing.VALUES.forEach {
				tooltip.add(it.name.toLowerCase().capitalize() + ": " + (stack.getStateForSide(it)?.block?.localizedName ?: "None"))
			}
		} else {
			tooltip.add("${TextFormatting.ITALIC}Press [Shift] for facades")
		}
	}

	override fun getTileEntityClass(): Class<TileEntityFacade> {
		return TileEntityFacade::class.java
	}

}