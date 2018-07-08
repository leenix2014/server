package game.packet;

public class PacketTypes {
	public static final int LOGIN_CMD 			= 0x01;	// 登录命令字
	public static final int LOGOUT_CMD 			= 0x02;	// 登出命令
	public static final int CHECK_UPDATE_CMD    = 0x03;	// 检查更新命令
	
	public static final int CREATE_ROOM_CMD 	= 0x11;	// 创建房间命令字
	
	public static final int DISMISS_ROOM_CMD 	= 0x12;	// 解散房间命令字
	
	public static final int JOIN_ROOM_CMD 		= 0x13;	// 加入房间命令字
	
	public static final int QUIT_ROOM_CMD 		= 0x14;	// 退出房间命令字
	
	public static final int DISMISS_ROOM_NOTIFY_CMD = 0x15;	// 解散房间广播命令字
	
	public static final int DISMISS_ROOM_OPINION_CMD = 0x16;// 解散房间意见命令字
	
	// Coin Room
	public static final int COIN_ROOM_LIST = 0x20;
	public static final int COIN_ROOM_JOIN = 0x21;
	public static final int COIN_ROOM_QUICK_JOIN = 0x22;
	public static final int COIN_ROOM_JOIN_NOTIFY = 0x23;
	public static final int COIN_ROOM_SIT_DOWN = 0x24;
	public static final int COIN_ROOM_SIT_DOWN_NOTIFY = 0x25;
	public static final int COIN_ROOM_READY = 0x26;
	public static final int COIN_ROOM_READY_NOTIFY = 0x27;
	public static final int COIN_ROOM_START = 0x28;
	public static final int COIN_ROOM_GRAB = 0x29;
	public static final int COIN_ROOM_GRAB_NOTIFY = 0x2A;
	public static final int COIN_ROOM_BANKER_NOTIFY = 0x2B;
	public static final int COIN_ROOM_BET = 0x2C;
	public static final int COIN_ROOM_BET_NOTIFY = 0x2D;
	public static final int COIN_ROOM_DEAL_NOTIFY = 0x2E;
	public static final int COIN_ROOM_SHOW_HAND = 0x2F;
	public static final int COIN_ROOM_OVER = 0x30;
	public static final int COIN_ROOM_STAND_UP = 0x31;
	public static final int COIN_ROOM_STAND_UP_NOTIFY = 0x32;
	public static final int COIN_ROOM_LEAVE = 0x33;
	public static final int COIN_ROOM_LEAVE_NOTIFY = 0x34;
	public static final int COIN_ROOM_GET_STAGE = 0x35;
	public static final int COIN_ROOM_STAGE_NOTIFY = 0x36;
	public static final int COIN_ROOM_SHOW_HAND_NOTIFY = 0x37;
	public static final int COIN_ROOM_LEAVE_ON_OVER = 0x38;
	
	public static final int MY_COIN_CMD            = 0x60; //我的金币
	public static final int CUBE_TO_COIN_CMD       = 0x61; //Cube转换为Coin
	public static final int TRANSFER_COIN_CMD      = 0x62; //金币转账命令字
	public static final int TRANSFER_RATE_CMD      = 0x63; //金币转账费率获取
	public static final int RECENT_TRANSFER        = 0x64; //最近转账记录
	public static final int TRANSFER_COIN_NOTIFY   = 0x65; //金币转账通知
	
	public static final int BIND_PHONE_CMD      = 0x70; //绑定手机号码
	public static final int BIND_AGENT_CMD      = 0x71; //绑定代理
	public static final int GET_AGENT_CMD       = 0x72; //获取绑定代理
	public static final int GET_USER_NAME       = 0x73; //获取用户昵称
	public static final int QUERY_USER          = 0x74; //模糊查询用户
	public static final int USER_RANK_LIST      = 0x75; //用户排行榜

	public static final int START_GAME_CMD		= 0x83;	// 开始游戏命令字
	
