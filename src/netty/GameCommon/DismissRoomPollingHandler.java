package netty.GameCommon;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.user.Users;
import netty.GameModels.DismissMgr;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.TimerTaskMgr;
import protocols.header;
import protocols.game.dismiss;

public class DismissRoomPollingHandler {
	
	private static Logger logger = LoggerFactory.getLogger(DismissRoomHandler.class);

	private byte[] body;
	
	private int userId;
	
	private int roomID;
	
	private boolean agree;
	
	public DismissRoomPollingHandler(int roomID){
		this.roomID = roomID;
	}
	
	public DismissRoomPollingHandler(byte[] body, int userId) {
		this.userId = userId;
		this.body = body;
		this.roomID = GameRoomMgr.getInstance().getRoomId(this.userId);
	}
	
	public void processHandle() {
		
		onRequestDataHandle();
		
		broadcastDismissResult(this.agree, false);
		
		if (DismissMgr.getInstance().isAllVoteDismiss(this.roomID)) {
			afterAllDissmissVote();
		}
	}
	
	private dismiss.opinionRequest onDecodecMsg() throws InvalidProtocolBufferException{
		dismiss.opinionRequest requestMsg = dismiss.opinionRequest.parseFrom(this.body);
		return requestMsg;
	}
	
	private void onRequestDataHandle(){
		try {
			dismiss.opinionRequest requestMsg = onDecodecMsg();

			this.agree = requestMsg.getAgree() == 1;
			DismissMgr.getInstance().recordDismissOpinion(this.roomID, requestMsg.getUid(), requestMsg.getAgree());
			logger.info("User("+this.userId+") "+(this.agree?"agree":"disagree")+" dismiss room("+this.roomID+")");
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	// 广播通知房间内的所有玩家
	public void broadcastDismissResult(boolean agree, boolean last){
		dismiss.opinionResponse.Builder respBuilder = dismiss.opinionResponse.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.DISMISS_ROOM_OPINION_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	if(last){
    		respBuilder.setError(0);
    		respBuilder.setName("");
    		respBuilder.setIsDismiss(agree?1:2);
    	} else {
    		TUsers user = Users.load(userId);
			if (user == null) {
				//无此账号
				respBuilder.setError(1);
				respBuilder.setErrDesc("你已离线，请重新登录");
			} else {
		    	respBuilder.setError(0);
		    	respBuilder.setName(user.NAME);
		    	respBuilder.setAgree(agree?1:2);
			}
    	}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
    	Set<Integer> uidVec = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
		for (Integer userId : uidVec) {
			PacketManager.send(userId, msgContent);
		}
	}
	
	// 解散房间通知所有用户，发起投票
	public void startDismissVote(){
		dismiss.dismissNotify.Builder respBuilder = dismiss.dismissNotify.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.DISMISS_ROOM_NOTIFY_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
    	for (Integer uid : uidSet) {
    		respBuilder.addUids(uid);
		}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
    	
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
		for (Integer userId : uidSet) {
			PacketManager.send(userId, msgContent);
		}
		
		if (GameInningMgr.getInstance().isGameStarted(this.roomID)) {
			// 设置解散房间投票流程超时定时器
			TimerTaskMgr.getInstance().laterAutoDismissFail(this.roomID);
		}
	}
	
	public void afterAllDissmissVote(){
		if(!DismissMgr.getInstance().needDismiss(this.roomID)){
			return;//防止该方法被多次调用
		}
		boolean allAgree = DismissMgr.getInstance().isAllAgreeDismiss(this.roomID);
		DismissMgr.getInstance().deleteDismissInfo(this.roomID);
		if (allAgree) {
			//删除房间信息
			//TimerTaskMgr.getInstance().cancelTimerTask(roomID);//取消dismiss超时任务
			new GameAllOverHandler(roomID, 1).processHandle();
			GameRoomMgr.getInstance().dismissRoom(roomID);
			logger.info("User("+this.userId+") dismiss room("+this.roomID+") all agree!");
		} else {
			broadcastDismissResult(false, true);//广播解散失败
			// 开始新一轮游戏
			new GameStartHandler(this.roomID).processHandle();
		}
	}
}
