package netty.GameModels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mozat.morange.dbcache.tables.TRoomInnings;

import protocols.config;

public class GameBetMgr {
	
	volatile private static GameBetMgr instance = null;
    
    private GameBetMgr(){
    }

    public static GameBetMgr getInstance() {

        if(instance == null){
            synchronized (GameBetMgr.class) {
                if(instance == null){
                    instance = new GameBetMgr();
                }
            }
        }

        return instance;
    }
    
    // Map<roomID, Map<userId, betMultiple>
    private Map<Integer, Map<Integer, Integer> > betMap = new HashMap<Integer, Map<Integer, Integer> >();
    
    // Map<roomId, Set<inning>>
    private Map<Integer, Set<Integer>> allBetHandledRoom = new HashMap<Integer, Set<Integer>>();
    
    public void setAllBetHandled(int roomId){
    	synchronized (GameBetMgr.class){
	    	int inning = GameInningMgr.getInstance().getCurrInning(roomId);
	    	Set<Integer> innings = allBetHandledRoom.get(roomId);
	    	if(innings == null){
	    		innings = new HashSet<Integer>();
	    	}
	    	innings.add(inning);
			allBetHandledRoom.put(roomId, innings);
    	}
    }
    
    public boolean isAllBetHandled(int roomId){
    	synchronized (GameBetMgr.class){
	    	int inning = GameInningMgr.getInstance().getCurrInning(roomId);
	    	Set<Integer> innings = allBetHandledRoom.get(roomId);
	    	if(innings == null){
	    		return false;
	    	}
	    	return innings.contains(inning);
    	}
    }
    
    // 玩家下注
    public void setPlayerBet(int roomID, int userId, int bet) {
    	synchronized (GameBetMgr.class) {
    		if (betMap.containsKey(roomID)){
    			Map<Integer, Integer> info = betMap.get(roomID);
    			info.put(userId, bet);
    		}
    		else {
    			Map<Integer, Integer> info = new HashMap<Integer, Integer>();
    			info.put(userId, bet);
    			betMap.put(roomID, info);
			}
    		int inning = GameInningMgr.getInstance().getCurrInning(roomID);
			TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrUSER_BET.set(bet)), 
					TRoomInnings.AttrROOM_ID.eq(roomID),
					TRoomInnings.AttrINNING.eq(inning),
					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
					TRoomInnings.AttrSTATUS.eq(0));
    	}
	}
    
    public int randomBet(){
    	int[] bets = new int[]{1, 2, 3, 5, 10};
    	Random ram = new Random();
    	int index = ram.nextInt(bets.length);
    	return bets[index];
    }
    
    // 结算或者解散房间时，清空下注信息。
    public void deleteRoomBets(int roomID) {
    	synchronized (GameBetMgr.class) {
    		betMap.remove(roomID);
    		allBetHandledRoom.remove(roomID);
    	}
    }
    
    // 判断是否所有玩家都已下注
    public boolean isAllBet(int roomID) {
    	synchronized (GameBetMgr.class) {
    		if (GameRoomMgr.getInstance().getRoomConfig(roomID).dealer == config.bull.DEALER.NONE) {
    			// 通比模式下玩家无需下注
				return true;
			}
    		if (!betMap.containsKey(roomID)) {
    			System.out.println("No user bet in room("+roomID+") yet!");
    			return false;
			}
    		int betCount = betMap.get(roomID).size();
    		int userCount = GameRoomMgr.getInstance().getRoomUserCount(roomID);
    		int offlineCount = GameRoomMgr.getInstance().getOfflineCount(roomID);
    		if(GameRoomMgr.getInstance().isDemoRoom(roomID)){
    			return betCount >= (userCount - (offlineCount-2) - 1);
    		}
			return betCount >= (userCount - offlineCount - 1);//庄家无需下注
    	}
	}
    
    public boolean isUserBetted(int roomId, int userId){
    	synchronized (GameBetMgr.class) {
    		Map<Integer, Integer> info = betMap.get(roomId);
    		if(info == null){
    			return false;
    		}
    		return info.containsKey(userId);
    	}
    }
    
    // 获取房间内玩家的 下注倍数
    public int getPlayerBet(int roomID, int userId) {
    	synchronized (GameBetMgr.class) {
    		if (!betMap.containsKey(roomID)) {
    			return defaultBet();
			}
    		Map<Integer, Integer> betInfo = betMap.get(roomID);
			if (!betInfo.containsKey(userId)) {
    			return defaultBet();
			}
			return betInfo.get(userId);
    	}
    }
    
    public Map<Integer, Integer> getBetMap(int roomId){
    	synchronized (GameBetMgr.class) {
	    	Map<Integer, Integer> map = new HashMap<>();
	    	Map<Integer, Integer> userBet = betMap.get(roomId);
	    	if(userBet != null){
	    		map.putAll(userBet);
	    	}
	    	return map;
    	}
    }
    
    public int defaultBet(){
    	return 0;
    }
}
