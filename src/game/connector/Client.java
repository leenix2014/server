package game.connector;

import java.util.Timer;
import java.util.TimerTask;

import game.packet.PacketTypes;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import protocols.header;

public class Client { 
	 private Channel channel = null;
	  
	 public Client(String host, int port) {  
	        TimerTask task = new TimerTask() {  
	            @Override  
	            public void run() {  
	                // task to run goes here  
	                try {
	                	//for (int i = 0; i < 10; i++) {
	                		System.out.println("timer run");
							sendHeartBeat();
						//}
	                	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }  
	        };  
	        Timer timer = new Timer();  
	        long delay = 3 * 1000;  
	        long intevalPeriod = 3 * 1000;  
	        // schedules the task to be run in an interval  
	        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
		 
	        EventLoopGroup work = new NioEventLoopGroup();  
	        Bootstrap boot = new Bootstrap();  
	        boot.group(work)  
	            .channel(NioSocketChannel.class)  
	            .handler(new ChannelInitializer<SocketChannel>() {  
	                @Override  
	                protected void initChannel(final SocketChannel ch) throws Exception {  
	                	ch.pipeline().addLast(new PacketDecoder(64 * 1024, 1, 2, 1, 0));
	                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {  
	                        @Override  
	                        public void channelActive(ChannelHandlerContext ctx) throws Exception { 
	                        	channel = ctx.channel();
	                        	System.out.println("与服务端连接成功");   
	                        }  
	  	                        
	                    	@Override  
	                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
	                            ByteBuf buf = (ByteBuf) msg;  
	                            try {
	                            	short packetId = buf.readShort();
	                            	if (packetId == PacketTypes.HEART_BEAT_CMD) {
										System.out.println("Recieve heart beat from server");
									}	                                
	                    		} finally {
	                    			ReferenceCountUtil.release(buf);
	                    		}
	                        } 
	  
	                    });  
	                }  
	            });  
	        try {  
	            ChannelFuture future = boot.connect(host, port).sync();  
	            future.channel().closeFuture().sync();  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        } finally {  
	            work.shutdownGracefully();  
	        }   
	    }
	 
	 public static long writeToBackend(Channel ch, ByteBuf inBuf) throws Exception{
	    	byte[] payload = new byte[inBuf.readableBytes()];
	    	inBuf.readBytes(payload);
	    	
	    	ByteBuf outBuf = ch.alloc().buffer();
	    	
	    	try {
	    		outBuf.writeByte((byte) -82);
		    	outBuf.writeShort(payload.length);
		    	outBuf.writeBytes(payload);
		    	outBuf.writeByte((byte) -81);
		    	
				//test==========
//				int bodySize = outBuf.readableBytes();  
//	            byte[] body = new byte[bodySize];  
//	            outBuf.readBytes(body);  
	            //test==========
		    	
		    	//send the message
		    	ch.unsafe().write(outBuf, new DefaultChannelPromise(ch));
		    	ch.unsafe().flush();
		    	return payload.length;
			} catch (Exception e) {
				ReferenceCountUtil.release(outBuf);
				throw e;
			}    	
	    }
	 
	 public void sendHeartBeat() throws Exception{
         if (channel != null) {
        	header.packet.Builder pktBuilder = header.packet.newBuilder();
 			pktBuilder.setCommand(PacketTypes.HEART_BEAT_CMD);
 			pktBuilder.setVersion(1);
 			pktBuilder.setSubversion(0);
 			
 			byte[] payload = pktBuilder.buildPartial().toByteArray();
 	        ByteBuf buf = Unpooled.buffer();   
 	        buf.writeBytes(payload);
 	        
            writeToBackend(channel, buf); 
		}
	 }
	  
	 public static void main(String[] args) { 
	     //Client client = new Client("127.0.0.1", 8003); 
	 }  
}
