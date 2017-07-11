package net.shadowfacts.facadeeverything.util

import net.minecraft.block.Block
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString

/**
 * @author shadowfacts
 */
object CommandStateId: CommandBase() {

	override fun getName() = "stateid"

	override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
		val id = Block.getStateId(Block.getBlockFromName(args[0])!!.getStateFromMeta(args[1].toInt()))
		sender.sendMessage(TextComponentString("ID: $id"))
	}

	override fun getUsage(sender: ICommandSender) = "/stateid <block> <meta>"

}