package netty.GameModels;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import com.mozat.morange.dbcache.tables.RoomCardBill;
import com.mozat.morange.dbcache.tables.TRoomConfig;
import com.mozat.morange.dbcache.tables.TRoomInnings;

import game.common.CommonConfig;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.session.SessionManager;
import netty.GameEvaluation.BullCardsConstantInterface;
import protocols.config;
import protocols.config.bull.DEALER;
import protocols.header;
import protocols.bull.standup;
import protocols.game.exit;

public class GameRoomMgr {
	
    volatile private static GameRoomMgr instance = null;
      
    private GameRoomMgr(){
    }

    public static GameRoomMgr getInstance() {

        if(instance == null){
            synchronized (GameRoomMgr.class) {
                if(instance == null){
                    instance = new GameRoomMgr();
                    initDemoRoom(instance);
                }
            }
        }

        return instance;
    }
    
    private static void initDemoRoom(GameRoomMgr ins){
    	Map<Integer, Integer> robots = new HashMap<Integer, Integer>();
    	robots.put(ROBOT1, 2);
    	robots.put(ROBOT2, 5);
    	ins.roomUsers.put(DEMO_ROOM_ID, robots);
    	
    	RoomConfig demo = new RoomConfig();
    	demo.blind = true;
    	demo.bscores = 1;
    	demo.dealer = DEALER.GRAB;
    	demo.game = BullCardsConstantInterface.ThreeCardGame;
    	demo.innings = 10000;
    	demo.maxBet = 10;
    	demo.seats = 5;
    	ins.roomCfgs.put(DEMO_ROOM_ID, demo);
    }
    
    private static final int OWNER_SEAT_ID = 1;

    // Map<roomID, Map<uid, seatId>> 这种结构可以方便用户换座位
    private Map<Integer, Map<Integer, Integer>> roomUsers = new HashMap<Integer, Map<Integer, Integer>>();
    
    // Map<roomID, Map<uid, seatId>>
    private Map<Integer, Map<Integer, Integer>> dismissRoomUsers = new HashMap<Integer, Map<Integer, Integer>>();
    
    // Map<roomID, roomConfig>
    private Map<Integer, RoomConfig> roomCfgs = new HashMap<Integer, RoomConfig>();
    
    public static class RoomConfig {
    	public int 					game;		// 游戏类型
    	public config.bull.DEALER 	dealer; 	// 坐庄类型
    	public int 					bscores; 	// 底分
    	public int 					seats; 		// 人数（现固定5人）
    	public int 					innings; 	// 总局数
    	public int 					pmscores; 	// 闲家封顶倍数，封顶牌局解散
    	public int 					dmscores; 	// 庄家封顶倍数，封顶牌局解散
    	public boolean 				blind;		// 盲注，true=不看牌下注 false=看牌下注
    	public int                  maxBet;     // 最大下注倍数，取值1、2、3、5、10
    	public boolean              hasGhost;   // 有大小王（鬼牌）
    	public int                  drawPercent;// 抽水百分比
    	public boolean              coinroom;   // 是否金币场私人房
    }
    
	private static final int MIN_ROOM_ID = 100000;
	
	private static final int MAX_ROOM_ID = 999999;
	
	private static final int RANGEVALUE = 900000;
	
	private static final int DEMO_ROOM_ID = 666666;
	public static final int ROBOT1 = 2;
	public static final int ROBOT2 = 5;
	
