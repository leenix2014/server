package game.coinroom.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.CoinRoom;
import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.util.MD5;

import game.coinroom.CoinRoomBetPacket;
import game.coinroom.CoinRoomGrabPacket;
import game.coinroom.CoinRoomJoinPacket;
import game.coinroom.CoinRoomLeaveOnOverPacket;
import game.coinroom.CoinRoomLeavePacket;
import game.coinroom.CoinRoomQuickJoinPacket;
import game.coinroom.CoinRoomReadyPacket;
import game.coinroom.CoinRoomShowHandPacket;
import game.coinroom.CoinRoomSitDownPacket;
import game.coinroom.CoinRoomStagePacket;
import game.coinroom.CoinRoomStandupPacket;
import game.coinroom.CoinRoomStartPacket;
import game.coinroom.util.ConvertUtil;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.user.Users;
import netty.GameEvaluation.BullCardsLogic;
import netty.GameModels.UserMgr;
import netty.util.StringUtil;
import protocols.header;
import protocols.coinroom.bet.bet_notify;
import protocols.coinroom.deal;
import protocols.coinroom.grab.banker_notify;
import protocols.coinroom.grab.grab_notify;
import protocols.coinroom.join.join_notify;
import protocols.coinroom.leave.leave_notify;
import protocols.coinroom.over;
import protocols.coinroom.ready.ready_notify;
import protocols.coinroom.showhand.showhand_notify;
import protocols.coinroom.sitdown.sitdown_notify;
import protocols.coinroom.stage.stage_notify;
import protocols.coinroom.standup.standup_notify;
import protocols.coinroom.start;

public class CoinRoomService {
	private static Logger logger = LoggerFactory.getLogger(CoinRoomService.class);
	// Map<roomId, roomModel>
	private static Map<Integer, CoinRoomModel> allCoinRoom = new ConcurrentHashMap<Integer, CoinRoomModel>();
	public static void init() {
		for(CoinRoom config :CoinRoom.getAllObjects()){
			CoinRoomModel model = allCoinRoom.get(config.ROOM_ID);
			if(model == null){
				allCoinRoom.put(config.ROOM_ID, new CoinRoomModel(config));
			} else {
				model.setConfig(config);//for refresh
			}
		}
	}
	// Map<userId, roomModel>
	private static Map<Integer, CoinRoomModel> userRoom = new ConcurrentHashMap<Integer, CoinRoomModel>();
	
	private static CoinRoomModel getGamingRoom(int userId){
		for(CoinRoomModel model : allCoinRoom.values()){
			if(model.isPlayer(userId)){
				return model;
			}
		}
		return null;
	}
	
	public static int getPlayerCount(int roomId){
		CoinRoomModel model = allCoinRoom.get(roomId);
		if(model == null){
			return 0;
		}
		return model.getPlayerCount();
	}
	
	public static void quickjoin(CoinRoomQuickJoinPacket packet){
		TUsers user = Users.load(packet.userId);
		int roomId = 0;
		for(CoinRoomModel model : allCoinRoom.values()){
			if(!packet.mode.name().equals(model.getConfig().MODE) || model.getConfig().ENCRYPTED){
				continue;
			}
			if(user.COIN_COUNT >= model.getConfig().MIN_COIN && !model.isFull()){
				roomId = model.getRoomId();
				break;
			}
		}
		logger.info("User({}) request quick join, find room({})", packet.userId, roomId);
		CoinRoomJoinPacket join = new CoinRoomJoinPacket();
		join.roomId = roomId;
		join.userId = packet.userId;
		join(join, true);
	}
	
	public static void join(CoinRoomJoinPacket packet){
		logger.info("User({}) request join room({})", packet.userId, packet.roomId);
		join(packet, false);
	}
	
