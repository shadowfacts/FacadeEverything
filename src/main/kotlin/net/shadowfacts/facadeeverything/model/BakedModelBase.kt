package net.shadowfacts.facadeeverything.model

import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraftforge.client.model.IPerspectiveAwareModel
import net.minecraftforge.common.model.TRSRTransformation
import org.apache.commons.lang3.tuple.ImmutablePair
import org.apache.commons.lang3.tuple.Pair
import java.util.*
import javax.vecmath.Matrix4f
import javax.vecmath.Vector3f

/**
 * @author shadowfacts
 */
abstract class BakedModelBase: IBakedModel, IPerspectiveAwareModel {

	companion object {
		private val flipX = TRSRTransformation(null, null, Vector3f(-1f, 1f, 1f), null)

		fun toLeftHand(transform: TRSRTransformation): TRSRTransformation {
			return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX))
		}

		fun getTransform(tx: Float, ty: Float, tz: Float, ax: Float, ay: Float, az: Float, s: Float): TRSRTransformation {
			return TRSRTransformation.blockCenterToCorner(TRSRTransformation(
					Vector3f(tx / 16, ty / 16, tz / 16),
					TRSRTransformation.quatFromXYZDegrees(Vector3f(ax, ay, az)),
					Vector3f(s, s, s),
					null))
		}
	}

	private val transforms: MutableMap<ItemCameraTransforms.TransformType, TRSRTransformation?> = EnumMap(ItemCameraTransforms.TransformType::class.java)

	override fun isAmbientOcclusion(): Boolean {
		return true
	}

	override fun isGui3d(): Boolean {
		return true
	}

	override fun isBuiltInRenderer(): Boolean {
		return false
	}

	@Deprecated("")
	override fun getItemCameraTransforms(): ItemCameraTransforms {
		return ItemCameraTransforms.DEFAULT
	}

	override fun getOverrides(): ItemOverrideList {
		return ItemOverrideList.NONE
	}

	override fun handlePerspective(type: ItemCameraTransforms.TransformType): Pair<out IBakedModel, Matrix4f> {
		return ImmutablePair.of(this, (transforms[type] ?: getTransform(0f, 0f, 0f, 0f, 0f, 0f, 1f)).matrix)
	}

	fun setTransform(type: ItemCameraTransforms.TransformType, transform: TRSRTransformation) {
		transforms[type] = transform
	}

	fun addThirdPersonTransform(transform: TRSRTransformation) {
		setTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, transform)
		setTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, toLeftHand(transform))
	}

	fun addFirstPersonTransform(transform: TRSRTransformation) {
		setTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, transform)
		setTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, toLeftHand(transform))
	}

	fun addDefaultBlockTransforms() {
		setTransform(ItemCameraTransforms.TransformType.GUI, getTransform(0f, 0f, 0f, 30f, 225f, 0f, 0.625f))
		setTransform(ItemCameraTransforms.TransformType.GROUND, getTransform(0f, 3f, 0f, 0f, 0f, 0f, 0.25f))
		setTransform(ItemCameraTransforms.TransformType.FIXED, getTransform(0f, 0f, 0f, 0f, 0f, 0f, 0.5f))
		addThirdPersonTransform(getTransform(0f, 2.5f, 0f, 75f, 45f, 0f, 0.375f))
		addFirstPersonTransform(getTransform(0f, 0f, 0f, 0f, 45f, 0f, 0.4f))
	}
	
	fun addDefaultItemTransforms() {
		setTransform(ItemCameraTransforms.TransformType.GROUND, getTransform(0f, 2f, 0f, 0f, 0f, 0f, 0.5f))
		setTransform(ItemCameraTransforms.TransformType.HEAD, getTransform(0f, 13f, 7f, 0f, 180f, 0f, 1f))
		addThirdPersonTransform(getTransform(0f, 3f, 1f, 0f, 0f, 0f, 0.55f))
		addFirstPersonTransform(getTransform(1.13f, 3.2f, 1.13f, 0f, -90f, 25f, 0.68f))
	}

}