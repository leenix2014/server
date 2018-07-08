package game.connector;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class PacketDecoder extends LengthFieldBasedFrameDecoder{
	private static Logger logger = LoggerFactory.getLogger(PacketDecoder.class);
	
	public PacketDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception{	
		Object obj = super.decode(ctx, in);
		if(obj != null && obj instanceof ByteBuf){
			ByteBuf buf = (ByteBuf)obj;	

			byte head = buf.getByte(buf.readerIndex());
			byte tail = buf.getByte(buf.readerIndex() + buf.readableBytes() - 1);
			if (head != -82) {
				throw new CorruptedFrameException("invalid head: " + head);
			}
			if (tail != -81) {
				throw new CorruptedFrameException("invalid tail: " + tail);
			}

			return buf.slice(buf.readerIndex() + 3, buf.readableBytes() - 4);
		}
		return null;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
		ctx.close();
		logger.error("PacketDecode error!", cause);
	}
	
}
