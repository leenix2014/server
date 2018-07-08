package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.util.MD5;

import game.packet.Packet;
import game.packet.PacketManager;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.changepassword;

public class ChangePasswordPacket extends Packet{
	//请求
	private String oldPwd;
	private String newPwd;
	
	//下发
	private int error;
	private String errDesc = "";
	
	@Override
	public void execPacket() {
		TUsers users = Users.load(userId);
		if (users == null) {
			//无此账号
			this.error = 99;
			errDesc = "你已离线,请重新登录";
			PacketManager.send(session, this);
			return;
		}
		
		String realPwd = users.PWD;
		if(!MD5.getHashString(realPwd).equals(oldPwd)){
			error = 2;//密码不正确
			errDesc = "旧密码不正确";
			PacketManager.send(session, this);
			logger.info("User({}) change password failed because old pwd error", userId);
			return;
		}
		if (realPwd.equals(newPwd)) {
			//新密码和旧密码相同
			this.error = 1;
			errDesc = "新密码和旧密码相同";
			PacketManager.send(session, this);
			logger.info("User({}) change password failed because old same as new", userId);
			return;
		}
		
		users.PWD = newPwd;
		if (!users.update()) {
			error = 98;
			errDesc = "服务器更新失败";
			logger.info("User({}) change password failed because update failed", userId);
			PacketManager.send(session, this);
			return;
		}
		
		this.error = 0;
		PacketManager.send(session, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		changepassword.request request = changepassword.request.parseFrom(bytes);
		newPwd = request.getNewpassword();
		oldPwd = request.getOldPwd();
		logger.info("User({}) request change password", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		changepassword.response.Builder builder = changepassword.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
