package net.shadowfacts.facadeeverything.network

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.shadowfacts.shadowmc.ShadowMC

/**
 * @author shadowfacts
 */
class PacketUpdate(var pos: BlockPos = BlockPos.ORIGIN, var tag: NBTTagCompound = NBTTagCompound()): IMessage {

	constructor(tile: TileEntity): this(tile.pos, tile.writeToNBT(NBTTagCompound()))

	override fun toBytes(buf: ByteBuf) {
		buf.writeLong(pos.toLong())
		ByteBufUtils.writeTag(buf, tag)
	}

	override fun fromBytes(buf: ByteBuf) {
		pos = BlockPos.fromLong(buf.readLong())
		tag = ByteBufUtils.readTag(buf)!!
	}

	object Handler: IMessageHandler<PacketUpdate, IMessage> {

		override fun onMessage(message: PacketUpdate, ctx: MessageContext): IMessage? {
			val tile = ShadowMC.proxy.clientWorld.getTileEntity(message.pos)
			tile?.readFromNBT(message.tag)
			ShadowMC.proxy.clientWorld.markBlockRangeForRenderUpdate(message.pos, message.pos)
			return null
		}

	}

}