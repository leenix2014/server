package netty.GameCommon;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TRoomConfig;
import com.mozat.morange.dbcache.tables.TRoomInnings;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.GameModels.DismissMgr;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameRoomMgr;
import protocols.header;
import protocols.game.dismiss;

public class DismissRoomHandler {
	
	private static Logger logger = LoggerFactory.getLogger(DismissRoomHandler.class);

	private byte[] body;
	
	private int roomID;
	
	private int userId;
	
	public DismissRoomHandler(byte[] body, int userId) {
		this.body = body;
		this.userId = userId;
	}
	
	public void processHandle() {
		
		onRequestDataHandle();
		
		onResponseDataHandle();
	}
	
	private dismiss.request onDecodecMsg() throws InvalidProtocolBufferException{
		dismiss.request requestMsg = dismiss.request.parseFrom(this.body);
		return requestMsg;
	}
	
	private void onRequestDataHandle(){
		try {
			dismiss.request requestMsg = onDecodecMsg();
			this.roomID = requestMsg.getInst();
			logger.info("User("+this.userId+") request dismiss room("+this.roomID+")");
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	private void onResponseDataHandle(){
		
		boolean result = DismissMgr.getInstance().recordDismiss(roomID);
		
		dismiss.response.Builder respBuilder = dismiss.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.DISMISS_ROOM_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	if (result) {
    		respBuilder.setError(0);
		} 
    	else {
    		respBuilder.setError(1);
    		respBuilder.setErrDesc("申请解散失败");
		}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(this.userId, msgContent);
		
		// 游戏还没开始，直接通知其它玩家；否则，会在当局游戏结束时通知
		if (!GameInningMgr.getInstance().isGameStarted(this.roomID)) {
			new DismissRoomPollingHandler(this.roomID).startDismissVote();//游戏没开始，客户端会直接解散，否则发起投票
			//删除房间信息
			GameRoomMgr.getInstance().dismissRoom(roomID);
			TRoomConfig.updateByCriteria(TRoomConfig.valueList(TRoomConfig.AttrSTATUS.set(2),TRoomConfig.AttrEND_TIME.set(new Date())), //2=dismissed
					TRoomConfig.AttrROOM_ID.eq(roomID), TRoomConfig.AttrSTATUS.eq(0));
			TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrSTATUS.set(2),TRoomInnings.AttrEND_TIME.set(new Date())), 
					TRoomInnings.AttrROOM_ID.eq(roomID), TRoomInnings.AttrINNING.eq(0), TRoomInnings.AttrSTATUS.eq(0));
			logger.info("User("+this.userId+") dismiss room("+this.roomID+") success!");
		}
	}
    
}
