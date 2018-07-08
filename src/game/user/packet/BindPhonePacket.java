package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.core.TableBase;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.phoneconfirm.PhoneConfirmManager;
import netty.util.StringUtil;
import protocols.header.packet.Builder;
import protocols.user.bindphone;

public class BindPhonePacket extends Packet{
	//请求
	private String countryCode;
	private String phoneNumber;
	private String code;
	private String pwd;
	
	//下发
	private int error;
	private String errDesc = "";
	
	@Override
	public void execPacket() {
		if (!PhoneConfirmManager.isValid(countryCode+phoneNumber, code)) {
			error = 1;
			errDesc = "验证码错误";
			logger.info("User({}) verify code wrong.", userId);
			PacketManager.send(userId, this);
			return;
		}
		TUsers user = TUsers.getOneByCriteria(TUsers.AttrCOUNTRY_CODE.eq(countryCode), TUsers.AttrPHONE.eq(phoneNumber));
		if(user != null && !(""+userId).equals(user.LOGIN_ID)){
			error = 2;
			errDesc = "该手机号码已被其他用户绑定";
			logger.info("User({}) bind phone failed because phone number used in User({})", userId, user.LOGIN_ID);
			PacketManager.send(userId, this);
			return;
		}
		
		TableBase.AttributeValue<?>[] updates;
		if(StringUtil.isEmpty(pwd)){
			updates = TUsers.valueList(TUsers.AttrMAC_ID.set(""), TUsers.AttrCOUNTRY_CODE.set(countryCode), TUsers.AttrPHONE.set(phoneNumber));
		} else {
			updates = TUsers.valueList(TUsers.AttrPWD.set(pwd), TUsers.AttrMAC_ID.set(""), TUsers.AttrCOUNTRY_CODE.set(countryCode), TUsers.AttrPHONE.set(phoneNumber));
		}
		
		TUsers.updateByCriteria(updates, TUsers.AttrLOGIN_ID.eq(userId+""));
		logger.info("User({}) bind phone({}) success.", userId, phoneNumber);
		error = 0;
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		bindphone.request request = bindphone.request.parseFrom(bytes);
		countryCode = request.getCountryCode();
		phoneNumber = request.getPhonenumber();
		code = request.getCode();
		pwd = request.getPwd();
		logger.info("User({}) request bind phone:{}", userId, request.getPhonenumber());
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		bindphone.response.Builder builder = bindphone.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCountryCode(countryCode);
		builder.setPhonenumber(phoneNumber);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
