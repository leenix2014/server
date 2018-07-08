package game.user.packet;


import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.user.ranklist;
import protocols.user.ranklist.user_info;

public class RankListPacket extends Packet{
	//请求
	private String type;
	
	//下发
	private List<TUsers> users;
	
	@Override
	public void execPacket() {
		if("win".equals(type)){
			users = TUsers.getManyBySQL("SELECT * FROM USERS t WHERE t.`WIN_COUNT` > 0 ORDER BY t.`WIN_COUNT` DESC LIMIT 10");
		} else {
			users = TUsers.getManyBySQL("SELECT * FROM USERS t WHERE t.`COIN_COUNT` > 0 ORDER BY t.`COIN_COUNT` DESC LIMIT 10");
		}
		PacketManager.send(session, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		ranklist.request req = ranklist.request.parseFrom(bytes);
		type = req.getType();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		ranklist.response.Builder builder = ranklist.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(users != null){
			for(int i=1;i<=users.size();i++){
				TUsers user = users.get(i-1);
				user_info.Builder info = user_info.newBuilder();
				info.setRank(i);
				info.setUserId(user.LOGIN_ID);
				info.setUserName(user.NAME);
				info.setAvatar(user.AVATAR_ID);
				if("win".equals(type)){
					info.setAmount(user.WIN_COUNT);
				} else {
					info.setAmount(user.COIN_COUNT);
				}
				builder.addUsers(info);
			}
		}
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
