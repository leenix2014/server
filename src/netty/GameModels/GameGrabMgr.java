package netty.GameModels;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mozat.morange.dbcache.tables.TRoomInnings;

import protocols.config;

public class GameGrabMgr {
	
	volatile private static GameGrabMgr instance = null;
    
    private GameGrabMgr(){
    }

    public static GameGrabMgr getInstance() {

        if(instance == null){
            synchronized (GameGrabMgr.class) {
                if(instance == null){
                    instance = new GameGrabMgr();
                }
            }
        }

        return instance;
    }
    
    // Map<roomID, Map<userId, grabMultiple>
    private Map<Integer, Map<Integer, Integer>> grabMap = new HashMap<Integer, Map<Integer, Integer> >();
    // 抢庄或者房主坐庄的庄家
    // Map<roomId, dealerId>
    private Map<Integer, Integer> dealerMap = new HashMap<Integer, Integer>();
    // Map<roomId, maxGrab>
    private Map<Integer, Integer> maxGrabMap = new HashMap<>();
    
    //回合制的庄家
    // Map<roomId, Map<inning, dealerId>>
    private Map<Integer, Map<Integer, Integer>> inningDealerMap = new HashMap<Integer, Map<Integer, Integer>>();
    
    // 更新玩家抢庄倍数
    public void setPlayerGrab(int roomID, int userId, int grab) {
    	int inning = GameInningMgr.getInstance().getCurrInning(roomID);
		TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrUSER_GRAB.set(grab)), 
				TRoomInnings.AttrROOM_ID.eq(roomID),
				TRoomInnings.AttrINNING.eq(inning),
				TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
				TRoomInnings.AttrSTATUS.eq(0));
    	synchronized (GameGrabMgr.class) {
    		// Map<seatID, grabMultiple>
    		Map<Integer, Integer> info = grabMap.get(roomID);
    		if (info == null){
    			info = new HashMap<Integer, Integer>();
	    		grabMap.put(roomID, info);
    		}
			info.put(userId, grab);
			
			Integer dealer = dealerMap.get(roomID);
			if(dealer == null){
	    		dealerMap.put(roomID, userId);
	    		maxGrabMap.put(roomID, grab);
	    		saveDealer(roomID, userId);
			} else if (maxGrabMap.get(roomID) < grab) {
				dealerMap.put(roomID, userId);
	    		maxGrabMap.put(roomID, grab);
				saveDealer(roomID, userId);
			}
    	}
	}
    
    public int getGrab(int roomId, int userId){
    	synchronized (GameGrabMgr.class) {
    		if (!grabMap.containsKey(roomId)) {
    			return 1;
			} 
    		Map<Integer, Integer> grabInfo = grabMap.get(roomId);
			if (!grabInfo.containsKey(userId)) {
    			return 1;
			}
			return grabInfo.get(userId);
    	}
    }
    
    public int randomGrab(){
    	int[] grabs = new int[]{1, 2, 3, 5, 10};
    	Random ram = new Random();
    	int index = ram.nextInt(grabs.length);
    	return grabs[index];
    }
    
    // 删除抢庄信息和庄家信息
    public void deleteRoomGrabs(int roomID) {
    	synchronized (GameGrabMgr.class) {
    		grabMap.remove(roomID);
    		dealerMap.remove(roomID);
    		maxGrabMap.remove(roomID);
    	}
    }
    
    // 判断是否所有玩家都已抢庄
    public boolean isAllGrabbed(int roomID) {
    	synchronized (GameGrabMgr.class) {
    		if (!grabMap.containsKey(roomID)) {
    			System.out.println("No user grab in room("+roomID+") yet!");
    			return false;
			}
    		int grabCount = grabMap.get(roomID).size();
    		int userCount = GameRoomMgr.getInstance().getRoomUserCount(roomID);
    		int offlineCount = GameRoomMgr.getInstance().getOfflineCount(roomID);

    		return grabCount >= userCount - offlineCount;
    	}
	}
    
    public boolean isUserGrabbed(int roomId, int userId){
    	synchronized (GameGrabMgr.class) {
    		Map<Integer, Integer> info = grabMap.get(roomId);
    		if(info == null){
    			return false;
    		}
    		return info.containsKey(userId);
    	}
    }
    
    // 获取抢庄或房主做庄模式下庄家
    public int getDealerSeat(int roomId) {
    	return GameRoomMgr.getInstance().getSeatId(roomId, getDealerId(roomId));
    }
    
    public int getDealerId(int roomId){
    	synchronized (GameGrabMgr.class) {
        	if(isInningDealer(roomId)){
        		return getInningDealer(roomId, GameInningMgr.getInstance().getCurrInning(roomId));
        	} else {
        		return getDealer(roomId);
        	}
        }
    }
    
    //获取回合制庄家
    private Integer getInningDealer(int roomID, int inning) {
    	Map<Integer, Integer> inningMap = inningDealerMap.get(roomID);
    	if(inningMap != null && inningMap.containsKey(inning)){
    		return inningMap.get(inning);
    	}
    	Integer dealer = null;
		if (GameRoomMgr.getInstance().getRoomConfig(roomID).dealer == config.bull.DEALER.WINNER) {
    		int winnerId = GameResultMgr.getInstance().getWinnerId(roomID);
    		if (winnerId == -1) {//平局或开局时
    			//获取上一盘庄家
    			if(inningMap != null && inningMap.get(inning-1) != null){
    				Integer lastDealer = inningMap.get(inning-1);
    				winnerId = lastDealer;
    			} else {
    				//开局房主坐庄
    				winnerId = GameRoomMgr.getInstance().getOwner(roomID);
    			}
			}
    		dealer = winnerId;
		} else {
            dealer = ramdomDealer(roomID);
		}
		if(inningMap == null){
			inningMap = new HashMap<Integer, Integer>();
			inningDealerMap.put(roomID, inningMap);
		}
        inningMap.put(inning, dealer);
        saveDealer(roomID, dealer);
		return dealer;
    }
    
    private int ramdomDealer(int roomID){
    	Set<Integer> uids = GameRoomMgr.getInstance().getRoomUsers(roomID);
		Random ran = new Random();
		return (int) uids.toArray()[ran.nextInt(uids.size())];
    }
    
    // 获取抢庄或房主做庄模式下庄家
    private Integer getDealer(int roomID) {
    	if (dealerMap.containsKey(roomID)) {
            return dealerMap.get(roomID);
        }
    	Integer dealer = null;
    	if (GameRoomMgr.getInstance().getRoomConfig(roomID).dealer == config.bull.DEALER.OWNER) {
            dealer = GameRoomMgr.getInstance().getOwner(roomID);
        } else {
        	dealer = ramdomDealer(roomID);
        }
        dealerMap.put(roomID, dealer);
        saveDealer(roomID, dealer);
        return dealer;
    }
    
    private void saveDealer(int roomId, int userId){
    	int inning = GameInningMgr.getInstance().getCurrInning(roomId);
    	int dealerSeatId = GameRoomMgr.getInstance().getSeatId(roomId, userId);
    	TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrIS_DEALER.set(true)), 
				TRoomInnings.AttrROOM_ID.eq(roomId),
				TRoomInnings.AttrINNING.eq(inning),
				TRoomInnings.AttrSEAT_ID.eq(dealerSeatId),
				TRoomInnings.AttrSTATUS.eq(0));
    	TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrIS_DEALER.set(false)), 
				TRoomInnings.AttrROOM_ID.eq(roomId),
				TRoomInnings.AttrINNING.eq(inning),
				TRoomInnings.AttrSEAT_ID.ne(dealerSeatId),
				TRoomInnings.AttrSTATUS.eq(0));
    }
    
    private boolean isInningDealer(int roomID){
		return GameRoomMgr.getInstance().getRoomConfig(roomID).dealer == config.bull.DEALER.WINNER;
	}
}
