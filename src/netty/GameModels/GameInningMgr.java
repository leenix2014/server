package netty.GameModels;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mozat.morange.dbcache.tables.TRoomInnings;

public class GameInningMgr {
	
	volatile private static GameInningMgr instance = null;
	
    private GameInningMgr(){
    }

    public static GameInningMgr getInstance() {

        if(instance == null){
            synchronized (GameInningMgr.class) {
                if(instance == null){
                    instance = new GameInningMgr();
                }
            }
        }

        return instance;
    }
    
    // Map<roomID, innings>
    // 当前游戏局数
    private Map<Integer, Integer> inningMap = new HashMap<Integer, Integer>();
    
    // 游戏是否已开始
    public boolean isGameStarted(int roomID) {
    	synchronized (GameInningMgr.class) {
    		return inningMap.containsKey(roomID);
    	}
    }
    
    // 更新局数信息
    public void inningIncrement(int roomID) {
    	synchronized (GameInningMgr.class) {
    		int inning = 1;
    		if (inningMap.containsKey(roomID)){
    			inning = inningMap.get(roomID) + 1;
    		}
    		inningMap.put(roomID, inning);
    		if(GameRoomMgr.getInstance().isDemoRoom(roomID)){
    			return;
    		}
    		// Map<uid, seatId>
    		Map<Integer, Integer> userSeat = GameRoomMgr.getInstance().getSeatMap(roomID);
    		for(Map.Entry<Integer, Integer> entry : userSeat.entrySet()){
    			int uid = entry.getKey();
    			int seatId = entry.getValue();
	    		TRoomInnings.create(TRoomInnings.AttrROOM_ID.set(roomID),
		    			TRoomInnings.AttrINNING.set(inning),
		    			TRoomInnings.AttrUSER_ID.set(uid),
		    			TRoomInnings.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(uid)),
		    			TRoomInnings.AttrUSER_BET.set(GameBetMgr.getInstance().defaultBet()),
		    			TRoomInnings.AttrUSER_GRAB.set(1),
		    			TRoomInnings.AttrSEAT_ID.set(seatId),
		    			TRoomInnings.AttrEND_SCORE.set(0),
		    			TRoomInnings.AttrEND_TOTAL.set(0),
		    			TRoomInnings.AttrCARDS.set(""),
		    			TRoomInnings.AttrSTATUS.set(0),
		    			TRoomInnings.AttrCREATE_TIME.set(new Date()),
		    			TRoomInnings.AttrEND_TIME.set(new Date(0)));
    		}
    	}
    }
    
    // 清除游戏局数信息
    public void clearInning(int roomID) {
    	synchronized (GameInningMgr.class) {
    		inningMap.remove(roomID);
    	}
    }
    
    // 返回当前游戏局数
    public int getCurrInning(int roomID) {
    	synchronized (GameInningMgr.class) {
    		if (!inningMap.containsKey(roomID)){
    			return 0;
    		}
    		return inningMap.get(roomID);
    	}
	}
    
    // 是否已达到用户设置的局数
    public boolean isReachRoomFixedInning(int roomID) {
    	synchronized (GameInningMgr.class) {
    		int fixedInning = GameRoomMgr.getInstance().getRoomConfig(roomID).innings;
    		return inningMap.containsKey(roomID)
    				&& inningMap.get(roomID) >= fixedInning;
    	}
    }
}