	public static final int PLAYER_SIT_CMD		= 0x81;	// 玩家坐下命令字
	
	public static final int ROOM_STATE_CMD		= 0x80;	// 游戏房间状态广播命令字
	
	public static final int GRABDEALER_CMD		= 0x85;	// 抢庄命令字
	
	public static final int PLAYER_STANDUP_CMD 	= 0x82;	// 玩家站起命令字
	
	public static final int DEALING_CMD			= 0x86;	// 游戏发牌命令字

	public static final int GAME_BET_CMD		= 0x87;	// 游戏下注命令字
	
	public static final int SHOWHAND_CMD		= 0x88;	// 发送手牌命令字
	
	public static final int SINGLE_GAMEOVER_CMD = 0x84;	// 単局游戏结束命令字
	
	public static final int TOTAL_GAMEOVER_CMD	= 0x89;	// 全局游戏结束命令字

	public static final int ROOM_CARD_CMD       = 0x90; // 更新房卡命令字
	
	public static final int PHONE_REGISER_CMD   = 0x91; // 手机号注册命令字
	
	public static final int CHANGE_PASSWORD_CMD = 0x92; // 修改密码命令字
	
	public static final int CONFIRM_REQUEST_CMD = 0x93; // 手机验证码命令字
	
	public static final int CONFIRM_INPUT_CMD   = 0x94; // 手机验证码校验命令字
	
	public static final int CHANGE_NAME_CMD   = 0x95; // 修改昵称命令字
	
	public static final int CHANGE_AVATAR_CMD   = 0x96; // 修改头像命令字
	
	public static final int LOGIN_RECORD_CMD   = 0x97; // 登录签到命令字
	
	public static final int GET_LOGIN_PRIZE_CMD   = 0x98; // 获取签到奖励命令字
	
	public static final int RECONNECT_CMD   = 0x99; // 断线重连
	
	public static final int TASK_DATA_CMD   = 0x9A; // 获取任务数据
	
	public static final int GET_TASK_PRIZE_CMD   = 0x9B; // 领取任务奖励
	
	public static final int SHARE_CMD   = 0x9C; // 分享链接
	
	public static final int CHANGE_LANG_CMD  = 0x9D; // 修改语种

	public static final int HEART_BEAT_CMD = 0x1001;	// 心跳包命令字
	
	public static final int GAMING_CMD = 0x1101;	// 查询正在游戏的房间
	
	public static final int LOTTERY_PRIZES_CMD = 0x1102;	// 抽奖奖品列表
	
	public static final int DRAW_PRIZE_CMD = 0x1103;	// 抽奖命令字
	
	public static final int RECENT_ROOM_CMD = 0x1104;	// 查询最近房间
	
	public static final int ROOM_SETTLES_CMD = 0x1105;	// 结算信息命令字
	
	public static final int AGAIN_NOTIFY_CMD = 0x1106;	// 再来N局命令字
	
	public static final int OFFLINE_NOTIFY_CMD = 0x1107;	// 离线广播
	
	public static final int ROOM_CARD_BILL_CMD = 0x1108;	// 房卡账单
	
	public static final int ORDER_GOOD_CMD = 0x1109;	// 商品下单
	
	public static final int DELIVER_GOOD_CMD = 0x1110;	// 商品发货
	
