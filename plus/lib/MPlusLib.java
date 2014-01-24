package com.mpay.plus.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class MPlusLib {
	public static final String TAG = "MPlusLib";

	public static boolean isFirst = true;
	public static String privateClient = "";
	public static RSAUtil3 rsa = null;
	private static RSAPrivateCrtKeyParameters privateClientKey;

	public static RSAUtil3 getRSA() {
		if (rsa == null) {
			rsa = new RSAUtil3();
		}
		return rsa;
	}

	public static String genPublicKey() throws Exception {
		String[] tt = getRSA().genKeys();
		privateClient = tt[1];
		return tt[0];
	}

	public static RSAPrivateCrtKeyParameters loadKey(String mPIN) {
		// Thuc hien Load Key Client
		// 1. Doc tu Record
		// 2. Giai ma = PIN
		// 3. Khoi tao Key
		// if (privateClientKey == null && !mPIN.equals("")) {
		try {
			String tmp = getRSA().decryptAES(User.sprivateEncKey, mPIN);
			myClass mc = new myClass(tmp);
			privateClientKey = (RSAPrivateCrtKeyParameters) mc.getKey();

			// sPINTemp = mPIN;
			return privateClientKey;
		} catch (Exception ex) {
			ex.printStackTrace();
			privateClientKey = null;
			return null;
		}
	}

	public static void setKey(String mPIN) {
		try {
			if (!privateClient.equals(""))
				User.sprivateEncKey = getRSA().encryptAES(privateClient, mPIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String frameRSA(String[] sData) throws Exception {
		String tmp = "";
		for (int i = 0; i < sData.length - 1; i++) {
			tmp += sData[i] + Config.FIELD_RSA;
		}
		
		MPlusLib.debug("RSA", tmp);
		
		tmp += sData[sData.length - 1];
		String frameND = "";
		frameND = getRSA().encodeRSA(tmp, privateClientKey);
		return frameND;
	}

	/**
	 * 
	 * Lay ma dang ky
	 * 
	 * */
	public static String getMaDangKy(String keyeMonkey, String pass) {
		String ma = "";
		try {
			// Hash[AES(keyeMonkey, Key=Mật khẩu)]
			String strPassEnAES = MPlusLib.getRSA().encryptAES(keyeMonkey, pass);
			ma = MPlusLib.getRSA().hashMD5Base64(strPassEnAES);
		} catch (Exception e) {
		}
		return ma;
	}
	
	public static String getStrAnswerAES(String answerEn, String pass) {
		String strAnswer = "";
		try {
			strAnswer = MPlusLib.getRSA().decryptAES(answerEn, pass);
		} catch (Exception e) {
		}
		return strAnswer;
	}

	/**
	 * @param regcode
	 *            ma kich hoat
	 * 
	 * */
	public static String getLenhKichHoat(String regcode) {
		String output = "";
		try{
			String rsa = MPlusLib.frameRSA(new String[] { regcode });
	
			rsa = rsa.replace("+", "%2B");
			rsa = rsa.replace("/", "%2F");
	
			output = Config.SMS_ACTIVE + rsa;
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getLenhKichHoat", ex);
		}
		return output;
	}

	public static String getFrameCommand(String[] sParam) {
		StringBuffer strBuf = new StringBuffer();
		if (sParam != null) {
			strBuf.append(sParam[0]);
			for (int i = 1; i < sParam.length; i++) {
				strBuf.append(Config.FIELD_RSA);
				strBuf.append(sParam[i]);
			}

			// strBuf.append(Config.FIELD_SEPERATE);
			// strBuf.append(User.sLang);
			// strBuf.append(Config.FIELD_SEPERATE);
			// strBuf.append(User.sVERSION);
			// strBuf.append(getMyTime());

			if (User.getTransCode().equals(Config.H_DOI_NGONNGU)
//					|| User.getTransCode().equals(Config.DANG_KY) 
					|| User.getTransCode().equals(Config.ACTIVE_APP)) {
				User.isFisrt = true;
				// String svendor= android.os.Build.MANUFACTURER.toUpperCase();
				// String smodel= android.os.Build.MODEL.toUpperCase();
				// String sOS=
				// String.valueOf(android.os.Build.VERSION.SDK_INT).toUpperCase();

				// BKCD<>MDCD<>VERS<>MNID<>CAID<>TPID<>BLID<>NEID<>UPDT
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(Config.BKCD);
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(Config.MDCD);
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sVERSION);

				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // menu main
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // menu card
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // menu topup
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // menu bill
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // news

				// UPDT=MFNM!MDNM!OS (UPDT=ANDROID!!)
				String svendor = "ANDROID";
				String smodel = "";
				String sOS = "";
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(svendor);
				strBuf.append(Config.FIELD_SEPERATE);
				strBuf.append(smodel);
				strBuf.append(Config.FIELD_SEPERATE);
				strBuf.append(sOS);

				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // menu Question
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append("00"); // menu Kenh
				
			} else if (isFirst) {
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(Config.BKCD);
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(Config.MDCD);
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sVERSION);

				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_MENU_MAIN); // menu main
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_MENU_CARD); // menu card
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_MENU_TOPUP); // menu topup
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_MENU_BILL); // menu bill
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_NEWS); // news

				// UPDT=MFNM!MDNM!OS (UPDT=ANDROID!!)
				String svendor = "ANDROID";
				String smodel = "";
				String sOS = "";
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(svendor);
				strBuf.append(Config.FIELD_SEPERATE);
				strBuf.append(smodel);
				strBuf.append(Config.FIELD_SEPERATE);
				strBuf.append(sOS);

				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_MENU_QUESTION); // menu Question
				strBuf.append(Config.DOMAIN_SEPERATE);
				strBuf.append(User.sID_MENU_KENH); // menu Kenh
			}
		}
		
		return strBuf.toString();
	}

	public static long getLongTime() {
		TimeZone tz = TimeZone.getTimeZone("GMT+7");
		Calendar calendar = Calendar.getInstance(tz);
		Date dd = new Date();
		calendar.setTime(dd);
		return calendar.getTime().getTime();
	}

	public static String getMyTime() {
		try {
			long newTime = User.iTimeDiff + MPlusLib.getLongTime();
			Date dd = new Date(newTime);
			return MPlusLib.dateToString(dd);
		} catch (Exception ex) {
		}
		return null;
	}

	public static String getCurrentDateString() {
		// MMddHHmmss
		TimeZone tz = TimeZone.getTimeZone("GMT+7");
		Calendar c = Calendar.getInstance(tz);
		Date dd = new Date(System.currentTimeMillis());
		c.setTime(dd);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DATE);
		// int h = c.get(Calendar.HOUR_OF_DAY);
		// int mi = c.get(Calendar.MINUTE);
		// int se = c.get(Calendar.SECOND);

		StringBuffer sBuf = new StringBuffer();
		if (User.sLang.equals(Config.LANG_EN)) {
			sBuf.append(d < 10 ? "0" : "");
			sBuf.append(d);
			sBuf.append("/");
			sBuf.append(m < 10 ? "0" : "");
			sBuf.append(m);
			sBuf.append("/");
			sBuf.append(y < 10 ? "0" : "");
			sBuf.append(y);
		} else {
			sBuf.append(m < 10 ? "0" : "");
			sBuf.append(m);
			sBuf.append("/");
			sBuf.append(d < 10 ? "0" : "");
			sBuf.append(d);
			sBuf.append("/");
			sBuf.append(y < 10 ? "0" : "");
			sBuf.append(y);
		}

		return sBuf.toString();
	}

	public static String dateToString(Date dd) {
		// MMddHHmmss
		TimeZone tz = TimeZone.getTimeZone("GMT+7");
		Calendar c = Calendar.getInstance(tz);
		if (dd == null)
			dd = new Date();
		c.setTime(dd);
		// int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DATE);
		int h = c.get(Calendar.HOUR_OF_DAY);
		int mi = c.get(Calendar.MINUTE);
		int se = c.get(Calendar.SECOND);
		// String t = (m < 10 ? "0" : "") + m + (d < 10 ? "0" : "") + d
		// + (h < 10 ? "0" : "") + h + (mi < 10 ? "0" : "") + mi
		// + (se < 10 ? "0" : "") + se;

		StringBuffer sBuf = new StringBuffer();
		sBuf.append(m < 10 ? "0" : "");
		sBuf.append(m);
		sBuf.append(d < 10 ? "0" : "");
		sBuf.append(d);
		sBuf.append(h < 10 ? "0" : "");
		sBuf.append(h);
		sBuf.append(mi < 10 ? "0" : "");
		sBuf.append(mi);
		sBuf.append(se < 10 ? "0" : "");
		sBuf.append(se);

		return sBuf.toString();
	}

	public static void showDebug(String data) {
		// System.out.println(data);
	}

	public static boolean isNewVersion(String version) {
		// Kiểm tra version nhận được có mới hơn version đang sử dụng
		// true: version mới ? version cũ
		// false: version mới <= version cũ
		if (version.equals(""))
			return false;
		try {
			String[] arOld = User.sVERSION.split("\\.");
			String[] arNew = version.split("\\.");
			int length = arOld.length > arNew.length ? arNew.length
					: arOld.length;
			for (int i = 0; i < length; ++i) {
				if (Integer.parseInt(arNew[i]) > Integer.parseInt(arOld[i]))
					return true;
				else if (Integer.parseInt(arNew[i]) < Integer
						.parseInt(arOld[i]))
					return false;
			}
			if (arNew.length > arOld.length)
				return true;
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	/**
	 * Mã hóa dữ liệu trước khi gửi lên Server
	 */
	public static String encode(String message) throws Exception {
		String ret = getRSA().encryptAES(message, Config.ecbKey);
		message = ret;
		return message;
	}

	/**
	 * Giải mã dữ liệu nhận về từ Server
	 * 
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		String ret;
		String[] aKetQua = str.split("#");
		long sTime = Long.parseLong(aKetQua[aKetQua.length - 1].trim());
		User.iTimeDiff = sTime - getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm dd/MM/yyyy");
//		if (User.sLang.equals("en"))
//			sdf = new SimpleDateFormat("kk:mm MM/dd/yyyy");
		Date d = new Date(sTime);
		User.setSrvTime(sdf.format(d));
		String sVal = aKetQua[0].trim();
		try {
			ret = getRSA().decryptAES(sVal.substring(4), Config.ecbKey);
			if (str.startsWith("val:")) {
				ret = "val:" + ret;
			} else if (str.startsWith("err:"))
				ret = "err:" + ret;
			else
				ret = "pen:" + ret;
		} catch (Exception e) {
			ret = null;
		}
		return ret;
	}

	public static long getTime() {
		TimeZone tz = TimeZone.getTimeZone("GMT+7");
		Calendar calendar = Calendar.getInstance(tz);
		Date dd = new Date();
		calendar.setTime(dd);
		return calendar.getTime().getTime();
	}

	public static String getResult(String result) {
		return result;
	}

	public static void debug(String tag, String msg) {
		Log.v(tag, msg);
	}

//	public static void debug(String tag, Exception ex) {
//		if (ex != null){
//			Log.v(tag, "EXCEPTION:");
//			ex.printStackTrace();
//		}
//		else
//			Log.v(tag, "EXCEPTION NULL");
//	}

	public static void debug(String TAG, String method, Exception ex) {
		if (ex != null){
			Log.v(TAG, "EXCEPTION:");
			ex.printStackTrace();
		}
		else
			Log.v(TAG, "EXCEPTION NULL");
	}
	
	public static void callSmsSender(Activity activity, String address,
			String body) {
		try {
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", body);
			sendIntent.putExtra("address", address);
			sendIntent.setType("vnd.android-dir/mms-sms");
			activity.startActivityForResult(sendIntent, 0);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "callSmsSender", ex);
		}
	}
}