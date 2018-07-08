package game.user.packet;


import game.packet.Packet;
import game.packet.PacketManager;
import game.session.SessionManager;
import protocols.header.packet.Builder;
import protocols.user.logout;

public class LogoutPacket extends Packet{
	//请求
	
	//下发
	private int error;
	private String errDesc="";
	
	@Override
	public void execPacket() {
		logger.info("User({}) request logout.", userId);
		SessionManager.kickSession(userId);
		error = 0;
		PacketManager.send(session, this);
	}

	@Override
	public void readBody(byte[] bytes) {
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		logout.response.Builder builder = logout.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