    // 随机生成RoomID，如果重复，需重新生成。
    public int generateRoom(int uid, RoomConfig config){
    	Random generatorRandom = new Random();
    	int roomID = generatorRandom.nextInt(RANGEVALUE) + MIN_ROOM_ID;
    	synchronized (GameRoomMgr.class) {
	    	if (roomUsers.containsKey(roomID)){
	    		boolean dulplicate = true;
	    		for (int i = MIN_ROOM_ID; i <= MAX_ROOM_ID; i++) {
	    			roomID = i;
					if (!roomUsers.containsKey(roomID)){
			    		dulplicate = false;
						break;
					}
				}
	    		
	    		if (dulplicate){
	    			roomID = -1;
	    		}
	    	}
	    	Map<Integer, Integer> userSeat = new HashMap<Integer, Integer>();
    		userSeat.put(uid, OWNER_SEAT_ID);
    		roomUsers.put(roomID, userSeat);
    		roomCfgs.put(roomID, config);
    		TRoomConfig.create(TRoomConfig.AttrBLIND.set(config.blind), 
    				TRoomConfig.AttrBSCORES.set(config.bscores),
    				TRoomConfig.AttrDEALER_TYPE.set(config.dealer.name()),
    				TRoomConfig.AttrDMSCORES.set(config.dmscores),
    				TRoomConfig.AttrDRAW_PERCENT.set(config.drawPercent),
    				TRoomConfig.AttrCOIN_ROOM.set(config.coinroom),
    				TRoomConfig.AttrGAME.set(config.game), 
    				TRoomConfig.AttrHAS_GHOST.set(config.hasGhost),
    				TRoomConfig.AttrINNINGS.set(config.innings),
    				TRoomConfig.AttrMAX_BET.set(config.maxBet),
    				TRoomConfig.AttrOWNER.set(uid),
    				TRoomConfig.AttrOWNER_NAME.set(UserMgr.getInstance().getUserName(uid)),
    				TRoomConfig.AttrPMSCORES.set(config.pmscores),
    				TRoomConfig.AttrROOM_ID.set(roomID),
    				TRoomConfig.AttrSEATS.set(config.seats),
    				TRoomConfig.AttrSTATUS.set(0), //running
    				TRoomConfig.AttrCREATE_TIME.set(new Date()),
    				TRoomConfig.AttrEND_TIME.set(new Date(0)));
    		TRoomInnings.create(TRoomInnings.AttrROOM_ID.set(roomID),
	    			TRoomInnings.AttrINNING.set(0),
	    			TRoomInnings.AttrUSER_ID.set(uid),
	    			TRoomInnings.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(uid)),
	    			TRoomInnings.AttrSEAT_ID.set(OWNER_SEAT_ID),
	    			TRoomInnings.AttrSTATUS.set(0),
	    			TRoomInnings.AttrCREATE_TIME.set(new Date()),
	    			TRoomInnings.AttrEND_TIME.set(new Date(0)));
	    }
    	
