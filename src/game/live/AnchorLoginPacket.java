package game.live;

import org.eclipse.jetty.util.StringUtil;

import com.mozat.morange.dbcache.tables.Anchor;
import com.mozat.morange.util.MD5;

import game.packet.Packet;
import game.packet.PacketManager;
import game.session.SessionManager;
import protocols.header.packet.Builder;
import protocols.live.anchorlogin;

public class AnchorLoginPacket extends Packet{
	//请求
	private int anchorId;
	private String pwd;
	
	//下发
	private int error;
	private String name = "";
	private String channel = "";
	private String title = "";
	private int cost = 0;
	
	@Override
	public void execPacket(){
		Anchor anchor = Anchor.getOne(anchorId);
		if(anchor == null){
			error = 1;
			errDesc = "主播不存在";
			PacketManager.send(session, this);
			logger.info("No such anchor:"+anchorId);
			return;
		}
		String realPwd = MD5.getHashString(StringUtil.nonNull(anchor.PWD));
		if(pwd == null || !pwd.equals(realPwd)){
			error = 2;
			errDesc = "密码错误";
			PacketManager.send(session, this);
			logger.info("Anchor("+anchorId+") password error!");
			return;
		}
		error = 0;
		name = anchor.ANCHOR_NAME;
		channel = anchor.CHANNEL;
		title = anchor.TITLE;
		cost = anchor.COST;
		SessionManager.addSession("zh_CN", anchor.ANCHOR_ID, session);
		PacketManager.send(session, this);
		logger.info("Anchor("+anchorId+") login success!");
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		anchorlogin.request request = anchorlogin.request.parseFrom(bytes);
		anchorId = request.getAnchorId();
		pwd = request.getEncrptPwd();
		logger.info("Anchor({}) request login", anchorId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		anchorlogin.response.Builder builder = anchorlogin.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setAnchorName(name);
		builder.setChannel(channel);
		builder.setTitle(title);
		builder.setCost(cost);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
