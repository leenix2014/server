package game.live.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.Anchor;
import com.mozat.morange.dbcache.tables.LiveRoom;

import game.common.CommonConfig;
import game.live.robot.LiveNameMgr;
import game.live.robot.Robot;
import netty.util.MathUtil;
import netty.util.StringUtil;

public class LiveRoomModel {
	
	private LiveRoom config;
	private Anchor anchor;
	private int msgCount = 0;
	private Set<Integer> admins = new HashSet<>();
    private Map<Integer, Date> users = new ConcurrentHashMap<>();
    private Set<Robot> robots = new HashSet<Robot>();
    private Set<Integer> leavingUser = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> offlineUser = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> silentUsers = Collections.synchronizedSet(new HashSet<Integer>());//被禁言用户
    private Map<Integer, Tiger> tigers = new ConcurrentHashMap<>();
    private Map<Integer, Fruit> fruits = new ConcurrentHashMap<>();
	
	public LiveRoomModel(LiveRoom config){
		this.setConfig(config);
	}
    
    public void join(int userId){
        users.put(userId, new Date());
    }
    
    public boolean leave(int userId){
    	return leavingUser.add(userId);
    }
    
    public void offline(int userId){
    	offlineUser.add(userId);
    }
    
    public void onlineAgain(int userId){
    	offlineUser.remove(userId);
	}

	public LiveRoom getConfig() {
		return config;
	}

	public void setConfig(LiveRoom config) {
		this.config = config;
		msgCount = config.MSG_COUNT;
		admins.clear();
		if(config.ADMIN_IDS != null && !StringUtil.isEmpty(config.ADMIN_IDS.trim())){
			String[] parts = config.ADMIN_IDS.trim().split(",");
			for(String part : parts){
				part = part.trim();
				int admin = MathUtil.parseInt(part);
				if(admin != 0) {
					admins.add(admin);
				}
			}
		}
		if("live".equals(config.ANCHOR_TYPE)){
    		int robotPerRoom = CommonConfig.getInt(CommonConfig.ROBOT_PERROOM, 5);
    		for(int i=0;i<robotPerRoom;i++){
    			Robot robot = new Robot(LiveNameMgr.randomName(), this);
    			robots.add(robot);
    			robot.takeAction();
    		}
		}
		anchor = Anchor.getOne(config.ANCHOR);
	}
	
	public Set<Integer> getRoomUser(){
		return users.keySet();
	}
	
	public boolean isPaid(int userId){
		Date payTime = users.get(userId);
		if(payTime == null){
			return false;
		}
		Date now = new Date();
		long interval = now.getTime() - payTime.getTime();
		int validHour = CommonConfig.getInt(CommonConfig.VALID_PAY_HOUR, 5);
		return interval <= validHour * 3600 * 1000;
    }
	
	public Set<Integer> getRoomUserExcept(int userId){
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(users.keySet());
		set.remove(userId);
		return set;
	}
	
	public boolean isAdmin(int userId){
		return admins.contains(userId);
	}

	public void silent(int userId){
		silentUsers.add(userId);
	}
	
	public void unsilent(int userId){
		silentUsers.remove(userId);
	}
	
	public boolean isSilent(int userId){
		return silentUsers.contains(userId);
	}

	public Anchor getAnchor() {
		return anchor;
	}

	public void addOnlineCount(){
		config.ONLINE_COUNT = config.ONLINE_COUNT + 1;
		config.update();
	}
	
	public int getOnlineCount(){
		return robots.size() + users.size();
	}
	
	public void addMsgCount(){
		msgCount++;
		if(msgCount % 100 == 0) {
			config.MSG_COUNT = msgCount;
			config.update();
		}
	}
	
	public int getMsgCount(){
		return msgCount;
	}
	
	public boolean stop(){
		config.STATUS = 1;
		config.END_TIME = new Date();
		if(!config.update()){
			return false;
		}
		for(Robot robot : robots){
			robot.stop();
		}
		return true;
	}
	
	public Tiger getTiger(int userId){
		Tiger tiger = tigers.get(userId);
		if(tiger == null){
			tiger = new Tiger();
			tigers.put(userId, tiger);
		}
		return tiger;
	}
	
	public Fruit getFruit(int userId){
		Fruit fruit = fruits.get(userId);
		if(fruit == null){
			fruit = new Fruit();
			fruits.put(userId, fruit);
		}
		return fruit;
	}
}
