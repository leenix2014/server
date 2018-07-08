package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.sendmsg;

public class SendMsgPacket extends Packet{
	//请求
	public int roomId;
	public String msg;
	public boolean crossRoom;
	
	//下发
	
	@Override
	public void execPacket(){
		LiveRoomService.sendMsg(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		sendmsg.request request = sendmsg.request.parseFrom(bytes);
		msg = request.getMsg();
		crossRoom = request.getCrossRoom();
		roomId = request.getRoomId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		sendmsg.response.Builder builder = sendmsg.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
