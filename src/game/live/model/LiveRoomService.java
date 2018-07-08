package game.live.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.Anchor;
import com.mozat.morange.dbcache.tables.AnchorBill;
import com.mozat.morange.dbcache.tables.AnchorMachine;
import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.LiveGift;
import com.mozat.morange.dbcache.tables.LiveRoom;
import com.mozat.morange.dbcache.tables.LiveSendGift;
import com.mozat.morange.dbcache.tables.LiveSendMsg;
import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.util.MD5;

import game.common.CommonConfig;
import game.live.BetFruitPacket;
import game.live.BetTigerPacket;
import game.live.EndLiveRoomPacket;
import game.live.ExitLiveRoomPacket;
import game.live.GetLiveRoomPacket;
import game.live.NewLiveRoomPacket;
import game.live.PayLiveRoomPacket;
import game.live.SendGiftPacket;
import game.live.SendMsgPacket;
import game.live.ShowLovePacket;
import game.live.qingmeng.QingMengService;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.roulette.RouletteGetUserPacket;
import game.roulette.RouletteSilentUserPacket;
import game.user.Users;
import net.sf.json.JSONObject;
import netty.GameModels.UserMgr;
import netty.util.StringUtil;
import netty.util.WeightRandom;
import protocols.header;
import protocols.live.betfruit.fruit_notify;
import protocols.live.bettiger.tiger_notify;
import protocols.live.common.live_room;
import protocols.live.common.user_info;
import protocols.live.endroom.anchorexit;
import protocols.live.payroom.join_notify;
import protocols.live.sendgift.gift_notify;
import protocols.live.sendmsg.msg_notify;
import protocols.live.showlove.showlove_notify;
import protocols.roulette.silentuser.silent_notify;

public class LiveRoomService {
	
	private static Logger logger = LoggerFactory.getLogger(LiveRoomService.class);
	// Map<roomId, roomModel>
	private static Map<Integer, LiveRoomModel> allLiveRoom = new ConcurrentHashMap<>();
	// Map<type, roomList>
    private static Map<String, List<LiveRoom>> typedRooms = new ConcurrentHashMap<>();
	public static void init() {
		typedRooms.clear();
		List<LiveRoom> dbRooms = LiveRoom.getManyByCriteria(LiveRoom.AttrSTATUS.eq(0));
		for(LiveRoom config : dbRooms){
			getTypeRooms(config.ANCHOR_TYPE).add(config);
			LiveRoomModel model = allLiveRoom.get(config.ID);
			if(model == null){
				allLiveRoom.put(config.ID, new LiveRoomModel(config));
			} else {
				model.setConfig(config);//for refresh
			}
		}
	}
	
	public static LiveRoom getAnchorRoom(int anchorId){
		for(List<LiveRoom> typeRooms : typedRooms.values()){
	    	for(LiveRoom room : typeRooms){
				if(room.ANCHOR == anchorId){
					return room;
				}
			}
		}
    	return null;
    }
	
	public static int getRoomAnchor(int roomId){
		for(List<LiveRoom> typeRooms : typedRooms.values()){
    		for(LiveRoom room : typeRooms){
    			if(room.ID == roomId){
    				return room.ANCHOR;
    			}
    		}
		}
		return -1;
    }
	
