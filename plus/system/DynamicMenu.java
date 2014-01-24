package com.mpay.plus.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mpay.agent.R;

public class DynamicMenu {

	private final String SHARE_NAME = "iShare";
	private final String SHARE_SETTING_SLIDING = "Setting_Sliding";
	
	private SharedPreferences iShare = null;

	public DynamicMenu(Context context) {
		if (iShare == null){
			iShare = (SharedPreferences) context.getSharedPreferences(
					SHARE_NAME, Context.MODE_PRIVATE);
			
			if(getSetting().length <= 0){
				String dataDefault = R.string.bk_link_address+"|icon_deliver_agent|com.mpay.plus.banks.FrmAddressMLink-"
									+R.string.info_hint+"|icon_deliver_agent|com.mpay.plus.system.FrmRegistrationInfo";
				iShare.edit().putString(SHARE_SETTING_SLIDING, dataDefault).commit();
			}
		}
	}
	
	public void addSettingOrUpdateSetting(int idTitle, String sIcon, String sClassName) {
		String data = idTitle + "|" + sIcon + "|" + sClassName;
		String isSetting = iShare.getString(SHARE_SETTING_SLIDING, "");
		if("".equals(isSetting))
			iShare.edit().putString(SHARE_SETTING_SLIDING, data+"-").commit();
		else {
			String []isSettingArr = isSetting.split("-", -1);
			boolean isTrue = false;
			for (int i = 0; i < isSettingArr.length; i++)
				if(data.equals(isSettingArr[i])){
					isTrue = true;
					break;
				}
			if(isTrue == false)
				iShare.edit().putString(SHARE_SETTING_SLIDING, isSetting+data+"-").commit();
		}
		
	}
	
	public void removeSetting(String data){
		String isSetting = iShare.getString(SHARE_SETTING_SLIDING, "");
		if(!"".equals(isSetting)){
			String sSetting = "";
			String []isSettingArr = isSetting.split("-", -1);
			for (int i = 0; i < isSettingArr.length; i++) 
				if(!data.equals(isSettingArr[i]))
					sSetting += isSettingArr[i]+"-";
			iShare.edit().putString(SHARE_SETTING_SLIDING, sSetting).commit();
		}
	}
	
	public String[] getSetting(){
		String data = iShare.getString(SHARE_SETTING_SLIDING, "").toString();
		if("".equals(data)) return new String[]{};
		else return data.split("-", -1);
	}
	
//	public void setSettingActivity(String parameter) {
//		if(iShare != null){
//			String data = iShare.getString(SHARE_SETTING_SLIDING, "");
//			iShare.edit().putString(SHARE_SETTING_SLIDING, "".equals(data) ? parameter : data+","+parameter).commit();
//		}
//	}
//	
//	public String getSettingActivity() {
//		return iShare.getString(KEY_PARAMETER, "").toString();
//	}
}
