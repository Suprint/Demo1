package com.mpay.plus.baseconnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mpay.agent.R;

public abstract class MPConnection {
	public static final byte UPDATE_REQUIRED = -3; // Chua dang ky, kich hoat
													// ung dung
	public static final byte ERROR_NOT_ACTIVATION = -2; // Chua dang ky, kich
														// hoat ung dung
	public static final byte ERROR_NONE = -1; // Không có lỗi
	public static final byte ERROR_NOT_CONNECT_INTERNET = 0;// Không có kết nối
															// mạng internet
	public static final byte ERROR_NOT_CONNECT_DOMAIN = 1;// Không có kết nối
															// tới domain
	public static final byte ERROR_NOT_OPEN_CONNNECT = 2;// Có mạng internet
															// nhưng ko mở được
															// luồn nhập xuất
	public static final byte ERROR_OPEN_INTERUPT = 3;// Bị ngắt do quá thời gian
														// cho phép kết nối
	public static final byte ERROR_NOT_ENCODE_DATA = 4;// Có lỗi khi mã hóa dữ
														// liệu gửi đi
	public static final byte ERROR_NOT_SEND_DATA = 5;// Có lỗi khi gửi dữ liệu
														// đi
	public static final byte ERROR_SEND_INTERUPT = 6;// Bị ngắt do quá thời gian
														// gửi dữ liệu
	public static final byte ERROR_NOT_DECODE_DATA = 7;// Có lỗi khi giải mã dữ
														// liệu nhận về
	public static final byte ERROR_RECEIVE_TIMEOUT = 8;// Kết quả đã gửi đi
														// nhưng quá thời gian
														// nhận(timeout). Chưa
														// rõ kết quả thực hiện
	public static final byte ERROR_RECEIVING_NOT_END = 9;// Kết quả đã được nhận
															// về nhưng chưa
															// nhận được ký tự
															// kết thúc
	public static final byte ERROR_RECEIVING_INTERUPT = 10;// Kết quả đang nhận
															// về thì bị mất kết
															// nối
	public static byte ERROR_TYPE = ERROR_NONE;

	public static final byte NULL_BYTE = '\0';
	// public boolean isConnected = false;
	public byte indexURL = 0;
	public int byteReceive;
	public long lastSentTime;
	public String sSendData;
	public String sReceive;
	public boolean isTransaction;
	public long startTime;

	private Context mContext;

	public MPConnection(Context context) {
		ERROR_TYPE = ERROR_NONE;
		indexURL = 0;
		lastSentTime = 0;
		mContext = context;
	}

	public boolean isSessionTimeOut() {
		return System.currentTimeMillis() - lastSentTime > 299990;
	}

	public String getDataReceive() {
		return sReceive;
	}

	public String getDataReceive2() {
		return sReceive.split("<>")[0];
	}

	public byte getTagErr() {
		return ERROR_TYPE;
	}

	public void setTagErr(byte iTagErr) {
		ERROR_TYPE = iTagErr;
	}

	public abstract void sendAndReceive(String message, boolean isvTransaction);

	private Context getContext() {
		return mContext;
	}

	public String getStringErr() {
		String sKQ = "";
		try {
			if (ERROR_TYPE == ERROR_NOT_CONNECT_INTERNET) {
				sKQ = "err:" + getContext().getString(R.string.internet_not);
			} else if (ERROR_TYPE >= ERROR_NOT_CONNECT_DOMAIN
					&& ERROR_TYPE <= ERROR_SEND_INTERUPT) {
				sKQ = "err:" + getContext().getString(R.string.connect_not);
			} else {
				if (isTransaction) {
					sKQ = "pen:"
							+ getContext().getString(
									R.string.transaction_pedding);
				} else
					sKQ = "err:"
							+ getContext().getString(R.string.error_receive);
			}
		} catch (Exception e) {
		}
		return sKQ;
	}

	public String getStringReceive() {
		String sKQ = "";
		String sTemp = getDataReceive2();
		if (sTemp.equals("") || sTemp.equals("err") || sTemp.equals("err:")
				|| sTemp.length() < 4) {
			if (isTransaction) {
				sKQ = "pen:"
						+ getContext().getString(R.string.transaction_pedding);
			} else {
				sKQ = "err:" + getContext().getString(R.string.error_receive);
			}
		} else
			sKQ = getDataReceive();
		return sKQ;
	}

	public boolean isNetworkAvailable() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			if (activeNetworkInfo != null
					&& activeNetworkInfo.isAvailable()
					&& activeNetworkInfo.isConnected()
					&& activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public void close() {
	}

}
