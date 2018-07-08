package game.live;

import java.util.Collection;

import com.mozat.morange.dbcache.tables.LiveGift;

import game.common.CommonConfig;
import game.live.model.LiveGiftMgr;
import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.live.common.gift_t;
import protocols.live.getgifts;

public class GetGiftsPacket extends Packet{
	//请求
	private String clientGiftVer;
	
	//下发
	private int error;
	private boolean needUpdate;
	private String latestVer;
	private Collection<LiveGift> gifts;
	
	@Override
	public void execPacket(){
		latestVer = CommonConfig.get(CommonConfig.CURR_GIFT_VERSION, "Normal");
		if(latestVer.equals(clientGiftVer)){
			error = 0;
			needUpdate = false;
			PacketManager.send(userId, this);
			return;
		}
		error = 0;
		needUpdate = true;
		gifts = LiveGiftMgr.getGifts();
		PacketManager.send(userId, this);
		logger.info("User({}) get gifts success!", userId);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		getgifts.request request = getgifts.request.parseFrom(bytes);
		clientGiftVer = request.getGiftVersion();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getgifts.response.Builder builder = getgifts.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setNeedUpdate(needUpdate);
		builder.setGiftVersion(latestVer);
		if(gifts != null){
			for(LiveGift gift : gifts){
				gift_t.Builder bld = gift_t.newBuilder();
				bld.setCost(gift.COST);
				bld.setCrossRoom(gift.CROSS_ROOM);
				bld.setGiftId(gift.GIFT_ID);
				bld.setName(gift.GIFT_NAME);
				bld.setPlayEffect(gift.PLAY_EFFECT);
				bld.setAnimation(gift.ANIMATION);
				builder.addGifts(bld);
			}
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
