package game.shop;

import java.util.Date;
import java.util.UUID;

import com.mozat.morange.dbcache.tables.ShopOrder;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.shop.orderforgood;

public class OrderGoodPacket extends Packet{
	//请求
	private int payType;
	private String goodId;
	
	//下发
	private int error = 0;
	private String orderId;
	
	@Override
	public void execPacket(){
		orderId = UUID.randomUUID().toString();
		ShopOrder order = ShopOrder.create(orderId, ShopOrder.AttrORDER_TIME.set(new Date())
				, ShopOrder.AttrDELIVERED.set(false)
				, ShopOrder.AttrGOOD_ID.set(goodId)
				, ShopOrder.AttrORDER_COUNT.set(1)
				, ShopOrder.AttrPAY_METHOD.set(1==payType?"IAP":payType+"")
				, ShopOrder.AttrPAYED.set(false));
		if(order == null){
			error = 1;
			errDesc = "订单创建失败，请重试";
			logger.info("User({}) place order failed!", userId);
		} else {
			error = 0;
			logger.info("User({}) place order success, good id is {}, order id is:{}.", userId, goodId, orderId);
		}
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		orderforgood.request request = orderforgood.request.parseFrom(bytes);
		payType = request.getBuyType();
		goodId = request.getGoodID();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		orderforgood.response.Builder builder = orderforgood.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setOrderID(orderId);
		builder.setGoodID(goodId);
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
