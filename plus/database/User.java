/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpay.plus.database;

import java.util.List;

import android.util.DisplayMetrics;

import com.mpay.plus.config.Config;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * 
 * @author THANHNAM
 * 
 */
public class User {
	public static String MID = "";
	public static String BANK_MID = "";
	
	public static String[] arrHotline = new String[] { "0904835836" }; // format: 11112222333
	public static String[] arrMyAcountList = new String[] { "" };// format: 11112222333
	public static List<String> arrMyAcountATMLink  = null; // Thẻ liên kết
	public static String myAccountLink = "";
	public static String sprivateEncKey = "";
	public static String sSeqNo = "0";
	public static String sVERSION = "3.0";
	public static String sLang = Config.LANG_VI;
	public static String sKetNoi = "GPRS";
	public static String srvTime = "";
	public static String transCode = "";
	public static String dstNo = "";
	public static String sIMEI = "";

	public static String sCustOTP = "";
	public static String sCustAnser = "";
	public static String sCustName = "";
	public static String sCustCMND = "";
	public static String sCustBirthday = "";
	public static String sCustQesID = "";
	public static boolean isActived = false;
	public static boolean isRegNapTien = false;
	public static boolean isFisrt = true;
	
	public static String sSMS_SERVERFONE = "";
	public static String sADVERD = "";
	public static String sID_MENU_MAIN = "0";
	public static String sID_NEWS = "0";
	public static String sID_MENU_CARD = "0";
	public static String sID_MENU_BILL = "0";
	public static String sID_MENU_TOPUP = "0";
	public static String sID_MENU_QUESTION = "0";
	public static String sID_MENU_KENH = "0";
	public static String sID_PROD_CD = "0";
	
//	// Type Question = 0, Kenh = 1;
//	public static String sTYPE_QUESTION = "0";
//	public static String sTYPE_KENH = "1";
	
	public static boolean isCHUA_DAT_MAT_KHAU = false;
	public static String sVersionAndLink = "";
	public static int iSoLanSaiMPIN = 0;
	public static long iTimeDiff = 0;
	public static int iURL_INDEX = 0;
	public static byte iUPDATE_FLAG = -1;// 0: chua kiem tra update, 1: co phien
											// ban moi bat buoc,2 : co phien ban
											// moi bat buoc, 2: khong co phien
											// ban moi hoac nguoi dung tu choi
											// update
	public static boolean isRegisted = false;
	public static int[] REQUIRED_IMAGE_SIZE = new int[] {1024, 1024};
//	public static int SCREENLAYOUT_SIZE = Configuration.SCREENLAYOUT_SIZE_NORMAL;
	public static int SCREENLAYOUT_DENSITY = DisplayMetrics.DENSITY_MEDIUM;	

	public static String getSeqNo() {
		return User.sSeqNo;
	}

	public static String getNxtSeqNo() {
		AMPlusCore.getDba().loadData();
//		Log.i("Log : ", "SeqNo Be : "+getSeqNo());
		int kq = Integer.parseInt(Integer.toString(Integer
				.parseInt("".equals(getSeqNo()) || getSeqNo() == null 
				? "0" : getSeqNo()) + 1)) % 10000;
		String tmp = String.valueOf(kq);
		setSeqNos(Util.sParseNumber(tmp, 4) + kq);
		AMPlusCore.getDba().saveUserTable();
//		Log.i("Log : ", "SeqNo af : "+getSeqNo());
		return getSeqNo();
	}

	public static void setSeqNos(String sSeq) {
		User.sSeqNo = sSeq;
	}

	public static String getDstNo() {
		return User.dstNo;
	}

	public static void setDstNo(String dstNo) {
		if (dstNo != null) {
			User.dstNo = dstNo;
		}
	}

	public static String getTransCode() {
		return User.transCode;
	}

	public static void setTransCode(String transCode) {
		User.transCode = transCode;
	}

	public static String getSrvTime() {
		return User.srvTime;
	}

	public static void setSrvTime(String srvTime) {
		User.srvTime = srvTime;
	}

	public static String getIMEI() {
		return User.sIMEI;
	}
}