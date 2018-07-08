package game.roulette.model;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.RouletteRoom;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.roulette.ConfirmBetPacket;
import game.roulette.GetRoomPacket;
import game.roulette.JoinRoulettePacket;
import game.roulette.LeavePacket;
import game.roulette.ResultPacket;
import game.roulette.RouletteGetUserPacket;
import game.roulette.RouletteSilentUserPacket;
import game.roulette.StagePacket;
import game.roulette.util.ConvertUtil;
import game.user.Users;
import netty.GameModels.UserMgr;
import netty.util.MapUtil;
import netty.util.StringUtil;
import protocols.header;
import protocols.roulette.common.bet;
import protocols.roulette.confirmbet.bet_notify;
import protocols.roulette.silentuser.silent_notify;
import protocols.roulette.stage.stage_notify;

public class RouletteRoomService {
	public static final int EUROPE_ROULETTE = 37;
	public static final int AMERICAN_ROULETTE = 38;
	public static final int FRANCE_ROULETTE = 25;
	
	private static Logger logger = LoggerFactory.getLogger(RouletteRoomService.class);
	// Map<roomId, roomModel>
	private static Map<String, RouletteRoomModel> allRouRoom = new ConcurrentHashMap<>();
	// Map<type, roomList>
    private static Map<Integer, List<RouletteRoom>> typedRooms = new ConcurrentHashMap<>();
	public static void init() {
		typedRooms.clear();
		for(RouletteRoom config :RouletteRoom.getAllObjects()){
			if(config.DELETED){
				continue;
			}
			getTypeRooms(config.GAME_TYPE).add(config);
			RouletteRoomModel model = allRouRoom.get(config.ROOM_ID);
			if(model == null){
				allRouRoom.put(config.ROOM_ID, new RouletteRoomModel(config));
			} else {
				model.setConfig(config);//for refresh
			}
		}
	}
	
	private static List<RouletteRoom> getTypeRooms(Integer type){
    	List<RouletteRoom> typeRooms = typedRooms.get(type);
		if(typeRooms == null){
			typeRooms = new Vector<RouletteRoom>();
			typedRooms.put(type, typeRooms);
		}
		return typeRooms;
    }
	// Map<userId, roomModel>
	private static Map<Integer, RouletteRoomModel> userRoom = new ConcurrentHashMap<Integer, RouletteRoomModel>();
	
	private static RouletteRoomModel getGamingRoom(int userId){
		for(RouletteRoomModel model : allRouRoom.values()){
			if(model.isInRoom(userId)){
				return model;
			}
		}
		return null;
	}
	
	// Map<type, Map<userId, position>>
    private static Map<Integer, Map<Integer, Integer>> userPos  = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> getPosMap(Integer type){
    	Map<Integer, Integer> posMap = userPos.get(type);
		if(posMap == null){
			posMap = new ConcurrentHashMap<Integer, Integer>();
			userPos.put(type, posMap);
		}
		return posMap;
    }
	
