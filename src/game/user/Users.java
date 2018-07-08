package game.user;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.log.TraceLog;

import game.user.packet.LoginBasePacket;
import netty.GameModels.UserMgr;
import netty.util.MathUtil;

public class Users{
	private final static Logger logger = LoggerFactory.getLogger(Users.class);
	
	//<userId,users>
	private static final Map<Integer, TUsers> map = new ConcurrentHashMap<>();
	
	public static TUsers load(int userId){
		TUsers users = map.get(userId);
		if(users != null){
			return users;
		}
		TUsers t = TUsers.getOneByCriteria(TUsers.AttrLOGIN_ID.eq(userId+""));
		if (t == null) {
			return null;
		}
		map.put(userId, t);
		return t;
	}
	
	public static TUsers loadFromDb(int userId){
		map.remove(userId);
		return load(userId);
	}
	
	public static TUsers loadDBForLogin(int sid, String uuid){
		TUsers t = TUsers.getOneByCriteria(
				LoginBasePacket.SID_FB == sid?TUsers.AttrFB_ID.eq(uuid):LoginBasePacket.SID_LOGIN_ID == sid?TUsers.AttrLOGIN_ID.eq(uuid):TUsers.AttrMAC_ID.eq(uuid));
		if(t == null){
			return null;
		}
		map.put(MathUtil.parseInt(t.LOGIN_ID), t);
		return t;
	}
	
	public static TUsers loadByPhone(String countryCode, String phone){
		TUsers t = TUsers.getOneByCriteria(TUsers.AttrPHONE.eq(phone), TUsers.AttrCOUNTRY_CODE.eq(countryCode));
		if(t == null){
			return null;
		}
		map.put(MathUtil.parseInt(t.LOGIN_ID), t);
		return t;
	}
	
	public static TUsers createUser(int sid, String param){
		String name = UserMgr.getInstance().getRandomUserName();
		int avatarId = getRandomUserAvatar();
		String loginId = createLoginId();
		
		TUsers t = TUsers.create(loginId,
					TUsers.AttrSID.set(sid),
					LoginBasePacket.SID_FB == sid?TUsers.AttrFB_ID.set(param):LoginBasePacket.SID_PHONE == sid?TUsers.AttrPHONE.set(param):TUsers.AttrMAC_ID.set(param),
					TUsers.AttrNAME.set(name),
					TUsers.AttrAVATAR_ID.set(avatarId),
					TUsers.AttrCREATE_TIME.set(new Date()));
		if(t != null){
			map.remove(MathUtil.parseInt(t.LOGIN_ID));
			logger.debug("users.create users success, param= " + param);
			return t;
		}
		logger.error("users.create users is error, param= " + param);
		return null;
	}
	
	public static TUsers createUser(int sid, String countryCode, String phone){
		String name = UserMgr.getInstance().getRandomUserName();
		int avatarId = getRandomUserAvatar();
		String loginId = createLoginId();
		
		TUsers t = TUsers.create(loginId,
					TUsers.AttrSID.set(sid),
					TUsers.AttrCOUNTRY_CODE.set(countryCode),
					TUsers.AttrPHONE.set(phone),
					TUsers.AttrNAME.set(name),
					TUsers.AttrAVATAR_ID.set(avatarId),
					TUsers.AttrCREATE_TIME.set(new Date()));
		if(t != null){
			map.remove(MathUtil.parseInt(t.LOGIN_ID));
			logger.debug("users.create users success, phone={}", phone);
			return t;
		}
		logger.error("users.create users is error, phone={}", phone);
		return null;
	}
	
	private static int getRandomUserAvatar(){
    	Random random = new Random();
		int index = random.nextInt(35);
		int id = index + 1;
		
		return id;
    }
	
	public static final int MIN_USER_ID = 666001;
	public static final int MAX_USER_ID = 999999;
	
	private static String createLoginId(){
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			int index = random.nextInt(MAX_USER_ID - MIN_USER_ID + 1) + MIN_USER_ID;
			String loginId = Integer.toString(index);
			TUsers users = TUsers.getOneByCriteria(TUsers.AttrLOGIN_ID.eq(loginId));
			if (users == null) {
				return loginId;
			}
		}
		
		TraceLog.info("Users.createLoginId random loginId failed");
		return "666000";
	} 
}
