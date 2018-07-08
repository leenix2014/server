package game.user.packet;


import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.user.roomcard;

public class RoomCardPacket extends Packet{
	//请求
	
	//下发
	public int roomCardCount;
	public int type;
	
	@Override
	public void execPacket() {
		
	}

	@Override
	public void readBody(byte[] bytes) {
		logger.info("User({}) query room card count.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		roomcard.response.Builder roomcardBuilder = roomcard.response.newBuilder();
		roomcardBuilder.setRoomCardCount(roomCardCount);
		roomcardBuilder.setType(type);
		
    	pktBuilder.setBody(roomcardBuilder.buildPartial().toByteString());		
	}
}
