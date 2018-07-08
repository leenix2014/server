package game.phoneconfirm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.mozat.morange.log.TraceLog;
import com.mozat.morange.util.HttpUtil;

public class PhoneConfirmManager {
	
	private static Map<String, PhoneConfirmCode> codeMap = new HashMap<String, PhoneConfirmCode>();
	
	public static boolean phoneRequestToConfirm(String phoneNumber){
		//随机生成六位数验证码
		Random random = new Random();
    	int number = random.nextInt(8998) + 1001;
    	String code = Integer.toString(number);
    	
    	PhoneConfirmCode confirmCode = new PhoneConfirmCode(code, new Date());
    	codeMap.put(phoneNumber, confirmCode);
    	
    	//这里调用http给手机发短信，成功则return true否则为false
    	String url = "https://www.ginota.com/gemp/sms/json?apiKey=43WIbLdMBf0M3pG5whCGII4zMf2SQEcH&apiSecret=%23%24%2377Z%40qL%25&srcAddr=MyCompany&dstAddr=" + phoneNumber + "&content=短信验证码是" + code;
		JSONObject obj = HttpUtil.getJSONObjectFromURL(url);
		int status = 1;
		if (obj != null && obj.has("status")) {
			try {
				status = obj.getInt("status");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				TraceLog.info("PhoneConfirmManager.phoneRequestToConfirm send msm exception,phoneNumber=" + phoneNumber);
			}
		}
    	
    	return status == 0;
	}
	
	public static boolean isValid(String phoneNumber, String code){
		if (!codeMap.containsKey(phoneNumber)) {
			return false;
		}
		
		PhoneConfirmCode confirmCode = codeMap.get(phoneNumber);
		if (!confirmCode.isValid(new Date())) {//该验证码已过期
			return false;
		}
		
		if (!confirmCode.isCodeCorrect(code)) {//验证码不对
			return false;
		}
		
		return true;
	}
	
	public static void main(String args[]) throws JSONException{
		TraceLog.info("xxxxxxxxxxxxxxStart");
		
		String url = "https://www.ginota.com/gemp/sms/json?apiKey=43WIbLdMBf0M3pG5whCGII4zMf2SQEcH&apiSecret=%23%24%2377Z%40qL%25&srcAddr=MyCompany&dstAddr=8613450382186&content=短信验证码是8888";
		JSONObject obj = HttpUtil.getJSONObjectFromURL(url);
		if (obj != null) {
			int status = obj.getInt("status");
			TraceLog.info("status=" + status);
		}
		
		TraceLog.info("xxxxxxxxxxxxxxEnd");
	}
}
