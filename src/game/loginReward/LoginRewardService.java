package game.loginReward;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mozat.morange.log.TraceLog;
import com.mozat.morange.util.DateUtil;

import game.common.CommonConfig;
import game.loginReward.packet.LoginRecordPacket;
import game.packet.PacketManager;
import netty.GameModels.UserMgr;

public class LoginRewardService {
	public static int maxLoginDay = 0;
	
	public static void init(){
		//初始化登陆奖励配置
		LoginRewardConfig.loadAll();
	}
	
	public static void createLoginRecord(int userId){
		boolean isLoginRewardLoop = CommonConfig.getBoolean(CommonConfig.LOGIN_REWARD_LOOP, true);
		LoginRecord loginRecord = LoginRecord.load(userId);
		if (loginRecord == null) {
			TraceLog.info("LoginRewardService.login load LoginRecord is null,userId=" + userId);
			return;
		}
		
		int loginDay = loginRecord.getLoginDay();
		if (loginDay <= 0) {//新用户第一次登陆,则是第一天签到
			loginRecord.setLoginDay(1);
			loginRecord.setLastLoginDate(new Date());
			loginRecord.setGotDays(new HashSet<Integer>());
			loginRecord.update();
			return;
		}
		
		Date lastLoginDate = loginRecord.getLastLoginDate();
		int differDays = DateUtil.getDifferDays(lastLoginDate, new Date());
		
		if (differDays <= 0) {//已签到过，无需再签到
			return;
		}
		loginRecord.setUserName(UserMgr.getInstance().getUserName(userId));
		loginRecord.setLastLoginDate(new Date());
		if (differDays > 1 //非连续登录，则从第一天开始从新签到
				|| (differDays == 1 && loginDay >= LoginRewardService.maxLoginDay && isLoginRewardLoop) //连续登录,超过最大天数，并且循环奖励，则从第一天开始
				) {
			loginRecord.setLoginDay(1);
			loginRecord.setGotDays(new HashSet<Integer>());
		} else{
			loginRecord.setLoginDay(loginDay + 1);
		}
		loginRecord.update();
	}
	
	//若有奖励可领取，则发登录记录消息到客户端
	public static void checkSendLoginRecord(int userId){
		LoginRecord loginRecord = LoginRecord.load(userId);
		if (loginRecord == null) {
			return;
		}
		
		int loginDay = loginRecord.getLoginDay();
		Set<Integer> gotDays = loginRecord.getGotDays();
		for (int i = 1; i <= loginDay; i++) {
			if (!gotDays.contains(i) && loginDay<=LoginRewardService.maxLoginDay) {
				sendLoginRecord(userId);
				return;
			}
		}
	}
	
	public static void sendLoginRecord(int userId){
		LoginRecord loginRecord = LoginRecord.load(userId);
		if (loginRecord == null) {
			TraceLog.info("LoginRewardService.sendLoginRecord load LoginRecord is null,userId=" + userId);
			return;
		}
		
		List<LoginRecordData> loginRecordDatas = new ArrayList<LoginRecordData>();
		
		int loginDay = loginRecord.getLoginDay();
		Set<Integer> gotDays = loginRecord.getGotDays();
		for (int i = 1; i <= maxLoginDay; i++) {
			int day = i;
			LoginRewardConfig config = LoginRewardConfig.getLoginRewardConfig(day);
			if(config == null) continue;
			boolean isLogin = day <= loginDay;
			boolean isGotPrize = gotDays.contains(day);
			int roomCard = config.getRoomCard();
			
			LoginRecordData data = new LoginRecordData();
			data.day = day;
			data.isSign = isLogin;
			data.isGotPrize = isGotPrize;
			data.roomCard = roomCard;
			
			loginRecordDatas.add(data);
		}
		
		LoginRecordPacket packet = new LoginRecordPacket();
		packet.setLoginRecordDatas(loginRecordDatas);
		PacketManager.send(userId, packet);
	}
}