	//直播相关
	public static final int ANCHOR_LOGIN       = 0x1200; //主播登录
	public static final int NEW_LIVE_ROOM      = 0x1201; //主播创建房间
	public static final int END_LIVE_ROOM      = 0x1202; //主播停止直播
	public static final int GET_LIVE_ROOM      = 0x1203; //用户获取房间
	public static final int PAY_LIVE_ROOM      = 0x1204; //用户付费并加入房间
	public static final int GET_GIFTS          = 0x1205; //获取房间内礼物信息
	public static final int SEND_MSG           = 0x1206; //用户发送消息
	public static final int SEND_GIFT          = 0x1207; //用户发送礼品
	public static final int ANCHOR_EXIT        = 0x1209; //主播退出广播
	public static final int INSERT_COIN        = 0x120A; //推币机插入硬币
	public static final int PUSH_OUT           = 0x120B; //推币机返回结果
	public static final int SEND_TIGER         = 0x120C; //用户同时发送三份礼物并抽奖
	public static final int JOIN_NOTIFY        = 0x1220; //直播加入房间广播
	public static final int GIFT_NOTIFY        = 0x1221; //直播送礼广播
	public static final int MSG_NOTIFY         = 0x1222; //直播聊天广播
	public static final int EXIT_LIVE_ROOM     = 0x1223; //用户离开直播间
	public static final int SHOW_LOVE          = 0x1224; //喜欢主播
	public static final int SHOW_LOVE_NOTIFY   = 0x1225; //喜欢主播广播
	public static final int CHECK_LIVE_PWD     = 0x1226; //验证主播房间密码
	
	public static final int WANNA_PLAY         = 0x1210; //请求玩娃娃机
	public static final int CATCHER_COMMAND    = 0x1211; //操作娃娃机
	public static final int CATCH_RESULT       = 0x1219; //娃娃机结果通知
	
	public static final int LIVE_BET_TIGER     = 0x1231; //老虎机下注
	public static final int LIVE_TIGER_NOTIFY  = 0x1232; //老虎机开奖
	
	public static final int LIVE_BET_FRUIT     = 0x1241; //水果机下注
	public static final int LIVE_FRUIT_NOTIFY  = 0x1242; //水果机开奖
	
	//轮盘
	public static final int ROU_GET_ROOM = 0x1300; //获取轮盘房间
	public static final int ROU_JOIN_ROOM = 0x1301; //加入轮盘房间
	public static final int ROU_CONFIRM_BET = 0x1302; //轮盘下注
	public static final int ROU_BET_NOTIFY = 0x1303; // 下注广播
	public static final int ROU_GET_STAGE = 0x1304; // 获取轮盘当前阶段
	public static final int ROU_STAGE_NOTIFY = 0x1305; // 轮盘阶段转换广播
	public static final int ROU_GET_USER = 0x1306; // 轮盘玩家列表
	public static final int ROU_SILENT_USER = 0x1307; // 轮盘玩家禁言
	public static final int ROU_SILENT_USER_NOTIFY = 0x1308; // 轮盘禁言通知
	public static final int ROU_RESULT = 0x1309; //轮盘开奖
	public static final int ROU_LEAVE = 0x1310; //退出轮盘
	public static final int ROU_HIST = 0x1311; //轮盘历史查询
	
	//百家乐Baraccat
	public static final int BACC_GET_ROOM = 0x1400; //获取百家乐房间
	public static final int BACC_JOIN_ROOM = 0x1401; //加入百家乐房间
	public static final int BACC_CONFIRM_BET = 0x1402; //百家乐下注
	public static final int BACC_BET_NOTIFY = 0x1403; // 下注广播
	public static final int BACC_GET_STAGE = 0x1404; // 获取百家乐当前阶段
	public static final int BACC_STAGE_NOTIFY = 0x1405; // 百家乐阶段转换广播
	public static final int BACC_JOIN_NOTIFY = 0x1407; //加入百家乐房间
	public static final int BACC_GET_USER = 0x1408; //获取无座玩家列表
	public static final int BACC_RESULT = 0x1409; //百家乐结果通知
	public static final int BACC_LEAVE = 0x1410; //退出轮盘
	public static final int BACC_LEAVE_NOTIFY = 0x1411; //退出轮盘
	public static final int BACC_SILENT_USER = 0x1412; //禁言用户
	public static final int BACC_SILENT_USER_NOTIFY = 0x1413; //禁言通知
	public static final int BACC_LAST_BET = 0x1414;//上局下注
}
