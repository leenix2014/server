package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.leaveonover;

// Not Used
public class CoinRoomLeaveOnOverPacket extends Packet{
	//请求
	
	//下发
	
	@Override
	public void execPacket(){
		CoinRoomService.leaveOnOver(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		leaveonover.response.Builder builder = leaveonover.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
