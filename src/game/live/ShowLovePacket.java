package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.showlove;

public class ShowLovePacket extends Packet{
	//请求
	public int roomId;
	public int count;
	
	//下发
	
	@Override
	public void execPacket(){
		LiveRoomService.showLove(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		showlove.request req = showlove.request.parseFrom(bytes);
		roomId = req.getRoomId();
		count = req.getCount();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		showlove.response.Builder builder = showlove.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
