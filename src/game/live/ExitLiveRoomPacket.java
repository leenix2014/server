package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.exitroom;

public class ExitLiveRoomPacket extends Packet{
	//请求
	public int roomId;
	
	//下发
	
	@Override
	public void execPacket(){
		LiveRoomService.exitRoom(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		exitroom.request req = exitroom.request.parseFrom(bytes);
		roomId = req.getRoomId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		exitroom.response.Builder builder = exitroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
