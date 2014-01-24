package com.mpay.plus.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;

import com.mpay.agent.R;
import com.mpay.mplus.dialog.DialogPending;
import com.mpay.plus.config.Config;

public class AutoUpdate {
	public static Context activity = null;
	private String fileEx = "";
	private String fileNa = "";
	public String strURL = "";
//	static ProgressDialog pd = null;
	static String strMsg = "";
	private static DialogPending dialogpendding;

	public AutoUpdate(Context activity, String sLink) {
		AutoUpdate.activity = activity;
		strURL = sLink;
	}

	public void check() {
		if (isNetworkAvailable(activity) == false) {
			return;
		}
		Start();
	}

	public static boolean isNetworkAvailable(Context ctx) {
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressLint("NewApi")
	private String doDownloadTheFile(String strPath) {
		if (URLUtil.isNetworkUrl(strPath)) {
			URL myURL;
			String ret = "";
			try {
				myURL = new URL(strPath);
			} catch (MalformedURLException e) {
				return ret;
			}
			URLConnection conn;
			try {
				conn = myURL.openConnection();
			} catch (IOException e) {
				return ret;
			}
			try {
				conn.connect();
			} catch (IOException e) {
				return ret;
			}
			InputStream is;
			try {
				is = conn.getInputStream();
			} catch (IOException e) {
				return ret;
			}
			File myTempFile;
			try {
				conn.setReadTimeout(Config.itimeReceive);
				myTempFile = File.createTempFile(fileNa, "." + fileEx);
				FileOutputStream fos = new FileOutputStream(myTempFile);
				byte buf[] = new byte[128];
				do {
					int numread = is.read(buf);
					if (numread <= 0) {
						break;
					}
					fos.write(buf, 0, numread);
				} while (true);
				try {
					is.close();
					is = null;
					fos.close();
					conn = null;
					myURL = null;
				} catch (Exception ex) {
				}

				try {
					Runtime.getRuntime().exec(
							"chmod 777 " + myTempFile.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}

				openFile(myTempFile);
			} catch (SocketTimeoutException ex) {
				return ret;
			} catch (IOException e) {
				return ret;
			}
		}
		return "";
	}

	private void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		activity.startActivity(intent);
	}

	public void delFile(String filePath) {
		File myFile = new File(filePath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	static class MyHandler extends Handler {
		WeakReference<Context> mActivity;

		MyHandler(Context activity) {
			mActivity = new WeakReference<Context>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			dialogpendding.dismiss();
			if (!strMsg.equals("")) {
				new AlertDialog.Builder(activity)
						.setTitle(activity.getString(R.string.thong_bao))
						.setMessage(strMsg)
						.setPositiveButton(activity.getString(R.string.cmd_ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		}
	};

	MyHandler handler = new MyHandler(activity);

	public void Start() {
		dialogpendding = new DialogPending(activity);
		dialogpendding.setTitles(activity.getString(R.string.dang_xu_ly_title));
		dialogpendding.setContents(activity.getString(R.string.dang_xu_ly));
		dialogpendding.show();
		
//		pd = ProgressDialog.show(activity, "",
//				activity.getString(R.string.dang_xu_ly), true, false);
//		pd.setCancelable(false);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					fileEx = strURL.substring(strURL.lastIndexOf(".") + 1,
							strURL.length()).toLowerCase();
					fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,
							strURL.lastIndexOf("."));
					strMsg = doDownloadTheFile(strURL);
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					strMsg = e.getMessage();
					handler.sendEmptyMessage(0);
				}
			}
		});
		thread.start();
	}
}