package net.shadowfacts.facadeeverything.gui.element

import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.init.Biomes
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.WorldType
import net.minecraft.world.biome.Biome
import net.minecraftforge.client.ForgeHooksClient
import net.minecraftforge.common.property.IExtendedBlockState
import net.shadowfacts.facadeeverything.block.ModBlocks
import net.shadowfacts.facadeeverything.block.facade.BlockFacade
import net.shadowfacts.facadeeverything.block.facade.TileEntityFacade
import net.shadowfacts.facadeeverything.block.assembly.TileEntityAssemblyTable
import net.shadowfacts.facadeeverything.model.block.BakedModelFacadeBlock
import net.shadowfacts.facadeeverything.util.base
import net.shadowfacts.facadeeverything.util.getStateForSide
import net.shadowfacts.shadowmc.ui.UIDimensions
import net.shadowfacts.shadowmc.ui.UIMouseInteractable
import net.shadowfacts.shadowmc.ui.element.UIElementBase
import net.shadowfacts.shadowmc.util.MouseButton
import net.shadowfacts.shadowmc.util.RelativeSide
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector3f

/**
 *
 *
 * @author shadowfacts
 */
class UIFacadePreview(val table: TileEntityAssemblyTable, id: String, vararg classes: String): UIElementBase("facade-preview", id, *classes), UIMouseInteractable, ITickable {

	companion object {
		private val buf = BufferUtils.createFloatBuffer(16)
	}

	private var disabled = false
	private var angleY = 0.0
	private var angleX = 0.0
	private var tracking = false
	private var autoRotate = true
	private var lastX = 0
	private var lastY = 0
	private var ticks = 0

	var facade: IExtendedBlockState = (ModBlocks.facade.defaultState as IExtendedBlockState).let {
		var state = it.withProperty(BlockFacade.BASE, Blocks.GLASS.defaultState)
		EnumFacing.VALUES.forEach {
			state = state.withProperty(BlockFacade.SIDE_PROPS[it], if (it.ordinal % 2 == 0) Blocks.GOLD_BLOCK.defaultState else null)
		}
		state
	}
		private set

	val tile = TileEntityFacade().apply {
		pos = BlockPos.ORIGIN
		base = Blocks.GLASS.defaultState
		RelativeSide.values().forEach {
			facades[it] = if (it.ordinal % 2 == 0) Blocks.GOLD_BLOCK.defaultState else null
		}
	}

	fun updatePreview() {
		val stack = table.getOutput()
		if (!stack.isEmpty) {
			disabled = false
			facade = facade.withProperty(BlockFacade.BASE, stack.base)
			tile.base = stack.base
			RelativeSide.values().forEach {
				val side = stack.getStateForSide(it)
				facade = facade.withProperty(BlockFacade.SIDE_PROPS[it.forFront(EnumFacing.NORTH)], side)
				tile.facades[it] = side
			}
		} else {
			disabled = true
		}
	}

	override fun getMinDimensions(): UIDimensions {
		return preferredDimensions
	}

	override fun getPreferredDimensions(): UIDimensions {
		return UIDimensions(74, 74)
	}

	override fun draw(mouseX: Int, mouseY: Int) {
		updatePreview()
		if (disabled) return

		val partialTicks = Minecraft.getMinecraft().renderPartialTicks
		var angleX = angleY
		var angleY = this.angleX
		if (autoRotate) {
			angleX = Math.sin(Math.toRadians(ticks + partialTicks.toDouble())) * 45
			angleY -= 2 * partialTicks
		}

		val matrix = Matrix4f()
				.rotate(Math.toRadians(angleX).toFloat(), Vector3f(1f, 0f, 0f))
				.rotate(Math.toRadians(angleY).toFloat(), Vector3f(0f, 1f, 0f))

		buf.rewind()
		matrix.storeTranspose(buf)
		buf.flip()

		GlStateManager.pushMatrix()
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT)
		GL11.glMatrixMode(GL11.GL_PROJECTION)
		GL11.glMatrixMode(GL11.GL_MODELVIEW)

