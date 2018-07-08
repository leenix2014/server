package game.packet;

import java.util.HashMap;
import java.util.Map;

import game.baccarat.BaccaratBetPacket;
import game.baccarat.BaccaratGetRoomPacket;
import game.baccarat.BaccaratGetUserPacket;
import game.baccarat.BaccaratJoinPacket;
import game.baccarat.BaccaratLastBetPacket;
import game.baccarat.BaccaratLeavePacket;
import game.baccarat.BaccaratResultPacket;
import game.baccarat.BaccaratSilentUserPacket;
import game.baccarat.BaccaratStagePacket;
import game.businesspackets.CreateRoomPacket;
import game.businesspackets.DismissRoomOpinionPacket;
import game.businesspackets.DismissRoomPacket;
import game.businesspackets.GameBetPacket;
import game.businesspackets.GrabDealerPacket;
import game.businesspackets.JoinRoomPacket;
import game.businesspackets.QuitRoomPacket;
import game.businesspackets.ShowHandPacket;
import game.businesspackets.StartGamePacket;
import game.coin.CubeToCoinPacket;
import game.coin.MyCoinPacket;
import game.coin.RecentTransferPacket;
import game.coin.TransferCoinPacket;
import game.coin.TransferRatePacket;
import game.coinroom.CoinRoomBetPacket;
import game.coinroom.CoinRoomGrabPacket;
import game.coinroom.CoinRoomJoinPacket;
import game.coinroom.CoinRoomLeaveOnOverPacket;
import game.coinroom.CoinRoomLeavePacket;
import game.coinroom.CoinRoomListPacket;
import game.coinroom.CoinRoomQuickJoinPacket;
import game.coinroom.CoinRoomReadyPacket;
import game.coinroom.CoinRoomShowHandPacket;
import game.coinroom.CoinRoomSitDownPacket;
import game.coinroom.CoinRoomStagePacket;
import game.coinroom.CoinRoomStandupPacket;
import game.coinroom.CoinRoomStartPacket;
import game.connector.Connector;
import game.history.BillsPacket;
import game.history.RecentRoomPacket;
import game.history.SettlesPacket;
import game.live.AnchorLoginPacket;
import game.live.BetFruitPacket;
import game.live.BetTigerPacket;
import game.live.CheckLiveRoomPwdPacket;
import game.live.EndLiveRoomPacket;
import game.live.ExitLiveRoomPacket;
import game.live.GetGiftsPacket;
import game.live.GetLiveRoomPacket;
import game.live.NewLiveRoomPacket;
import game.live.PayLiveRoomPacket;
import game.live.SendGiftPacket;
import game.live.SendMsgPacket;
import game.live.ShowLovePacket;
import game.live.catcher.CommandPacket;
import game.live.catcher.WannaPlayPacket;
import game.live.pusher.InsertCoinPacket;
import game.loginReward.packet.GetLoginPrizePacket;
import game.loginReward.packet.LoginRecordPacket;
import game.lottery.DrawPrizePacket;
import game.lottery.PrizesPacket;
import game.phoneconfirm.packet.ConfirmInputPacket;
import game.phoneconfirm.packet.ConfirmRequestPacket;
import game.roulette.ConfirmBetPacket;
import game.roulette.GetRoomPacket;
import game.roulette.JoinRoulettePacket;
import game.roulette.LeavePacket;
import game.roulette.ResultPacket;
import game.roulette.RouletteGetUserPacket;
import game.roulette.RouletteHistoryPacket;
import game.roulette.RouletteSilentUserPacket;
import game.roulette.StagePacket;
import game.session.GameSession;
import game.session.SessionManager;
import game.shop.DeliverGoodPacket;
import game.shop.OrderGoodPacket;
import game.task.packet.GetTaskPrizePacket;
import game.task.packet.SharePacket;
import game.task.packet.TaskDataPacket;
import game.user.packet.BindAgentPacket;
import game.user.packet.BindPhonePacket;
import game.user.packet.ChangeAvatarPacket;
import game.user.packet.ChangeLangPacket;
import game.user.packet.ChangeNamePacket;
import game.user.packet.ChangePasswordPacket;
import game.user.packet.CheckUpdatePacket;
import game.user.packet.GamingPacket;
import game.user.packet.GetAgentPacket;
import game.user.packet.GetUserNamePacket;
import game.user.packet.LogoutPacket;
import game.user.packet.PhoneRegisterPacket;
import game.user.packet.QueryUserPacket;
import game.user.packet.RankListPacket;
import game.user.packet.ReconnectPacket;
import game.user.packet.RoomCardPacket;
import game.user.packet.SignInPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocols.header;

