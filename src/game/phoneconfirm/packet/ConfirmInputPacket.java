package game.phoneconfirm.packet;


import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.Packet;
import game.packet.PacketManager;
import game.phoneconfirm.PhoneConfirmManager;
import protocols.header.packet.Builder;
import protocols.phoneconfirm.confirminput;

public class ConfirmInputPacket extends Packet{
	//请求
	confirminput.request request;
	
	//下发
	public int error;
	private String errDesc = "";
	
	@Override
	public void execPacket(){
		String phoneNumber = request.getPhoneNumber();
		String code = request.getCode();
		if (!PhoneConfirmManager.isValid(phoneNumber, code)) {
			error = 1;
			errDesc = "验证码错误或已失效";
			PacketManager.send(session, this);
			logger.info("User({}) verify code wrong.", userId);
			return;
		}
		
		error = 0;
    	PacketManager.send(session, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		request = confirminput.request.parseFrom(bytes);
		logger.info("User({}) request confirm verify code", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		confirminput.response.Builder builder = confirminput.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
