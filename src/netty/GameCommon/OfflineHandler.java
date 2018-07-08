package netty.GameCommon;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.baccarat.model.BaccaratRoomService;
import game.coinroom.model.CoinRoomService;
import game.live.model.LiveRoomService;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.roulette.model.RouletteRoomService;
import netty.GameModels.GameRoomMgr;
import protocols.header;
import protocols.bull.offline;

public class OfflineHandler {
	
	private static Logger logger = LoggerFactory.getLogger(OfflineHandler.class);
	
	private int userId;
	
	public OfflineHandler(int userId) {
		this.userId = userId;
	}
	
	public void processHandle() {
		gameRoomHandle();
		liveRoomHandle();
		rouletteHandle();
		baccaratHandle();
		coinRoomHandle();
	}
	
	private void coinRoomHandle(){
		CoinRoomService.offline(userId);
	}
	
	private void rouletteHandle(){
		RouletteRoomService.offline(userId);
	}
	
	private void baccaratHandle(){
		BaccaratRoomService.offline(userId);
	}
	
	private void liveRoomHandle(){
		if(userId < 1000){
			LiveRoomService.anchorExit(userId);
		}
	}
	
	private void gameRoomHandle(){
		int roomId = GameRoomMgr.getInstance().getRoomId(userId);
    	if(roomId == -1){
    		logger.info("User({}) offline, but not gaming.", userId);
    		return;
    	}
    	Set<Integer> uids = GameRoomMgr.getInstance().getRoomUsers(roomId);
    	if(uids.size() <= 1){
    		logger.info("User({}) offline, but only self in room({})", userId, roomId);
    		return;
    	}
    	
    	int seatId = GameRoomMgr.getInstance().getSeatId(roomId, userId);
    	if(GameRoomMgr.getInstance().isDemoRoom(roomId)){
    		GameRoomMgr.getInstance().quitRoom(roomId, userId);
    	}
    	
    	header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.OFFLINE_NOTIFY_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
		offline.response.Builder offlineBuilder = offline.response.newBuilder();
		offlineBuilder.setSeat(seatId);
		offlineBuilder.setUserId(this.userId);
		head.setBody(offlineBuilder.buildPartial().toByteString());
		byte[] msgContent = head.buildPartial().toByteArray();
		for(Integer uid : uids){
			if(uid == this.userId){
				continue;
			}
			PacketManager.send(uid, msgContent);
		}
		logger.info("User({}) offline, notify other user in room({}) success.", userId, roomId);
	}
}
