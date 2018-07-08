package netty.GameCommon;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.user.Users;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.GameRoomMgr.RoomConfig;
import netty.GameModels.PlayerCardsMgr;
import netty.GameModels.UserMgr;
import netty.base.DealingHander;
import protocols.config;
import protocols.header;
import protocols.bull.dealing;
import protocols.bull.sitdown;
import protocols.bull.state;
import protocols.game.join;

public class JoinRoomHandler {
	
	private static Logger logger = LoggerFactory.getLogger(JoinRoomHandler.class);
	
	private static final int JOINROOM_SUCC = 0;
	
	private static final int JOINROOM_NOTEXIST = 1;
	
	private static final int JOINROOM_MAXNUM = 2;
	
	private static final int JOINROOM_GAMING = 3;
	
	private byte[] body;
	
	private int roomID;
	
	private int userUID;
	
	private String errDesc = "";
	
	public JoinRoomHandler(byte[] body, int userUID) {
		this.body = body;
		this.userUID = userUID;
	}
	
	public void processHandle() {
		
		onRequestDataHandle();
		
		onResponseDataHandle();
	}
	
	private join.request onDecodecMsg() throws InvalidProtocolBufferException{
		join.request requestMsg = join.request.parseFrom(this.body);		
		return requestMsg;
	}
		
