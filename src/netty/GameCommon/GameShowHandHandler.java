package netty.GameCommon;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.GameModels.GameResultMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.PlayerCardsMgr;
import netty.base.SingleEndHandler;
import protocols.header;
import protocols.bull.showhand;

public class GameShowHandHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GameShowHandHandler.class);
	
	private byte[] body;
	
	private int userId;
	
	private int roomID;
	
	private Vector<Integer> handCards;
	
	public GameShowHandHandler(int roomID) {
		this.roomID = roomID;
	}
	
	public GameShowHandHandler(byte[] body, int userId){
		this.body = body;
		this.userId = userId;
		this.roomID = GameRoomMgr.getInstance().getRoomId(this.userId);
	}

	public void processHandle() {
		
		onRequestDataHandle();
		
		onResponseDataHandle();
	}
	
	private showhand.request onDecodecMsg() throws InvalidProtocolBufferException{
		showhand.request requestMsg = showhand.request.parseFrom(this.body);
		logger.info("User("+this.userId+") show handcards in room("+this.roomID+")");
		return requestMsg;
	}
	
	private void onRequestDataHandle(){
		try {
			showhand.request requestMsg = onDecodecMsg();
			
			Vector<Integer> handCards = new Vector<Integer>();
			for (int i = 0; i < requestMsg.getHandCount(); ++i) {
				handCards.add(requestMsg.getHand(i));
			}
			
			this.handCards = handCards;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	private void onResponseDataHandle(){
		boolean modified = false;
		List<Integer> serverHandCards = PlayerCardsMgr.getInstance().getPlayerHandCard(this.roomID, this.userId);
		for(Integer clientHandCard : this.handCards){
			if(!serverHandCards.contains(clientHandCard)){
				modified = true;
				break;
			}
		}
		
		showhand.response.Builder respBuilder = showhand.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.SHOWHAND_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	if(!modified){
    		logger.info("modified:" + modified);
	    	PlayerCardsMgr.getInstance().onUpdatePlayerCards(this.roomID, this.userId, this.handCards);
	    	respBuilder.setError(0);
	    	respBuilder.setErrDesc("");
    	} else {
    		respBuilder.setError(1); //1=客户端的手牌与服务器的手牌不一致
    		respBuilder.setErrDesc("客户端的手牌与服务器的手牌不一致");
    		logger.info("Detected handcard modified: userId=" + this.userId + ", roomId=" + this.roomID);
    		logger.info("server hand cards:"+toString(serverHandCards));
    		logger.info("client hand cards:"+toString(this.handCards));
    	}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(userId, msgContent);
		
		if (PlayerCardsMgr.getInstance().isAllPlayerShowHand(this.roomID)) {
			afterAllShowHand();
		}
	}
	
	private String toString(List<Integer> vec){
		if(vec == null || vec.isEmpty()){
			return "";
		}
		String ret = "";
		for(Integer integer : vec){
			ret += integer + ",";
		}
		return ret.substring(0, ret.length()-1);
	}
	
	public void afterAllShowHand() {
		if(GameResultMgr.getInstance().isAllShowHand(roomID)){
			return;//防止重复执行
		}
		GameResultMgr.getInstance().setAllShowHand(roomID);
		//TimerTaskMgr.getInstance().cancelTimerTask(roomID);//取消show hand超时任务
		new SingleEndHandler(this.roomID).notifyOver();
	}
}
