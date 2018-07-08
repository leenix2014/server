package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.getagent;

// Not Used
public class GetAgentPacket extends Packet{
	//请求
	
	//下发
	public int error;
	private String agentId;
	
	@Override
	public void execPacket() {
		TUsers user = Users.load(userId);
		if (user == null) {
			//无此账号
			this.error = 1;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			logger.error("User({}) is null", userId);
			return;
		}
		
		this.error = 0;
		this.agentId = user.AGENT_ID;
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		logger.info("User({}) request get agent.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getagent.response.Builder builder = getagent.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setAgentId(agentId);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