	private void onRequestDataHandle(){
		try {
			join.request requestMsg = onDecodecMsg();
			this.roomID = requestMsg.getInst();
			logger.info("User("+this.userUID+") request join room("+this.roomID+")");
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	private void onResponseDataHandle(){
		GameRoomMgr roomMgr = GameRoomMgr.getInstance();
		if(!roomMgr.isRoomExists(this.roomID) || this.userUID == 0){
			errDesc = "房间号不存在";
			onJoinResponseMsg(JOINROOM_NOTEXIST);
			logger.info("User("+this.userUID+") join non-exist room("+this.roomID+")");
			return;
		}
		if(!roomMgr.isUserInRoom(this.userUID, this.roomID) && roomMgr.isRoomFull(this.roomID)){
			errDesc = "加入的房间人数已满";
			onJoinResponseMsg(JOINROOM_MAXNUM);
			logger.info("User("+this.userUID+") join a full room("+this.roomID+")");
			return;
		}
		int gamingRoom = roomMgr.getRoomId(this.userUID);
		if(gamingRoom != -1 && gamingRoom != this.roomID){
			errDesc = "加入房间失败，房间"+gamingRoom+"正在进行牌局";
			onJoinResponseMsg(JOINROOM_GAMING);
			logger.info("User("+this.userUID+") gaming in room("+gamingRoom+"), join failed.");
			return;
		}
		if(roomMgr.isCoinRoom(this.roomID)){
			int myCoin = UserMgr.getInstance().getUserCoin(userUID);
			if(myCoin <= 0){
				errDesc = "金币不足，加入房间失败";
				onJoinResponseMsg(4);
				logger.info("User({}) join private coin room without enough coin({}).", userUID, myCoin);
				return;
			}
		}
		int seatId = roomMgr.joinRoom(this.roomID, this.userUID);
		onJoinResponseMsg(JOINROOM_SUCC);
		logger.info("User("+this.userUID+") join room("+this.roomID+") success, seatId:"+seatId);
		onSendRoomStateMsg();
		onPlayerSitDownNotify();
		sendPlayerHandCard();
	}
	
    private void onJoinResponseMsg(int result) {
    	
    	join.response.Builder respBuilder = join.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.JOIN_ROOM_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	respBuilder.setError(result);
    	respBuilder.setErrDesc(errDesc);
    	if (result == JOINROOM_SUCC) {
        	respBuilder.setGame(GameRoomMgr.getInstance().getRoomConfig(this.roomID).game);
		} else if (result == JOINROOM_GAMING){
			respBuilder.setInst(GameRoomMgr.getInstance().getRoomId(this.userUID));
		}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(this.userUID, msgContent);
    }
    
    private void onSendRoomStateMsg(){
    	state.response.Builder respBuilder = state.response.newBuilder();
    	
    	header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.ROOM_STATE_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setError(0);	// ������룬0=�ɹ� 
    	respBuilder.setErrDesc("");
    	respBuilder.setInst(this.roomID);	// ����ID
    	respBuilder.setOwner(GameRoomMgr.getInstance().getOwnerSeat(this.roomID));	// ���� seatID
    	respBuilder.setPlaying(GameInningMgr.getInstance().isGameStarted(this.roomID));	// �ƾ��Ƿ��ڽ���
    	respBuilder.setInning(GameInningMgr.getInstance().getCurrInning(this.roomID));	// ����ÿ�ֿ�ʼ��+1

    	RoomConfig gameConfig = GameRoomMgr.getInstance().getRoomConfig(this.roomID);
    	config.bull.Builder bullConfigBuilder = config.bull.newBuilder();
		bullConfigBuilder.setDealer(gameConfig.dealer);
		bullConfigBuilder.setBscores(gameConfig.bscores);
		bullConfigBuilder.setSeats(gameConfig.seats);
		bullConfigBuilder.setInnings(gameConfig.innings);
		bullConfigBuilder.setPmscores(gameConfig.pmscores);
		bullConfigBuilder.setDmscores(gameConfig.dmscores);
		bullConfigBuilder.setBlind(gameConfig.blind);
		bullConfigBuilder.setMaxBet(gameConfig.maxBet);
		bullConfigBuilder.setHasGhost(gameConfig.hasGhost);
		respBuilder.setConfig(bullConfigBuilder);	// ��Ϸ����
		
		Map<Integer, Integer> userSeat = GameRoomMgr.getInstance().getSeatMap(this.roomID);
		for(Map.Entry<Integer, Integer> entry : userSeat.entrySet()){
			int userId = entry.getKey();
			int seatId = entry.getValue();
			String name = "";
			int avatarId = 1;
			TUsers users = Users.load(userId);
			int roomcard = 0;
			int coin = 0;
			if(users != null){
				name = users.NAME;
				avatarId = users.AVATAR_ID;
				roomcard = users.ROOM_CARD_COUNT;
				coin = users.COIN_COUNT;
			}
			
			config.seat_t.Builder seat = config.seat_t.newBuilder();
			seat.setId(seatId);
			seat.setUid(userId);
			seat.setName(name);
			seat.setAvatar(avatarId);
			seat.setRoomcard(GameRoomMgr.getInstance().isCoinRoom(this.roomID)?coin:roomcard);
			
			respBuilder.addSeats(seat);	// ���������������λ��Ϣ
		}
		
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(this.userUID, msgContent);
    }
    
    //当前用户加入房间后，发送sitdown包给其他玩家
    private void onPlayerSitDownNotify() {
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
    	TUsers sitdownUser = Users.load(this.userUID);
    	for (int userId : uidSet) {
			if (userId != this.userUID) {
				String name = "";
				int avatarId = 1;
				int roomcard = 0;
				int coin = 0;
				if(sitdownUser != null){
					name = sitdownUser.NAME;
					avatarId = sitdownUser.AVATAR_ID;
					roomcard = sitdownUser.ROOM_CARD_COUNT;
					coin = sitdownUser.COIN_COUNT;
				}
				
			    sitdown.response.Builder respBuilder = sitdown.response.newBuilder();
			    
			    header.packet.Builder head = header.packet.newBuilder();
		    	head.setCommand(PacketTypes.PLAYER_SIT_CMD);
		    	head.setVersion(1);
		    	head.setSubversion(0);
		    	
		    	respBuilder.setError(0);
		    	respBuilder.setErrDesc("");
		    	respBuilder.setId(GameRoomMgr.getInstance().getSeatId(roomID, userUID)); 	// ��λID
		    	respBuilder.setUid(this.userUID);	// UID
		    	respBuilder.setName(name);
		    	respBuilder.setAvatar(avatarId);
		    	respBuilder.setRoomcard(GameRoomMgr.getInstance().isCoinRoom(this.roomID)?coin:roomcard);
		    	
		    	head.setBody(respBuilder.buildPartial().toByteString());
				
		    	byte[] msgContent = head.buildPartial().toByteArray();
		    	
		    	PacketManager.send(userId, msgContent);
			}
		}
	}
    
    private void sendPlayerHandCard() {
    	int seatId = GameRoomMgr.getInstance().getSeatId(this.roomID, this.userUID);
    	List<Integer> cards = PlayerCardsMgr.getInstance().getPlayerHandCard(this.roomID, this.userUID);
    	
    	if(cards.isEmpty()){
    		return;
    	}
    	
    	int status = PlayerCardsMgr.getInstance().getPhaseStatus(this.roomID);
    	int start = 0;
    	int end = 5;
    	if(DealingHander.PHASE1 == status){
    		end = 3;
    	}
    	
	    dealing.response.Builder respBuilder = dealing.response.newBuilder();
	    
	    header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.DEALING_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setSeat(seatId);
    	for (int i = start; i < end; ++i) {
	    	respBuilder.addHand(cards.get(i));
		}
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
    	PacketManager.send(this.userUID, msgContent);
	}
}
