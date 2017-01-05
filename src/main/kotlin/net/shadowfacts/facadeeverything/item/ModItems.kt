package net.shadowfacts.facadeeverything.item

import net.shadowfacts.shadowmc.item.ModItems

/**
 * @author shadowfacts
 */
object ModItems: ModItems() {

	val facade = ItemFacade()

	override fun init() {
		register(facade)
	}

}