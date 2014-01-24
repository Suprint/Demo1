//package com.mpay.plus.baseconnect;
//
//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.telephony.SmsManager;
//import android.widget.Toast;
//
//import com.mpay.R;
//import com.mpay.plus.mplus.AMPlusCore;
//
//public class SMSProcess {
//	public static String strReturn;
//	public Object lock;
//	public boolean kt;
//
//	public void sendSMS(final AMPlusCore aMPlus, String phoneNumber, String message) {
//		strReturn = "";
//		lock = new Object();
//		kt = true;
//		String SENT = "SMS_SENT";
//		String DELIVERED = "SMS_DELIVERED";
//		
//		PendingIntent piSend = PendingIntent.getBroadcast(aMPlus, 0, new Intent(SENT), 0);
//		PendingIntent piDelive = PendingIntent.getBroadcast(aMPlus, 0, new Intent(DELIVERED), 0);
//		aMPlus.registerReceiver(new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				int icode = getResultCode();
//				switch (icode) {
//				case Activity.RESULT_OK:
//					strReturn = aMPlus.getString(R.string.sms_sended);
//					break;
//				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				case SmsManager.RESULT_ERROR_NO_SERVICE:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				case SmsManager.RESULT_ERROR_NULL_PDU:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				case SmsManager.RESULT_ERROR_RADIO_OFF:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				default:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//				}
//				Toast.makeText(aMPlus, strReturn, Toast.LENGTH_LONG).show();
//			}
//		}, new IntentFilter(SENT));
//		
//		aMPlus.registerReceiver(new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				int icode = getResultCode();
//				switch (icode) {
//				case Activity.RESULT_OK:
//					strReturn = aMPlus.getString(R.string.sms_sended);
//					break;
//				case Activity.RESULT_CANCELED:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				default:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//				}
//				Toast.makeText(aMPlus, strReturn, Toast.LENGTH_LONG).show();
//			}
//		}, new IntentFilter(DELIVERED));
//		
//		try {
//			SmsManager sms = SmsManager.getDefault();
//			sms.sendTextMessage(phoneNumber, null, message, piSend, piDelive);
//		} catch (Exception ex) {
//			strReturn = aMPlus.getString(R.string.sms_not_send);
//			Toast.makeText(aMPlus, strReturn, Toast.LENGTH_LONG).show();
//		}
//	}
//	
//	public void sendActiveSMS(final AMPlusCore aMPlus, String phoneNumber, String message) {
//		strReturn = "";
//		lock = new Object();
//		kt = true;
//		String SENT = "SMS_SENT";
//		String DELIVERED = "SMS_DELIVERED";
//		
//		PendingIntent piSend = PendingIntent.getBroadcast(aMPlus, 0, new Intent(SENT), 0);
//		PendingIntent piDelive = PendingIntent.getBroadcast(aMPlus, 0, new Intent(DELIVERED), 0);
//		aMPlus.registerReceiver(new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				int icode = getResultCode();
//				switch (icode) {
//				case Activity.RESULT_OK:
//					strReturn = aMPlus.getString(R.string.sms_sended);
//					break;
//				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				case SmsManager.RESULT_ERROR_NO_SERVICE:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				case SmsManager.RESULT_ERROR_NULL_PDU:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				case SmsManager.RESULT_ERROR_RADIO_OFF:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				default:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//				}
//				
//				AMPlusCore.sThongBao = strReturn;
//				aMPlus.onCreateDialog(AMPlusCore.THONG_BAO).show();
//			}
//		}, new IntentFilter(SENT));
//		
//		aMPlus.registerReceiver(new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				int icode = getResultCode();
//				switch (icode) {
//				case Activity.RESULT_OK:
//					strReturn = aMPlus.getString(R.string.sms_sended);
//					break;
//				case Activity.RESULT_CANCELED:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//					break;
//				default:
//					strReturn = aMPlus.getString(R.string.sms_not_send);
//				}
//				
//				AMPlusCore.sThongBao = strReturn;
//				aMPlus.onCreateDialog(AMPlusCore.THONG_BAO).show();
//			}
//		}, new IntentFilter(DELIVERED));
//		
//		try {
//			SmsManager sms = SmsManager.getDefault();
//			sms.sendTextMessage(phoneNumber, null, message, piSend, piDelive);
//		} catch (Exception ex) {
//			strReturn = aMPlus.getString(R.string.sms_not_send);
//			AMPlusCore.sThongBao = strReturn;
//			aMPlus.onCreateDialog(AMPlusCore.THONG_BAO).show();
//		}
//	}
//}