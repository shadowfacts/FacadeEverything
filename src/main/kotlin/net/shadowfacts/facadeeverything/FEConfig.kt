package net.shadowfacts.facadeeverything

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import java.io.File

/**
 * @author shadowfacts
 */
object FEConfig {

	var config: Config = ConfigFactory.load("assets/$MOD_ID/reference.conf")
		private set

	val inWorldCamoTooltip: Boolean
		get() = config.getBoolean("$MOD_ID.inWorldCamoTooltip")

	fun init(configDir: File) {
		val f = File(configDir, "shadowfacts/FacadeEverything.conf")
		if (!f.parentFile.exists()) {
			f.parentFile.mkdirs()
		}
		if (!f.exists()) {
			f.createNewFile()
		}

		config = ConfigFactory.parseFile(f).withFallback(config)

		val toRender = config.root().withOnlyKey(MOD_ID)
		val s = toRender.render(ConfigRenderOptions.defaults().setOriginComments(false).setJson(false))
		f.writeText(s)
	}

}