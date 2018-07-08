package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.endroom;

public class EndLiveRoomPacket extends Packet{
	//请求
	
	//下发
	
	@Override
	public void execPacket(){
		LiveRoomService.endLiveRoom(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		endroom.response.Builder builder = endroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
