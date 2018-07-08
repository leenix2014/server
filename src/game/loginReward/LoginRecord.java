package game.loginReward;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.TLoginRecord;
import com.mozat.morange.dbcache.util.ObjectCacheUtil;
import com.mozat.morange.dbcache.util.ObjectCacheUtilManager;

import netty.GameModels.UserMgr;

public class LoginRecord{
	private final static Logger logger = LoggerFactory.getLogger(LoginRecord.class);
	
	//<userId,LoginRecord>
	private static final ObjectCacheUtil<Integer, LoginRecord> map = ObjectCacheUtilManager.create();
	
	private final TLoginRecord tLoginRecord;
	
	//内存数据
	private Set<Integer> gotDays = new HashSet<Integer>();
	
	public LoginRecord(TLoginRecord tl) {
		this.tLoginRecord = tl;
		
		gotDays = getGotDaysByStr(this.tLoginRecord.GOT_REWART_DAY);
	}
	
	public int getUserId(){
		return tLoginRecord.USER_ID;
	}
	
	public int getLoginDay(){
		return tLoginRecord.LOGIN_DAY;
	}
	
	public void setLoginDay(int value){
		tLoginRecord.LOGIN_DAY = value;
	}
	
 	public Date getLastLoginDate(){
		return tLoginRecord.LAST_LOGIN_DATE;
	}
 	
 	public void setLastLoginDate(Date value){
		tLoginRecord.LAST_LOGIN_DATE = value;
	}
 	
 	public void setUserName(String userName){
 		tLoginRecord.USER_NAME = userName;
 	}
	
 	private Set<Integer> getGotDaysByStr(String str){
		Set<Integer> ret = new HashSet<Integer>();
		if (str == null || str.equals("") || str.isEmpty()) {
			return ret;
		}
		
		String temp[] = str.split("\\+");
		for (String unitStr : temp) {
			int day = Integer.parseInt(unitStr);
			ret.add(day);
		}
		
		return ret;
	}
	
	private String getGotDaysStrBySet(Set<Integer> set){
		StringBuilder builder = new StringBuilder();
		int size = set.size();
		int index = 0;
		for (Integer day : set) {
			builder.append(day);
			index++;
			if (index < size) {
				builder.append("+");
			}
		}
		
		return builder.toString();
	}
	
	public Set<Integer> getGotDays(){
		return gotDays;
	}
	
	public void setGotDays(Set<Integer> value){
		this.gotDays = value;
		String str = getGotDaysStrBySet(this.gotDays);
		tLoginRecord.GOT_REWART_DAY = str;
	}
	
	private static void removeCache(int userId){
		map.remove(userId);
	}
	
	public static LoginRecord create(int userId, int loginDay, String gotRewardDays, Date lastLoginDate){
		TLoginRecord ret = TLoginRecord.create(
				TLoginRecord.AttrUSER_ID.set(userId),
				TLoginRecord.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				TLoginRecord.AttrLOGIN_DAY.set(loginDay),
				TLoginRecord.AttrGOT_REWART_DAY.set(gotRewardDays),
				TLoginRecord.AttrLAST_LOGIN_DATE.set(lastLoginDate));
		if(ret == null){
			logger.error("LoginRecord.create LoginRecord is error, userId= " + userId);
			return null;
		}
		removeCache(ret.USER_ID);
		logger.debug("LoginRecord.create LoginRecord success, userId= " + userId);
		LoginRecord loginRecord = new LoginRecord(ret);
		return loginRecord;
	}
	
	public static LoginRecord load(int userId){
		LoginRecord record = map.get(userId);
		if(record != null){
			return record;
		}
		record = loadDB(userId);
		if (record == null) {
			record = create(userId, 0, "", new Date());
		}
		
		if (record != null) {
			map.set(record.getUserId(), record);
		}
		return record;
	}
	
	private static LoginRecord loadDB(int userId){
		TLoginRecord t = TLoginRecord.getOneByCriteria(TLoginRecord.AttrUSER_ID.eq(userId));
		
		if (t != null) {
			LoginRecord record = new LoginRecord(t);
			return record;
		}
		
		return null;
	}
	
	public boolean update(){
		boolean ret = tLoginRecord.update();
		if(ret){
			removeCache(tLoginRecord.USER_ID);
			logger.debug("LoginRecord.update success, userId = " + tLoginRecord.USER_ID);
		}else{
			logger.debug("LoginRecord.update failed, userId = " + tLoginRecord.USER_ID);
		}
		return ret;
	}
}
