package game.coin;

import java.util.List;

import com.mozat.morange.dbcache.tables.Transfer;
import com.mozat.morange.util.DateUtil;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.coin.recenttransfer;
import protocols.coin.recenttransfer.transfer_record;

public class RecentTransferPacket extends Packet{
	//请求
	
	//下发
	private List<Transfer> records;
	
	@Override
	public void execPacket() {
		error = 0;
		//records = Transfer.getManyByCriteria(Transfer.AttrFROM_ID.eq(userId+""));
		records = Transfer.getManyBySQL("SELECT * FROM TRANSFER t WHERE t.`FROM_ID` = ? ORDER BY RECORD_TIME DESC LIMIT 30", userId);
		PacketManager.send(session, this);
	}

	@Override
	public void readBody(byte[] bytes) throws Exception{
		//recenttransfer.request req = recenttransfer.request.parseFrom(bytes);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		recenttransfer.response.Builder builder = recenttransfer.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(records != null){
			for(Transfer record : records){
				transfer_record.Builder bld = transfer_record.newBuilder();
				bld.setAmount(record.AMOUNT);
				bld.setFee(record.FEE);
				bld.setUserId(record.TO_ID);
				bld.setUserName(record.TO_NAME);
				bld.setRecordTime(DateUtil.yyyyMMddHHmmss(record.RECORD_TIME));
				
				builder.addRecords(bld);
			}
		}
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
