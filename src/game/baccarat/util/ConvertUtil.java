package game.baccarat.util;

import com.mozat.morange.dbcache.tables.BaccaratRoom;
import com.mozat.morange.dbcache.tables.TUsers;

import game.baccarat.model.BaccaratRoomModel;
import game.baccarat.model.Waybill;
import game.user.Users;
import netty.util.MathUtil;
import protocols.baccarat.common.ask;
import protocols.baccarat.common.bet;
import protocols.baccarat.common.column;
import protocols.baccarat.common.room_info;
import protocols.baccarat.common.user_info;
import protocols.baccarat.common.waybill;

public class ConvertUtil {
	
	public static user_info.Builder toUserInfo(int userId, BaccaratRoomModel model){
		TUsers user = Users.load(userId);
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setAvatar(user.AVATAR_ID);
		userInfo.setCoin(user.COIN_COUNT);
		userInfo.setUserId(MathUtil.parseInt(user.LOGIN_ID));
		userInfo.setUserName(user.NAME);
		userInfo.setSeatId(model.getSeatId(userId));
		userInfo.setSilent(model.isSilent(userId));
		userInfo.setBigPlayer(model.isBigPlayer(userId));
		return userInfo;
	}
	
	public static room_info toRoomInfo(BaccaratRoomModel model){
		BaccaratRoom config = model.getConfig();
		room_info.Builder room = room_info.newBuilder();
		room.setBetTime(config.BET_TIME);
		room.setDrawPercent(config.DRAW_PERCENT);
		room.setMaxBet(config.MAX_BET);
		room.setMinBet(config.MIN_BET);
		room.setMinCoin(config.MIN_COIN);
		room.setRoomId(config.ROOM_ID);
		room.setMaxRed(config.MAX_RED);
		room.setTiePairMinBet(config.TIE_PAIR_MIN_BET);
		room.setTiePairMaxBet(config.TIE_PAIR_MAX_BET);
		room.setOverTime(config.OVER_TIME);
		room.setPlayerCount(model.getPlayerCount());
		room.setRemains(model.getRemains());
		room.setChannel(config.CHANNEL);
		return room.build();
	}
	
	public static bet toBet(String target, int coins){
		bet.Builder builder = bet.newBuilder();
		builder.setTarget(target);
		builder.setCoins(coins);
		return builder.build();
	}
	
	public static waybill toWaybill(Waybill bill){
		waybill.Builder builder = waybill.newBuilder();
		int[][] mainRoad = bill.getMainRoad();
		for(int i=0;i<mainRoad.length;i++){
			column.Builder col = column.newBuilder();
			if(MathUtil.isAllZero(mainRoad[i])){
				continue;
			}
			for(int j=0;j<mainRoad[i].length;j++){
				col.addPivot(mainRoad[i][j]);
			}
			builder.addMainRoad(col);
		}
		
		int[][] dishRoad = bill.getDishRoad();
		for(int i=0;i<dishRoad.length;i++){
			column.Builder col = column.newBuilder();
			if(MathUtil.isAllZero(dishRoad[i])){
				continue;
			}
			for(int j=0;j<dishRoad[i].length;j++){
				col.addPivot(dishRoad[i][j]);
			}
			builder.addDishRoad(col);
		}
		int[][] bigEyeRoad = bill.getBigEyeRoad();
		for(int i=0;i<bigEyeRoad.length;i++){
			column.Builder col = column.newBuilder();
			if(MathUtil.isAllZero(bigEyeRoad[i])){
				continue;
			}
			for(int j=0;j<bigEyeRoad[i].length;j++){
				col.addPivot(bigEyeRoad[i][j]);
			}
			builder.addBigEyeRoad(col);
		}
		int[][] smallRoad = bill.getSmallRoad();
		for(int i=0;i<smallRoad.length;i++){
			column.Builder col = column.newBuilder();
			if(MathUtil.isAllZero(smallRoad[i])){
				continue;
			}
			for(int j=0;j<smallRoad[i].length;j++){
				col.addPivot(smallRoad[i][j]);
			}
			builder.addSmallRoad(col);
		}
		int[][] roachRoad = bill.getRoachRoad();
		for(int i=0;i<roachRoad.length;i++){
			column.Builder col = column.newBuilder();
			if(MathUtil.isAllZero(roachRoad[i])){
				continue;
			}
			for(int j=0;j<roachRoad[i].length;j++){
				col.addPivot(roachRoad[i][j]);
			}
			builder.addRoachRoad(col);
		}
		
		builder.setTotal(bill.size());
		builder.setBankerPair(bill.getBankerPair());
		builder.setBankerWin(bill.getBankerWin());
		builder.setPlayerPair(bill.getPlayerPair());
		builder.setPlayerWin(bill.getPlayerWin());
		builder.setTie(bill.getTie());
		builder.setSky(bill.getSky());
		int[] bankerAsk = bill.getBankerAsk();
		ask.Builder ab = ask.newBuilder();
		ab.setBigeye(bankerAsk[0]);
		ab.setSmall(bankerAsk[1]);
		ab.setRoach(bankerAsk[2]);
		builder.setBanker(ab);
		int[] playerAsk = bill.getPlayerAsk();
		ask.Builder ap = ask.newBuilder();
		ap.setBigeye(playerAsk[0]);
		ap.setSmall(playerAsk[1]);
		ap.setRoach(playerAsk[2]);
		builder.setPlayer(ap);
		
		return builder.build();
	}
	
	public static String translate(String target){
		if("banker_win".equals(target)){
			return "庄";
		} else if("player_win".equals(target)){
			return "闲";
		} else if("banker_pair".equals(target)){
			return "庄对";
		} else if("player_pair".equals(target)){
			return "闲对";
		} else if("tie".equals(target)){
			return "和";
		} else {
			return target;
		}
	}
}
