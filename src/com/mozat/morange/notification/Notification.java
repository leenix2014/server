package com.mozat.morange.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.TNewNotification;
import com.mozat.morange.dbcache.util.ObjectCacheUtil;
import com.mozat.morange.dbcache.util.ObjectCacheUtilManager;

public class Notification {

	//private static final String key = "notification_";
	private static final Logger logger = LoggerFactory.getLogger(Notification.class);
	private static final ObjectCacheUtil<Integer, List<Notification>> cachedMap = ObjectCacheUtilManager.create();

	/*
	private int id;
	private int monetId;
	private String content;
	private boolean hasBeenSent;
	private String parameter;
	private Date updateTime;
	private Date sendTime;
	private int type;
	*/
	
	TNewNotification tNotification;

	public int getId(){
		return tNotification.id;
	}
	
	public int getMonetId() {
		return this.tNotification.monetId;
	}

	public String getContent() {
		return this.tNotification.content;
	}

	public Date getUpdateTime() {
		return this.tNotification.updateTime;
	}

	public boolean getHasBeenSent() {
		return this.tNotification.hasBeenSent;
	}

	public int getType() {
		return this.tNotification.type;
	}

	public Date getSendTime() {
		return this.tNotification.sendTime;
	}

	public String getParameter() {
		return this.tNotification.parameter;
	}

	private static List<Notification> loadCache(int monetId) {
		//return (List<Notification>) Global.getCache().getObjectValue(key + monetId);
		return cachedMap.get(monetId);
	}

	/*
	private static void removeCache(int monetId) {
		Global.getCache().remove(key + monetId);
	}
	*/

	private static void setCache(int monetId, List<Notification> n) {
		//Global.getCache().set(key + monetId, n);
		cachedMap.set(monetId, n);
	}

	static public Notification findById(int monetId, int id){
		List<Notification> list = load(monetId);
		
		for(Notification n : list){
			if(n.tNotification.id == id){
				return n;
			}
		}
		return null;
	}
	
	public static List<Notification> load(int monetId) {
		List<Notification> n = loadCache(monetId);
		if (n != null) {
			return n;
		}
		n = loadDB(monetId);
		if (n != null) {
			setCache(monetId, n);
		}
		return n;
	}

	private static List<Notification> loadDB(int monetId) {
		/*
		try {
			DBResultSet ds = Global.modb.execSQLQuery("SELECT [id],[monetId],[type],[content],[parameter],[updateTime],[hasBeenSent],[sendTime] FROM [newNotification] where monetId = ?", new Object[]{monetId});
			List<Notification> notificationList = new ArrayList<Notification>();
			if (ds != null) {
				while (ds.next()) {
					Notification n = new Notification();
					n.id = ds.getInt("id");
					n.monetId = ds.getInt("monetId");
					n.type = ds.getInt("type");
					n.content = ds.getString("content");
					n.parameter = ds.getString("parameter");
					n.updateTime = ds.getDate("updateTime");
					n.sendTime = ds.getDate("sendTime");
					n.hasBeenSent = ds.getBoolean("hasBeenSent");

					notificationList.add(n);
				}
			} else {
				logger.error("[Notification]load db is null, monetId = " + monetId);
			}
			return notificationList;
		} catch (Exception e) {
			logger.error("[Notification]load db is wrong, monetId = " + monetId, e);
		}
		return null;
		*/
		
		List<Notification> notificationList = new ArrayList<Notification>();
		
		List<TNewNotification> tList = TNewNotification.getManyByCriteria(TNewNotification.AttrMonetId.eq(monetId));
		for (TNewNotification t: tList){
			Notification n = new Notification();
			n.tNotification = t;
			
			notificationList.add(n);
		}
		
		return notificationList;
	}

	public static TNewNotification create(int monetId, int type, String content, String parameter, boolean hasBeenSent, Date sendTime) {
		/*
		boolean ret = createDB(monetId, type, content, parameter, hasBeenSent, sendTime);
		if (ret) {
			removeCache(monetId);
			return true;
		}
		*/
		
		TNewNotification t = createDB(monetId, type, content, parameter, hasBeenSent, sendTime);
		if (t != null){
			/*
			Notification n = new Notification();
			n.tNotification = t;
			
			List<Notification> nList = load(monetId);
			if (null != nList){
				nList.add(n);
			}
			else{
				nList = new ArrayList<Notification>();
				nList.add(n);
				setCache(monetId, nList);
			}
			*/

			cachedMap.remove(monetId);
			
			return t;
		}
		
		logger.error("[Notification]create is failure, monetId = " + monetId + ", content = " + content + ", hasBeenSent = " + hasBeenSent);
		return null;
	}

	private static TNewNotification createDB(int monetId, int type, String content, String parameter, boolean hasBeenSent, Date sendTime) {
		/*
		try {
			return Global.getModb().execSQLUpdate("insert into newNotification(monetId, type, content, parameter, createTime, hasBeenSent, sendTime, updateTime)" +
							" values(?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[]{monetId, type, content, parameter, new Date(), hasBeenSent, sendTime, new Date()}) > 0;
		} catch (Exception e) {
			logger.error("[Notification]create db is wrong, monetId = " + monetId + ", content = " + content + ", hasBeenSent = " + hasBeenSent + ",type=" + type, e);
		}
		return false;
		*/
		
		Date now = new Date();
		
		TNewNotification t = TNewNotification.create(
				TNewNotification.AttrMonetId.set(monetId),
				TNewNotification.AttrType.set(type),
				TNewNotification.AttrContent.set(content),
				TNewNotification.AttrParameter.set(parameter),
				TNewNotification.AttrCreateTime.set(now),
				TNewNotification.AttrHasBeenSent.set(hasBeenSent),
				TNewNotification.AttrSendTime.set(sendTime),
				TNewNotification.AttrUpdateTime.set(now)
				);
		
		return t;
	}

	private boolean update() {
		//updateTime = new Date();
		this.tNotification.updateTime = new Date();
		boolean ret = updateDB();
		if (ret) {
			return true;
		}
		//removeCache(monetId);
		logger.error("[Notification]update is wrong, monetId = " + this.getMonetId() + ",type=" + this.getType());
		return false;
	}

	private boolean updateDB() {
		/*
		try {
			return Global.modb.execSQLUpdate("update newNotification set content = ?, parameter = ?, updateTime = ?, hasBeenSent = ?, sendTime = ? where id = ?", new Object[]{
					content, parameter, updateTime, hasBeenSent, sendTime, id}) > 0;
		} catch (Exception e) {
			logger.error("[Notification]update db is wrong, monetId = " + monetId + ",type=" + type, e);
		}
		return false;
		*/
		
		return this.tNotification.update();
	}

	public boolean setNotification(String content, String parameter, Date sendTime, boolean hasBeenSent) {
		this.tNotification.content = content;
		this.tNotification.parameter = parameter;
		this.tNotification.sendTime = sendTime;
		this.tNotification.hasBeenSent = hasBeenSent;
		return update();
	}

	public boolean read() {
		this.tNotification.hasBeenSent = true;
		this.tNotification.sendTime = new Date();
		return this.update();
	}
	
	public static long getLastSendTime(int monetId){
		List<Notification> list = Notification.load(monetId);
		if(0 >= list.size()){
			return 0;
		}
		Date date = new Date(0);
		for(Notification n : list){
			if(n.tNotification.hasBeenSent && n.tNotification.sendTime.after(date)){
				date = n.tNotification.sendTime;
			}
		}
		
		return date.getTime();
	}
}
