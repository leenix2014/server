package game.connector;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder2 extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1,
			List<Object> arg2) throws Exception {
		int lineLength = 128 * 1024;
		 int endPostion = findEndPostion(arg1);  
		  
	        if (endPostion != -1) {  
	            int endWordLength = arg1.getByte(endPostion) == '\n' ? 1 : 2;  
	            int bodySize = (endPostion - arg1.readerIndex()) + endWordLength;  
	            arg2.add(arg1.readBytes(bodySize)); 
	            
	        } else {  
	            // 在指定消息长度的内没有读到结束符，就说明读到的是不符合规则的数据包  
	            if (arg1.readableBytes() >= lineLength) {  
	                throw new Exception("消息不完整。");  
	            }  
	        }  
		
	}
	
	
	  
    private int findEndPostion(ByteBuf buf) {  
        int size = buf.writerIndex();  
        for (int index = buf.readerIndex(); index < size; index++) {  
            if (buf.getByte(index) == '\n') {  
                return index;  
                // 13 10  
            } else if (buf.getByte(index) == '\r' && index < size - 1 && buf.getByte(index + 1) == '\n') {  
                return index;  
            }  
        }  
        return -1;  
    } 
	
}
