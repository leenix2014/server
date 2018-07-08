package game.baccarat.model;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.BaccaratRoom;
import com.mozat.morange.dbcache.tables.TUsers;

import game.baccarat.BaccaratBetPacket;
import game.baccarat.BaccaratGetRoomPacket;
import game.baccarat.BaccaratGetUserPacket;
import game.baccarat.BaccaratJoinPacket;
import game.baccarat.BaccaratLastBetPacket;
import game.baccarat.BaccaratLeavePacket;
import game.baccarat.BaccaratResultPacket;
import game.baccarat.BaccaratSilentUserPacket;
import game.baccarat.BaccaratStagePacket;
import game.baccarat.util.ConvertUtil;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.user.Users;
import netty.GameModels.UserMgr;
import netty.util.MapUtil;
import netty.util.StringUtil;
import protocols.header;
import protocols.baccarat.confirmbet.bet_notify;
import protocols.baccarat.joinroom.join_notify;
import protocols.baccarat.leave.leave_notify;
import protocols.baccarat.silentuser.silent_notify;
import protocols.baccarat.stage.stage_notify;


public class BaccaratRoomService {
	private static Logger logger = LoggerFactory.getLogger(BaccaratRoomService.class);
	// Map<roomId, roomModel>
	private static Map<String, BaccaratRoomModel> allBaccRoom = new ConcurrentHashMap<>();
	private static List<BaccaratRoom> rooms = new Vector<>();
	
	public static void init() {
		rooms.clear();
		for(BaccaratRoom config :BaccaratRoom.getAllObjects()){
			if(config.DELETED){
				continue;
			}
			rooms.add(config);
			BaccaratRoomModel model = allBaccRoom.get(config.ROOM_ID);
			if(model == null){
				allBaccRoom.put(config.ROOM_ID, new BaccaratRoomModel(config));
			} else {
				model.setConfig(config);//for refresh
			}
		}
	}
	
	// Map<userId, roomModel>
	private static Map<Integer, BaccaratRoomModel> userRoom = new ConcurrentHashMap<Integer, BaccaratRoomModel>();
	
	private static BaccaratRoomModel getGamingRoom(int userId){
		for(BaccaratRoomModel model : allBaccRoom.values()){
			if(model.isInRoom(userId)){
				return model;
			}
		}
		return null;
	}
	
	// Map<userId, position>
    private static Map<Integer, Integer> userPos  = new ConcurrentHashMap<>();
	
	public static void getRoom(BaccaratGetRoomPacket packet){
		if(rooms.isEmpty()){
			packet.error = 1;
			packet.errDesc = "暂无百家乐房间，请联系运营商配置";
			logger.info("User({}) at room failed because no baccarat room.", packet.userId);
			PacketManager.send(packet.getSession(), packet);
			return;
		}
		Integer pos = userPos.get(packet.userId);
		int index;
		if(pos == null){
			// 第一次
			index = new Random().nextInt(rooms.size());
		} else {
			if(StringUtil.isEmpty(packet.direction)){
				packet.error = 0;
				packet.model = allBaccRoom.get(rooms.get(pos).ROOM_ID);;
				PacketManager.send(packet.getSession(), packet);
				return;
			}
			int size = rooms.size();
			index = pos.intValue();
			if("up".equals(packet.direction)){
				index++;
			}else {
				index--;
			}
			if(index < 0){
				index = size - 1;
			}
			if(index >= size){
				index = 0;
			}
		}
		userPos.put(packet.userId, index);
		BaccaratRoom room = rooms.get(index);
		packet.error = 0;
		BaccaratRoomModel model = allBaccRoom.get(room.ROOM_ID);
		if(model != null){
			packet.model = model;
		}
		PacketManager.send(packet.getSession(), packet);
		logger.info("User({}) get baccarat room({}) success.", packet.userId, model.getConfig().ROOM_ID);
		return;
	}
	
