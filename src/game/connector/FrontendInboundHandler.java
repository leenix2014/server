package game.connector;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.packet.Packet;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.session.GameSession;
import game.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import protocols.header;

public class FrontendInboundHandler extends ChannelInboundHandlerAdapter{
	private static Logger logger = LoggerFactory.getLogger(FrontendInboundHandler.class);
	private GameSession session = null;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		GameSession gameSession = new GameSession();
		gameSession.setChannel(ctx.channel());
		session = gameSession;
		super.channelActive(ctx);	
	}
	
	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        ByteBuf buf = (ByteBuf) msg;  
        try {
        	int bodySize = buf.readableBytes();  
            byte[] body = new byte[bodySize];  
            buf.readBytes(body);    
            
        	header.packet pkt = header.packet.parseFrom(body);
        	int packetType = pkt.getCommand(); 
        	if (packetType == PacketTypes.HEART_BEAT_CMD) {
				sendHeartBeat();
			}else{
				Packet packet = PacketManager.getPacketByType(packetType);
				if (packet == null) {
					logger.error("FrontendInboundHandler.channelRead getPacketByType packet is null");  
				}else{
					packet.setSession(session);
					packet.readBody(pkt.getBody().toByteArray());
					Server.getMonetService().dispatch(packet);
				}
			}

		} finally {
			ReferenceCountUtil.release(buf);
		}
    }  
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception{
		if (session != null && session.getUserId() != 0) {
			SessionManager.kickSession(session.getUserId());
		}
		
		super.channelInactive(ctx);
	}
	
	public void sendHeartBeat() throws Exception{
		if (session != null) {
			header.packet.Builder pktBuilder = header.packet.newBuilder();
			pktBuilder.setCommand(PacketTypes.HEART_BEAT_CMD);
			pktBuilder.setVersion(1);
			pktBuilder.setSubversion(0);
			
			byte[] payload = pktBuilder.buildPartial().toByteArray();
	        PacketManager.send(session, payload);
		}		
	}
}
