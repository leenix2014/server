package game.lottery;

import java.util.Collection;

import com.mozat.morange.dbcache.tables.PrizeConfig;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.lottery.prizes;

public class PrizesPacket extends Packet{
	//请求
	private String lang;
	
	//下发
	private Collection<PrizeConfig> prizeconfigs;
	private PrizeConfig prize;
	private int drawCount;
	
	@Override
	public void execPacket(){
		this.prizeconfigs = PrizeService.getPrizes();
		this.prize = PrizeService.drawPrize(userId, true);
		this.drawCount = PrizeService.getUserDrawCount(userId);
		PacketManager.send(userId, this);
		logger.info("User({}) get prize list, next prize is {}, remaining draw count:{}.", userId, prize.toString(), drawCount);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		prizes.request request = prizes.request.parseFrom(bytes);
		lang = request.getLang();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		prizes.response.Builder builder = prizes.response.newBuilder();
		for (PrizeConfig data : prizeconfigs) {
			prizes.prize_t.Builder dataBuilder = prizes.prize_t.newBuilder();
			dataBuilder.setPrizeId(data.PRIZE_ID);
			dataBuilder.setDesc(lang=="en_US"?data.DESC_EN:data.DESC_ZH);
			dataBuilder.setType(data.TYPE);
			dataBuilder.setCount(data.COUNT);
			
			builder.addPrizes(dataBuilder);
		}
		
		prizes.prize_t.Builder prizeBuilder = prizes.prize_t.newBuilder();
		prizeBuilder.setPrizeId(prize.PRIZE_ID);
		prizeBuilder.setDesc(lang=="en_US"?prize.DESC_EN:prize.DESC_ZH);
		prizeBuilder.setType(prize.TYPE);
		prizeBuilder.setCount(prize.COUNT);
		builder.setPrize(prizeBuilder);
		
		builder.setDrawCount(drawCount);
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
