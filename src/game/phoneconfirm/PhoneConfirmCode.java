package game.phoneconfirm;

import java.util.Date;

import game.common.CommonConfig;

public class PhoneConfirmCode{
	private String code;
	private Date createDate;
	
	public PhoneConfirmCode(String code, Date createDate){
		this.code = code;
		this.createDate = createDate;
	}
	
	public boolean isValid(Date date){
		int second = (int)((date.getTime() - this.createDate.getTime()) / 1000);
		return second <= CommonConfig.getInt(CommonConfig.VERIFY_CODE_VALID_SECONDS, 600);
	}
	
	public boolean isCodeCorrect(String inputCode){
		if (inputCode.equals(this.code)) {
			return true;
		}
		
		return false;
	}
}
