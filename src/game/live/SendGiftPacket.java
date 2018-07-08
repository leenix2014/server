package game.live;

import com.mozat.morange.dbcache.tables.Anchor;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.live.sendgift;

public class SendGiftPacket extends Packet {
	//请求
	public int roomId;
	public int giftId;
	public int giftCount;
	
	//下发
	public int spurtStatus = -1;
	public boolean bingo = false;
	public int amount = 0;
	public Anchor anchor;
	
	@Override
	public void execPacket(){
		LiveRoomService.sendGift(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		sendgift.request request = sendgift.request.parseFrom(bytes);
		roomId = request.getRoomId();
		giftId = request.getGiftId();
		giftCount = request.getGiftCount();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		sendgift.response.Builder builder = sendgift.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setSpurtStatus(spurtStatus);
		builder.setGiftId(giftId);
		builder.setBingo(bingo);
		builder.setAmount(amount);
//		builder.setCube(UserMgr.getInstance().getCuber(userId));
		builder.setCube(UserMgr.getInstance().getUserCoin(userId));
		if(anchor != null){
			builder.setHistoryCube(anchor.HISTORY_CUBE);
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
