package game.live;


import com.mozat.morange.dbcache.tables.LiveRoom;

import game.live.model.LiveRoomModel;
import game.live.model.LiveRoomService;
import game.live.util.ConvertUtil;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.getroom;

public class GetLiveRoomPacket extends Packet{
	//请求
	public String direction;
	public String type;
	
	//下发
	public LiveRoomModel model;

	@Override
	public void execPacket(){
		LiveRoomService.getRoom(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		getroom.request request = getroom.request.parseFrom(bytes);
		direction = request.getDirection();
		type = request.getType();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getroom.response.Builder builder = getroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
		if(model != null){
			LiveRoom room = model.getConfig();
			builder.setRoomId(room.ID);
			builder.setPaid(model.isPaid(userId));
			builder.setRoom(ConvertUtil.toRoomInfo(model, userId));
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
