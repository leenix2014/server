package game.roulette.util;

import com.mozat.morange.dbcache.tables.RouletteRoom;
import com.mozat.morange.dbcache.tables.TUsers;

import game.roulette.model.RouletteRoomModel;
import game.user.Users;
import netty.util.MathUtil;
import protocols.roulette.common.bet;
import protocols.roulette.common.room_info;
import protocols.roulette.common.user_info;

public class ConvertUtil {
	
	public static user_info.Builder toUserInfo(int userId, RouletteRoomModel model){
		TUsers user = Users.load(userId);
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setAvatar(user.AVATAR_ID);
		userInfo.setCoin(user.COIN_COUNT);
		userInfo.setUserId(MathUtil.parseInt(user.LOGIN_ID));
		userInfo.setUserName(user.NAME);
		userInfo.setSilent(model.isSilent(userId));
		return userInfo;
	}
	
	public static room_info toRoomInfo(RouletteRoom config){
		room_info.Builder room = room_info.newBuilder();
		room.setBetTime(config.BET_TIME);
		room.setDrawPercent(config.DRAW_PERCENT);
		room.setGameType(config.GAME_TYPE);
		room.setMaxBet(config.MAX_BET);
		room.setMaxTotalBet(config.MAX_TOTAL_BET);
		room.setMinBet(config.MIN_BET);
		room.setMinCoin(config.MIN_COIN);
		room.setPrizeTime(config.PRIZE_TIME);
		room.setRoomId(config.ROOM_ID);
		room.setChannel(config.CHANNEL);
		return room.build();
	}
	
	public static bet toBet(String target, int coins){
		bet.Builder builder = bet.newBuilder();
		builder.setTarget(target);
		builder.setCoins(coins);
		return builder.build();
	}
}
