package game.phoneconfirm.packet;


import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.Packet;
import game.packet.PacketManager;
import game.phoneconfirm.PhoneConfirmManager;
import protocols.header.packet.Builder;
import protocols.phoneconfirm.confirmrequest;

public class ConfirmRequestPacket extends Packet{
	//请求
	confirmrequest.request request;
	
	//下发
	public int error;
	
	@Override
	public void execPacket(){
		String phoneNumber = request.getPhonenumber();
		int error = 0;
		if(!PhoneConfirmManager.phoneRequestToConfirm(phoneNumber)){
			error = 1;
			errDesc = "获取验证码失败，请稍后重试";
			logger.info("User({}) get verify code error.", userId);
		}
		
		ConfirmRequestPacket confirmRequestPacket = new ConfirmRequestPacket();
    	confirmRequestPacket.error = error;
    	PacketManager.send(session, confirmRequestPacket);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
	     request = confirmrequest.request.parseFrom(bytes);
	     logger.info("User({}) request verify code", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		confirmrequest.response.Builder builder = confirmrequest.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