    	return roomID;
    }
    
    public boolean isDemoRoom(int roomId){
    	return roomId == DEMO_ROOM_ID;
    }
    
    // 返回房间所有配置信息
    public RoomConfig getRoomConfig(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!roomCfgs.containsKey(roomID)) {
    			return new RoomConfig();
			} 
    		return roomCfgs.get(roomID);
    	}
    }
    
    public int getRoomBase(int roomId){
    	synchronized (GameRoomMgr.class) {
    		RoomConfig config = roomCfgs.get(roomId);
    		if(config == null){
    			return 1;
    		}
    		return config.bscores;
    	}
    }
    
    // 获取游戏类型，牛牛还是三五张
    public int getRoomGameType(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!roomCfgs.containsKey(roomID)) {
    			return -1;
			}
    		return roomCfgs.get(roomID).game;
    	}
    }
    
    public boolean isCoinRoom(int roomId){
    	synchronized (GameRoomMgr.class) {
    		if (!roomCfgs.containsKey(roomId)) {
    			return false;
			}
    		return roomCfgs.get(roomId).coinroom;
    	}
    }
    
    public int getDrawPercent(int roomId){
    	synchronized (GameRoomMgr.class) {
    		if (!roomCfgs.containsKey(roomId)) {
    			return 0;
			}
    		int percent = roomCfgs.get(roomId).drawPercent;
    		return percent == 0?CommonConfig.getPercent(CommonConfig.DRAW_PERCENT, 2):percent;
    	}
    }
    
    public String getRoomGameDesc(int roomId) {
    	return getRoomGameType(roomId) == BullCardsConstantInterface.BullGame?"斗牛":"三五张";
    }
    
    public boolean isWatchBet(int roomID){
    	if (!roomCfgs.containsKey(roomID)){
			return false;
		}
    	return !roomCfgs.get(roomID).blind;
    }
    
    public void dismissRoom(int roomID){
    	synchronized (GameRoomMgr.class) {
    		if(isDemoRoom(roomID)){
    			initDemoRoom(this);
    			return;
    		}
    		dismissRoomUsers.put(roomID, roomUsers.remove(roomID));
    		roomCfgs.remove(roomID);
    	}
    }
    
    public boolean isRoomExists(int roomId){
    	synchronized (GameRoomMgr.class) {
    		return roomUsers.containsKey(roomId);
    	}
    }
    
    public boolean isUserInRoom(int uid, int roomId){
    	synchronized (GameRoomMgr.class) {
    		if(!roomUsers.containsKey(roomId)){
    			return false;
    		}
    		return roomUsers.get(roomId).containsKey(uid);
    	}
    }
    
    public boolean isRoomFull(int roomId){
    	synchronized (GameRoomMgr.class) {
    		if(!roomUsers.containsKey(roomId)){
    			return true;
    		}
    		int userCount = roomUsers.get(roomId).keySet().size();
    		int maxSeat = roomCfgs.get(roomId).seats;
    		return userCount >= maxSeat;
    	}
    }
    
    public int joinRoom(int roomID, int uid){
    	synchronized (GameRoomMgr.class) {
	    	if (!roomUsers.containsKey(roomID)){
	    		System.out.println("join the non-exist room. roomID = " + roomID);
				return -1;
	    	}
	    	int currSeat = this.getSeatId(roomID, uid);
			if(currSeat != -1){
				return currSeat;
			}
	    	int seatId = assignSeatId(roomID, uid);
	    	if(isDemoRoom(roomID)){
	    		return seatId;
	    	}
	    	TRoomInnings.create(TRoomInnings.AttrROOM_ID.set(roomID),
	    			TRoomInnings.AttrINNING.set(0),
	    			TRoomInnings.AttrUSER_ID.set(uid),
	    			TRoomInnings.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(uid)),
	    			TRoomInnings.AttrSEAT_ID.set(OWNER_SEAT_ID),
	    			TRoomInnings.AttrSTATUS.set(0),
	    			TRoomInnings.AttrCREATE_TIME.set(new Date()),
	    			TRoomInnings.AttrEND_TIME.set(new Date(0)));
    		return seatId;
    	}
    }
    // 分配座位号
    private int assignSeatId(int roomID, int uid) {
		int seatID = -1;
		Map<Integer, Integer> userSeat = roomUsers.get(roomID);
		int maxSeat = roomCfgs.get(roomID).seats;
		// 分配seatID
		for (int i = OWNER_SEAT_ID; i <= maxSeat; i++) {
			if (!userSeat.containsValue(i)) {
				seatID = i;
				userSeat.put(uid, seatID);
				break;
			}
		}
		
    	return seatID;
    }
    
    // 用户退出房间
    public int quitRoom(int roomId, int userId){
    	synchronized (GameRoomMgr.class) {
	    	if (!roomUsers.containsKey(roomId)){
	    		return 0;
	    	}
	    	Map<Integer, Integer> userSeat = roomUsers.get(roomId);
    		if (!userSeat.containsKey(userId)){
				return 0;
    		}
    		selfNotify(userId);
    		otherNotify(roomId, userId);
    		int seaId = getSeatId(roomId, userId);
    		TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrSTATUS.set(2),TRoomInnings.AttrEND_TIME.set(new Date())), 
					TRoomInnings.AttrROOM_ID.eq(roomId), TRoomInnings.AttrINNING.eq(0), TRoomInnings.AttrSEAT_ID.eq(seaId), TRoomInnings.AttrSTATUS.eq(0));
    		userSeat.remove(userId);
    		return seaId;
    	}
    }
    
    private void selfNotify(int userId){
    	exit.response.Builder respBuilder = exit.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.QUIT_ROOM_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setError(0);
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(userId, msgContent);
    }
    
    private void otherNotify(int roomId, int userId) {
    	Set<Integer> uids = GameRoomMgr.getInstance().getRoomUsers(roomId);
    	for (int uid : uids) {
    		if(uid == userId){
    			continue;
    		}
			int userSeatId = GameRoomMgr.getInstance().getSeatId(roomId, userId);
		    standup.response.Builder respBuilder = standup.response.newBuilder();
		    
		    header.packet.Builder head = header.packet.newBuilder();
	    	head.setCommand(PacketTypes.PLAYER_STANDUP_CMD);
	    	head.setVersion(1);
	    	head.setSubversion(0);
	    	 
	    	
	    	respBuilder.setError(0);
	    	respBuilder.setErrDesc("");
	    	respBuilder.setId(userSeatId); 	// 退出玩家座位ID
	    	respBuilder.setUid(userId);	// 退出玩家用户ID
	    	
	    	head.setBody(respBuilder.buildPartial().toByteString());
			
	    	byte[] msgContent = head.buildPartial().toByteArray();
	    	PacketManager.send(uid, msgContent);
		}
	}
    
    // 获取房间内所有用户
    public Set<Integer> getRoomUsers(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!roomUsers.containsKey(roomID)){
    			return new HashSet<Integer>();
    		}
			return roomUsers.get(roomID).keySet();
    	}
    }
    
    public int getRoomUserCount(int roomID){
    	return getRoomUsers(roomID).size();
    }
    
    public int getOfflineCount(int roomId){
    	int offlineCount = 0;
    	synchronized (GameRoomMgr.class) {
	    	Set<Integer> uids = getRoomUsers(roomId);
	    	for(Integer uid : uids){
	    		if(SessionManager.isOffline(uid)){
	    			offlineCount++;
	    		}
	    	}
    	}
    	return offlineCount;
    }
    
    public boolean isAllOffline(int roomId){
    	return getOfflineCount(roomId) >= getRoomUserCount(roomId);
    }
    
    public Set<Integer> getDismissRoomUsers(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!dismissRoomUsers.containsKey(roomID)){
    			return new HashSet<Integer>();
    		}
			return dismissRoomUsers.get(roomID).keySet();
    	}
    }
    
    //获得玩家房间id，若-1则不在房间中
    public int getRoomId(int userId){
    	synchronized (GameRoomMgr.class) {
	    	Set<Map.Entry<Integer , Map<Integer, Integer>>> entrySet = roomUsers.entrySet();
	    	for (Map.Entry<Integer , Map<Integer, Integer>> entry : entrySet) {
	    		int roomId = entry.getKey();
	    		Map<Integer, Integer> uids = entry.getValue();
				if(uids.containsKey(userId)){
					return roomId;
				}
			}
    	}
    	
    	return -1;
    }
    
    // 获取房间所有座位号
    public Vector<Integer> getRoomSeatIds(int roomID) {
    	synchronized (GameRoomMgr.class) {
			if (!roomUsers.containsKey(roomID)){
				return new Vector<Integer>();
			}
			Map<Integer, Integer> userSeat = roomUsers.get(roomID);
			Vector<Integer> seats = new Vector<Integer>();
			for (Integer seatId : userSeat.values()) {  
				seats.add(seatId);
			}
			return seats;
		}
    }
    
    // 通过seatId获取uid
    public int getUidBySeatId(int roomID, int seatID) {
    	synchronized (GameRoomMgr.class) {
			if (!roomUsers.containsKey(roomID)){
				return -1;
			}
			Map<Integer, Integer> userSeat = roomUsers.get(roomID);
			if (!userSeat.containsValue(seatID)) {
				return -1;
			}
			for(Map.Entry<Integer, Integer> entry : userSeat.entrySet()){
				int uid = entry.getKey();
				int seatId = entry.getValue();
				if(seatId == seatID){
					return uid;
				}
			}
			return -1;
    	}
    }
    
    // 获取房间座位分布
    // Map<uid, seatId>
    public Map<Integer, Integer> getSeatMap(int roomID) {
    	synchronized (GameRoomMgr.class) {
    		if (!roomUsers.containsKey(roomID)) {
    			return new HashMap<Integer, Integer>();
    		}
    		return roomUsers.get(roomID);
    	}
    }
    
    // 获取房主
    public int getOwnerSeat(int roomId) {
    	return OWNER_SEAT_ID;
	}
    
    public int getOwner(int roomId){
    	return getUidBySeatId(roomId, getOwnerSeat(roomId));
    }
    
    //获取座位，无座位则返回-1
    public int getSeatId(int roomID, int userId){
    	synchronized (GameRoomMgr.class) {
    		if (!roomUsers.containsKey(roomID)) {
    			return -1;
			}
    		Map<Integer, Integer> userSeat = roomUsers.get(roomID);
    		if(!userSeat.containsKey(userId)){
    			return -1;
    		}
    		return userSeat.get(userId);
    	}
    }
    
    public void consumeRoomCard(int roomId){
    	if(isDemoRoom(roomId) || isCoinRoom(roomId)){
    		return;
    	}
    	int ownerId = getOwner(roomId);
		//一局扣一张房卡
		int oldRoomCardCount = UserMgr.getInstance().getCuber(ownerId);
		int nowRoomCardCount = oldRoomCardCount - 1;
		UserMgr.getInstance().setCuber(ownerId, nowRoomCardCount);
		RoomCardBill bill = RoomCardBill.getOneByCriteria(RoomCardBill.AttrUSER_ID.eq(ownerId+""),
				RoomCardBill.AttrSOURCE.eq("consume"),
				RoomCardBill.AttrSOURCE_ID.eq(roomId+""));
		if(bill == null){
			RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(ownerId+""), 
					RoomCardBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(ownerId)),
					RoomCardBill.AttrSOURCE.set("consume"),
					RoomCardBill.AttrSOURCE_ID.set(roomId+""),
					RoomCardBill.AttrSOURCE_NAME.set("开房间"+roomId+"("+GameRoomMgr.getInstance().getRoomGameDesc(roomId)+")"),
					RoomCardBill.AttrAMOUNT.set(-1),
					RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
					RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
					RoomCardBill.AttrCREATE_TIME.set(new Date()));
		} else {
			bill.AMOUNT = bill.AMOUNT - 1;
			bill.AFTER_BAL = bill.AFTER_BAL - 1;
			bill.update();
		}
		UserMgr.getInstance().sendCuber(ownerId , 1);
    }
}
