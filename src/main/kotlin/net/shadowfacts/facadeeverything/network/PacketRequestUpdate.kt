package net.shadowfacts.facadeeverything.network

import io.netty.buffer.ByteBuf
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * @author shadowfacts
 */
class PacketRequestUpdate(var dimension: Int = 0, var pos: BlockPos = BlockPos.ORIGIN): IMessage {

	constructor(tile: TileEntity): this(tile.world.provider.dimension, tile.pos)

	override fun toBytes(buf: ByteBuf) {
		buf.writeInt(dimension)
		buf.writeLong(pos.toLong())
	}

	override fun fromBytes(buf: ByteBuf) {
		dimension = buf.readInt()
		pos = BlockPos.fromLong(buf.readLong())
	}

	object Handler: IMessageHandler<PacketRequestUpdate, PacketUpdate> {

		override fun onMessage(message: PacketRequestUpdate, ctx: MessageContext): PacketUpdate? {
			val tile = FMLCommonHandler.instance().minecraftServerInstance.getWorld(message.dimension).getTileEntity(message.pos)
			if (tile != null) {
				return PacketUpdate(tile)
			} else {
				return null
			}
		}

	}

}