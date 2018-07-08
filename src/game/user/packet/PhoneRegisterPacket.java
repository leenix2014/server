package game.user.packet;

import java.net.InetSocketAddress;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.log.TraceLog;

import game.packet.PacketManager;
import game.phoneconfirm.PhoneConfirmManager;
import game.user.Users;
import io.netty.channel.Channel;
import protocols.header.packet.Builder;
import protocols.user.phoneregister;

public class PhoneRegisterPacket extends LoginBasePacket{
	//请求
	private String phoneNumber;
	private String password;
	private String code;
	
	@Override
	public void execPacket() {
		if (!PhoneConfirmManager.isValid(countryCode+phoneNumber, code)) {
			error = 1;
			errDesc = "验证码错误或已失效";
			logger.info("User({}) verify code wrong.", userId);
			PacketManager.send(session, this);
			return;
		}
		
		TUsers user = TUsers.getOneByCriteria(TUsers.AttrCOUNTRY_CODE.eq(countryCode),
				TUsers.AttrPHONE.eq(phoneNumber));
		if (user == null) {
			user = Users.createUser(sid, countryCode, phoneNumber);
			
			//首次登录，设置首次登录ip
			if (user != null) {
				newUser = true;
				Channel channel = session.getChannel();
				InetSocketAddress insocket = (InetSocketAddress)channel.remoteAddress();
		        String clientIP = insocket.getAddress().getHostAddress();
				user.REG_IP = clientIP;
				user.OS = platform;
				user.update();
			}
		}
			
		if (user == null) {
			TraceLog.info("PhoneRegisterPacket.execPacket createByPhone users is null,phoneNumber=" + phoneNumber);
			error = 2;
			errDesc = "注册账号失败";
			PacketManager.send(session, this);
			return;
		}
		
		user.PWD = password;
		user.update();
		logger.info("User({}) register phone:{} success!", user.LOGIN_ID, phoneNumber);
		
		super.login();
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		phoneregister.request request = phoneregister.request.parseFrom(bytes);
		this.sid = LoginBasePacket.SID_PHONE;
		this.uuid = "";
		phoneNumber = request.getPhonenumber();
		this.token = phoneNumber;
		this.countryCode = request.getCountryCode();
		password = request.getPassword();
		this.extra = password;
		this.platform = request.getPlatform()==1?PLATFORM_IOS:PLATFORM_ANDROID;
		this.appVer = request.getAppVer();
		this.code = request.getCode();
		logger.info("User({}) request register phone:{}", userId, phoneNumber);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		phoneregister.response.Builder builder = phoneregister.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
