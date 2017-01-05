package net.shadowfacts.facadeeverything.block.painter

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.shadowfacts.facadeeverything.FacadeEverything
import net.shadowfacts.facadeeverything.gui.GUIHandler
import net.shadowfacts.shadowmc.block.BlockTE

/**
 * @author shadowfacts
 */
class BlockPainter: BlockTE<TileEntityPainter>(Material.ROCK, "painter") {

	init {
		unlocalizedName = registryName.toString()
		setCreativeTab(CreativeTabs.DECORATIONS)
	}

	override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		player.openGui(FacadeEverything, GUIHandler.PAINTER, world, pos.x, pos.y, pos.z)
		return true
	}

	override fun getTileEntityClass(): Class<TileEntityPainter> {
		return TileEntityPainter::class.java
	}

}