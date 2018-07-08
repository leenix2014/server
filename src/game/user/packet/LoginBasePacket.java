package game.user.packet;

import java.net.InetSocketAddress;

import com.mozat.morange.dbcache.tables.AppVersion;
import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.log.TraceLog;
import com.mozat.morange.util.MD5;

import game.loginReward.LoginRewardService;
import game.packet.Packet;
import game.packet.PacketManager;
import game.session.SessionManager;
import game.user.Users;
import io.netty.channel.Channel;
import netty.util.MathUtil;

public abstract class LoginBasePacket extends Packet {
	public static final int SID_TOKEN = 0;
	public static final int SID_VISITOR = 1;
	public static final int SID_FB = 2;
	public static final int SID_PHONE = 3;
	public static final int SID_LOGIN_ID = 4;
	
	public static final String PLATFORM_IOS = "iOS";
	public static final String PLATFORM_ANDROID = "Android";
	
	protected boolean newUser = false;
	
	// request parameters
	protected int sid;//1=游客 2=FB 3=手机登陆
	protected String uuid;//1=MacId 2=facebookid 3=combineDeviceID
	protected String token;//1="" 2="" 3=phoneNum
	protected String countryCode;//1="" 2="" 3=countryCode
	protected String extra;//1="" 2="" 3=password
	
	protected String platform;
	protected String appVer;
	protected String lang = "zh_CN";
	
	// 响应
	public int error;
	public String errDesc = "";
	protected String appUrl = "";
	
	protected void login(){
		logger.info("User login, sid("+sid+"),uuid("+uuid+"),token("+token+"),extra("+extra+"),platform("+platform+"),appVer("+appVer+")");
		TUsers users = getLoginUser();
		if(users == null){
			return;
		}
		
		AppVersion latestApp = AppVersion.getOneByCriteria(AppVersion.AttrPLATFORM.eq(platform),
				AppVersion.AttrLATEST.eq(true));
		
		String latestVer = appVer;
		boolean mustUpd = false;
		if(latestApp != null && !latestApp.APP_VER.equals(appVer)){
			latestVer = latestApp.APP_VER;
			mustUpd = latestApp.MUST_UPDATE;
			logger.info("latestVer("+latestVer+"), mustUpdate("+mustUpd+")");
		}
		
		int userId = MathUtil.parseInt(users.LOGIN_ID);
		//下发消息到客户端
		SignInPacket signIn = new SignInPacket();
		signIn.error = 0;
		signIn.signedup = newUser;
		signIn.uid = userId;
		signIn.sid = sid;
		signIn.token = token;
		signIn.name = users.NAME;
		signIn.avatarId = users.AVATAR_ID;
		signIn.latestVer = latestVer;
		signIn.mustUpdate = mustUpd;
		signIn.appUrl = latestApp.DOWNLOAD_URL;
		PacketManager.send(session, signIn);
		logger.info("User("+users.LOGIN_ID+") login success, sid("+sid+"),param("+uuid+"),token("+token+"),extra("+extra+"),platform("+platform+"),appVer("+appVer+")");
		
		//登录签到
		LoginRewardService.createLoginRecord(userId);
		
		//设置user last ip
		Channel channel = session.getChannel();
		InetSocketAddress insocket = (InetSocketAddress)channel.remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
		users.LAST_IP = clientIP;
		users.update();
	}
	
	protected TUsers getLoginUser(){
		TUsers user;
		if(SID_PHONE == sid){
			user = Users.loadByPhone(countryCode, token);
		} else {
			user = Users.loadDBForLogin(sid, uuid);
		}
		if(SID_PHONE == sid || SID_LOGIN_ID == sid){
			SignInPacket signIn = new SignInPacket();
			signIn.sid = sid;
			signIn.token = token;
			if (user == null) {
				signIn.error = 1;//无此手机账号
				signIn.errDesc = SID_PHONE == sid?"无此手机账号":"无此登录账号";
				PacketManager.send(session, signIn);
				return null;
			}
			
			//验证密码
			if(!(this instanceof PhoneRegisterPacket)){
				String pwd = user.PWD;
				if (!extra.equals(MD5.getHashString(pwd))) {
					signIn.error = 2;//密码不对
					signIn.errDesc = "密码不正确";
					PacketManager.send(session, signIn);
					return null;
				}
			}
		}
		if((user == null && SID_VISITOR == sid || SID_FB == sid)){
			user = Users.createUser(sid, uuid);
			
			//首次登录，设置首次登录ip
			if (user != null) {
				newUser = true;
				Channel channel = session.getChannel();
				InetSocketAddress insocket = (InetSocketAddress)channel.remoteAddress();
		        String clientIP = insocket.getAddress().getHostAddress();
				user.REG_IP = clientIP;
				if(platform != null){
					user.OS = platform;
				}
				user.update();
			}
		}
		
		if (user == null) {
			TraceLog.info("UserService.login getUsersForLogin failed,sid=" + sid + ",param=" + uuid + ",extra=" + extra);
			return null;
		}
		
		SessionManager.addSession(lang, MathUtil.parseInt(user.LOGIN_ID), session);
		return user;
	}
}