public class PacketManager {
	private static Map<Integer, Class<?>> packetClass = new HashMap<Integer, Class<?>>();
	private static Map<Class<?>, Integer> classToType = new HashMap<Class<?>, Integer>();
	
	public static void init(){
		//非业务包
		resisterPacket(PacketTypes.PHONE_REGISER_CMD, PhoneRegisterPacket.class); //手机号注册
		resisterPacket(PacketTypes.CONFIRM_REQUEST_CMD, ConfirmRequestPacket.class); //手机验证码
		resisterPacket(PacketTypes.CONFIRM_INPUT_CMD, ConfirmInputPacket.class); //手机验证码校验
		
		//业务包
		resisterPacket(PacketTypes.USER_RANK_LIST, RankListPacket.class); //用户排行榜
		resisterPacket(PacketTypes.BIND_AGENT_CMD, BindAgentPacket.class); //代理绑定
		resisterPacket(PacketTypes.GET_AGENT_CMD, GetAgentPacket.class); //代理绑定
		resisterPacket(PacketTypes.GET_USER_NAME, GetUserNamePacket.class); //根据登录id获取用户名
		resisterPacket(PacketTypes.QUERY_USER, QueryUserPacket.class); //根据登录id获取用户名
		resisterPacket(PacketTypes.BIND_PHONE_CMD, BindPhonePacket.class); //手机绑定
		resisterPacket(PacketTypes.CHANGE_PASSWORD_CMD, ChangePasswordPacket.class); //修改密码
		resisterPacket(PacketTypes.CHANGE_AVATAR_CMD, ChangeAvatarPacket.class); //修改头像
		resisterPacket(PacketTypes.CHANGE_NAME_CMD, ChangeNamePacket.class); //修改昵称
		resisterPacket(PacketTypes.CHANGE_LANG_CMD, ChangeLangPacket.class); //修改语种
		resisterPacket(PacketTypes.LOGIN_CMD, SignInPacket.class); //登录
		resisterPacket(PacketTypes.LOGOUT_CMD, LogoutPacket.class); //登出
		resisterPacket(PacketTypes.CHECK_UPDATE_CMD, CheckUpdatePacket.class); //登出
		resisterPacket(PacketTypes.CREATE_ROOM_CMD, CreateRoomPacket.class); //创建房间
		resisterPacket(PacketTypes.DISMISS_ROOM_CMD, DismissRoomPacket.class); //解散房间
		resisterPacket(PacketTypes.JOIN_ROOM_CMD, JoinRoomPacket.class); //加入房间
		resisterPacket(PacketTypes.QUIT_ROOM_CMD, QuitRoomPacket.class); //退出房间
		resisterPacket(PacketTypes.DISMISS_ROOM_OPINION_CMD, DismissRoomOpinionPacket.class); //解散房间意见
		resisterPacket(PacketTypes.START_GAME_CMD, StartGamePacket.class); //开始游戏
		resisterPacket(PacketTypes.GRABDEALER_CMD, GrabDealerPacket.class); //抢庄
		resisterPacket(PacketTypes.GAME_BET_CMD, GameBetPacket.class); //游戏下注
		resisterPacket(PacketTypes.SHOWHAND_CMD, ShowHandPacket.class); //发送手牌
		resisterPacket(PacketTypes.ROOM_CARD_CMD, RoomCardPacket.class); //更新房卡
		resisterPacket(PacketTypes.LOGIN_RECORD_CMD, LoginRecordPacket.class); //登录记录
		resisterPacket(PacketTypes.GET_LOGIN_PRIZE_CMD, GetLoginPrizePacket.class); //领取登录奖励
		resisterPacket(PacketTypes.RECONNECT_CMD, ReconnectPacket.class); //断线重连
		resisterPacket(PacketTypes.TASK_DATA_CMD, TaskDataPacket.class); //获取任务数据
		resisterPacket(PacketTypes.GET_TASK_PRIZE_CMD, GetTaskPrizePacket.class); //领取任务奖励
		resisterPacket(PacketTypes.SHARE_CMD, SharePacket.class); //分享链接
		resisterPacket(PacketTypes.GAMING_CMD, GamingPacket.class); //查询正在游戏的房间
		resisterPacket(PacketTypes.LOTTERY_PRIZES_CMD, PrizesPacket.class); //获取抽奖奖品列表
		resisterPacket(PacketTypes.DRAW_PRIZE_CMD, DrawPrizePacket.class); //抽奖
		resisterPacket(PacketTypes.RECENT_ROOM_CMD, RecentRoomPacket.class); //查询最近房间历史
		resisterPacket(PacketTypes.ROOM_SETTLES_CMD, SettlesPacket.class); //查询房间内牌局历史
		resisterPacket(PacketTypes.ROOM_CARD_BILL_CMD, BillsPacket.class); //房卡账单
		resisterPacket(PacketTypes.ORDER_GOOD_CMD, OrderGoodPacket.class); //商品下单
		resisterPacket(PacketTypes.DELIVER_GOOD_CMD, DeliverGoodPacket.class); //商品发货
		
		resisterPacket(PacketTypes.CUBE_TO_COIN_CMD, CubeToCoinPacket.class); //金币转换
		resisterPacket(PacketTypes.MY_COIN_CMD, MyCoinPacket.class); //金币查询
		resisterPacket(PacketTypes.TRANSFER_COIN_CMD, TransferCoinPacket.class); //金币转账
		resisterPacket(PacketTypes.TRANSFER_RATE_CMD, TransferRatePacket.class); //金币转账费率
		resisterPacket(PacketTypes.RECENT_TRANSFER, RecentTransferPacket.class); //最近转账记录
		
		//直播业务包
		resisterPacket(PacketTypes.ANCHOR_LOGIN, AnchorLoginPacket.class); //主播登录
		resisterPacket(PacketTypes.NEW_LIVE_ROOM, NewLiveRoomPacket.class); //主播创建房间
		resisterPacket(PacketTypes.END_LIVE_ROOM, EndLiveRoomPacket.class); //主播停止直播
		resisterPacket(PacketTypes.GET_LIVE_ROOM, GetLiveRoomPacket.class); //用户获取房间
		resisterPacket(PacketTypes.PAY_LIVE_ROOM, PayLiveRoomPacket.class); //用户付费并加入房间
		resisterPacket(PacketTypes.EXIT_LIVE_ROOM, ExitLiveRoomPacket.class); //用户离开直播间
		resisterPacket(PacketTypes.GET_GIFTS, GetGiftsPacket.class); //获取房间内礼物信息
		resisterPacket(PacketTypes.SEND_MSG, SendMsgPacket.class); //用户发送消息
		resisterPacket(PacketTypes.SEND_GIFT, SendGiftPacket.class); //用户发送礼品
		resisterPacket(PacketTypes.SHOW_LOVE, ShowLovePacket.class); //用户离开直播间
		resisterPacket(PacketTypes.CHECK_LIVE_PWD, CheckLiveRoomPwdPacket.class); //用户离开直播间
		resisterPacket(PacketTypes.INSERT_COIN, InsertCoinPacket.class); //推币机插入金币
		resisterPacket(PacketTypes.WANNA_PLAY, WannaPlayPacket.class); //想玩娃娃机
		resisterPacket(PacketTypes.CATCHER_COMMAND, CommandPacket.class); //操作娃娃机
		resisterPacket(PacketTypes.LIVE_BET_TIGER, BetTigerPacket.class); //老虎机下注
		resisterPacket(PacketTypes.LIVE_BET_FRUIT, BetFruitPacket.class); //水果机下注
		
		//轮盘业务包
		resisterPacket(PacketTypes.ROU_GET_ROOM, GetRoomPacket.class); //获取轮盘房间
		resisterPacket(PacketTypes.ROU_JOIN_ROOM, JoinRoulettePacket.class); //加入轮盘房间
		resisterPacket(PacketTypes.ROU_CONFIRM_BET, ConfirmBetPacket.class); //轮盘下注
		resisterPacket(PacketTypes.ROU_GET_STAGE, StagePacket.class); //获取轮盘当前阶段
		resisterPacket(PacketTypes.ROU_RESULT, ResultPacket.class); //轮盘开奖
		resisterPacket(PacketTypes.ROU_LEAVE, LeavePacket.class); //退出轮盘
		resisterPacket(PacketTypes.ROU_HIST, RouletteHistoryPacket.class); //退出轮盘
		resisterPacket(PacketTypes.ROU_GET_USER, RouletteGetUserPacket.class); //获取轮盘玩家列表
		resisterPacket(PacketTypes.ROU_SILENT_USER, RouletteSilentUserPacket.class); //获取轮盘玩家列表		
		
		//百家乐业务包
		resisterPacket(PacketTypes.BACC_GET_ROOM, BaccaratGetRoomPacket.class); //获取百家乐房间
		resisterPacket(PacketTypes.BACC_JOIN_ROOM, BaccaratJoinPacket.class); //加入百家乐房间
		resisterPacket(PacketTypes.BACC_GET_USER, BaccaratGetUserPacket.class); //获取无座玩家列表
		resisterPacket(PacketTypes.BACC_CONFIRM_BET, BaccaratBetPacket.class); //百家乐下注
		resisterPacket(PacketTypes.BACC_LAST_BET, BaccaratLastBetPacket.class); //百家乐上局下注
		resisterPacket(PacketTypes.BACC_GET_STAGE, BaccaratStagePacket.class); //获取百家乐游戏当前阶段
		resisterPacket(PacketTypes.BACC_RESULT, BaccaratResultPacket.class); //百家乐开奖
		resisterPacket(PacketTypes.BACC_LEAVE, BaccaratLeavePacket.class); //退出百家乐
		resisterPacket(PacketTypes.BACC_SILENT_USER, BaccaratSilentUserPacket.class); //百家乐禁言用户
		
		//金币场业务包
		resisterPacket(PacketTypes.COIN_ROOM_LIST, CoinRoomListPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_JOIN, CoinRoomJoinPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_QUICK_JOIN, CoinRoomQuickJoinPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_SIT_DOWN, CoinRoomSitDownPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_READY, CoinRoomReadyPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_START, CoinRoomStartPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_GRAB, CoinRoomGrabPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_BET, CoinRoomBetPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_SHOW_HAND, CoinRoomShowHandPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_STAND_UP, CoinRoomStandupPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_LEAVE, CoinRoomLeavePacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_LEAVE_ON_OVER, CoinRoomLeaveOnOverPacket.class);
		resisterPacket(PacketTypes.COIN_ROOM_GET_STAGE, CoinRoomStagePacket.class);
	}
	
