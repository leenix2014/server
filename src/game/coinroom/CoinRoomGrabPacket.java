package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.grab;

public class CoinRoomGrabPacket extends Packet{
	//请求
	public int multiple;
	
	//下发
	public int error;
	public String errDesc = "";
	public int myGrab;
	
	@Override
	public void execPacket(){
		CoinRoomService.grab(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		grab.request req = grab.request.parseFrom(bytes);
		multiple = req.getGrab();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		grab.response.Builder builder = grab.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setGrab(myGrab);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
