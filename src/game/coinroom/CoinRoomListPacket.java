package game.coinroom;

import java.util.List;

import com.mozat.morange.dbcache.tables.CoinRoom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.coinroom.common.room_grade;
import protocols.coinroom.common.room_info;
import protocols.coinroom.common.room_type;
import protocols.coinroom.roomlist;
import protocols.coinroom.roomlist.room_t;

public class CoinRoomListPacket extends Packet{
	//请求
	room_type mode;
	room_grade grade;
	
	//下发
	public int error;
	List<CoinRoom> rooms;
	
	@Override
	public void execPacket(){
		rooms = CoinRoom.getManyByCriteria(CoinRoom.AttrMODE.eq(mode.name()),
				CoinRoom.AttrGRADE.eq(grade.name()));
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		roomlist.request req = roomlist.request.parseFrom(bytes);
		mode = req.getMode();
		grade = req.getGrade();
		logger.info("User({}) request coin room list. mode:{}, grade:{}", userId, mode, grade);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		roomlist.response.Builder builder = roomlist.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(rooms != null){
			for(CoinRoom room:rooms){
				room_t.Builder roomBld = room_t.newBuilder();
				room_info.Builder bld = room_info.newBuilder();
				bld.setRoomId(room.ROOM_ID);
				bld.setMode(room_type.valueOf(room.MODE));
				bld.setBaseScore(room.BASE_SCORE);
				bld.setMinCoin(room.MIN_COIN);
				bld.setMaxSeat(room.MAX_SEAT);
				bld.setEncrypted(room.ENCRYPTED);
				bld.setGrade(room_grade.valueOf(room.GRADE));
				bld.setDrawPercent(room.DRAW_PERCENT);
				roomBld.setConfig(bld);
				roomBld.setPlaying(CoinRoomService.getPlayerCount(room.ROOM_ID));
				builder.addRooms(roomBld);
			}
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
