package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import game.session.SessionManager;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.changelang;

public class ChangeLangPacket extends Packet{
	//请求
	private String lang;
	
	@Override
	public void execPacket() {
		TUsers user = Users.load(userId);
		if (user == null) {
			//无此账号
			this.error = 1;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			return;
		}
		
		user.LANG = lang;
		user.update();
		
		this.error = 0;
		SessionManager.getSession(userId).setLang(lang);
		PacketManager.send(userId, this);
		logger.info("User({}) change name to {} success!", userId, lang);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		changelang.request req = changelang.request.parseFrom(bytes);
		lang = req.getLang();
		logger.info("User({}) request change language.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		changelang.response.Builder builder = changelang.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setLang(lang);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
