package game.loginReward.packet;

import java.util.List;

import game.loginReward.LoginRecordData;
import game.loginReward.LoginRewardService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.loginreward.loginrecord;

public class LoginRecordPacket extends Packet{
	//请求
	
	//下发
	private List<LoginRecordData> loginRecordDatas;
	
	@Override
	public void execPacket(){
		LoginRewardService.sendLoginRecord(this.userId);
	}
	
	@Override
	public void readBody(byte[] bytes){
		logger.info("User({}) request login records.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		loginrecord.response.Builder builder = loginrecord.response.newBuilder();
		for (LoginRecordData data : loginRecordDatas) {
			loginrecord.record_t.Builder recordBuilder = loginrecord.record_t.newBuilder();
			recordBuilder.setDay(data.day);
			recordBuilder.setIsLogin(data.isSign);
			recordBuilder.setIsGotPrize(data.isGotPrize);
			recordBuilder.setRoomcard(data.roomCard);
			
			builder.addRecords(recordBuilder);
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());			
	}

	public void setLoginRecordDatas(List<LoginRecordData> datas){
		this.loginRecordDatas = datas;
	}
	
}
