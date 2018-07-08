package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.task.TaskService;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.changeavatar;

public class ChangeAvatarPacket extends Packet{
	//请求
	changeavatar.request requset;
	
	//下发
	public int error;
	
	@Override
	public void execPacket() {
		int newAvatarId = requset.getNewavatarid();
		if (newAvatarId < 1 || newAvatarId > 35) {
			//头像id超出范围
			this.error = 1;
			errDesc = "没有此头像";
			PacketManager.send(userId, this);
			return;
		}
		
		TUsers users = Users.load(userId);
		if (users == null) {
			//无此账号
			this.error = 1;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			return;
		}
		
		users.AVATAR_ID = newAvatarId;
		if (!users.update()) {
			error = 1;
			errDesc = "修改头像失败，请稍后再试";
			PacketManager.send(userId, this);
			logger.info("User({}) change avatar to {} failed because update failed!", userId, newAvatarId);
			return;
		}
		
		//添加任务计数
		TaskService.addTaskDoneCount(userId, TaskService.TASK_TYPE_CHANGE_AVATAR, 1);
		
		this.error = 0;
		PacketManager.send(userId, this);
		logger.info("User({}) change avatar to {} success!", userId, newAvatarId);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		requset = changeavatar.request.parseFrom(bytes);
		logger.info("User({}) request change avatar", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		changeavatar.response.Builder builder = changeavatar.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