	public static void join(BaccaratJoinPacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = allBaccRoom.get(packet.roomId);
		if(model == null){
			packet.error = 1;//房间号不存在
			packet.errDesc = "房间不存在或已销毁";
			logger.info("User({}) join the non exist Baccarat Room({})", userId, packet.roomId);
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
		BaccaratRoomModel gamingRoom = getGamingRoom(userId);
		if(gamingRoom != null){
			packet.error = 3;
			packet.errDesc = "你有百家乐正在进行，将加入原来的"+gamingRoom.getRoomId()+"房间";
			model = gamingRoom;
		} else {
			packet.error = 0;
			model.join(userId);
		}
		packet.model = model;
		userRoom.put(userId, model);
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.BACC_JOIN_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	join_notify.Builder joinNotify = join_notify.newBuilder();
		joinNotify.setJoinUser(ConvertUtil.toUserInfo(userId, model));
		joinNotify.setPlayerCount(model.getPlayerCount());
    	head.setBody(joinNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void bet(BaccaratBetPacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		boolean betFailed = false;
    	for(Map.Entry<String, Integer> entry : MapUtil.sumMap(packet.mybet, model.getUserBet(userId)).entrySet()){
    		String target = entry.getKey();
    		int coins = entry.getValue();
    		if("tie".equals(target) || "banker_pair".equals(target) || "player_pair".equals(target)){
    			if(coins < model.getConfig().TIE_PAIR_MIN_BET){
	    			packet.error = 6;
	    			packet.errDesc = "下注目标("+ConvertUtil.translate(target)+")低于本房间和对子下限("+model.getConfig().TIE_PAIR_MIN_BET+")";
	    			betFailed = true;
	    			break;
	    		}
	    		if(coins > model.getConfig().TIE_PAIR_MAX_BET){
	    			packet.error = 7;
	    			packet.errDesc = "下注目标("+ConvertUtil.translate(target)+")超过本房间和对子上限("+model.getConfig().TIE_PAIR_MAX_BET+")";
	    			betFailed = true;
	    			break;
	    		}
    		} else {
	    		if(coins < model.getConfig().MIN_BET){
	    			packet.error = 4;
	    			packet.errDesc = "下注目标("+ConvertUtil.translate(target)+")低于本房间最小下注("+model.getConfig().MIN_BET+")";
	    			betFailed = true;
	    			break;
	    		}
	    		if(coins > model.getConfig().MAX_BET){
	    			packet.error = 5;
	    			packet.errDesc = "下注目标("+ConvertUtil.translate(target)+")超过本房间最大下注("+model.getConfig().MAX_BET+")";
	    			betFailed = true;
	    			break;
	    		}
    		}
    	}
    	if(betFailed){
    		PacketManager.send(packet.getSession(), packet);
    		return;
    	}
		int userCoin = UserMgr.getInstance().getUserCoin(userId);
		int totalCoin = 0;
		for(Map.Entry<String, Integer> userBet : packet.mybet.entrySet()){
			totalCoin += userBet.getValue();
		}
		
		if(userCoin < totalCoin){
			packet.error = 1;
			packet.errDesc = "没有足够的金币下注";
			PacketManager.send(packet.getSession(), packet);
			logger.info("User({}) bet failed: no enough coin!", userId);
			return;
		}
		String stage = model.getStage();
		if(!BaccaratRoomModel.BET_STAGE.equals(stage)){
			packet.error = 2;
			packet.errDesc = "目前不是下注阶段";
			PacketManager.send(packet.getSession(), packet);
			logger.info("User({}) bet failed because not in betting time!", userId);
			return;
		}
		if(totalCoin == 0){
			packet.error = 3;
			packet.errDesc = "上次下注已成功，本次无下注";
			PacketManager.send(packet.getSession(), packet);
			logger.info("User({}) bet failed: no bet!", userId);
			return;
		}
    	model.addUserBet(userId, packet.mybet);
		packet.error = 0;
		packet.model = model;
		PacketManager.send(packet.getSession(), packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.BACC_BET_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	bet_notify.Builder betNotify = bet_notify.newBuilder();
    	betNotify.setBettor(ConvertUtil.toUserInfo(userId, model));
    	betNotify.addAllBets(packet.sumBet);
    	head.setBody(betNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void tellStage(BaccaratStagePacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) request get stage in room({})", userId, model.getRoomId());
		packet.error = 0;
		packet.currStage = model.getStage();
		packet.countDown = model.getCountDown();
		PacketManager.send(userId, packet);//单个用户查询stage
	}
	
	public static void notifyStage(BaccaratRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.BACC_STAGE_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	stage_notify.Builder stageNotify = stage_notify.newBuilder();
    	stageNotify.setCurrStage(model.getStage());
    	stageNotify.setCountDown(model.getCountDown());
    	head.setBody(stageNotify.buildPartial().toByteString());
		for(int userId : model.getRoomUser()){
			PacketManager.send(userId, head.buildPartial().toByteArray());
		}
    	logger.info("Room({}) stage changed, new stage is {}", model.getRoomId(), model.getStage());
	}
	
	// Reserved
	public static void tellResult(BaccaratResultPacket packet){
		tellResult(packet.userId);
	}
	public static void tellResult(int userId){
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		BaccaratResultPacket packet = new BaccaratResultPacket();
		packet.userId = userId;
		packet.error = 0;
		packet.model = model;
		PacketManager.send(userId, packet);
	}
	
	
	public static void leave(BaccaratLeavePacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			packet.error = 99;
			packet.errDesc = "请先加入房间";
			PacketManager.send(userId, packet);
			return;
		}
		int seatId = model.getSeatId(userId);
		if(!model.leave(userId)){
			packet.error = 1;
			packet.errDesc = "本局你有下注，将在牌局结束离开";
			PacketManager.send(userId, packet);
			return;
		}
		notifySelfLeave(userId);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.BACC_LEAVE_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	leave_notify.Builder leaveNotify = leave_notify.newBuilder();
		leaveNotify.setLeaveUser(ConvertUtil.toUserInfo(userId, model).setSeatId(seatId));
		leaveNotify.setPlayerCount(model.getPlayerCount());
    	head.setBody(leaveNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void offline(int userId){
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.offline(userId);
	}
	
	public static void onlineAgain(int userId){
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.onlineAgain(userId);
	}
	
	public static void notifySelfLeave(int userId){
		BaccaratLeavePacket packet = new BaccaratLeavePacket();
		packet.error = 0;
		packet.errDesc = "";
		PacketManager.send(userId, packet);
	}
	
	public static void getUser(BaccaratGetUserPacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		packet.model = model;
		PacketManager.send(userId, packet);
	}
	
	public static void silentUser(BaccaratSilentUserPacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			packet.error = 99;
			packet.errDesc = "请先加入房间";
			PacketManager.send(userId, packet);
			return;
		}
		if(!model.isAdmin(userId)){
			packet.error = 1;
			packet.errDesc = "你不是本房间管理员，无权禁言他人";
			PacketManager.send(userId, packet);
			return;
		}
		if(packet.silent){
			model.silent(packet.silentUser);
		} else {
			model.unsilent(packet.silentUser);
		}
		PacketManager.send(userId, packet);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.BACC_SILENT_USER_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	silent_notify.Builder notify = silent_notify.newBuilder();
		notify.setSilentUser(ConvertUtil.toUserInfo(packet.silentUser, model));
    	head.setBody(notify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void lastbet(BaccaratLastBetPacket packet){
		int userId = packet.userId;
		BaccaratRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		packet.error = 0;
		packet.model = model;
		PacketManager.send(packet.getSession(), packet);
	}
}