	public static void getRoom(GetRoomPacket packet){
		List<RouletteRoom> typeRooms = getTypeRooms(packet.type);
		if(typeRooms.isEmpty()){
			packet.error = 1;
			packet.errDesc = "没有此类型轮盘房间";
			logger.info("User({}) get roulette room failed because no such type({}) room.", packet.userId, packet.type);
			PacketManager.send(packet.getSession(), packet);
			return;
		}
		Map<Integer, Integer> posMap = getPosMap(packet.type);
		Integer pos = posMap.get(packet.userId);
		int index;
		if(pos == null){
			// 第一次
			index = new Random().nextInt(typeRooms.size());
		} else {
			if(StringUtil.isEmpty(packet.direction)){
				packet.error = 0;
				packet.config = typeRooms.get(pos);
				PacketManager.send(packet.getSession(), packet);
				return;
			}
			int size = typeRooms.size();
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
		posMap.put(packet.userId, index);
		packet.error = 0;
		packet.config = typeRooms.get(index);
		PacketManager.send(packet.getSession(), packet);
		logger.info("User({}) get roulette room({}) success.", packet.userId, packet.config.ROOM_ID);
		return;
	}
	
	public static void join(JoinRoulettePacket packet){
		int userId = packet.userId;
		RouletteRoomModel model = allRouRoom.get(packet.roomId);
		if(model == null){
			packet.error = 1;//房间号不存在
			packet.errDesc = "房间不存在或已销毁";
			logger.info("User({}) join the non exist Roulette Room({})", userId, packet.roomId);
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
		RouletteRoomModel gamingRoom = getGamingRoom(userId);
		if(gamingRoom != null){
			packet.error = 3;
			packet.errDesc = "你有轮盘正在进行，将加入原来的"+gamingRoom.getRoomId()+"房间";
			model = gamingRoom;
		} else {
			packet.error = 0;
			model.join(userId);
		}
		packet.model = model;
		userRoom.put(userId, model);
		PacketManager.send(userId, packet);
	}
	
	public static void bet(ConfirmBetPacket packet){
		int userId = packet.userId;
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		boolean betFailed = false;
    	for(Map.Entry<String, Integer> entry : MapUtil.sumMap(packet.mybet, model.getUserBet(userId)).entrySet()){
    		String target = entry.getKey();
    		int coins = entry.getValue();
    		if(coins < model.getConfig().MIN_BET){
    			packet.error = 4;
    			packet.errDesc = "下注目标("+target+")低于本房间最小下注("+model.getConfig().MIN_BET+")";
    			betFailed = true;
    			break;
    		}
    		if(coins > model.getConfig().MAX_BET){
    			packet.error = 5;
    			packet.errDesc = "下注目标("+target+")超过本房间最大下注("+model.getConfig().MAX_BET+")";
    			betFailed = true;
    			break;
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
		if(!RouletteRoomModel.BET_STAGE.equals(stage)){
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
    	head.setCommand(PacketTypes.ROU_BET_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	bet_notify.Builder betNotify = bet_notify.newBuilder();
    	betNotify.addAllBets(packet.sumBet);
    	Map<String, Integer> totalBet = model.getTotalBet();
		for(Map.Entry<String, Integer> entry : totalBet.entrySet()){
			bet.Builder b = bet.newBuilder();
			b.setTarget(entry.getKey());
			b.setCoins(entry.getValue());
			betNotify.addTotalBets(b);
		}
    	head.setBody(betNotify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUserExcept(userId)){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void tellStage(StagePacket packet){
		int userId = packet.userId;
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		logger.info("User({}) request get stage in room({})", userId, model.getRoomId());
		packet.error = 0;
		packet.currStage = model.getStage();
		packet.countDown = model.getCountDown();
		PacketManager.send(userId, packet);//单个用户查询stage
	}
	
	public static void notifyStage(RouletteRoomModel model){
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.ROU_STAGE_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	stage_notify.Builder stageNotify = stage_notify.newBuilder();
    	stageNotify.setCurrStage(model.getStage());
    	stageNotify.setCountDown(model.getCountDown());
    	stageNotify.setResult(model.getResult());
    	head.setBody(stageNotify.buildPartial().toByteString());
		for(int userId : model.getRoomUser()){
			PacketManager.send(userId, head.buildPartial().toByteArray());
		}
    	logger.info("Room({}) stage changed, new stage is {}", model.getRoomId(), model.getStage());
	}
	
	public static void tellResult(ResultPacket packet){
		tellResult(packet.userId);
	}
	public static void tellResult(int userId){
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		ResultPacket packet = new ResultPacket();
		packet.userId = userId;
		packet.error = 0;
		packet.model = model;
		PacketManager.send(userId, packet);
	}
	
	
	public static void leave(LeavePacket packet){
		int userId = packet.userId;
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		if(!model.leave(userId)){
			packet.error = 1;
			packet.errDesc = "本局你有下注，将在牌局结束离开";
			PacketManager.send(userId, packet);
			return;
		}
		notifySelfLeave(userId);
	}
	
	public static void offline(int userId){
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.offline(userId);
	}
	
	public static void onlineAgain(int userId){
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.onlineAgain(userId);
	}
	
	public static void notifySelfLeave(int userId){
		LeavePacket packet = new LeavePacket();
		packet.error = 0;
		packet.errDesc = "";
		PacketManager.send(userId, packet);
	}
	
	public static void getUser(RouletteGetUserPacket packet){
		int userId = packet.userId;
		RouletteRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		packet.model = model;
		PacketManager.send(userId, packet);
	}
	
	public static void silentUser(RouletteSilentUserPacket packet){
		int userId = packet.userId;
		RouletteRoomModel model = userRoom.get(userId);
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
    	head.setCommand(PacketTypes.ROU_SILENT_USER_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	silent_notify.Builder notify = silent_notify.newBuilder();
		notify.setSilentUser(ConvertUtil.toUserInfo(packet.silentUser, model));
    	head.setBody(notify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
}
