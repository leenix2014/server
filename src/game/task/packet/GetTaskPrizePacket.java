package game.task.packet;

import java.util.Date;

import com.mozat.morange.dbcache.tables.RoomCardBill;

import game.packet.Packet;
import game.packet.PacketManager;
import game.task.TaskConfig;
import game.task.TaskData;
import game.task.TaskRecord;
import game.task.TaskService;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.task.gettaskprize;
import protocols.task.taskdata;

public class GetTaskPrizePacket extends Packet{
	//请求
	gettaskprize.reconRequest request;
	
	//下发
	public int error;
	public int taskId;
	public int roomCard;
	public TaskData data = null;

	@Override
	public void execPacket(){
		int taskId = request.getTaskId();
		TaskConfig config = TaskConfig.getTaskConfig(taskId);
		if (config == null) {
			GetTaskPrizePacket packet = new GetTaskPrizePacket();
			packet.error = 1;
			packet.errDesc = "任务不存在";
			packet.taskId = -1;
			packet.roomCard = -1;
			PacketManager.send(userId, packet);
			logger.info("User({}) get task({}) prize failed because task not exist", userId, request.getTaskId());
			return;
		}
		int type = config.getType();
		TaskRecord record = TaskRecord.load(userId, type);
		if (record == null) {
			GetTaskPrizePacket packet = new GetTaskPrizePacket();
			packet.error = 1;
			packet.errDesc = "任务未完成";
			packet.taskId = -1;
			packet.roomCard = -1;
			PacketManager.send(userId, packet);
			logger.info("User({}) get task({}) prize failed because load task record failed", userId, request.getTaskId());
			return;
		}
		
		if (record.getIsGotPrize() == 1 || record.getDoneCount() < config.getFinishCount()) {
			GetTaskPrizePacket packet = new GetTaskPrizePacket();
			packet.error = 1;
			packet.errDesc = "任务未完成或已领取过奖励";
			packet.taskId = -1;
			packet.roomCard = -1;
			PacketManager.send(userId, packet);
			logger.info("User({}) get task({}) prize failed because task not finished or already got prize", userId, request.getTaskId());
			return;
		}
		
		//发奖励
		int roomCard = config.getRoomCard();
		int oldRoomCardCount = UserMgr.getInstance().getCuber(userId);
		int nowRoomCardCount = oldRoomCardCount + roomCard;
		UserMgr.getInstance().setCuber(userId, nowRoomCardCount);
		
		record.setIsGotPrize(1);
		record.update();
		
		RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(userId+""), 
				RoomCardBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				RoomCardBill.AttrSOURCE.set("task"),
				RoomCardBill.AttrSOURCE_ID.set(taskId+""),
				RoomCardBill.AttrSOURCE_NAME.set(config.getDesc()),
				RoomCardBill.AttrAMOUNT.set(roomCard),
				RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
				RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
				RoomCardBill.AttrCREATE_TIME.set(new Date()));
		
		//调用一下更新任务
		TaskRecord newTaskRecord = TaskService.getCurTaskByType(userId, type);
		
		GetTaskPrizePacket packet = new GetTaskPrizePacket();
		packet.error = 0;
		packet.taskId = taskId;
		packet.roomCard = roomCard;
		
		int newTaskId = newTaskRecord.getCurTaskId();
		if(newTaskId != taskId){
			TaskConfig newConfig = TaskConfig.getTaskConfig(newTaskId);
			if (newConfig != null) {
				TaskData data = new TaskData();
				data.taskId = newTaskId;
				data.desc = "en_US".equals(request.getLang())? newConfig.getDescEn():newConfig.getDesc();
				data.doneCount = newTaskRecord.getDoneCount();
				data.finishCount = newConfig.getFinishCount();
				data.roomCard = newConfig.getRoomCard();
				data.isGotPrize = newTaskRecord.getIsGotPrize();
				data.taskType = newConfig.getType();
				packet.data = data;
			}
		}
		
		PacketManager.send(userId, packet);
		logger.info("User({}) get task({}) prize success, reward room card:{}", userId, request.getTaskId(), roomCard);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		request = gettaskprize.reconRequest.parseFrom(bytes);
		logger.info("User({}) request get task({}) prize", userId, request.getTaskId());
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		gettaskprize.reconResponse.Builder builder = gettaskprize.reconResponse.newBuilder();
		builder.setError(this.error);
		builder.setErrDesc(errDesc);
		builder.setTaskId(this.taskId);
		builder.setRoomCard(this.roomCard);
		
		if (data != null) {
			taskdata.taskdata_t.Builder dataBuilder = taskdata.taskdata_t.newBuilder();
			dataBuilder.setTaskId(data.taskId);
			dataBuilder.setDesc(data.desc);
			dataBuilder.setDoneCount(data.doneCount);
			dataBuilder.setFinishCount(data.finishCount);
			dataBuilder.setRoomCard(data.roomCard);
			
			builder.setNexttask(dataBuilder);
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());					
	}
}
