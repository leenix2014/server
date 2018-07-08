package game.connector;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;

public class Connector {
	private static Logger logger = LoggerFactory.getLogger(Connector.class);
	
	private ServerConfig config;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;
	
	public void init(){
		config = new ServerConfig();
	}
	
	public void start() throws Exception{
		logger.info("开始启动网络层");
		int loopSize = config.threadNumber;
		logger.info("初始化线程(worker=" + loopSize + ")");
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(loopSize);
		openFrontend(bossGroup, workerGroup, null, config.port);
		logger.info("成功启动网络层");
	}
	
	public void stop(){
		logger.info("停止网络层...");
		if (bossGroup != null) {
			bossGroup.shutdownGracefully().syncUninterruptibly();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully().syncUninterruptibly();
		}
		logger.info("网络层已停止...");
	}
	
	/**
	 * 监听客户端连接
	 * @param bossGroup    监听线程(Acceptor)
	 * @param workerGroup  IO线程组
	 * @param ip           监听IP
	 * @param port         监听端口
	 * @throws Exception 
	 */
	public void openFrontend(EventLoopGroup bossGroup, EventLoopGroup workerGroup, String ip, int port) throws Exception{
		ServerBootstrap bootstrap = new ServerBootstrap()
		.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100)
		.option(ChannelOption.SO_REUSEADDR, true)
		.handler(new LoggingHandler(LogLevel.DEBUG))
		.childOption(ChannelOption.SO_SNDBUF, config.frontendSendBuf)
		.childOption(ChannelOption.SO_RCVBUF, config.frontendRecvBuf)
		.childOption(ChannelOption.TCP_NODELAY, true)
		.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
		.childHandler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				//ch.pipeline().addLast(new PacketDecoder(128 * 1024, 1, 4, 1, 0));//在一个包的长度不够大的情况下，不需要packet decoder，这里暂时不加
				ch.pipeline().addLast(new PacketDecoder(64 * 1024, 1, 2, 1, 0));
				ch.pipeline().addLast(new IdleStateHandler(config.frontendTimeout, 0, 0));
				ch.pipeline().addLast(new FrontendInboundHandler());
			}
		});
		
		if (ip != null && !"".equals(ip.trim())) {
			bootstrap.bind(ip, port).sync();
		}else{
			bootstrap.bind(port).sync();
		}
	}
	
	public static void writeToFrontend(Channel ch, ByteBuf inBuf) {
    	byte[] payload = new byte[inBuf.readableBytes()];
    	inBuf.readBytes(payload);
    	
    	ByteBuf outBuf = ch.alloc().buffer();
    	
    	try {
    		outBuf.writeByte((byte) -82);
	    	//outBuf.writeInt(payload.length);
    		outBuf.writeShort(payload.length);
	    	outBuf.writeBytes(payload);
	    	outBuf.writeByte((byte) -81);
	    	
	    	//send the message
	    	ch.writeAndFlush(outBuf);
		} catch (Exception e) {
			logger.error("Connector.writeToFrontend error:", e);
			ReferenceCountUtil.release(outBuf);
		} finally{
			ReferenceCountUtil.release(inBuf);
		}
    }
	
	public static void main(String[] args) {  
        Connector connector = new Connector();
        connector.init();
        try {
			connector.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}
