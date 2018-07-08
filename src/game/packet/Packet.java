package game.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.session.GameSession;
import protocols.header;

public abstract class Packet {
	public int userId;
	protected GameSession session;
	protected static Logger logger = LoggerFactory.getLogger(Packet.class);
	
	public int error;
	public String errDesc = "";
	
	public void setSession(GameSession session){
		this.session = session;
		this.userId = session.getUserId();
	}
	
	public GameSession getSession(){
		return this.session;
	}
	
	//必须用protobuf的反序列化
	public abstract void readBody(byte[] bytes) throws Exception;
	
	//必须用protobuf的序列化
	public abstract void writeBody(header.packet.Builder pktBuilder);
	
	public abstract void execPacket() throws Exception;
}
