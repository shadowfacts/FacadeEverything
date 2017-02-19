package net.shadowfacts.facadeeverything.block.facade

import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IExtendedBlockState
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.property.UnlistedPropertyState
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.sides
import net.shadowfacts.shadowmc.block.BlockTE

/**
 * @author shadowfacts
 */
class BlockFacade: BlockTE<TileEntityFacade>(Material.ROCK, "facade_block") {

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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, ModelResourceLocation(ResourceLocation(MOD_ID, "facade_block"), "inventory"))
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

	override fun getTileEntityClass(): Class<TileEntityFacade> {
		return TileEntityFacade::class.java
	}

}