package game.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.TTaskRecord;
import com.mozat.morange.dbcache.util.ObjectCacheUtil;
import com.mozat.morange.dbcache.util.ObjectCacheUtilManager;

import netty.GameModels.UserMgr;

public class TaskRecord{
	private final static Logger logger = LoggerFactory.getLogger(TaskRecord.class);
	
	//<userId+taskType,TaskRecord>
	private static final ObjectCacheUtil<String, TaskRecord> map = ObjectCacheUtilManager.create();
	
	private final TTaskRecord tTaskRecord;
	
	private static String getKey(int userId, int taskType){
		return userId + "_" + taskType;
	}
	
	public TaskRecord(TTaskRecord t) {
		this.tTaskRecord = t;
	}
	
	public int getUserId(){
		return tTaskRecord.USER_ID;
	}
	
	public int getType(){
		return tTaskRecord.TYPE;
	}
	
	public void setCurTaskId(int value){
		tTaskRecord.CUR_TASK_ID = value;
	}
	
	public int getCurTaskId(){
		return tTaskRecord.CUR_TASK_ID;
	}
	
	public void setDoneCount(int value){
		tTaskRecord.DONE_COUNT = value;
	}
	
	public int getDoneCount(){
		return tTaskRecord.DONE_COUNT;
	}
	
	public void setIsGotPrize(int value){
		tTaskRecord.IS_GOT_PRIZE = value;
	}
	
	public int getIsGotPrize(){
		return tTaskRecord.IS_GOT_PRIZE;
	}
	
	public static TaskRecord create(int userId, int type, int curTaskId){
		TTaskRecord ret = TTaskRecord.create(
				TTaskRecord.AttrUSER_ID.set(userId),
				TTaskRecord.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				TTaskRecord.AttrTYPE.set(type),
				TTaskRecord.AttrCUR_TASK_ID.set(curTaskId),
				TTaskRecord.AttrCUR_TASK_NAME.set(TaskConfig.getTaskConfigDesc(curTaskId)),
				TTaskRecord.AttrDONE_COUNT.set(0),
				TTaskRecord.AttrIS_GOT_PRIZE.set(0));
		if(ret != null){
			map.remove(getKey(ret.USER_ID, ret.TYPE));
			logger.debug("TaskRecord.create TaskRecord success, userId= " + userId + ",type=" + type);
			TaskRecord record = new TaskRecord(ret);
			return record;
		}
		logger.error("TaskRecord.create TaskRecord error, userId= " + userId + ",type=" + type);
		return null;
	}
	
	public static TaskRecord load(int userId, int type){
		TaskRecord record = map.get(getKey(userId, type));
		if(record != null){
			return record;
		}
		record = loadDB(userId, type);
		if(record != null){
			return record;
		}
		int curTaskId = TaskConfig.getFirstTaskIdByType(type);
		if (curTaskId == -1) {
			return null;
		}
		record = create(userId, type, curTaskId);
		if(record == null){
			return null;
		}
		map.set(getKey(record.getUserId(), record.getType()), record);
		return record;
	}
	
	private static TaskRecord loadDB(int userId, int type){
		TTaskRecord t = TTaskRecord.getOneByCriteria(TTaskRecord.AttrUSER_ID.eq(userId),
				TTaskRecord.AttrTYPE.eq(type));
		if(t == null){
			return null;
		}
		TaskRecord record = new TaskRecord(t);
		return record;
	}
	
	public boolean update(){
		boolean ret = tTaskRecord.update();
		if(ret){
			map.remove(getKey(tTaskRecord.USER_ID , tTaskRecord.TYPE));
			logger.debug("TaskRecord.update success, userId = " + tTaskRecord.USER_ID);
		}else{
			logger.debug("TaskRecord.update failed, userId = " + tTaskRecord.USER_ID);
		}
		return ret;
	}
}
