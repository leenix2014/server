package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.common.live_room;
import protocols.live.newroom;

public class NewLiveRoomPacket extends Packet{
	//请求
	public live_room room;
	
	//下发
	public int roomId;
	
	@Override
	public void execPacket(){
		LiveRoomService.newLiveRoom(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		newroom.request request = newroom.request.parseFrom(bytes);
		room = request.getRoom();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		newroom.response.Builder builder = newroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setRoomId(roomId);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
