package game.task;

import com.mozat.morange.log.TraceLog;

public class TaskService {
	public static final int TASK_TYPE_BULL_WIN = 1;//斗牛胜利任务
	public static final int TASK_TYPE_CREATE_ROOM = 2;
	public static final int TASK_TYPE_CHANGE_AVATAR = 3;
	public static final int TASK_TYPE_CHANGE_NAME = 4;
	public static final int TASK_TYPE_SEPCAIL_CARD = 5;//
	public static final int TASK_TYPE_SHARE = 6;
	public static final int TASK_TYPE_35CARD_WIN = 7;//35张胜利任务
		
	public static void init(){
		//初始化登陆奖励配置
		TaskConfig.loadAll();
	}
	
	public static TaskRecord getCurTaskByType(int userId, int type){
		TaskRecord record = TaskRecord.load(userId, type);
		if (record == null) {
			return null;
		}
		
		int nextTaskId = TaskConfig.getNextTaskId(type, record.getCurTaskId());
		
		if (record.getIsGotPrize() == 1 && nextTaskId != -1) {
			record.setCurTaskId(nextTaskId);
			record.setDoneCount(0);
			record.setIsGotPrize(0);
			record.update();
		}
		
		return record;
	}
	
	/**
	 * 做好一个任务调用一下这个借口，count为完成的次数，可参考TASK_TYPE_CREATE_ROOM创建房间任务的使用
	 * ueseId,用户唯一id
	 * taskType,任务类型  如TaskService.TASK_TYPE_WIN
	 * count,完成的次数
	 */
	public static boolean addTaskDoneCount(int userId, int taskType, int count){
		TaskRecord record = getCurTaskByType(userId, taskType);
		if(record == null){
			TraceLog.info("TaskService.addTaskDoneCount getCurTaskByType record is null,userId=" + userId + ",taskType=" + taskType);
			return false;
		}
		
		int doneCount = record.getDoneCount() + count;
		record.setDoneCount(doneCount);
		return record.update();
	}
}