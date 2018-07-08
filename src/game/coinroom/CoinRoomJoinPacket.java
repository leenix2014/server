package game.coinroom;

import java.util.Map;

import com.mozat.morange.dbcache.tables.CoinRoom;

import game.coinroom.model.CoinRoomModel;
import game.coinroom.model.CoinRoomService;
import game.coinroom.util.ConvertUtil;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.common.room_grade;
import protocols.coinroom.common.room_info;
import protocols.coinroom.common.room_type;
import protocols.coinroom.join;
import protocols.coinroom.join.player_info;
import protocols.coinroom.join.room_t;

public class CoinRoomJoinPacket extends Packet{
	//请求
	public int roomId;
	public String pwd;
	
	//下发
	public int error;
	public CoinRoomModel model;
	
	@Override
	public void execPacket(){
		CoinRoomService.join(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		join.request req = join.request.parseFrom(bytes);
		roomId = req.getRoomId();
		pwd = req.getPwd();
		logger.info("User({}) request join room({}).", userId, roomId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		join.response.Builder builder = join.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(model != null) {
			CoinRoom room = model.getConfig();
			room_t.Builder roomInfo = room_t.newBuilder();
			room_info.Builder bld = room_info.newBuilder();
			bld.setRoomId(room.ROOM_ID);
			bld.setMode(room_type.valueOf(room.MODE));
			bld.setBaseScore(room.BASE_SCORE);
			bld.setMinCoin(room.MIN_COIN);
			bld.setMaxSeat(room.MAX_SEAT);
			bld.setEncrypted(room.ENCRYPTED);
			bld.setGrade(room_grade.valueOf(room.GRADE));
			bld.setDrawPercent(room.DRAW_PERCENT);
			roomInfo.setConfig(bld);
			roomInfo.setStage(model.getStage());
			roomInfo.setCountDown(model.getCountDown());
			
			builder.setRoom(roomInfo);
			builder.setSelfSeatId(model.getSeatId(userId));
			builder.setSelfRole(model.getUserRole(userId));
			
			for(Map.Entry<Integer, Integer> entry : model.getPlayerAndSitdowner().entrySet()){
				int playerId = entry.getKey();
				int playerSeat = entry.getValue();
				player_info.Builder player = player_info.newBuilder();
				player.setSeatId(playerSeat);
				player.setPlayer(ConvertUtil.toUserInfo(playerId));
				player.setRole(model.getUserRole(playerId));
				player.setGrab(model.getGrab(playerId));
				player.setBet(model.getBet(playerId));
				player.addAllHand(userId==playerId?model.getCards(playerId):model.getShowCards(playerId));
				player.setCardType(model.getCardType(playerId));
				player.setScore(model.getScore(playerId));
				player.setDraw(model.getDraw(playerId));
				builder.addPlayer(player);
			}
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
