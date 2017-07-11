package net.shadowfacts.facadeeverything.property

import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.property.IUnlistedProperty

/**
 * @author shadowfacts
 */
class UnlistedPropertyState(val propName: String): IUnlistedProperty<IBlockState> {

	override fun getName() = propName

	override fun valueToString(value: IBlockState): String {
		return value.toString()
	}

	override fun isValid(value: IBlockState) = true

	override fun getType() = IBlockState::class.java

}