package com.mozat.morange.service;


class Type {
	final static int SYSTEM = 0;
	final static int SERVICE_MIN = 1;
	final static int LOGIN = 2;
	final static int EMAIL = 3;
	final static int RSS = 4;
	final static int CHAT = 5;
	final static int FILE = 6;
	final static int CAMERA = 7;
	final static int ECHO = 8;
	final static int CONTACT = 9;
	final static int CALENDAR = 10;
	final static int TASK = 11;
	final static int COMMAND = 12;
	final static int ECHO2 = 13;
	final static int SERVICE_MAX = 0x0FFFFFFF;
	final static int CLIENT_MIN = 0x10000000;
	final static int CLIENT = 0x10000001;
	final static int CLIENT_MAX = 0x1FFFFFFF;
	final static int RESERVED_MIN = 0x20000000;
	final static int RESERVED_MAX = 0xFFFFFFFF;

	int value = 0;
	

    public final static int MULTICAST   	= 0xFFFFFFFF;
    public final static int CLIENT_BROADCAST   = 0xFFFFFFF0;

	public Type(int value) {
		this.value = value;
	}

	boolean isSystem() {
		return value == SYSTEM;
	}

	boolean isService() {
		return (value > SERVICE_MIN) && (value < SERVICE_MAX);
	}

	boolean isClient() {
		return (value > CLIENT_MIN) && (value < CLIENT_MAX);
	}

	public String toString() {
		String ret = ",Type=";
		switch (value) {
		case SYSTEM:
			ret += "SYSTEM";
			break;
		case LOGIN:
			ret += "LOGIN";
			break;
		case EMAIL:
			ret += "EMAIL";
			break;
		case RSS:
			ret += "RSS";
			break;
		case CHAT:
			ret += "CHAT";
			break;
		case FILE:
			ret += "FILE";
			break;
		case CAMERA:
			ret += "CAMERA";
			break;
		case ECHO:
			ret += "ECHO";
			break;
		case CONTACT:
			ret += "CONTACT";
			break;
		case CALENDAR:
			ret += "CALENDAR";
			break;
		case TASK:
			ret += "TASK";
			break;
		case COMMAND:
			ret += "COMMAND";
			break;
		case ECHO2:
			ret += "ECHO2";
			break;
		case CLIENT:
			ret += "CLIENT";
			break;
		default:
			ret += "UNKNOWN";
		}
		;
		return ret;
	}

}

class SystemID {
	final static int PingPong = 1;
	final static int OnDeliveryFailure = 2;
	final static int Login = 3;
	final static int LoginResult = 4;
	final static int CloseConnection = 5;
	final static int DomainLogin = 6;
	final static int OnPeerLogin = 7;
	final static int OnPeerLogout = 8;
	final static int SystemMessage = 9;
	final static int OnRecipientBusy = 10;
	final static int OnPeerList = 11;
	public final static int OnRemotePeerLogin = 21;
	public final static int OnRemotePeerLogout = 22;
	public final static int OnRemotePeerLoginList = 23;
	public final static int OnRemotePeerLogoutList = 25;

	int systemId = 0;

	SystemID(int id) {
		this.systemId = id;
	}

	public String toString() {
		String ret = ",Id=";
		switch (systemId) {
		case PingPong:
			ret += "PingPong";
			break;
		case OnDeliveryFailure:
			ret += "OnDeliveryFailure";
			break;
		case Login:
			ret += "Login";
			break;
		case LoginResult:
			ret += "LoginResult";
			break;
		case CloseConnection:
			ret += "CloseConnection";
			break;
		case DomainLogin:
			ret += "DomainLogin";
			break;
		case OnPeerLogin:
			ret += "OnPeerLogin";
			break;
		case OnPeerLogout:
			ret += "OnPeerLogout";
			break;
		case SystemMessage:
			ret += "SystemMessage";
			break;
		case OnRecipientBusy:
			ret += "OnRecipientBusy";
			break;
		case OnPeerList:
			ret += "OnPeerList";
			break;
		default:
			ret += "UNKNOWN";
		}
		;
		return ret;
	}

}

class GameServerAction{
	public static final int FLAG_CHAT = 1;
	public static final int FLAG_COMMAND = 0;
	public static final int FLAG_RMI = 2;
}
//
//class MGServerAction {
//	final static int LoginUa = 0;
//	final static int GetManifest = 1;
//	final static int GetResource = 2;
//	final static int SendData = 3;
//	final static int GetResources = 4;
//
//	public int action = 0;
//	public MoPacket pkt = null;
//	public String msg = "";
//	public DataInputStream tin = null;
//
//	public MGServerAction(int action, MoPacket pkt, String msg,
//			DataInputStream tin) {
//		this.action = action;
//		this.pkt = pkt;
//		this.msg = msg;
//		this.tin = tin;
//	}
//
//	@Override
//	public String toString() {
//		return this.action + ", " + msg;
//	}
//}
