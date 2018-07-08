package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.AppVersion;

import game.packet.Packet;
import game.packet.PacketManager;
import netty.util.StringUtil;
import protocols.header.packet.Builder;
import protocols.user.checkupdate;

public class CheckUpdatePacket extends Packet{
	//请求
	private String platform;
	private String clientVer;
	
	//下发
	private boolean needUpdate;
	private String latestVer = "";
	private String appUrl = "";
	
	@Override
	public void execPacket() {
		AppVersion latestApp = AppVersion.getOneByCriteria(AppVersion.AttrPLATFORM.eq(platform),
				AppVersion.AttrLATEST.eq(true));
		
		if(latestApp != null && StringUtil.versionBiggerThan(latestApp.APP_VER, clientVer)){
			needUpdate = true;
			latestVer = latestApp.APP_VER;
			appUrl = latestApp.DOWNLOAD_URL;
		} else {
			needUpdate = false;
			latestVer = clientVer;
		}
		
		error = 0;
		PacketManager.send(session, this);
		logger.info("User({}) check update. client version={}, latest version={}, needUpdate={}", userId, clientVer, latestVer, needUpdate);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		checkupdate.request req = checkupdate.request.parseFrom(bytes);
		clientVer = req.getAppVer();
		platform = req.getPlatform()==1?LoginBasePacket.PLATFORM_IOS:LoginBasePacket.PLATFORM_ANDROID;
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		checkupdate.response.Builder builder = checkupdate.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setNeedUpdate(needUpdate);
		builder.setLatestVer(latestVer);
		builder.setAppUrl(appUrl);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
