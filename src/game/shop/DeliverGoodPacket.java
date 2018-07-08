package game.shop;

import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mozat.morange.dbcache.tables.RoomCardBill;
import com.mozat.morange.dbcache.tables.ShopGood;
import com.mozat.morange.dbcache.tables.ShopOrder;
import com.mozat.morange.util.HttpUtil;

import game.common.CommonConfig;
import game.packet.Packet;
import game.packet.PacketManager;
import netty.GameModels.UserMgr;
import netty.util.MathUtil;
import protocols.header.packet.Builder;
import protocols.shop.deliveryforgood;

public class DeliverGoodPacket extends Packet{
	//请求
	private String orderId;
	private String iapReceipt;
	
	//下发
	private int error;
	private String goodId;
	private int count;
	
	@Override
	public void execPacket(){
		ShopOrder order = ShopOrder.getOne(orderId);
		if(order == null){
			error = 1;
			errDesc = "订单不存在";
			logger.info("User({}) deliver good failed because no such order, order id is {}.", userId, orderId);
			PacketManager.send(userId, this);
			return;
		}
		if(order.DELIVERED){
			error = 2;
			errDesc = "订单已发货，无法重复发货";
			logger.info("User({}) deliver good failed because order delivered, order id is {}.", userId, orderId);
			PacketManager.send(userId, this);
			return;
		}
		goodId = order.GOOD_ID;
		String url = CommonConfig.get(CommonConfig.APP_STORE_VERIFY_URL,"https://buy.itunes.apple.com/verifyReceipt");
		HttpEntity reqEntity = new StringEntity("{\"receipt-data\":\""+iapReceipt+"\"}" , "UTF-8");
		String res = HttpUtil.postHttps(url, reqEntity);
		String status = null;
		String productId = null;
		int quantity = 1;
		try {
			JSONObject json = new JSONObject(res);
			status = json.getString("status");
			JSONObject receipt = json.getJSONObject("receipt");
			JSONArray in_app = receipt.getJSONArray("in_app");
			if(in_app.length() > 1){
				logger.warn("User({}) buy more than one kind product:{}", userId, in_app.toString());
			}
			if(in_app.length() > 0){
				JSONObject txnDetail = in_app.getJSONObject(0);
				productId = txnDetail.getString("product_id");
				quantity = MathUtil.parseInt(txnDetail.getString("quantity"), 1);
			}
		} catch (JSONException e) {
			logger.error("json format error:"+res, e);
		}
		if(!"0".equals(status)){
			error = 3;
			errDesc = "缴费未成功";
			logger.info("User({}) deliver good failed because receipt verify error, order id is {}, res:{}", userId, orderId, res);
			PacketManager.send(userId, this);
			return;
		}
		ShopOrder.updateByCriteria(ShopOrder.valueList(ShopOrder.AttrPAYED.set(true), ShopOrder.AttrORDER_COUNT.set(quantity)), ShopOrder.AttrORDER_ID.eq(orderId));
		if(!goodId.equals(productId)){
			error = 4;
			errDesc = "订单商品与凭据商品不一致";
			logger.info("User({}) deliver good failed because order and receipt not consistent, order id is {}, orderProductId:{}, receiptProductId:{}, res:{}", userId, orderId, goodId, productId, res);
			PacketManager.send(userId, this);
			return;
		}
		
		ShopGood good = ShopGood.getOne(productId);
		if(good == null){
			error = 5;
			errDesc = "服务端没有该商品";
			logger.error("User({}) deliver good failed because good not configed, order id is {}, good id is {}", userId, orderId, productId);
			PacketManager.send(userId, this);
			return;
		}
		// Begin Deliver Good
		count = quantity * good.GOOD_COUNT;
		if("coin".equals(good.GOOD_TYPE)){
			// deliver coin
		} else {
			// deliver room card
			int oldRoomCardCount = UserMgr.getInstance().getCuber(userId);
			int nowRoomCardCount = oldRoomCardCount + count;
			UserMgr.getInstance().setCuber(userId, nowRoomCardCount);
			RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(userId+""), 
					RoomCardBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
					RoomCardBill.AttrSOURCE.set("appstore"),
					RoomCardBill.AttrSOURCE_ID.set(orderId),
					RoomCardBill.AttrSOURCE_NAME.set("苹果商城充值"),
					RoomCardBill.AttrAMOUNT.set(count),
					RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
					RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
					RoomCardBill.AttrCREATE_TIME.set(new Date()));
		}
		ShopOrder.updateByCriteria(ShopOrder.valueList(ShopOrder.AttrDELIVERED.set(true), ShopOrder.AttrDELIVER_TIME.set(new Date())), ShopOrder.AttrORDER_ID.eq(orderId));
		error = 0;
		logger.info("User({}) deliver good success, order id is {}, goodId:{}, quantity:{}, total:{}", userId, orderId, productId, quantity, count);
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		deliveryforgood.request request = deliveryforgood.request.parseFrom(bytes);
		orderId = request.getOrderId();
		iapReceipt = request.getIapReceipt();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		deliveryforgood.response.Builder builder = deliveryforgood.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setGoodID(goodId);
		builder.setCount(count);
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
