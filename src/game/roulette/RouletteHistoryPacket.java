package game.roulette;

import java.util.List;

import com.mozat.morange.dbcache.tables.RouletteBetSum;
import com.mozat.morange.util.DateUtil;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.roulette.history;
import protocols.roulette.history.roulette;

public class RouletteHistoryPacket extends Packet{
	//请求
	public String roomId;
	
	//下发
	private List<RouletteBetSum> records;
	
	@Override
	public void execPacket(){
		//records = RouletteBetSum.getManyByCriteria(RouletteBetSum.AttrUSER_ID.eq(userId), RouletteBetSum.AttrROOM_ID.eq(roomId));
		records = RouletteBetSum.getManyBySQL("SELECT * FROM ROULETTE_BET_SUM  WHERE `USER_ID` = ? AND `ROOM_ID` = ? order by RECORD_TIME desc limit 80", userId, roomId);
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		history.request req = history.request.parseFrom(bytes);
		roomId = req.getRoomId();
		logger.info("User({}) request history in roulette", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		history.response.Builder builder = history.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(records != null){
			for(RouletteBetSum record : records){
				roulette.Builder rou = roulette.newBuilder();
				rou.setRoomId(record.ROOM_ID);
				rou.setResult(record.RESULT);
				rou.setTotalBet(record.BET_TOTAL);
				rou.setTotalReward(record.REWARD_TOTAL);
				rou.setTime(DateUtil.yyyyMMddHHmmss(record.RECORD_TIME));
				builder.addRoulettes(rou);
			}
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
