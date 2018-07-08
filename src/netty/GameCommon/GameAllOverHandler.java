package netty.GameCommon;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.TRoomConfig;
import com.mozat.morange.dbcache.tables.TRoomInnings;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.GameModels.GameBetMgr;
import netty.GameModels.GameGrabMgr;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameResultMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.PlayerCardsMgr;
import netty.util.MathUtil;
import protocols.header;
import protocols.bull.finish;

public class GameAllOverHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GameAllOverHandler.class);
	
	private int roomID;
	
	private int reason;
	
	public GameAllOverHandler(int roomID, int reason){
		this.roomID = roomID;
		this.reason = reason;
	}

	public void processHandle() {
		onResponseDataHandle();
	}
	
	private void onResponseDataHandle(){
		if(GameRoomMgr.getInstance().isDemoRoom(this.roomID) && reason == 0){
			GameInningMgr.getInstance().clearInning(this.roomID);
			new GameStartHandler(this.roomID).processHandle();
			return;
		}
		finish.response.Builder respBuilder = finish.response.newBuilder();

		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.TOTAL_GAMEOVER_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
        respBuilder.setInning(GameInningMgr.getInstance().getCurrInning(this.roomID));
        respBuilder.setReason(reason);	// 结束原因，0=inning up 1=房间解散 2=玩家封顶 3=庄家封顶 4=所有玩家离线
        
        int pmscores = GameRoomMgr.getInstance().getRoomConfig(this.roomID).pmscores;
		int dealerId = GameGrabMgr.getInstance().getDealerId(roomID);
		// Map<userId, total>
        Map<Integer, Integer> totalScores = GameResultMgr.getInstance().onGetRoomTotalResult(this.roomID);
        int maxTotal = 0;
        for(int total : totalScores.values()){
        	if(total > maxTotal){
        		maxTotal = total;
        	}
        }
		for(Map.Entry<Integer, Integer> entry : totalScores.entrySet()){
			int userId = entry.getKey();
			int total = entry.getValue();
			finish.seat_t.Builder seatInfo = finish.seat_t.newBuilder();
			seatInfo.setId(GameRoomMgr.getInstance().getSeatId(roomID, userId));			// 座位ID
			seatInfo.setUid(userId);	// UID
			seatInfo.setTotal(total);	// 总分
			int totalDraw = Math.round(GameResultMgr.getInstance().getTotalDraw(roomID, userId));
			seatInfo.setDraw(totalDraw);
			if((reason == 2 && total < 0 && MathUtil.abs(total) >= pmscores)
					|| (reason == 3 && userId == dealerId)){
				seatInfo.setBankrupt(true);
			}
			
			if(total == maxTotal){
				seatInfo.setWinner(true);
			}
			
			TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrEND_TOTAL.set(total),TRoomInnings.AttrEND_DRAW.set((float)totalDraw)), 
					TRoomInnings.AttrROOM_ID.eq(roomID), TRoomInnings.AttrINNING.eq(0), TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)));
			
			respBuilder.addSeats(seatInfo);
		}
        
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
		for (Integer userId : uidSet) {
			PacketManager.send(userId, msgContent);
		}
		
		// 清空内存信息
		onCleanRecordInfo();
		int status = reason + 1;//1=innings up;2=dismissed;3=player reach top;4=dealer reach top
		TRoomConfig.updateByCriteria(TRoomConfig.valueList(TRoomConfig.AttrSTATUS.set(status), TRoomConfig.AttrEND_TIME.set(new Date())), 
				TRoomConfig.AttrROOM_ID.eq(roomID), TRoomConfig.AttrSTATUS.eq(0));
		TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrSTATUS.set(status)), 
				TRoomInnings.AttrROOM_ID.eq(roomID), TRoomInnings.AttrSTATUS.eq(0));
		logger.info("Room("+this.roomID+") all over, reason:"+this.reason);
	}
	
	private void onCleanRecordInfo() {
		GameGrabMgr.getInstance().deleteRoomGrabs(this.roomID);
		GameBetMgr.getInstance().deleteRoomBets(this.roomID);
		GameRoomMgr.getInstance().dismissRoom(this.roomID);
		PlayerCardsMgr.getInstance().clearHandCard(this.roomID);
		GameResultMgr.getInstance().clearResult(this.roomID);
		GameInningMgr.getInstance().clearInning(this.roomID);
	}
}