	public static void newLiveRoom(NewLiveRoomPacket packet){
		live_room room = packet.room;
		String channel = room.getChannel();
		int anchorId = packet.userId;
		if(room == null || StringUtil.isEmpty(room.getChannel())){
			packet.error = 1;
			packet.errDesc = "声网频道不能为空";
			PacketManager.send(packet.userId, packet);
			logger.info("live room channel is invalid");
			return;
		}
		if(isChannelUsed(channel)){
			packet.error = 2;
			packet.errDesc = "当前频道已被其他主播使用";
			PacketManager.send(anchorId, packet);
			logger.info("create live room failed because channel in use! room cofig:"+room.toString());
			return;
		}
		Anchor anchor = Anchor.getOne(anchorId);
		LiveRoom liveRoom = LiveRoom.create(LiveRoom.AttrCHANNEL.set(room.getChannel()),
				LiveRoom.AttrANCHOR.set(anchorId),
				LiveRoom.AttrANCHOR_NAME.set(anchor==null?"":anchor.ANCHOR_NAME),
				LiveRoom.AttrANCHOR_TYPE.set(anchor==null?"":anchor.ANCHOR_TYPE),
				LiveRoom.AttrTITLE.set(room.getTitle()),
				LiveRoom.AttrCOST.set(room.getCost()),
				LiveRoom.AttrSTATUS.set(0),
				LiveRoom.AttrONLINE_COUNT.set(0),
				LiveRoom.AttrCREATE_TIME.set(new Date()));
		if(liveRoom == null){
			packet.error = 3;
			packet.errDesc = "房间创建失败";
			PacketManager.send(anchorId, packet);
			logger.info("create live room failed! room cofig:"+room.toString());
			return;
		}
		packet.error = 0;
		packet.roomId = liveRoom.ID;
		addLiveRoom(liveRoom);
		LiveRoomModel model = allLiveRoom.get(packet.roomId);
		model.join(anchorId);
		PacketManager.send(anchorId, packet);
		logger.info("create live room success! room cofig:"+room.toString());
	}
	
	private static boolean isChannelUsed(String channel){
		for(List<LiveRoom> typeRooms : typedRooms.values()){
	    	for(LiveRoom room : typeRooms){
				if(room.CHANNEL == channel){
					return true;
				}
			}
		}
    	return false;
    }
	
	public static void addLiveRoom(LiveRoom room){
		List<LiveRoom> typeRooms = getTypeRooms(room.ANCHOR_TYPE);
		// 房间号重复则无需添加
		Iterator<LiveRoom> iter = typeRooms.iterator();
		while(iter.hasNext()){
			LiveRoom ancRoom = iter.next();
			if(ancRoom.ID == room.ID){
				return;
			}
		}
		typeRooms.add(room);
		LiveRoomModel model = new LiveRoomModel(room);
		userRoom.put(room.ANCHOR, model);
		allLiveRoom.put(room.ID, model);
    }
	
