package com.mozat.morange.service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.util.IOUtil;

public class MoPacket {
	private static final Logger logger = LoggerFactory.getLogger(MoPacket.class);
	byte[] payload = null;
	Type type = null;
	int len = 0;
	int id = 0;
	int reserved = 0;

	public MoPacket(DataInputStream in) throws Exception {
		len = in.readInt() - 12;
		if (len < 0 || len > 1024 * 1024 * 20)
			throw new Exception("pkt is too long: " + len);
		type = new Type(in.readInt());
		id = in.readInt();
		reserved = in.readInt();
		payload = new byte[len];
		in.readFully(payload, 0, len);
	}

	public MoPacket(int type, int id, int len, byte[] payload) {
		this.type = new Type(type);
		this.id = id;
		this.len = len;
		this.payload = payload;
	}

	public void sendOut(DataOutputStream out) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(bout);
		os.writeInt(len + 12);
		os.writeInt(type.value);
		os.writeInt(id);
		os.writeInt(reserved);
		os.write(payload);
		byte[] payload2 = bout.toByteArray();
		bout.close();
		
		synchronized(out) {
			out.write(payload2);
		}
	}

	@Override
	public String toString() {
		String ret = type.toString();
		if (type.isSystem()) {
			ret += new SystemID(id);
		}
		ret += (",Id=" + id);
		ret += (",length=" + len);
		ret += (",reserved=" + reserved);
		return ret;
	}
	
	
	
	static public byte[] getPayloadByStr(String msgId,String str , int flag){
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bos);
			IOUtil.writeString(os, str);
			os.flush();
			byte[] payloadByteArr = bos.toByteArray();
			os.close();
			bos.close();
			return genPayload(msgId, payloadByteArr , flag);
		} catch (Exception e) {
			logger.error("get payload byteArray is wrong.", e);
		}
		return null;
	}
	
	public static byte[] genPayload(String msgId, byte[] payload , int flag){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			logger.info("[GameServer]msgId = " + msgId);
			IOUtil.writeVarchar(dos, msgId);
			
			if (payload.length > 500) {
				dos.writeByte(1);
				ByteArrayOutputStream tempBos = new ByteArrayOutputStream();
				GZIPOutputStream gos = new GZIPOutputStream(tempBos);
				gos.write(payload);
				gos.flush();
				gos.close();
				payload = tempBos.toByteArray();
			} else {
				dos.writeByte(0);
			}
			dos.writeByte(flag);
			
			dos.write(payload);
			dos.close();
			return bos.toByteArray();
		}catch (Exception e) {
			logger.error("get action-payload is wrong !", e);
		}
		return null;
	}

	
	
	
	
	/**
	 * 发送消息给单个用户
	 * toId : 表示的是发送给那个用户。大于0
	 */
	public static MoPacket addToSendQueueBySingle(int toId , String msgId  ,String chatStr , int commandFlag){
		logger.debug("[GameServer] addToSendQueueBySingle ,toId = " + toId + ", chatStr = " + chatStr);
		
		//再发送一条给对方
		byte[] chatPayload =getPayloadByStr(msgId , chatStr , commandFlag);
		MoPacket chatPkt = new MoPacket(Type.CLIENT, toId, chatPayload.length, chatPayload);
		return chatPkt;
	}
	
	/**
	 * 发送消息给所以用户（世界）
	 * toId : 发送给所有用户。等于 0
	 */
	public static MoPacket addToSendQUeueByWorld(int ownerId , String msgId , String chatStr, int commandFlag){
		logger.debug("[GameServer] addToSendQUeueByWorld , msgId = " + msgId + ",ownerId = " 
				+ ownerId + ", chatStr = " + chatStr);
		//再给所有人发送
		int toId = 0;
		byte[] chatPayload = getPayloadByStr(msgId , chatStr,commandFlag);
		MoPacket chatPkt = new MoPacket(Type.CLIENT_BROADCAST, toId, chatPayload.length, chatPayload);
		return chatPkt;
	}
	
	/**
	 * 系统消息。这里指的是玩家主动触发的系统事件（例如：升级之后或者获取某种道具，系统推送的提示语）
	 * toId : 发送给自己。大于 0
	 */
//	private static MoPacket addToSendQueueBySystem(int ownerId , String msgId , String chatStr){
//		logger.debug("[GameServer]addToSendQueueBySystem ,ownerId = "+ ownerId + ",chatStr = " + chatStr);
//		sendToYourself(ownerId, msgId, chatStr);
//	}
	
	/**
	 * 系统消息。这里指的是系统主动触发的事件（例如：系统推送的活动等）
	 * toId : 发送给所有用户。等于0
	 */
	public static MoPacket addToSendQueueBySystemForAll(String msgId , String chatStr, int commandFlag){
		logger.debug("[GameServer]addToSendQueueBySystemForAll , chatStr = " + chatStr);
		byte[] ownChatPayload = getPayloadByStr(msgId, chatStr,commandFlag);
		MoPacket ownMoPacket = new MoPacket(Type.CLIENT_BROADCAST, 0, ownChatPayload.length, ownChatPayload);
		return ownMoPacket;
	}
	
	/**
	 * 公会消息。
	 */
//	private static List<MoPacket> addToSendQueueByGroup(JSONArray toIds,int ownerId , String msgId  ,String chatStr){
//		try {
//			logger.debug("[GameServer] addToSendQueueByGroup , ownerId = " + ownerId + ", chatStr = " + chatStr);
//			
//			List<MoPacket> list = new ArrayList<MoPacket>();
//			
//			//再发送一条给公会中的人
//			int size = toIds.length();
//			for(int i = 0 ; i < size ; i ++){
//				int toId = toIds.getInt(i);
//				byte[] chatPayload = GameServer.getPayloadByStr(msgId , chatStr);
//				MoPacket chatPkt = new MoPacket(Type.CLIENT, toId, chatPayload.length, chatPayload);
//				list.add(chatPkt);
//			}
//			return list;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	/**
	 * 发送给自己
	 */
	public static MoPacket sendToYourself(int ownerId,String msgId , String chatStr, int commandFlag){
		logger.debug("[GameServer]sendItToYourself ,ownerId = "+ ownerId + ",chatStr = " + chatStr);
		byte[] ownChatPayload = getPayloadByStr(msgId , chatStr,commandFlag);
		MoPacket ownChatPkt = new MoPacket(Type.CLIENT,ownerId , ownChatPayload.length, ownChatPayload);
		return ownChatPkt;
	}
	
}
