package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.Agent;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.bindagent;

public class BindAgentPacket extends Packet{
	//请求
	private String agentId;
	
	//下发
	public int error;
	
	@Override
	public void execPacket() {
		TUsers user = Users.load(userId);
		Agent agent = Agent.getOneByCriteria(Agent.AttrLOGIN_ID.eq(agentId), Agent.AttrDELETED.eq(false));
		if (agent == null || user == null) {
			//无此账号
			this.error = 1;
			errDesc = "代理不存在";
			PacketManager.send(userId, this);
			if(user == null){
				logger.error("User({}) is null", userId);
			} else {
				logger.info("User({}) bind nonexist agent({})", userId, agentId);
			}
			return;
		}
		
		user.AGENT_ID = agentId;
		if (!user.update()) {
			error = 2;
			errDesc = "绑定代理失败，请重试";
			PacketManager.send(userId, this);
			logger.error("User({}) bind agent({}) failed because update failed!", userId, agentId);
			return;
		}
		
		error = 0;
		logger.info("User({}) bind agent({}) success!", userId, agentId);
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		bindagent.request req = bindagent.request.parseFrom(bytes);
		agentId = req.getAgentId();
		logger.info("User({}) request bind agent({})", userId, agentId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		bindagent.response.Builder builder = bindagent.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(error == 0){
			builder.setAgentId(agentId);
		}
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