	private static void join(CoinRoomJoinPacket packet, boolean quickjoin){
		int userId = packet.userId;
		CoinRoomModel model = allCoinRoom.get(packet.roomId);
		if(model == null){
			if(quickjoin){
				packet.error = 4;//没有合适的房间
				packet.errDesc = "没有合适的房间";
			} else {
				packet.error = 1;//房间号不存在
				packet.errDesc = "房间已销毁，请刷新列表";
			}
			logger.info("User({}) join the non exist Room({})", userId, packet.roomId);
			PacketManager.send(userId, packet);
			return;
		}
		if(model.getConfig().ENCRYPTED && !StringUtil.nonNull(packet.pwd).equals(MD5.getHashString(model.getConfig().PWD))){
			packet.error = 5;//密码不正确
			packet.errDesc = "房间密码错误";
			logger.info("User({}) join the encrypted room({}) and password error!", userId, packet.roomId);
			PacketManager.send(userId, packet);
			return;
		}
		TUsers user = Users.load(userId);
		int userCoin = user.COIN_COUNT;
		if(userCoin < model.getConfig().MIN_COIN){
			packet.error = 2;
			packet.errDesc = "金币不足，请先充值";
			logger.info("User({}) join Room({}) failed because no enough coin({})", userId, packet.roomId, userCoin);
			PacketManager.send(userId, packet);
			return;
		}
		CoinRoomModel gamingRoom = getGamingRoom(userId);
		if(gamingRoom != null){
			packet.error = 3;
			packet.errDesc = "你有牌局正在进行，将加入之前的房间";
			model = gamingRoom;
		} else {
			packet.error = 0;
			model.join(userId);
		}
		packet.model = model;
		userRoom.put(userId, model);
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_JOIN_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	join_notify.Builder joinNotify = join_notify.newBuilder();
    	joinNotify.setJoinUser(ConvertUtil.toUserInfo(user));
    	joinNotify.setRole(model.getUserRole(userId));
    	joinNotify.setSeatId(model.getSeatId(userId));
    	head.setBody(joinNotify.buildPartial().toByteString());
    	for(int roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void ready(CoinRoomReadyPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) ready in room({})", userId, model.getRoomId());
		boolean allReady = model.ready(userId);
		packet.error = 0;
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_READY_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	ready_notify.Builder readyNotify = ready_notify.newBuilder();
    	readyNotify.setSeatId(model.getSeatId(userId));
    	readyNotify.setAllReady(allReady);
    	head.setBody(readyNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	//保留给手动开始游戏
	public static void start(CoinRoomStartPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) request start game in room({})", userId, model.getRoomId());
		model.start();
	}
	
	public static void notifyStart(CoinRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_START);
    	head.setVersion(1);
    	head.setSubversion(0);
    	start.response.Builder startNotify = start.response.newBuilder();
    	startNotify.setError(0);
    	head.setBody(startNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
    	logger.info("Room({}) start game", model.getRoomId());
	}
	
	public static void grab(CoinRoomGrabPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) grab({}) in room({})", userId, packet.multiple, model.getRoomId());
		model.grab(userId, packet.multiple>0?packet.multiple:1);
		packet.error = 0;
		packet.myGrab = model.getGrab(userId);
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_GRAB_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	grab_notify.Builder grabNotify = grab_notify.newBuilder();
    	grabNotify.setSeatId(model.getSeatId(userId));
    	grabNotify.setGrab(packet.multiple);
    	head.setBody(grabNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void notifyBanker(CoinRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_BANKER_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	banker_notify.Builder bankerNotify = banker_notify.newBuilder();
    	int bankerId = model.getBanker();
    	bankerNotify.setBankerSeatId(model.getSeatId(bankerId));
    	bankerNotify.setBankerGrab(model.getGrab(bankerId));
    	head.setBody(bankerNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
    	logger.info("Room({}) all grabbed, banker is {}", model.getRoomId(), bankerId);
	}
	
	public static void bet(CoinRoomBetPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) bet({}) in room({})", userId, packet.multiple, model.getRoomId());
		model.bet(userId, packet.multiple>0?packet.multiple:1);
		packet.error = 0;
		packet.myBet = model.getBet(userId);
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_BET_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	bet_notify.Builder betNotify = bet_notify.newBuilder();
    	betNotify.setSeatId(model.getSeatId(userId));
    	betNotify.setBet(packet.multiple);
    	head.setBody(betNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void notifyStage(CoinRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_STAGE_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	stage_notify.Builder bankerNotify = stage_notify.newBuilder();
    	bankerNotify.setCurr(model.getStage());
    	bankerNotify.setCountDown(model.getCountDown());
    	head.setBody(bankerNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
    	logger.info("Room({}) stage changed, new stage is {}", model.getRoomId(), model.getStage().name());
	}
	
	public static void notifyCards(CoinRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_DEAL_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	for(Map.Entry<Integer, List<Integer>> entry : model.getPlayerCards().entrySet()){
    		int playerId = entry.getKey();
    		List<Integer> cards = entry.getValue();
    		deal.response.Builder dealNotify = deal.response.newBuilder();
        	dealNotify.setSeatId(model.getSeatId(playerId));
        	for(Integer card:cards){
        		dealNotify.addHand(card);
        	}
        	head.setBody(dealNotify.buildPartial().toByteString());
    		PacketManager.send(playerId, head.buildPartial().toByteArray());
    	}
    	logger.info("Room({}) deal finished", model.getRoomId());
	}
	
	public static void showhand(CoinRoomShowHandPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) showhand in room({})", userId, model.getRoomId());
		if(!model.showhand(userId, packet.handCards)){
			packet.error = 1;
			packet.errDesc = "客户端的牌与服务器端的牌不一致";
			PacketManager.send(userId, packet);
			return;
		}
		packet.error = 0;
		packet.cardType = model.getCardType(userId);
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_SHOW_HAND_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	showhand_notify.Builder showhandNotify = showhand_notify.newBuilder();
    	showhandNotify.setSeatId(model.getSeatId(userId));
    	for(Integer card : packet.handCards){
    		showhandNotify.addHand(card);
    	}
    	showhandNotify.setCardType(packet.cardType);
    	head.setBody(showhandNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void notifyOver(CoinRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_OVER);
    	head.setVersion(1);
    	head.setSubversion(0);
    	over.response.Builder overNotify = over.response.newBuilder();
    	overNotify.setBaseScore(model.getConfig().BASE_SCORE);
    	int banker = model.getBanker();
    	if(banker > 0){
    		overNotify.setBankerSeatId(model.getSeatId(banker));
    		overNotify.setBankerGrab(model.getGrab(banker));
    	}
    	for(Map.Entry<Integer, Integer> entry : model.getPlayerSeat().entrySet()){
    		int playerId = entry.getKey();
    		int seatId = entry.getValue();
    		over.seat_t.Builder overInfo = over.seat_t.newBuilder();
        	overInfo.setSeatId(seatId);
        	overInfo.setGrab(model.getGrab(playerId));
        	overInfo.setBet(model.getBet(playerId));
        	List<Integer> cards = model.getCards(playerId);
        	for(Integer card:cards){
        		overInfo.addHand(card);
        	}
        	int cardType = model.getCardType(playerId);
        	overInfo.setCardType(cardType);
        	overInfo.setCardTypeMul(BullCardsLogic.getCardTypeMul(cardType));
        	int score = model.getScore(playerId);
        	int draw = model.getDraw(playerId);
        	overInfo.setScore(score-draw);
        	overInfo.setDraw(draw);
        	int remainCoin = UserMgr.getInstance().getUserCoin(playerId);
        	overInfo.setRemainCoin(remainCoin);
        	overInfo.setNeedCharge(remainCoin <= model.getConfig().NEED_CHARGE_COIN?true:false);
        	overInfo.setChargeTo(Math.max(model.getConfig().MIN_COIN, model.getConfig().NEED_CHARGE_COIN));
        	overNotify.addSeats(overInfo);
    	}
    	head.setBody(overNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
    	//logger.info("Room({}) phase({}) over, result({})", model.getRoomId(), model.getPhase().name(), overNotify.toString());
    	logger.info("Room({}) phase({}) over", model.getRoomId(), model.getPhase().name());
	}
	
	public static void stage(CoinRoomStagePacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) request get stage in room({})", userId, model.getRoomId());
		packet.error = 0;
		packet.curr = model.getStage();
		packet.countDown = model.getCountDown();
		PacketManager.send(userId, packet);
	}
	
	public static void sitdown(CoinRoomSitDownPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) request sitdown in room({}), seat({})", userId, model.getRoomId(), packet.seatId);
		packet.error = model.sitdown(userId, packet.seatId);
		if(packet.error == 1){
			packet.errDesc = "该座位已有其他玩家";
		}
		PacketManager.send(userId, packet);
		
		if(packet.error == 0){
			header.packet.Builder head = header.packet.newBuilder();
	    	head.setCommand(PacketTypes.COIN_ROOM_SIT_DOWN_NOTIFY);
	    	head.setVersion(1);
	    	head.setSubversion(0);
	    	sitdown_notify.Builder sitdownNotify = sitdown_notify.newBuilder();
	    	sitdownNotify.setSeatId(model.getSeatId(userId));
	    	sitdownNotify.setSitdownUser(ConvertUtil.toUserInfo(userId));
	    	head.setBody(sitdownNotify.buildPartial().toByteString());
	    	for(Integer roomUser : model.getRoomUserExcept(userId)){
	    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
	    	}
		}
	}
	
	public static void standup(CoinRoomStandupPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		int seatId = model.getSeatId(userId);
		logger.info("User({}) request standup in room({}), seat({})", userId, model.getRoomId(), seatId);
		packet.error = model.standup(userId);
		if(packet.error == 1){
			packet.errDesc = "玩完本局才能站起";
		}
		PacketManager.send(userId, packet);
		
		logger.info("User({}) standup in coin room({}) status:{}.", userId, model.getRoomId(), packet.error);
		
		if(seatId >0 && packet.error == 0){
			header.packet.Builder head = header.packet.newBuilder();
	    	head.setCommand(PacketTypes.COIN_ROOM_STAND_UP_NOTIFY);
	    	head.setVersion(1);
	    	head.setSubversion(0);
	    	standup_notify.Builder standupNotify = standup_notify.newBuilder();
	    	standupNotify.setSeatId(seatId);
	    	for(Integer roomUser : model.getRoomUserExcept(userId)){
	    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
	    	}
		}
	}
	
	public static void leave(CoinRoomLeavePacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		int seatId = model.getSeatId(userId);
		logger.info("User({}) request leave room({}), seat({})", userId, model.getRoomId(), seatId);
		packet.error = model.leave(userId);
		if(packet.error == 1){
			packet.errDesc = "玩完本局退出";
		}
		PacketManager.send(userId, packet);
		
		logger.info("User({}) leave coin room({}) status:{}.", userId, model.getRoomId(), packet.error);
		
		if(seatId >0 && packet.error == 0){
			notifyLeave(model, seatId);
		}
	}
	
	public static void onlineAgain(int userId){
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.onlineAgain(userId);
	}
	
	public static void offline(int userId){
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		int seatId = model.getSeatId(userId);
		logger.info("User({}) offline leave room({}), seat({})", userId, model.getRoomId(), seatId);
		int error = model.leave(userId);
		if(seatId >0 && error == 0){
			notifyLeave(model, seatId);
		}
	}
	
	public static void leaveOnOver(CoinRoomLeaveOnOverPacket packet){
		int userId = packet.userId;
		CoinRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.leaveOnOver(userId);
		logger.info("User({}) request leave coin room({}) when game over", userId, model.getRoomId());
		packet.error = 0;
		PacketManager.send(userId, packet);
	}
	
	public static void notifySelfLeave(int userId){
		CoinRoomLeavePacket packet = new CoinRoomLeavePacket();
		packet.error = 0;
		PacketManager.send(userId, packet);
	}
	
	public static void notifyLeave(CoinRoomModel model, int seatId){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.COIN_ROOM_LEAVE_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	leave_notify.Builder leaveNotify = leave_notify.newBuilder();
    	leaveNotify.setSeatId(seatId);
    	head.setBody(leaveNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
}
