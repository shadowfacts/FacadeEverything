package net.shadowfacts.facadeeverything.item

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.resources.I18n
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.shadowfacts.facadeeverything.util.getFacadeState

/**
 * @author shadowfacts
 */
class ItemFacade: ItemBase("facade") {

	companion object {
		val blank: ItemStack
			get() = ItemStack(ModItems.facade)

		val TAG_FACADE = "facade"

		fun forState(state: IBlockState): ItemStack {
			return blank.apply {
				tagCompound = NBTTagCompound()
				tagCompound!!.setInteger(TAG_FACADE, Block.getStateId(state))
			}
		}
	}

	init {
		creativeTab = CreativeTabs.DECORATIONS
	}

	override fun getItemStackDisplayName(stack: ItemStack): String {
		val state = stack.getFacadeState()
		if (state != null) {
			return I18n.format("$unlocalizedName.name", state.block.localizedName)
		} else {
			return I18n.format("${unlocalizedName}.blank.name")
		}
	}

}