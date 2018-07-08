package netty.GameCommon;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.GameModels.GameGrabMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.TimerTaskMgr;
import protocols.header;
import protocols.bull.grabbing;

public class GrabDealerHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GrabDealerHandler.class);
	
	private byte[] body;
	
	private int userId;
	
	private int roomID;
	
	private int grab;
	
	public GrabDealerHandler(int roomID) {
		this.roomID = roomID;
	}

	public GrabDealerHandler(byte[] body, int userId) {
		this.userId = userId;
		this.body = body;
		this.roomID = GameRoomMgr.getInstance().getRoomId(this.userId);
	}
	
	public void processHandle() {
		
		onRequestDataHandle();
		
		onResponseDataHandle();
	}
	
	private grabbing.request onDecodecMsg() throws InvalidProtocolBufferException{
		grabbing.request requestMsg = grabbing.request.parseFrom(this.body);
		return requestMsg;
	}
	
	private void onRequestDataHandle(){
		try {
			grabbing.request requestMsg = onDecodecMsg();
			this.grab = requestMsg.getGrabs();
			logger.info("User("+this.userId+") grab "+this.grab+" in room("+this.roomID+")");
			
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	private void onResponseDataHandle(){
		broadcastGrab(this.userId, this.grab);
		
		if(GameRoomMgr.getInstance().isDemoRoom(this.roomID) 
				&& !GameGrabMgr.getInstance().isUserGrabbed(roomID, GameRoomMgr.ROBOT1)){
			broadcastGrab(GameRoomMgr.ROBOT1, GameGrabMgr.getInstance().randomGrab());
			broadcastGrab(GameRoomMgr.ROBOT2, GameGrabMgr.getInstance().randomGrab());
		}
		
		if (GameGrabMgr.getInstance().isAllGrabbed(this.roomID)) {
			TimerTaskMgr.getInstance().laterGrabTimeout(this.roomID, TimerTaskMgr.DEALER_SETTLED_DELAY);
		}
	}
	
	private void broadcastGrab(int userId, int grab){
		GameGrabMgr.getInstance().setPlayerGrab(this.roomID, userId, grab);
		grabbing.response.Builder respBuilder = grabbing.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.GRABDEALER_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setError(0);
    	respBuilder.setErrDesc("");
    	respBuilder.setGrabs(grab);
    	respBuilder.setSeat(GameRoomMgr.getInstance().getSeatId(roomID, userId));
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
		for (Integer uid : uidSet) {
			PacketManager.send(uid, msgContent);
		}
	}
	
	public void broadcastDealer() {
		grabbing.response.Builder respBuilder = grabbing.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.GRABDEALER_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	int dealerId = GameGrabMgr.getInstance().getDealerId(roomID);
    	respBuilder.setError(0);
    	respBuilder.setErrDesc("");
    	respBuilder.setGrabs(GameGrabMgr.getInstance().getGrab(this.roomID, dealerId));
    	respBuilder.setDealer(GameGrabMgr.getInstance().getDealerSeat(this.roomID));
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
		for (Integer userId : uidSet) {
			PacketManager.send(userId, msgContent);
		}
		
		TimerTaskMgr.getInstance().laterBetTimeout(this.roomID);
	}
}
