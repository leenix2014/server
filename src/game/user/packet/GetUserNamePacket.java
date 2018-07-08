package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.getusername;

public class GetUserNamePacket extends Packet{
	//请求
	private int queryId;
	
	//下发
	private String userName;
	
	@Override
	public void execPacket() {
		TUsers user = Users.load(queryId);	
		userName = user==null?"":user.NAME;
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		getusername.request req = getusername.request.parseFrom(bytes);
		queryId = req.getUserId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getusername.response.Builder builder = getusername.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setUserName(userName);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