	public static void resisterPacket(int packetType , Class<? extends Packet> cl){
		packetClass.put(packetType, cl);
		classToType.put(cl, packetType);
	}
	
	public static int getPacketType(Class<? extends Packet> cl){
		Integer packetType = classToType.get(cl);
		if(packetType == null){
			return 0;
		}
		return packetType;
	}
	
	public static Packet getPacketByType(int packetType){
		if (!packetClass.containsKey(packetType)) {
			return null;
		}
		try {
			return (Packet)(packetClass.get(packetType).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void send(int userId, Packet packet){
		GameSession session = SessionManager.getSession(userId);
		if (session == null) {
			//TraceLog.info("PacketManager.send packet by userId get no session,userId=" + userId);
			return;
		}
		
		send(session, packet);
	}
	
	public static void send(GameSession session, Packet packet){
		header.packet.Builder pktBuilder = header.packet.newBuilder();
		pktBuilder.setCommand(getPacketType(packet.getClass()));
		pktBuilder.setVersion(1);
		pktBuilder.setSubversion(0);
		packet.writeBody(pktBuilder);
    	
		byte[] payload = pktBuilder.buildPartial().toByteArray();
        ByteBuf buf = Unpooled.buffer();   
        buf.writeBytes(payload);
		Connector.writeToFrontend(session.getChannel(), buf);
	}
	
	/**
	 * 接入游戏业务逻辑用的，一般情况下不建议使用此方法
	 */
	public static void send(int userId, byte[] msg){
		GameSession session = SessionManager.getSession(userId);
		if (session == null) {
			//TraceLog.info("PacketManager.send msg by userId get no session,userId=" + userId);
			return;
		}
		
        send(session, msg);
	}
	
	/**
	 * 接入游戏业务逻辑用的，一般情况下不建议使用此方法
	 */
	public static void send(GameSession session, byte[] msg){
        ByteBuf buf = Unpooled.buffer();   
        buf.writeBytes(msg);
		Connector.writeToFrontend(session.getChannel(), buf);
	}
}
