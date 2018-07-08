package game.roulette;


import com.mozat.morange.dbcache.tables.RouletteRoom;

import game.packet.Packet;
import game.roulette.model.RouletteRoomService;
import game.roulette.util.ConvertUtil;
import protocols.header.packet.Builder;
import protocols.roulette.getroom;

public class GetRoomPacket extends Packet{
	//请求
	public int type;
	public String direction;
	
	//下发
	public RouletteRoom config;
	
	@Override
	public void execPacket(){
		RouletteRoomService.getRoom(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		getroom.request request = getroom.request.parseFrom(bytes);
		type = request.getGameType();
		if(type == 0){
			type = RouletteRoomService.EUROPE_ROULETTE;
		}
		direction = request.getDirection();
		logger.info("User({}) get roulette room, direction({})", userId, direction);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getroom.response.Builder builder = getroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(config != null){
			builder.setConfig(ConvertUtil.toRoomInfo(config));
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
