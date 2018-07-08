package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.leave;

public class CoinRoomLeavePacket extends Packet{
	//请求
	
	//下发
	public int error;
	public String errDesc = "";
	
	@Override
	public void execPacket(){
		CoinRoomService.leave(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		logger.info("User({}) request leave coin room.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		leave.response.Builder builder = leave.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
