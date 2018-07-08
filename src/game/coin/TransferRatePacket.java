package game.coin;

import game.common.CommonConfig;
import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.coin.transferrate;

// Not Used
public class TransferRatePacket extends Packet{
	//请求
	private int amount;
	
	//下发
	private int error;
	private String errDesc = "";
	private int percent;
	private int fee;
	
	@Override
	public void execPacket() {
		error = 0;
		percent = CommonConfig.getPercent(CommonConfig.TRANSFER_COIN_PERCENT, 3);
		fee = Math.round((float)amount/100 * percent);
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws Exception{
		transferrate.request req = transferrate.request.parseFrom(bytes);
		amount = req.getAmount();
		logger.info("User({}) request cal fee of transfer coin, amount={}", userId, amount);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		transferrate.response.Builder builder = transferrate.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setPercent(percent);
		builder.setFee(fee);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
