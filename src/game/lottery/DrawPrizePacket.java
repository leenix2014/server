package game.lottery;


import com.mozat.morange.dbcache.tables.PrizeConfig;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.lottery.drawprize;
import protocols.lottery.prizes;

public class DrawPrizePacket extends Packet{
	//请求
	private String lang;
	
	//下发
	public int error;
	private PrizeConfig prize;
	private int drawCount;

	@Override
	public void execPacket(){
		if(PrizeService.decreseDrawCount(userId)){
			this.error = 0;
			this.prize = PrizeService.drawPrize(userId);
			this.drawCount = PrizeService.getUserDrawCount(userId);
			logger.info("User({}) draw prize, next prize is {}, remaining draw count:{}.", userId, prize.toString(),drawCount);
		} else {
			this.error = 1;//已没有抽奖次数
			errDesc = "已没有抽奖次数";
			this.drawCount = PrizeService.getUserDrawCount(userId);
			logger.info("User({}) draw prize and have no chances!", userId);
		}
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		drawprize.drawrequest request = drawprize.drawrequest.parseFrom(bytes);
		lang = request.getLang();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		drawprize.drawresponse.Builder builder = drawprize.drawresponse.newBuilder();
		builder.setError(this.error);
		builder.setErrDesc(errDesc);
		
		if(prize != null){
			prizes.prize_t.Builder prizeBuilder = prizes.prize_t.newBuilder();
			prizeBuilder.setPrizeId(prize.PRIZE_ID);
			prizeBuilder.setDesc(lang=="en_US"?prize.DESC_EN:prize.DESC_ZH);
			prizeBuilder.setType(prize.TYPE);
			prizeBuilder.setCount(prize.COUNT);
			builder.setPrize(prizeBuilder);
		}
		
		builder.setDrawCount(drawCount);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());					
	}
}