	public static void endLiveRoom(EndLiveRoomPacket packet){
		int userId = packet.userId;
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			packet.error = 99;
			packet.errDesc = "请先加入房间";
			PacketManager.send(userId, packet);
			return;
		}
		anchorExit(packet.userId);
	}
	
	public static void anchorExit(int anchorId){
		LiveRoom room = getAnchorRoom(anchorId);
    	if(room == null){
    		EndLiveRoomPacket res = new EndLiveRoomPacket();
			res.error = 1;
			res.errDesc = "主播已退出房间，请勿重复退出";
			PacketManager.send(anchorId, res);
			logger.info("Anchor("+anchorId+") not in live room");
			return;
		}
    	anchorexit.Builder msg = anchorexit.newBuilder();
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.ANCHOR_EXIT);
    	head.setVersion(1);
    	head.setSubversion(0);
    	head.setBody(msg.buildPartial().toByteString());
    	//广播通知主播退出
    	LiveRoomModel model = userRoom.get(anchorId);
		for(int user : model.getRoomUser()){
			if(user == anchorId){
				continue;
			}
			PacketManager.send(user, head.buildPartial().toByteArray());
		}
		EndLiveRoomPacket res = new EndLiveRoomPacket();
		res.error = 0;
		if(model.stop()){
			List<LiveRoom> typeRooms = getTypeRooms(room.ANCHOR_TYPE);
			typeRooms.remove(room);
			logger.info("Anchor("+room.ANCHOR+") end room("+room.ID+") success!");
		}
		PacketManager.send(anchorId, res);
	}
	
	private static List<LiveRoom> getTypeRooms(String type){
    	List<LiveRoom> typeRooms = typedRooms.get(type);
		if(typeRooms == null){
			typeRooms = new Vector<LiveRoom>();
			typedRooms.put(type, typeRooms);
		}
		return typeRooms;
    }
	// Map<userId, roomModel>
	private static Map<Integer, LiveRoomModel> userRoom = new ConcurrentHashMap<Integer, LiveRoomModel>();
	
	// Map<type, Map<userId, position>>
    private static Map<String, Map<Integer, Integer>> userPos  = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> getPosMap(String type){
    	Map<Integer, Integer> posMap = userPos.get(type);
		if(posMap == null){
			posMap = new ConcurrentHashMap<Integer, Integer>();
			userPos.put(type, posMap);
		}
		return posMap;
    }
	
	public static void getRoom(GetLiveRoomPacket packet){
		List<LiveRoom> typeRooms = getTypeRooms(packet.type);
		if(typeRooms.isEmpty()){
			packet.error = 1;
			packet.errDesc = "没有直播中的房间";
			logger.info("User({}) get live room failed because no such type({}) room.", packet.userId, packet.type);
			PacketManager.send(packet.getSession(), packet);
			return;
		}
		Map<Integer, Integer> posMap = getPosMap(packet.type);
		Integer pos = posMap.get(packet.userId);
		int index;
		if(pos == null){
			// 第一次
			index = new Random().nextInt(typeRooms.size());
		} else if(StringUtil.isEmpty(packet.direction) || "first".equals(packet.direction)) {
			index = pos;
		} else {
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
		packet.model = allLiveRoom.get(typeRooms.get(index).ID);
		PacketManager.send(packet.getSession(), packet);
		logger.info("User({}) get live room success:{}", packet.userId, packet.model.getConfig().toString());
		return;
	}
	
	public static void join(PayLiveRoomPacket packet){
		int userId = packet.userId;
		int roomId = packet.roomId;
		LiveRoomModel model = allLiveRoom.get(roomId);
		if(model == null){
			packet.error = 1;//房间号不存在
			packet.errDesc = "房间号不存在";
			logger.info("User({}) pay room failed, because no such room({})", userId, roomId);
			PacketManager.send(userId, packet);
			return;
		}
		LiveRoom room = model.getConfig();
		if(room.STATUS != 0){
			packet.error = 2;
			packet.errDesc = "房间已停播";
			PacketManager.send(userId, packet);
			logger.info("User({}) pay room failed, because room already finished. roomId:{}", userId, roomId);
			return;
		}
		if(model.isPaid(userId)){
			packet.error = 0;
			packet.model = model;
			//model.join(userId);
			userRoom.put(userId, model);
			PacketManager.send(userId, packet);
			logger.info("User({}) no need to pay room, because already paied.", userId);
			return;
		}
		if(room.ENCRYPTED && !StringUtil.nonNull(packet.pwd).equals(MD5.getHashString(room.PWD))){
			packet.error = 5;//密码不正确
			packet.errDesc = "房间密码错误";
			logger.info("User({}) join the encrypted live room({}) and password error!", userId, room.ID);
			PacketManager.send(userId, packet);
			return;
		}
		int cost = room.COST;
		int oldCoin = UserMgr.getInstance().getUserCoin(userId);
		if(oldCoin < cost){
			packet.error = 3;
			packet.errDesc = "金币不足";
			PacketManager.send(userId, packet);
			logger.info("User({}) pay room failed, because no enough coin. roomId:{},curr:{},cost:{}", userId, roomId, oldCoin, cost);
			return;
		}
		if(cost > 0){
			Date now = new Date();
			String userName = UserMgr.getInstance().getUserName(userId);
			int nowCoin = oldCoin - cost;
			UserMgr.getInstance().setUserCoin(userId, nowCoin);
			CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
					CoinBill.AttrUSER_NAME.set(userName),
					CoinBill.AttrSOURCE.set("live"),
					CoinBill.AttrSOURCE_ID.set(roomId+""),
					CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")消费"),
					CoinBill.AttrAMOUNT.set(-cost),
					CoinBill.AttrBEFORE_BAL.set(oldCoin),
					CoinBill.AttrAFTER_BAL.set(nowCoin),
					CoinBill.AttrCREATE_TIME.set(now));
			Anchor anchor = model.getAnchor();
			AnchorBill.create(AnchorBill.AttrAMOUNT.set(cost),
					AnchorBill.AttrANCHOR_ID.set(room.ANCHOR),
					AnchorBill.AttrANCHOR_NAME.set(anchor.ANCHOR_NAME),
					AnchorBill.AttrANCHOR_TYPE.set(anchor.ANCHOR_TYPE),
					AnchorBill.AttrBILL_TYPE.set("enter"),
					AnchorBill.AttrCHANNEL.set(room.CHANNEL),
					AnchorBill.AttrRECORD_TIME.set(now),
					AnchorBill.AttrROOM_ID.set(room.ID),
					AnchorBill.AttrUSER_ID.set(userId),
					AnchorBill.AttrUSER_NAME.set(userName));
		}
		packet.error = 0;
		packet.model = model;
		model.join(userId);
		model.addOnlineCount();
		PacketManager.send(userId, packet);
		logger.info("User({}) user pay room success.roomId:{}", userId, roomId);
		
		userRoom.put(userId, model);
		
		//房间内广播通知用户进场
		TUsers user =  Users.load(userId);
		if(user == null){
			logger.error("Can't find User({})", userId);
			return;
		}
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setRank(user.RANK);
		userInfo.setRankName("");
		userInfo.setUserId(userId);
		userInfo.setUserName(user.NAME);
		
		join_notify.Builder notify = join_notify.newBuilder();
		notify.setJoinUser(userInfo.build());
		notify.setOnlineCount(model.getOnlineCount());
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.JOIN_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	head.setBody(notify.buildPartial().toByteString());
		
		for(Integer other : model.getRoomUser()){
			PacketManager.send(other, head.buildPartial().toByteArray());
		}
	}
	
	public static void offline(int userId){
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.offline(userId);
	}
	
	public static void onlineAgain(int userId){
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		model.onlineAgain(userId);
	}
	
	public static void getUser(RouletteGetUserPacket packet){
		int userId = packet.userId;
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
//		packet.model = model;
		PacketManager.send(userId, packet);
	}
	
	public static void silentUser(RouletteSilentUserPacket packet){
		int userId = packet.userId;
		LiveRoomModel model = userRoom.get(userId);
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
//		notify.setSilentUser(ConvertUtil.toUserInfo(packet.silentUser, model));
    	head.setBody(notify.buildPartial().toByteString());
    	for(Integer roomUser : model.getRoomUser()){
    		PacketManager.send(roomUser, head.buildPartial().toByteArray());
    	}
	}
	
	public static void sendMsg(SendMsgPacket packet){
		int userId = packet.userId;
		int roomId = packet.roomId;
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			packet.error = 2;
			packet.errDesc = "房间不存在或已停播";
			logger.error("User({}) not in live room!", userId);
			PacketManager.send(userId, packet);
			return;
		}
		TUsers user =  Users.load(userId);
		if(user == null){
			packet.error = 1;
			packet.errDesc = "你已离线，请重新登录";
			logger.error("Can't find User({})", userId);
			PacketManager.send(userId, packet);
			return;
		}
		int cost = CommonConfig.getInt(CommonConfig.GLOBAL_MSG_COST, 10);
		int oldCoin = UserMgr.getInstance().getUserCoin(userId);
		if(packet.crossRoom && oldCoin < cost){
			packet.error = 3;
			packet.errDesc = "金币不足";
			PacketManager.send(userId, packet);
			logger.info("User({}) send global message failed, because no enough room card. roomId:{},curr:{},cost:{}", userId, packet.roomId, oldCoin, cost);
			return;
		}
		LiveRoom room = model.getConfig();
		Date now = new Date();
		String userName = UserMgr.getInstance().getUserName(userId);
		if(packet.crossRoom){
			int nowCoin = oldCoin - cost;
			UserMgr.getInstance().setUserCoin(userId, nowCoin);
			CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""), 
					CoinBill.AttrUSER_NAME.set(userName),
					CoinBill.AttrSOURCE.set("live"),
					CoinBill.AttrSOURCE_ID.set(roomId+""),
					CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")内发送全局消息"),
					CoinBill.AttrAMOUNT.set(-cost),
					CoinBill.AttrBEFORE_BAL.set(oldCoin),
					CoinBill.AttrAFTER_BAL.set(nowCoin),
					CoinBill.AttrCREATE_TIME.set(now));
		}
		model.addMsgCount();
		
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setRank(user.RANK);
		userInfo.setRankName("");
		userInfo.setUserId(userId);
		userInfo.setUserName(user.NAME);
		
		msg_notify.Builder notify = msg_notify.newBuilder();
		notify.setSender(userInfo.build());
		notify.setMsg(userName+":"+packet.msg);
		notify.setMsgCount(room.MSG_COUNT);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.MSG_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	head.setBody(notify.buildPartial().toByteString());
		
    	Set<Integer> users = packet.crossRoom?getPlatformUser():model.getRoomUser();
		for(Integer other : users){
			PacketManager.send(other, head.buildPartial().toByteArray());
		}
		packet.error = 0;
		PacketManager.send(userId, packet);
		LiveSendMsg.create(LiveSendMsg.AttrANCHOR.set(room.ANCHOR),
				LiveSendMsg.AttrANCHOR_NAME.set(room.ANCHOR_NAME),
				LiveSendMsg.AttrCOST.set(packet.crossRoom?cost:0),
				LiveSendMsg.AttrCREATE_TIME.set(now),
				LiveSendMsg.AttrCROSS_ROOM.set(packet.crossRoom),
				LiveSendMsg.AttrMSG.set(packet.msg),
				LiveSendMsg.AttrROOM_ID.set(packet.roomId),
				LiveSendMsg.AttrUSER_ID.set(userId),
				LiveSendMsg.AttrUSER_NAME.set(userName));
	}
	
	public static void sendGift(SendGiftPacket packet){
		int userId = packet.userId;
		int roomId = packet.roomId;
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			packet.error = 3;
			packet.errDesc = "你已离开直播间，请重新加入直播间";
			logger.error("User({}) not in live room!", userId);
			PacketManager.send(userId, packet);
			return;
		}
		LiveGift gift = LiveGiftMgr.getGift(packet.giftId);
		if(gift == null){
			packet.error = 1;
			packet.errDesc = "礼物不存在";
			logger.error("Can't find Gift("+packet.giftId+")");
			PacketManager.send(userId, packet);
			return;
		}
		TUsers user =  Users.load(userId);
		if(user == null){
			packet.error = 2;
			packet.errDesc = "你已离线，请重新登录";
			logger.error("Can't find User({})", userId);
			PacketManager.send(userId, packet);
			return;
		}
		if(packet.giftCount <= 0){
			packet.giftCount = 1;
		}
		int cost = gift.COST * packet.giftCount;
		int oldCoin = UserMgr.getInstance().getUserCoin(userId);
		if(oldCoin < cost){
			packet.error = 4;
			packet.errDesc = "金币不足";
			PacketManager.send(userId, packet);
			logger.info("User({}) send gift failed, because no enough coin. roomId:{},curr:{},cost:{}", userId, roomId, oldCoin, cost);
			return;
		}
		LiveRoom room = model.getConfig();
		Date now = new Date();
		String userName = UserMgr.getInstance().getUserName(userId);
		int nowCoin = oldCoin - cost;
		UserMgr.getInstance().setUserCoin(userId, nowCoin);
		CoinBill bill = CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
				CoinBill.AttrUSER_NAME.set(userName),
				CoinBill.AttrSOURCE.set("live"),
				CoinBill.AttrSOURCE_ID.set(roomId+""),
				CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")消费"),
				CoinBill.AttrAMOUNT.set(-cost),
				CoinBill.AttrBEFORE_BAL.set(oldCoin),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
		
		packet.anchor = model.getAnchor();
		if(packet.anchor != null){
			packet.anchor.HISTORY_CUBE = packet.anchor.HISTORY_CUBE + cost;
			packet.anchor.update();
			AnchorBill.create(AnchorBill.AttrAMOUNT.set(cost),
					AnchorBill.AttrANCHOR_ID.set(room.ANCHOR),
					AnchorBill.AttrANCHOR_NAME.set(packet.anchor.ANCHOR_NAME),
					AnchorBill.AttrANCHOR_TYPE.set(packet.anchor.ANCHOR_TYPE),
					AnchorBill.AttrBILL_TYPE.set("gift"),
					AnchorBill.AttrCHANNEL.set(room.CHANNEL),
					AnchorBill.AttrRECORD_TIME.set(now),
					AnchorBill.AttrROOM_ID.set(room.ID),
					AnchorBill.AttrUSER_ID.set(userId),
					AnchorBill.AttrUSER_NAME.set(userName));
		}
		
		WeightRandom<Integer> random = new WeightRandom<>();
		if(StringUtil.isNotEmpty(gift.GIFT_POSS)){
			JSONObject json = JSONObject.fromObject(gift.GIFT_POSS);
			Map<Integer, Integer> map = new HashMap<>();
			map.put(0, json.getInt("0"));
			map.put(1, json.getInt("1"));
			map.put(2, json.getInt("2"));
			map.put(5, json.getInt("5"));
			map.put(10, json.getInt("10"));
			map.put(20, json.getInt("20"));
			map.put(50, json.getInt("50"));
			map.put(100, json.getInt("100"));
			random.setWeightedObjects(map);
		} else {
			Map<Integer, Integer> bingoPoss = new HashMap<>();
			bingoPoss.put(0, 920);
			bingoPoss.put(1, 10);
			bingoPoss.put(2, 0);
			bingoPoss.put(5, 10);
			bingoPoss.put(10, 10);
			bingoPoss.put(20, 2);
			bingoPoss.put(50, 2);
			bingoPoss.put(100, 2);
			random.setWeightedObjects(bingoPoss);
		}
		
		int amount = 0;
		for(int i=1;i<=packet.giftCount;i++){
			Integer mul = random.next();
			if(mul == null){
				logger.error("live room send gift random init error!");
				continue;
			}
			amount += gift.COST * mul;
		}
		if(amount > 0){
			packet.bingo = true;
			packet.amount = amount;
			oldCoin = user.COIN_COUNT;
			nowCoin = oldCoin + amount;
			UserMgr.getInstance().setUserCoin(userId, nowCoin);
			CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""), 
					CoinBill.AttrUSER_NAME.set(user.NAME),
					CoinBill.AttrSOURCE.set("live"),
					CoinBill.AttrSOURCE_ID.set(roomId+""),
					CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")内发送礼物("+gift.GIFT_NAME+")并中奖"),
					CoinBill.AttrAMOUNT.set(amount),
					CoinBill.AttrBEFORE_BAL.set(oldCoin),
					CoinBill.AttrAFTER_BAL.set(nowCoin),
					CoinBill.AttrCREATE_TIME.set(now));
		}
		
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setRank(user.RANK);
		userInfo.setRankName("");
		userInfo.setUserId(userId);
		userInfo.setUserName(user.NAME);
		
		gift_notify.Builder notify = gift_notify.newBuilder();
		notify.setSender(userInfo.build());
		notify.setGiftCount(packet.giftCount);
		notify.setGiftId(packet.giftId);
		notify.setBingo(amount>=1000);
		notify.setAmount(amount);
		notify.setHistoryCube(packet.anchor.HISTORY_CUBE);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.GIFT_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	head.setBody(notify.buildPartial().toByteString());
		
    	Set<Integer> users = gift.CROSS_ROOM?getPlatformUser():model.getRoomUser();
		for(Integer other : users){
			PacketManager.send(other, head.buildPartial().toByteArray());
		}
		LiveSendGift savedGift = LiveSendGift.create(LiveSendGift.AttrANCHOR.set(room.ANCHOR),
				LiveSendGift.AttrANCHOR_NAME.set(room.ANCHOR_NAME),
				LiveSendGift.AttrCOST.set(gift.COST),
				LiveSendGift.AttrGIFT_COUNT.set(packet.giftCount),
				LiveSendGift.AttrCREATE_TIME.set(now),
				LiveSendGift.AttrCROSS_ROOM.set(gift.CROSS_ROOM),
				LiveSendGift.AttrGIFT_ID.set(packet.giftId),
				LiveSendGift.AttrGIFT_NAME.set(gift.GIFT_NAME),
				LiveSendGift.AttrROOM_ID.set(roomId),
				LiveSendGift.AttrSPURTED.set(false),
				LiveSendGift.AttrUSER_ID.set(userId),
				LiveSendGift.AttrUSER_NAME.set(userName));
		
		if(!gift.SPURT){
			packet.error = 0;
			packet.spurtStatus = -1;
			packet.errDesc = "无需喷气";
			PacketManager.send(userId, packet);
			return;
		}
		int anchorId = LiveRoomService.getRoomAnchor(roomId);
		if(anchorId == -1){
			packet.error = 0;
			packet.spurtStatus = 1;
			packet.errDesc = "主播已停播";
			PacketManager.send(userId, packet);
			logger.info("User({}) send gift success but can't spurt because can't find room anchor. roomId({})", userId, roomId);
			return;
		}
		List<AnchorMachine> macs = AnchorMachine.getManyByCriteria(AnchorMachine.AttrANCHOR_ID.eq(anchorId),AnchorMachine.AttrIS_ONLINE.eq("1"));
		if(macs == null || macs.isEmpty()){
			packet.error = 0;
			packet.spurtStatus = 2;
			packet.errDesc = "主播房间内没有在线设备";
			PacketManager.send(userId, packet);
			logger.info("User({}) send gift success but can't spurt because no machine online in room({}), anchorId:{}", userId, roomId, anchorId);
			return;
		}
		AnchorMachine mac = macs.get(0);
		String status = QingMengService.handle("1", anchorId, mac.MACHINE_MAC, StringUtil.toSize(bill.ID+"", 4, '0'));
		packet.error = 0;
		packet.spurtStatus = "1".equals(status)?0:2;
		packet.errDesc = "1".equals(status)?"":"喷气操作失败";
		PacketManager.send(userId, packet);
		if(savedGift != null){
			savedGift.SPURTED = "1".equals(status);
			savedGift.update();
		}
	}
	
	public static void showLove(ShowLovePacket packet){
		int userId = packet.userId;
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			return;
		}
		PacketManager.send(userId, packet);
		
		showlove_notify.Builder notify = showlove_notify.newBuilder();
		notify.setCount(packet.count);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.SHOW_LOVE_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	head.setBody(notify.buildPartial().toByteString());
		
		for(Integer other : model.getRoomUser()){
			PacketManager.send(other, head.buildPartial().toByteArray());
		}
	}
	
	public static void exitRoom(ExitLiveRoomPacket packet){
		int userId = packet.userId;
		LiveRoomModel model = userRoom.get(userId);
		if(model == null){
			PacketManager.send(userId, packet);
			return;
		}
		userRoom.remove(userId);
		PacketManager.send(userId, packet);
	}
	
	public static Set<Integer> getPlatformUser(){
		Set<Integer> users = new HashSet<Integer>();
		for(LiveRoomModel model : allLiveRoom.values()){
			users.addAll(model.getRoomUser());
		}
		return users;
    }
	
	public static void betTiger(BetTigerPacket packet){
		int userId = packet.userId;
		int roomId = packet.roomId;
		LiveRoomModel model = userRoom.get(userId);
		TUsers user = Users.load(userId);
		if(model == null){
			return;
		}
		int cost = packet.line * packet.bet;
		int oldCoin = user.COIN_COUNT;
		if(oldCoin < cost){
			packet.error = 3;
			packet.errDesc = "老虎机下注金币不足";
			PacketManager.send(userId, packet);
			logger.info("User({}) bet tiger failed, because no enough coin. roomId:{},curr:{},cost:{}", userId, roomId, oldCoin, cost);
			return;
		}
		LiveRoom room = model.getConfig();
		Date now = new Date();
		int nowCoin = oldCoin - cost;
		UserMgr.getInstance().setUserCoin(userId, nowCoin);
		CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
				CoinBill.AttrUSER_NAME.set(user.NAME),
				CoinBill.AttrSOURCE.set("live"),
				CoinBill.AttrSOURCE_ID.set(roomId+""),
				CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")下注老虎机"),
				CoinBill.AttrAMOUNT.set(-cost),
				CoinBill.AttrBEFORE_BAL.set(oldCoin),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
		packet.error = 0;
		Tiger tiger = model.getTiger(userId);
		packet.betRemain = nowCoin;
		tiger.bet(packet.line, packet.bet);
		PacketManager.send(userId, packet);
		
		// 老虎机开奖
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				tiger.openPrize();
				int reward = tiger.getTotalReward();
				if(reward > 0){
					TUsers user = Users.load(userId);
					int oldCoin = user.COIN_COUNT;
					int nowCoin = oldCoin + reward;
					user.COIN_COUNT = nowCoin;
					user.update();
					CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
							CoinBill.AttrUSER_NAME.set(user.NAME),
							CoinBill.AttrSOURCE.set("live"),
							CoinBill.AttrSOURCE_ID.set(roomId+""),
							CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")下注老虎机获奖"),
							CoinBill.AttrAMOUNT.set(reward),
							CoinBill.AttrBEFORE_BAL.set(oldCoin),
							CoinBill.AttrAFTER_BAL.set(nowCoin),
							CoinBill.AttrCREATE_TIME.set(new Date()));
				}
				tiger_notify.Builder notify = tiger_notify.newBuilder();
				notify.setTiger(tiger.toClient());
				notify.setTotalReward(tiger.getTotalReward());
				notify.setDetail(tiger.getDetail());
				notify.setCurrCoin(UserMgr.getInstance().getUserCoin(userId));
				header.packet.Builder head = header.packet.newBuilder();
		    	head.setCommand(PacketTypes.LIVE_TIGER_NOTIFY);
		    	head.setVersion(1);
		    	head.setSubversion(0);
		    	head.setBody(notify.buildPartial().toByteString());
				PacketManager.send(userId, head.buildPartial().toByteArray());
			}
		}, 2000);
	}
	
	public static void betFruit(BetFruitPacket packet){
		int userId = packet.userId;
		int roomId = packet.roomId;
		LiveRoomModel model = userRoom.get(userId);
		TUsers user = Users.load(userId);
		if(model == null){
			return;
		}
		int cost = packet.bet.getBar();
		cost += packet.bet.getSeven();
		cost += packet.bet.getStar();
		cost += packet.bet.getMelon();
		cost += packet.bet.getBell();
		cost += packet.bet.getLemon();
		cost += packet.bet.getOrange();
		cost += packet.bet.getApple();
		if(cost <= 0){
			packet.error = 2;
			packet.errDesc = "下注金额需大于0";
			PacketManager.send(userId, packet);
			return;
		}
		int oldCoin = user.COIN_COUNT;
		if(oldCoin < cost){
			packet.error = 3;
			packet.errDesc = "水果机下注金币不足";
			PacketManager.send(userId, packet);
			logger.info("User({}) bet fruit failed, because no enough coin. roomId:{},curr:{},cost:{}", userId, roomId, oldCoin, cost);
			return;
		}
		LiveRoom room = model.getConfig();
		Date now = new Date();
		int nowCoin = oldCoin - cost;
		UserMgr.getInstance().setUserCoin(userId, nowCoin);
		CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
				CoinBill.AttrUSER_NAME.set(user.NAME),
				CoinBill.AttrSOURCE.set("live"),
				CoinBill.AttrSOURCE_ID.set(roomId+""),
				CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")下注水果机"),
				CoinBill.AttrAMOUNT.set(-cost),
				CoinBill.AttrBEFORE_BAL.set(oldCoin),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
		packet.error = 0;
		Fruit fruit = model.getFruit(userId);
		packet.betRemain = nowCoin;
		fruit.bet(packet.bet);
		PacketManager.send(userId, packet);
		
		// 水果机开奖
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				fruit.openPrize();
				int reward = fruit.getReward();
				if(reward > 0){
					int oldCoin = user.COIN_COUNT;
					int nowCoin = oldCoin + reward;
					user.COIN_COUNT = nowCoin;
					user.update();
					CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
							CoinBill.AttrUSER_NAME.set(user.NAME),
							CoinBill.AttrSOURCE.set("live"),
							CoinBill.AttrSOURCE_ID.set(roomId+""),
							CoinBill.AttrSOURCE_NAME.set("直播房间("+room.CHANNEL+")下注水果机获奖"),
							CoinBill.AttrAMOUNT.set(reward),
							CoinBill.AttrBEFORE_BAL.set(oldCoin),
							CoinBill.AttrAFTER_BAL.set(nowCoin),
							CoinBill.AttrCREATE_TIME.set(new Date()));
				}
				fruit_notify.Builder notify = fruit_notify.newBuilder();
				notify.setResult(fruit.getResult());
				notify.setReward(fruit.getReward());
				notify.setCurrCoin(UserMgr.getInstance().getUserCoin(userId));
				header.packet.Builder head = header.packet.newBuilder();
		    	head.setCommand(PacketTypes.LIVE_FRUIT_NOTIFY);
		    	head.setVersion(1);
		    	head.setSubversion(0);
		    	head.setBody(notify.buildPartial().toByteString());
				PacketManager.send(userId, head.buildPartial().toByteArray());
			}
		}, 2000);
	}
}
