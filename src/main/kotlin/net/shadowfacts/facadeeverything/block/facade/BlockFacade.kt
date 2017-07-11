package net.shadowfacts.facadeeverything.block.facade

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IExtendedBlockState
import net.shadowfacts.facadeeverything.MOD_ID
import net.shadowfacts.facadeeverything.property.UnlistedPropertyState
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.setStateForSide
import net.shadowfacts.facadeeverything.util.sides
import net.shadowfacts.shadowmc.block.BlockTE
import net.shadowfacts.shadowmc.util.RelativeSide

/**
 * @author shadowfacts
 */
class BlockFacade: BlockTE<TileEntityFacade>(Material.ROCK, "facade_block") {

	companion object {
		val FRONT: PropertyDirection = PropertyDirection.create("front", EnumFacing.Plane.HORIZONTAL)
		val BASE = UnlistedPropertyState("base")
		val SIDE_PROPS: Map<EnumFacing, UnlistedPropertyState> = mutableMapOf<EnumFacing, UnlistedPropertyState>().apply {
			EnumFacing.VALUES.forEach {
				put(it, UnlistedPropertyState(it.name.toLowerCase()))
			}
		}
	}

	init {
		unlocalizedName = registryName.toString()
		defaultState = blockState.baseState.withProperty(FRONT, EnumFacing.NORTH)
	}

	override fun createItemBlock(): Item {
		return ItemBlockFacade()
	}

	fun getStack(world: World, pos: BlockPos): ItemStack {
		val tile = getTileEntity(world, pos)
		val stack = ItemStack(this)
		stack.base = tile.base
		RelativeSide.values().forEach {
			stack.setStateForSide(it, tile.facades[it])
		}
		return stack
	}

	override fun getDrops(world: IBlockAccess?, pos: BlockPos?, state: IBlockState?, fortune: Int): MutableList<ItemStack> {
		return mutableListOf()
	}

	override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
		if (getTileEntity(world, pos).facades.values.any { it != null }) {
			Block.spawnAsEntity(world, pos, getStack(world, pos))
		}
		super.breakBlock(world, pos, state)
	}

	override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
		return getStack(world, pos)
	}

	override fun createBlockState(): BlockStateContainer {
		val list = mutableListOf(BASE)
		list.addAll(SIDE_PROPS.values)
		return ExtendedBlockState(this, arrayOf(FRONT), list.toTypedArray())
	}

	override fun getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState {
		if (state is IExtendedBlockState) {
			val front = state.getValue(FRONT)
			val te = getTileEntity(world, pos)
			@Suppress("NAME_SHADOWING")
			var state: IExtendedBlockState = state.withProperty(BASE, te.base)
			RelativeSide.values().forEach {
				state = state.withProperty(SIDE_PROPS[it.forFront(front)], te.facades[it])
			}
			return state
		}
		return state
	}

	override fun getMetaFromState(state: IBlockState): Int {
		return state.getValue(FRONT).ordinal
	}

	@Deprecated("")
	override fun getStateFromMeta(meta: Int): IBlockState {
		return defaultState.withProperty(FRONT, EnumFacing.getFront(meta))
	}

	override fun initItemModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), EnumFacing.NORTH.ordinal, ModelResourceLocation(ResourceLocation(MOD_ID, "facade_block"), "inventory"))
	}

	override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
		return defaultState.withProperty(FRONT, getFront(pos, placer))
	}

	private fun getFront(pos: BlockPos, entity: EntityLivingBase): EnumFacing {
		return EnumFacing.getFacingFromVector((entity.posX - pos.x).toFloat(), 0f, (entity.posZ - pos.z).toFloat())
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

	@Deprecated("")
	override fun isOpaqueCube(state: IBlockState?): Boolean {
		return false
	}

	@Deprecated("")
	override fun isFullCube(state: IBlockState?): Boolean {
		return false
	}

	override fun getTileEntityClass(): Class<TileEntityFacade> {
		return TileEntityFacade::class.java
	}

}