		GlStateManager.pushMatrix()
		GlStateManager.enableCull()
		GlStateManager.translate(x + dimensions.height / 4f, y + dimensions.height * 3/4f, 0f)
		GlStateManager.scale(40f, -40f, 1f)
		GlStateManager.translate(0.5, 0.5, 0.5)
		GlStateManager.multMatrix(buf)
		GlStateManager.translate(-0.5, -0.5, -0.5)
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
		mc.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false)
		val buffer = Tessellator.getInstance().buffer
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)
		BlockRenderLayer.values().forEach {
			ForgeHooksClient.setRenderLayer(it)
			mc.blockRendererDispatcher.blockModelRenderer.renderModel(DummyWorld.update(BlockPos.ORIGIN, facade, tile), BakedModelFacadeBlock, facade, BlockPos.ORIGIN, buffer, false)
		}
		ForgeHooksClient.setRenderLayer(null)

		buffer.setTranslation(0.0, 0.0, 0.0)
		Tessellator.getInstance().draw()
		mc.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap()
		GlStateManager.popMatrix()

		GlStateManager.pushMatrix()
		GlStateManager.translate(x + 9f, y + 9f, 0f)
		GlStateManager.scale(8f, -8f, 1f)
		GlStateManager.multMatrix(buf)
		GlStateManager.disableTexture2D()
		GL11.glLineWidth(2f)
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
		buffer.pos(0.0, 0.0, 0.0).color(0f, 1f, 0f, 1f).endVertex()
		buffer.pos(0.0, 1.0, 0.0).color(0f, 1f, 0f, 1f).endVertex()
		buffer.pos(0.0, 0.0, 0.0).color(1f, 0f, 0f, 1f).endVertex()
		buffer.pos(1.0, 0.0, 0.0).color(1f, 0f, 0f, 1f).endVertex()
		buffer.pos(0.0, 0.0, 0.0).color(0f, 0f, 1f, 1f).endVertex()
		buffer.pos(0.0, 0.0, 1.0).color(0f, 0f, 1f, 1f).endVertex()
		Tessellator.getInstance().draw()
		GlStateManager.enableTexture2D()
		GlStateManager.popMatrix()

		GlStateManager.popMatrix()
	}

	override fun drawTooltip(mouseX: Int, mouseY: Int) {

	}

	override fun mouseClickDown(mouseX: Int, mouseY: Int, button: MouseButton) {
		if (button == MouseButton.RIGHT) {
			autoRotate = false
			tracking = true
			lastX = mouseX
			lastY = mouseY
		}
	}

	override fun mouseMove(mouseX: Int, mouseY: Int, button: MouseButton, timeSinceLastClick: Long) {
		super.mouseMove(mouseX, mouseY, button, timeSinceLastClick)

		if (tracking && button == MouseButton.RIGHT) {
			val dx = mouseX - lastX
			val dy = mouseY - lastY

			angleY = Math.min(Math.max(angleY + dy, -90.0), 90.0)
			angleX -= dx

			lastX = mouseX
			lastY = mouseY
		}
	}

	override fun mouseClickUp(mouseX: Int, mouseY: Int, button: MouseButton) {
		if (button == MouseButton.RIGHT) {
			tracking = false
		}
	}

	override fun update() {
		if (autoRotate) {
			angleY = Math.sin(Math.toRadians(ticks.toDouble())) * 22.5
			angleX -= 2.0
			ticks++
		}
	}

	private object DummyWorld: IBlockAccess {

		var pos: BlockPos = BlockPos.ORIGIN
		var state: IBlockState = Blocks.STONE.defaultState
		var tile: TileEntity? = null

		fun update(pos: BlockPos, state: IBlockState, tile: TileEntity?): DummyWorld {
			this.pos = pos
			this.state = state
			this.tile = tile
			return this
		}

		override fun isSideSolid(pos: BlockPos, side: EnumFacing, default: Boolean): Boolean {
			return if (this.pos == pos) state.isSideSolid(this, pos, side) else default
		}

		override fun isAirBlock(pos: BlockPos): Boolean {
			return this.pos == pos && state.block.isAir(state, this, pos)
		}

		override fun getBlockState(pos: BlockPos): IBlockState {
			return if (this.pos == pos) return state else Blocks.AIR.defaultState
		}

		override fun getStrongPower(pos: BlockPos, direction: EnumFacing?): Int {
			return 0
		}

		override fun getCombinedLight(pos: BlockPos, lightValue: Int): Int {
			return lightValue
		}

		override fun getTileEntity(pos: BlockPos): TileEntity? {
			return if (this.pos == pos) tile else null
		}

		override fun getBiome(pos: BlockPos?): Biome {
			return Biomes.PLAINS
		}

		override fun getWorldType(): WorldType {
			return WorldType.DEFAULT
		}

	}

}