package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.task.TaskService;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.changename;

public class ChangeNamePacket extends Packet{
	//请求
	changename.request requset;
	
	//下发
	public int error;
	
	@Override
	public void execPacket() {
		String name = requset.getNewname();
		TUsers users = Users.load(userId);
		if (users == null) {
			//无此账号
			this.error = 1;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			return;
		}
		
		users.NAME = name;
		if (!users.update()) {
			error = 1;
			errDesc = "修改昵称失败，请稍后再试";
			PacketManager.send(userId, this);
			logger.info("User({}) change name to {} failed because update failed!", userId, name);
			return;
		}
		
		//添加任务计数
		TaskService.addTaskDoneCount(userId, TaskService.TASK_TYPE_CHANGE_NAME, 1);
		
		this.error = 0;
		PacketManager.send(userId, this);
		logger.info("User({}) change name to {} success!", userId, name);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		requset = changename.request.parseFrom(bytes);
		logger.info("User({}) request change name", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		changename.response.Builder builder = changename.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
