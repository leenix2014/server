package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.common.room_grade;
import protocols.coinroom.common.room_type;
import protocols.coinroom.join;

public class CoinRoomQuickJoinPacket extends Packet{
	//请求
	public room_type mode;
	public room_grade grade;
	
	//下发
	public int error;
	
	@Override
	public void execPacket(){
		CoinRoomService.quickjoin(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		join.quickjoin req = join.quickjoin.parseFrom(bytes);
		mode = req.getMode();
		grade = req.getGrade();
		logger.info("User({}) request quick join. mode:{}, grade:{}.", userId, mode, grade);
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		
	}
}
