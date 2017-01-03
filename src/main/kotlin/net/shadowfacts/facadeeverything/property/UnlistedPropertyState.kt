package net.shadowfacts.facadeeverything.property

import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.property.IUnlistedProperty

/**
 * @author shadowfacts
 */
class UnlistedPropertyState(val propName: String): IUnlistedProperty<IBlockState> {

	override fun getName(): String {
		return propName
	}

	override fun valueToString(value: IBlockState?): String {
		return value?.toString() ?: "null"
	}

	override fun isValid(value: IBlockState?): Boolean {
		return true
	}

	override fun getType(): Class<IBlockState> {
		return IBlockState::class.java
	}

}