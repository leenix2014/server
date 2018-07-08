package game.user.packet;


import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.user.queryuser;
import protocols.user.queryuser.user_info;

public class QueryUserPacket extends Packet{
	//请求
	private String key;
	
	//下发
	private List<TUsers> users;
	
	@Override
	public void execPacket() {
		String query = "%"+key+"%";
		users = TUsers.getManyBySQL("SELECT * FROM USERS t WHERE t.`LOGIN_ID` LIKE ? OR t.`NAME` LIKE ? limit 5", query, query);
		PacketManager.send(session, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		queryuser.request req = queryuser.request.parseFrom(bytes);
		key = req.getKey();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		queryuser.response.Builder builder = queryuser.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(users != null){
			for(TUsers user : users){
				user_info.Builder info = user_info.newBuilder();
				info.setUserId(user.LOGIN_ID);
				info.setUserName(user.NAME);
				builder.addUsers(info);
			}
		}
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
