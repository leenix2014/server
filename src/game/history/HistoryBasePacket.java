package game.history;

import java.util.Date;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.mozat.morange.dbcache.tables.TRoomConfig;
import com.mozat.morange.dbcache.tables.TRoomInnings;
import com.mozat.morange.util.DateUtil;

import game.packet.Packet;
import netty.util.MathUtil;
import protocols.config;
import protocols.history.rooms;
import protocols.history.settles;

public abstract class HistoryBasePacket extends Packet {
	protected rooms.room_t.Builder toBuilder(TRoomConfig roomConfig){
		rooms.room_t.Builder configBuilder = rooms.room_t.newBuilder();
		configBuilder.setRoomId(roomConfig.ROOM_ID);
		configBuilder.setGame(roomConfig.GAME);
		configBuilder.setOwner(roomConfig.OWNER);
		configBuilder.setOwnerName(roomConfig.OWNER_NAME);
		configBuilder.setStatus(roomConfig.STATUS);
		configBuilder.setCreateTime(DateUtil.yyyyMMddHHmmss(roomConfig.CREATE_TIME));
		configBuilder.setEndTime(new Date(0).equals(roomConfig.END_TIME)?"":DateUtil.yyyyMMddHHmmss(roomConfig.END_TIME));
		
		config.bull.Builder bullBuilder = config.bull.newBuilder();
		bullBuilder.setDealer(config.bull.DEALER.valueOf(roomConfig.DEALER_TYPE));
		bullBuilder.setBscores(roomConfig.BSCORES);
		bullBuilder.setSeats(roomConfig.SEATS);
		bullBuilder.setInnings(roomConfig.INNINGS);
		bullBuilder.setPmscores(roomConfig.PMSCORES);
		bullBuilder.setDmscores(roomConfig.DMSCORES);
		bullBuilder.setBlind(roomConfig.BLIND);
		bullBuilder.setMaxBet(roomConfig.MAX_BET);
		bullBuilder.setHasGhost(roomConfig.HAS_GHOST);
		
		configBuilder.setConfig(bullBuilder);
		return configBuilder;
	}
	
	protected settles.settle_t.Builder toBuilder(TRoomInnings inning, boolean middle){
		settles.settle_t.Builder inningBuilder = settles.settle_t.newBuilder();
		inningBuilder.setInning(inning.INNING);
		inningBuilder.setUserId(inning.USER_ID);
		inningBuilder.setUserName(inning.USER_NAME);
		inningBuilder.setSeatId(inning.SEAT_ID);
		inningBuilder.setUserBet(inning.USER_BET);
		inningBuilder.setUserGrab(inning.USER_GRAB);
		if(middle){
			Vector<Integer> cards = getCards(inning.CARDS);
			for(int i=0;i<3;i++){
				inningBuilder.addCards(cards.get(i));
			}
			inningBuilder.setCardType(MathUtil.parseInt(inning.MIDDLE_CARD_TYPE));
			inningBuilder.setScore(inning.MIDDLE_SCORE);
			inningBuilder.setTotal(inning.MIDDLE_TOTAL);
		} else {
			Vector<Integer> cards = getCards(StringUtils.isEmpty(inning.SHOW_HAND_CARDS)?inning.CARDS:inning.SHOW_HAND_CARDS);
			for(int card : cards){
				inningBuilder.addCards(card);
			}
			inningBuilder.setCardType(MathUtil.parseInt(inning.CARD_TYPE));
			inningBuilder.setScore(inning.END_SCORE);
			inningBuilder.setTotal(inning.END_TOTAL);
		}
		inningBuilder.setDealer(inning.IS_DEALER);
		inningBuilder.setBeginTime(DateUtil.yyyyMMddHHmmss(inning.CREATE_TIME));
		inningBuilder.setEndTime(new Date(0).equals(inning.END_TIME)?"":DateUtil.yyyyMMddHHmmss(inning.END_TIME));
		return inningBuilder;
	}
	
	private Vector<Integer> getCards(String cards){
		String[] parts = cards.split(",");
		Vector<Integer> vec = new Vector<Integer>();
		for(String part : parts){
			vec.add(MathUtil.parseInt(part));
		}
		return vec;
	}
}
