package net.shadowfacts.facadeeverything.item

import net.shadowfacts.facadeeverything.item.applicator.ItemApplicator
import net.shadowfacts.shadowmc.item.ModItems

/**
 * @author shadowfacts
 */
object ModItems: ModItems() {

	val facade = ItemFacade()
	val applicator = ItemApplicator()

	override fun init() {
		register(facade)
		register(applicator)
	}

}