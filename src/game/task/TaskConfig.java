package game.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.TTaskConfig;

public class TaskConfig{
	//<taskId,TaskConfig>
	private static Map<Integer, TaskConfig> configMap = new ConcurrentHashMap<Integer, TaskConfig>();
	//<type,TaskIdList>
	private static Map<Integer, List<Integer>> taskIdMap = new ConcurrentHashMap<Integer, List<Integer>>();
	
	private static List<Integer> taskTypeList = new Vector<Integer>();
	
	private final TTaskConfig tTaskConfig;
	
	public static void loadAll(){
		configMap.clear();
		taskIdMap.clear();
		taskTypeList.clear();
		ArrayList<TTaskConfig> tConfs = TTaskConfig.getAllObjects();
		if (tConfs != null) {
			for(TTaskConfig tConf : tConfs){
				TaskConfig conf = new TaskConfig(tConf);
				//放入configMap
				configMap.put(tConf.TASK_ID, conf);
				
				//放入taskIdMap
				int type = tConf.TYPE;
				List<Integer> taskIdList = taskIdMap.get(type);
				if (taskIdList == null) {
					taskIdList = new ArrayList<Integer>();
					taskIdMap.put(type, taskIdList);
					taskTypeList.add(type);
				}
				taskIdList.add(tConf.TASK_ID);
			}
			
			//对taskIdMap里面list排序
			for (List<Integer> taskIdList : taskIdMap.values()) {
				Collections.sort(taskIdList);
			}
		}
	}
	
	public TaskConfig(TTaskConfig t) {
		this.tTaskConfig = t;
	}
	
	public String getDesc(){
		return tTaskConfig.DESC_ZH;
	}
	
	public String getDescEn(){
		return tTaskConfig.DESC_EN;
	}
	
	public int getOrder(){
		return tTaskConfig.SHOW_ORDER;
	}
	
	public int getFinishCount(){
		return tTaskConfig.FINISH_COUNT;
	}
	
	public int getRoomCard(){
		return tTaskConfig.ROOM_CARD;
	}
	
	public int getType(){
		return tTaskConfig.TYPE;
	}
	
 	public static TaskConfig getTaskConfig(int taskId){
		return configMap.get(taskId);
	}
	
 	public static String getTaskConfigDesc(int taskId){
 		TaskConfig config = configMap.get(taskId);
		return config == null?"":config.getDesc();
	}
 	
	public static int getFirstTaskIdByType(int type){
		List<Integer> taskIdList = taskIdMap.get(type);
		if (taskIdList == null || taskIdList.size() <= 0) {
			return -1;
		}
		
		return taskIdList.get(0);
	}
	
	public static int getNextTaskId(int type, int taskId){
		List<Integer> taskIdList = taskIdMap.get(type);
		if (taskIdList == null || taskIdList.size() <= 0) {
			return -1;
		}
		for (Integer tempTaskId : taskIdList) {
			if (tempTaskId > taskId) {
				return tempTaskId;
			}
		}
		
		return -1;
	}
	
	public static List<Integer> getTaskTypeList(){
		return taskTypeList;
	}
}
