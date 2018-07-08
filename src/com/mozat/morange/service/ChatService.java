package com.mozat.morange.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.game.EnumResult;
enum EnumChat{
	CHAT_TYPE_SINGLE(1,"私人"),
	CHAT_TYPE_WORLD(2,"世界"),
	CHAT_TYPE_GROUP(3 ,"公会"),
	CHAT_TYPR_SYS_TO_SINGLE(4 , "系统发送给单个人"),
	CHAT_TYPR_SYS_TO_ALL(5,"系统发送给所有人");
	
	private int index;
	private String name;
	
	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	private EnumChat(int index , String name){
		this.index = index;
		this.name = name;
	}
	public static EnumChat getChat(int index){
		for (EnumChat c : EnumChat.values()) {
            if (c.getIndex() == index) {  
                return c;  
            }  
        }  
        return null;
	}
}
public class ChatService{
	public static boolean isSYSTOSINGLE(int type){
		return EnumChat.CHAT_TYPR_SYS_TO_SINGLE.getIndex() == type ? true : false;
	}
	public static boolean isSYSTOALL(int type){
		return EnumChat.CHAT_TYPR_SYS_TO_ALL.getIndex() == type ? true :false;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
	
	public static List<MoPacket> sendChatMsg(JSONObject j , MoPacket pkt , String msgId , String chatStr , int flag){
		try {
			List<MoPacket> list = new ArrayList<MoPacket>();
			int ownerId = pkt.id;
			int type = j.getInt("type");
			EnumChat e = EnumChat.getChat(type);
			if(e != EnumChat.CHAT_TYPR_SYS_TO_ALL){
				//如果操作不成功，那么只对自己发送信息。不发送错误信息给其他玩家
				MoPacket p = MoPacket.sendToYourself(ownerId, msgId, chatStr,flag);
				list.add(p);
			}
			
			
			int r = j.getInt("r");
			if(r == EnumResult.RESULT_OK){
				switch(e){
					case CHAT_TYPE_WORLD:{
						//发送消息给所以用户（世界）
						MoPacket p1 = MoPacket.addToSendQUeueByWorld(ownerId , msgId, chatStr,flag);
						List<MoPacket> list1 = new ArrayList<MoPacket>();
						list1.add(p1);
						return list1;
					}
					case CHAT_TYPE_SINGLE:{
						//发送信息给单个用户
						int toId = j.getInt("tId");
						MoPacket p1 = MoPacket.addToSendQueueBySingle(toId , msgId, chatStr,flag);
						list.add(p1);
						break;
					}
					case CHAT_TYPR_SYS_TO_SINGLE:{
						//系统发送给单个用户（由于前面已经发送了，这里不需要重复发送）
						//addToSendQueueBySystem(ownerId , msgId , chatStr);
						break;
					}
					case CHAT_TYPR_SYS_TO_ALL:{
						//系统推送信息给所有用户
						MoPacket p1 = MoPacket.addToSendQueueBySystemForAll(msgId, chatStr,flag);
						list.add(p1);
						break;
					}
					case CHAT_TYPE_GROUP:{
						Object toIdsStr = j.get("toIds");
						JSONArray toIds = new JSONArray(toIdsStr);
						int size = toIds.length();
						for(int i = 0 ; i < size ; i++){
							int toId = toIds.getInt(i);
							MoPacket p1 = MoPacket.addToSendQueueBySingle(toId , msgId, chatStr,flag);
							list.add(p1);
						}
						break;
					}
					default:
						break;
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("[GameServer]sendChatMsg ,  id = " +pkt.id + ", j = " + j.toString() +", chatStr = " +
					chatStr + ",exception : " , e);
			return null;
		}
	}
}
