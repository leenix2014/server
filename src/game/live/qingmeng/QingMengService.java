package game.live.qingmeng;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.util.HttpUtil;

import net.sf.json.JSONObject;

public abstract class QingMengService {
	
	public static Logger logger = LoggerFactory.getLogger(QingMengService.class);
	
	public static String platformId = "1496380138";
	
	public static String handle(String command, int anchorId, String machineMac){
		String flagCode = randomCode(4);
		return handle(command, anchorId, machineMac, flagCode);
	}
	public static String handle(String command, int anchorId, String machineMac, String flagCode){
		command = translate(command);
		String status = "";
		for(int i=0; i< 3;i++){
			StringBuffer sb = new StringBuffer("http://op.qmeng.me/index.php?m=Api&c=User&a=handle");
			sb.append("&platform_id=").append(platformId);
			sb.append("&commend=").append(command);
			sb.append("&platform_userid=").append(anchorId);
			sb.append("&machine_MAC=").append(machineMac);
			sb.append("&flag_code=").append(flagCode);
			String res = HttpUtil.doGet(sb.toString(), "");
			JSONObject json = JSONObject.fromObject(res);
			status = json.getString("status");
			if("1".equals(status)){
				logger.info("Handle success.url={}", sb.toString());
				break;
			} else {
				flagCode = randomCode(4);
				logger.error("Handle failed because qingmeng status not success!url:{}, current trying is:{}, res:{}",sb.toString(), (i+1), res);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return status;
	}
	
	private static String randomCode(int size){
		String code = "";
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < size; i++) {
			code += chars.charAt((int)(Math.random() * 62));
		}
		return code;
	}
	
	private static String translate(String cmd){
		if("insertcoin".equals(cmd)){
			return "15";
		}
		if("start".equals(cmd)){
			return "14";
		}
		if("catch".equals(cmd)){
			return "13";
		}
		if("up".equals(cmd)){
			return "9";
		}
		if("down".equals(cmd)){
			return "10";
		}
		if("left".equals(cmd)){
			return "11";
		}
		if("right".equals(cmd)){
			return "12";
		}
		return cmd;
	}
}
