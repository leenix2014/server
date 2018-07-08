package game.task.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.packet.Packet;
import game.packet.PacketManager;
import game.task.TaskConfig;
import game.task.TaskData;
import game.task.TaskRecord;
import game.task.TaskService;
import protocols.header.packet.Builder;
import protocols.task.taskdata;

public class TaskDataPacket extends Packet{
	//请求
	taskdata.request request;
	
	String lang;
	
	//下发
	private List<TaskData> taskDatas;
	
	@Override
	public void execPacket(){
		List<TaskData> taskDatas = new ArrayList<TaskData>();
		
		List<Integer> taskTypeList = TaskConfig.getTaskTypeList();
		for (Integer taskType: taskTypeList) {
			TaskRecord record = TaskService.getCurTaskByType(userId, taskType);
			if (record == null) continue;
			TaskConfig config = TaskConfig.getTaskConfig(record.getCurTaskId());
			if (config == null) continue;
			
			TaskData data = new TaskData();
			data.taskId = record.getCurTaskId();
			data.desc = "en_US".equals(lang)?config.getDescEn():config.getDesc();
			data.doneCount = record.getDoneCount()>config.getFinishCount()?config.getFinishCount():record.getDoneCount();
			data.finishCount = config.getFinishCount();
			data.roomCard = config.getRoomCard();
			data.isGotPrize = record.getIsGotPrize();
			data.taskType = config.getType();
			data.order = config.getOrder();
			
			taskDatas.add(data);
		}
		
		TaskDataPacket packet = new TaskDataPacket();
		packet.setTaskDatas(taskDatas);
		PacketManager.send(userId, packet);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		request = taskdata.request.parseFrom(bytes);
		lang = request.getLang();
		logger.info("User({}) get task data.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		taskdata.response.Builder builder = taskdata.response.newBuilder();
		Collections.sort(taskDatas);
		for (TaskData data : taskDatas) {
			taskdata.taskdata_t.Builder dataBuilder = taskdata.taskdata_t.newBuilder();
			dataBuilder.setTaskId(data.taskId);
			dataBuilder.setDesc(data.desc);
			dataBuilder.setDoneCount(data.doneCount);
			dataBuilder.setFinishCount(data.finishCount);
			dataBuilder.setRoomCard(data.roomCard);
			dataBuilder.setIsGotPrize(data.isGotPrize);
			dataBuilder.setTaskType(data.taskType);
			
			builder.addDatas(dataBuilder);
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());			
	}

	public void setTaskDatas(List<TaskData> datas){
		this.taskDatas = datas;
	}
	
}
