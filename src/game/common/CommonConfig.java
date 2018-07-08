package game.common;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.TCommonConfig;

import netty.util.MathUtil;

public class CommonConfig{
	
	public static final String VERIFY_CODE_VALID_SECONDS = "verifyCodeValidSeconds";
	public static final String LOGIN_REWARD_LOOP = "isLoginRewardLoop";//登录是否循环赠送
	public static final String TOPUP_SWITCH = "topupSwitch";//是否开启充值按钮
	public static final String TOPUP_CONTACT_SWITCH = "topupContactSwitch";//是否显示充值联系方式
	public static final String PRIZE_SWITCH = "prizeSwitch";//是否开启抽奖功能
	public static final String GHOST_SWITCH = "ghostSwitch";//是否开启鬼牌功能
	public static final String LIVE_SWITCH = "liveSwitch";//直播开关
	public static final String ROULETTE_SWITCH = "rouletteSwitch";//轮盘开关
	public static final String COIN_ROOM_SWITCH = "coinRoomSwitch";//金币场开关
	public static final String BACCARAT_SWITCH = "baccaratSwitch";//百家乐开关
	public static final String APP_STORE_VERIFY_URL = "appVerifyUrl";//苹果验证Receipt的url
	public static final String DRAW_PERCENT = "drawPercent";//抽水百分比
	public static final String CURR_GIFT_VERSION = "currGiftVersion";//直播当前礼品包
	public static final String VALID_PAY_HOUR = "validPayHour";//直播付款的有效时间，小时为单位
	public static final String GLOBAL_MSG_COST = "globalMsgCost";//全平台消息价格
	public static final String CUBE_COIN_RATE = "cubeCoinRate";//Cube和Coin的比例
	public static final String ROULETTE_PERCENT = "roulettePercent";//轮盘抽水百分比
	public static final String ROBOT_PERROOM = "robotPerRoom";//每个房间的机器人数量
	public static final String ROBOT_ACTION_MIN_INTERVAL = "robotActionMinInterval";//机器人两个动作直接的最小间隔
	public static final String ROBOT_ACTION_MAX_INTERVAL = "robotActionMaxInterval";//机器人两个动作直接的最大间隔
	public static final String ROBOT_SEND_GIFT_PERCENT = "robotSendGiftPercent";//机器人送礼物概率
	
	public static final String TRANSFER_COIN_PERCENT = "transferCoinPercent";//金币转账抽佣比例
	
	//<key,CommonConfig>
	private static final Map<String, String> map = new ConcurrentHashMap<String, String>();
	
	public static void init(){
		map.clear();
		ArrayList<TCommonConfig> tConfs = TCommonConfig.getAllObjects();
		if (tConfs != null) {
			for(TCommonConfig tConf : tConfs){
				map.put(tConf.CONFIG_KEY, tConf.CONFIG_VALUE);
			}
		}
	}
	
	public static String get(String key){
		return get(key, "");
	}
	
	public static String get(String key, String defValue){
		return map.get(key) == null?defValue:map.get(key);
	}
	
	public static int getInt(String key, int defValue){
		return map.get(key) == null?defValue:MathUtil.parseInt(map.get(key));
	}
	
	public static int getPercent(String key, int defValue){
		int percent = getInt(key, defValue);
		if(percent > 100){
			percent = 100;
		}
		if(percent < 0){
			percent = 0;
		}
		return percent;
	}
	
	public static boolean getBoolean(String key, boolean defValue){
		return map.get(key) == null?defValue:"true".equals(map.get(key));
	}
}
