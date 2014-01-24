package com.mpay.plus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.widget.EditText;

/**
 * @author THANHNAM
 * 
 */
public class Util {
	
	public static String insertString(String st, String[] astr) {
		StringBuffer sBuf = new StringBuffer();
		String[] tmp = st.split("\\{");
		if (tmp != null)
			for (int i = 0; i < tmp.length; i++) {
				sBuf.append(tmp[i]);
				if (astr.length > i)
					sBuf.append(astr[i]);
			}
		return sBuf.toString();
	}

	public static String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}
	
	public static String formatAccount(CharSequence s) {
		String tmp = "";
		int count = 0;
		for (int i = 0; i < s.length(); ++i) {
			if ((i >= 0 && i <  4) || (i >= (s.length() - 4) &&  i < s.length())) {
				tmp += s.charAt(i);
			}else {
				if(count < 4){
					tmp += "x";
				}
				count++;
			}
		}
		return tmp;
	}

	public static String formatDate(CharSequence s) {
		int groupDigits = 0;
		String tmp = "";
		for (int i = 0; i < s.length(); ++i) {
			tmp += s.charAt(i);
			++groupDigits;
			if (groupDigits % 2 == 0 && i != s.length() - 1 && i < 4) {
				tmp += Config.markDate;
				groupDigits = 0;
			}
		}
		return tmp;
	}

	public static String formatNumbersAsMid(CharSequence s) {
		int groupDigits = 0;
		String tmp = "";
		for (int i = 0; i < s.length(); ++i) {
			tmp += s.charAt(i);
			++groupDigits;
			if (groupDigits == 4 && i != s.length() - 1) {
				tmp += Config.markMID;
				groupDigits = 0;
			}
		}
		return tmp;
	}

	public static String formatNumbersAsTien(String s) {
		String tmp = "";
		if (s != null && !s.equals("")) {
			String stemp = "";
			if (String.valueOf(s.charAt(0)).equals("-")
					|| String.valueOf(s.charAt(0)).equals("+")) {
				stemp = String.valueOf(s.charAt(0));
				s = s.substring(1);
			}
			int l = s.length();
			if (l > 3) {
				int f = l % 3;
				if (f == 0)
					f = 3;
				tmp = s.substring(0, f);
				for (int i = f; i < l; i = i + 3) {
					tmp += Config.markMoney + s.substring(i, i + 3);
				}
			} else
				tmp = s;
			if (!stemp.equals(""))
				tmp = stemp + tmp;
		}
		return tmp;
	}

	public static String keepContent(String s) {
		return s.toString().replaceAll("[^a-zA-Z0-9\\s\\.\\,]", "");
	}

	public static String keepGameAcc(String s) {
		return s.toString().replaceAll("[^a-zA-Z0-9\\_\\.\\-]", "");
	}
	
	public static String keepABCAndNumber(String s) {
		return s.toString().replaceAll("[^a-zA-Z0-9]", ""); // Should of course
															// be
		// more robust
	}

	public static String keepNumbersOnly(String s) {
		return s.toString().replaceAll("[^0-9]", ""); // Should of course be
														// more robust
	}

	private static boolean initSelectMID(int selecttion) {
		if((selecttion > 0 && selecttion < 4) || 
				(selecttion > 4 && selecttion < 9) || 
					(selecttion > 9 && selecttion < 14) || 
						(selecttion > 14 && selecttion < 19)|| 
							(selecttion > 19 && selecttion < 25)){
			return true;
		}else {
			return false;
		}
	}
	
	private static int initResults(int selecttion, int countText) {
		if(initSelectMID(selecttion)){
			selecttion++;
		}else {
			selecttion+=2;
		}
		if(selecttion > countText){
			if(initSelectMID(selecttion)){
				selecttion--;
			}else {
				selecttion-=2;
			}
		}
		return selecttion;
	}

	/**
	 * Kiểm tra số tiền có thỏa mãn một số điều kiện để thực hiện giao dịch
	 * input = 1,000,000 >=10.000 <100.000.000
	 * 
	 * @param soTien
	 * @return
	 */
	public static boolean KiemTraSoTien(String soTien) {
		if (soTien == null || soTien.equals(""))
			return false;
		String money = keepNumbersOnly(soTien);
		try {
			int temp = Integer.parseInt(money);
			if (temp < Config.iMONEY_MIN_VALUE
					|| temp > Config.iMONEY_MAX_VALUE)
				return false;
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static String sParseNumber(String sNum, int iLen) {
		String tmp = "";
		int k = iLen - sNum.length();
		for (int i = 0; i < k; i++) {
			tmp = "0" + tmp;
		}
		return tmp;
	}

	public static boolean KiemTraChuCaiVaSo(CharSequence str) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
		if (!pattern.matcher(str).matches())
			return false;
		return true;
	}

	public static String getMinute() {
		TimeZone tz = TimeZone.getTimeZone("GMT+7");
		Calendar calendar = Calendar.getInstance(tz);
		Date dd = new Date();
		calendar.setTime(dd);
		int mi = calendar.get(Calendar.MINUTE);
		int ss = calendar.get(Calendar.SECOND);
		String t = String.valueOf(mi) + String.valueOf(ss);
		return t;
	}

	protected static final Pattern MID_PATTERN = Pattern
			.compile("([0-9]{0,4})|([0-9]{4}-)|([0-9]{4}-[0-9]{0,4})|([0-9]{4}-[0-9]{4}-[0-9]{0,4})");
	protected static final Pattern TIEN_PATTERN = Pattern
			.compile("([0-9]{0,3})|([0-9]{1,2},[0-9]{3})|([0-9]{1,2},[0-9]{3},[0-9]{3})");
	protected static final Pattern DATE_PATTERN = Pattern
			.compile("(([012][0-9])||([3][01]))[/](([0][0-9])||[1][0-2])[/](19||20)[0-9][0-9]");
	
	public static void ChuanHoaDate(EditText etDate, TextWatcher watcher) {
			if (etDate.getText().toString().length() > 0 
					&& !DATE_PATTERN.matcher(etDate.getText().toString()).matches()) {
				String numbersOnly = Util.keepNumbersOnly(etDate.getText()
						.toString());
				String code = Util.formatDate(numbersOnly);
				etDate.removeTextChangedListener(watcher);
				etDate.setText(code);
				int countText = etDate.getText().toString().length();
				etDate.setSelection(countText);
				etDate.addTextChangedListener(watcher);
			}
	}
	
	public static void ChuanHoaMID(EditText etMID, TextWatcher watcher, int selecttion, int before) {
		try {
			int countText = etMID.getText().toString().length();
			if (countText > 0
					&& !MID_PATTERN.matcher(etMID.getText().toString()).matches()) {
				String numbersOnly = Util.keepNumbersOnly(etMID.getText().toString());
				String code = Util.formatNumbersAsMid(numbersOnly);
				etMID.removeTextChangedListener(watcher);
				etMID.setText(code);
				countText = etMID.getText().toString().length();
				if(before == 1){
					etMID.setSelection(selecttion--);
				} else if(before == 0){
					int select = initResults(selecttion, countText);
					if(select > countText){
						select = etMID.getText().toString().length();
					}
					etMID.setSelection(select);
				}else{
					etMID.setSelection(countText);
				}
				etMID.addTextChangedListener(watcher);
			}
		} catch (Exception e) {
			etMID.setSelection(etMID.getText().length());
//			Log.i("Log : ", "ChuanHoaMID : "+e.getMessage());
		}
	}

	public static void ChuanHoaMPIN(EditText etMPIN, TextWatcher watcher) {
		String numbersOnly = Util.keepNumbersOnly(etMPIN.getText().toString());
		etMPIN.removeTextChangedListener(watcher);
		etMPIN.setText(numbersOnly);
		etMPIN.setSelection(etMPIN.getText().toString().length());
		etMPIN.addTextChangedListener(watcher);
	}

	public static void ChuanHoaPhoneNumber(EditText etTel, TextWatcher watcher, int selecttion, int before) {
		try {
			int countText = etTel.getText().toString().length();
			if (countText > 0
					&& !MID_PATTERN.matcher(etTel.getText().toString()).matches()) {
				String numbersOnly = Util.keepNumbersOnly(etTel.getText().toString());
				String code = Util.formatNumbersAsMid(numbersOnly);
				etTel.removeTextChangedListener(watcher);
				etTel.setText(code);
				countText = etTel.getText().toString().length();
				if(before == 1){
					etTel.setSelection(selecttion--);
				} else if(before == 0){
					int select = initResults(selecttion, countText);
					if(select > countText){
						select = etTel.getText().toString().length();
					}
					etTel.setSelection(select);
				}else{
					etTel.setSelection(countText);
				}
				etTel.addTextChangedListener(watcher);
			}
		} catch (Exception e) {
			etTel.setSelection(etTel.getText().length());
//			Log.i("Log : ", "ChuanHoaPhoneNumber : "+e.getMessage());
		}
	}

	public static void ChuanHoaSoTien(EditText etSoTien, TextWatcher watcher) {
		try {
			int countText = etSoTien.getText().toString().length();
			if (countText > 0
					&& !TIEN_PATTERN.matcher(etSoTien.getText().toString())
							.matches()) {
				String numbersOnly = Util.keepNumbersOnly(etSoTien.getText()
						.toString());
				String code = Util.formatNumbersAsTien(numbersOnly);
				etSoTien.removeTextChangedListener(watcher);
				etSoTien.setText(code);
				countText = etSoTien.getText().toString().length();
				etSoTien.setSelection(countText);
				etSoTien.addTextChangedListener(watcher);
			}
		} catch (Exception e) {
			etSoTien.setSelection(etSoTien.getText().toString().length());
//			Log.i("Log : ", "ChuanHoaSoTien : "+e.getMessage());
		}
	}

	public static void chuanHoaNoiDung(EditText etDetail, TextWatcher watcher) {
		String str = etDetail.getText().toString();
		// int iCareIndex = etDetail.getSelectionEnd();
		int ileng1 = str.length();
		if (!KiemTraChuCaiVaSo(str)) {
			str = keepContent(str);
		}
		if (ileng1 > str.length()) {
			etDetail.removeTextChangedListener(watcher);
			etDetail.setText(str);
			etDetail.setSelection(str.length());
			etDetail.addTextChangedListener(watcher);
		}
	}
	
	public static void chuanHoaTenRieng(EditText etDetail, TextWatcher watcher) {
		String str = etDetail.getText().toString();
		int ileng1 = str.length();
		if (!KiemTraChuCaiVaSo(str)) {
			str = keepContent(str);
			str = str.toUpperCase();
		}
		if (ileng1 > str.length()) {
			etDetail.removeTextChangedListener(watcher);
			etDetail.setText(str);
			etDetail.setSelection(str.length());
			etDetail.addTextChangedListener(watcher);
		}
	}

	public static void chuanHoaMaHoaDon(EditText etDetail, TextWatcher watcher) {
		String str = etDetail.getText().toString();
		int ileng1 = str.length();
		
		if (!KiemTraChuCaiVaSo(str)) {
			str = keepABCAndNumber(str);
		}
		if (ileng1 > str.length()) {
			etDetail.removeTextChangedListener(watcher);
			etDetail.setText(str.toUpperCase());
			etDetail.setSelection(str.length());
			etDetail.addTextChangedListener(watcher);
		}
	}
	
	public static void chuanHoaGameAccount(EditText etDetail, TextWatcher watcher) {
		String str = etDetail.getText().toString();
		if (!KiemTraChuCaiVaSo(str)) {
			str = keepGameAcc(str);
		}
		
//		if (ileng1 > str.length()) {
			etDetail.removeTextChangedListener(watcher);
			etDetail.setText(str);
			etDetail.setSelection(str.length());
			etDetail.addTextChangedListener(watcher);
//		}
	}

	public static void chuanHoaMatKhau(EditText etPass, TextWatcher watcher) {
		String str = etPass.getText().toString();
		// int iCareIndex = etPass.getSelectionEnd();
		int ileng1 = str.length();
		if (!KiemTraChuCaiVaSo(str)) {
			str = keepABCAndNumber(str);
		}
		if (ileng1 > str.length()) {
			etPass.removeTextChangedListener(watcher);
			etPass.setText(str);
			etPass.setSelection(str.length());
			etPass.addTextChangedListener(watcher);
		}
	}
	
	public static void chuanHoaEmail(EditText etPass, TextWatcher watcher) {
//		String str = etPass.getText().toString();
//		// int iCareIndex = etPass.getSelectionEnd();
//		int ileng1 = str.length();
//		if (!KiemTraChuCaiVaSo(str)) {
//			str = keepABCAndNumber(str);
//		}
//		if (ileng1 > str.length()) {
//			etPass.removeTextChangedListener(watcher);
//			etPass.setText(str);
//			etPass.setSelection(str.length());
//			etPass.addTextChangedListener(watcher);
//		}
	}

	public static boolean isCIFNo(String sCif) {
		int iLen = sCif.length();
		if (iLen < 12)
			return false;
		if (sCif.startsWith(Config.BKCD))
			return true;
		else
			return false;
	}

	public static String sCatVal(String msg) {
		// Cat bo? VAL:
		String sVal = msg;
		if (msg.startsWith("err") || msg.startsWith("val"))
			sVal = msg.substring(4);
		return sVal.trim();
	}

	public static String getTimeClient() {
		try {
			long newTime = User.iTimeDiff + MPlusLib.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			Date d = new Date(newTime);
			return sdf.format(d);
		} catch (Exception ex) {
		}
		return null;
	}

	public static long getTimeClient2() {
		long newTime = User.iTimeDiff + MPlusLib.getTime();
		return newTime;
	}

	public static String getTimeClient3(String iTime) {
		try {
			Date date = null;
			try {
				long lTime = Long.parseLong(iTime);
				date = new Date(lTime);
				if (User.sLang.equals(Config.LANG_EN))
					return new SimpleDateFormat("hh:mm MM/dd/yyyy").format(date);
				else 
					return new SimpleDateFormat("hh:mm dd/MM/yyyy").format(date);
			} catch (NumberFormatException e) {
				try {
					if (User.sLang.equals(Config.LANG_EN))
						date = new SimpleDateFormat("hh:mm dd/MM/yyyy").parse(iTime.toString());
					else 
						return iTime;
				} catch (ParseException e2) {
					return iTime;
				}

				if (User.sLang.equals(Config.LANG_EN))
					return new SimpleDateFormat("hh:mm MM/dd/yyyy").format(date);
				else 
					return iTime;
			}
		}catch(Exception e){
		}
			return iTime;
	}
	
	public static String getTimeClient4(String iTime) {
		try {
			if (User.sLang.equals(Config.LANG_EN)){
				Date sdf = new SimpleDateFormat("dd/MM/yyyy").parse(iTime.toString());
				return new SimpleDateFormat("MM/dd/yyyy").format(sdf);
			}else {
				return iTime;
			}
		} catch (ParseException ex) {
		}
		return null;
	}


	public static boolean isIn24h(long iTime) {
		long newTime = getTimeClient2();
		MPlusLib.showDebug(String.valueOf(newTime - iTime));
		if (newTime - iTime < 24 * 60 * 60 * 1000) {
			return true;
		}
		return false;
	}

	public static String getDateFormat(int year, int month, int day) {
		String sday = String.valueOf(day);
		String smonth = String.valueOf(month + 1);
		if (day < 10)
			sday = "0" + sday;
		if (month < 9)
			smonth = "0" + smonth;
		String syear = String.valueOf(year);
		// if (User.sLang.equals("en")){
		// return smonth + "/" + sday +"/" +syear;
		// }else{
		return sday + "/" + smonth + "/" + syear;
		// }
	}

	public static boolean isNgayCap(String sDate) {
		boolean kt = true;
		try {
			String[] arrNgayCapTemp = sDate.split("/");
			TimeZone tz = TimeZone.getTimeZone("GMT+7");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(new Date());
			int day = Integer.parseInt(arrNgayCapTemp[0]);
			int month = Integer.parseInt(arrNgayCapTemp[1]);
			int year = Integer.parseInt(arrNgayCapTemp[2]);
			calendar.set(year, month - 1, day);
			Calendar calendar2 = Calendar.getInstance(tz);
			calendar2.setTime(new Date());
			if (calendar.getTime().getTime() > calendar2.getTime().getTime()) {
				kt = false;
			}
		} catch (Exception ex) {
			kt = false;
			ex.printStackTrace();
		}
		return kt;
	}

	/**
     * Lay id cua icon co ten iconName
     * 
     * */
    public static int getIconId(Context context, String iconName){
    	int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
    	if(id == 0)
    		id = context.getResources().getIdentifier(Config.ICON_DEFAULT_PRODDUCT, "drawable", context.getPackageName());
    	return id; 
    }
    
    /**
     * Lay id cua icon co ten iconName
     * 
     * */
    public static Drawable getImage(Context context, String iconName){
    	try{
	    	Drawable newMarker = context.getResources().getDrawable(getIconId(context, iconName));
	    	return newMarker;
    	} catch (Exception e) {
			return context.getResources().getDrawable(getIconId(context, Config.ICON_DEFAULT_PRODDUCT));
		}
    }

    /**
     * Kiem tra str co phai la 1 uri
     * */
    public static boolean isUri(String str){
    	return str.startsWith("http:");
    }
    
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
    		"^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$");
    
    /**
     * Kiem tra str co phai la 1 uri
     * */
    public static boolean isValidEmail(String target){
	    if (target == null) {
	        return false;
	    } else {
	        return EMAIL_ADDRESS_PATTERN.matcher(target).matches();
	    }
    }
     
    
    public static String getSuitImageUrl(String url) {
		String newUrl = url;
		
		String param = "";
		if(User.SCREENLAYOUT_DENSITY == DisplayMetrics.DENSITY_XHIGH) {
			param = "_xlg";
		} else if(User.SCREENLAYOUT_DENSITY == DisplayMetrics.DENSITY_HIGH) {
			param = "";
		} else if(User.SCREENLAYOUT_DENSITY == DisplayMetrics.DENSITY_MEDIUM) {
			param = "_nm";
		} else if(User.SCREENLAYOUT_DENSITY == DisplayMetrics.DENSITY_LOW) {
			param = "_nm";
		}
		
		int index = url.lastIndexOf('.');
		if(index > -1 && index < url.length())
			newUrl = url.substring(0, index) + param + url.substring(index, url.length());
		return newUrl;
	}
}