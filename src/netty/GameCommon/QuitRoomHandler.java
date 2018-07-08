package netty.GameCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameRoomMgr;
import protocols.header;
import protocols.game.exit;

public class QuitRoomHandler {
	
	private static Logger logger = LoggerFactory.getLogger(QuitRoomHandler.class);
	
	private byte[] body;
	
	private int roomID;
	
	private int userUID;
	
	public QuitRoomHandler(byte[] body, int userUID) {
		this.body = body;
		this.userUID = userUID;
	}
	
	public void processHandle() {
		
		onRequestDataHandle();
		
		onResponseDataHandle();
	}
	
	private exit.request onDecodecMsg() throws InvalidProtocolBufferException{
		exit.request requestMsg = exit.request.parseFrom(this.body);
		logger.info("User("+this.userUID+") request quit room("+this.roomID+")");
		return requestMsg;
	}
	
	private void onRequestDataHandle(){
		try {
			exit.request requestMsg = onDecodecMsg();
			this.roomID = requestMsg.getInst();
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	private void onResponseDataHandle(){
		boolean gameStarted = GameInningMgr.getInstance().isGameStarted(this.roomID);
		if(gameStarted){
			int userCount = GameRoomMgr.getInstance().getRoomUserCount(roomID);
			int offlineCount = GameRoomMgr.getInstance().getOfflineCount(roomID);
			if((userCount - offlineCount) == 1){
				//只剩下一个人时，允许退出房间
				GameRoomMgr.getInstance().quitRoom(this.roomID, this.userUID);
			} else {
				onQuitResponseMsg(1);//1=游戏已开始，退出失败
			}
		} else {
			GameRoomMgr.getInstance().quitRoom(this.roomID, this.userUID);
		}
	}
	
    // 返回exit.response响应
    private void onQuitResponseMsg(int error) {
    	
    	exit.response.Builder respBuilder = exit.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.QUIT_ROOM_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setError(error);
    	if(error == 1){
    		respBuilder.setErrDesc("牌局进行中不能退出");
    	}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(this.userUID, msgContent);
    	
    	logger.info("User("+this.userUID+") quit room("+this.roomID+") result:"+error);
    }
}
