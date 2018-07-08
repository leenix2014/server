package netty.GameModels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DismissMgr {
	
	volatile private static DismissMgr instance = null;
    
    private DismissMgr(){
    }

    public static DismissMgr getInstance() {
        if(instance == null){
            synchronized (DismissMgr.class) {
                if(instance == null){
                    instance = new DismissMgr();
                }
            }
        }
        return instance;
    }
    
    // Map<roomID, Map<uid, vote>> vote:1=agree,2=disagree
    private Map<Integer, Map<Integer, Integer>> dismissVote = new HashMap<Integer, Map<Integer, Integer> >();
    
    // Set<roomID>
    private Set<Integer> needDismissRoom = new HashSet<Integer>();
    
    public void deleteDismissInfo(int roomID){
    	synchronized (GameRoomMgr.class) {
    		dismissVote.remove(roomID);
			needDismissRoom.remove(roomID);
    	}
    }
    
    //判断是否有解散房间请求
    public boolean needDismiss(int roomID){
    	synchronized (GameRoomMgr.class) {
    		return needDismissRoom.contains(roomID);
    	}
    }
    
    //接收解散房间请求
    public boolean recordDismiss(int roomID){
    	synchronized (GameRoomMgr.class) {
			needDismissRoom.add(roomID);
    		return true;
    	}
    }
    
    // 记录解散房间用户投票信息
    public void recordDismissOpinion(int roomID, int uid, int agree){
    	synchronized (GameRoomMgr.class) {
    		Map<Integer, Integer> voteMap;
    		if (!dismissVote.containsKey(roomID)) {
    			voteMap = new HashMap<Integer, Integer>();
    			voteMap.put(uid, agree);
    			dismissVote.put(roomID, voteMap);
    			return;
    		}
    		voteMap = dismissVote.get(roomID);
			voteMap.put(uid, agree);
    	}
    }
    
    // 解散房间是否所有用户都已投票
    public boolean isAllVoteDismiss(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!dismissVote.containsKey(roomID)) {
    			System.out.println("No user vote dismiss in room("+roomID+") yet!");
				return false;
    		}
    		Map<Integer, Integer> votes = dismissVote.get(roomID);
    		int userCount = GameRoomMgr.getInstance().getRoomUserCount(roomID);
    		int dismissIgnoreCount = GameRoomMgr.getInstance().getOfflineCount(roomID);
    		return votes.size() >= (userCount - dismissIgnoreCount);
    	}
    }
    
    // 所有用户都同意才能解散房间
    public boolean isAllAgreeDismiss(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!dismissVote.containsKey(roomID)) {
    			System.out.println("No user vote dismiss in room("+roomID+") yet!");
				return false;
    		}
    		Map<Integer, Integer> votes = dismissVote.get(roomID);
    		int agreeCount = 0;
			for (Integer vote : votes.values()) {
				if (vote == 1) {
					agreeCount++;
				}
			}
			int userCount = GameRoomMgr.getInstance().getRoomUserCount(roomID);
			int dismissIgnoreCount = GameRoomMgr.getInstance().getOfflineCount(roomID);
			return agreeCount >= (userCount - dismissIgnoreCount);
    	}
    }
}
