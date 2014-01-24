package com.mpay.plus.baseconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;

public class MPHttpConn extends MPConnection {
	HttpURLConnection http = null;
	public boolean isProcessing;

	public MPHttpConn(Context context) {
		super(context);
	}

	public void sendAndReceive(String sData, boolean isvTransaction) {

		if (isNetworkAvailable()) {
			ERROR_TYPE = -1;
			sReceive = "";
			this.isProcessing = true;
			this.isTransaction = isvTransaction;
			startTime = 0;
			try {
				sSendData = Config.BKCD + MPlusLib.encode(sData);
			} catch (Exception ex) {
			}
			Thread threadProcess = new Thread(new Runnable() {
				public void run() {
					startTime = lastSentTime = System.currentTimeMillis();
					URL url;
					try {
						User.iURL_INDEX = (byte) (User.iURL_INDEX >= Config.aURL_HTTP.length ? 0
								: User.iURL_INDEX);
						url = new URL("http",
								Config.aURL_HTTP[User.iURL_INDEX],
								Config.iHTTP_PORT, Config.sHTTPSERVICE
										+ sSendData);
						http = (HttpURLConnection) url.openConnection();
						// http.setAllowUserInteraction(false);
						// http.setInstanceFollowRedirects(true);
						// http.setRequestMethod("GET");
						http.setReadTimeout(Config.itimeReceive);
						// http.connect();
					} catch (MalformedURLException e2) {
						ERROR_TYPE = ERROR_NOT_CONNECT_DOMAIN;
						User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_HTTP.length) ? 0
								: (User.iURL_INDEX + 1));
						isProcessing = false;
					} catch (IOException e1) {
						ERROR_TYPE = ERROR_NOT_OPEN_CONNNECT;
						User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_HTTP.length) ? 0
								: (User.iURL_INDEX + 1));
						isProcessing = false;
					}
					int resposeCode = -1;
					try {
						resposeCode = http.getResponseCode();
					} catch (Exception ex) {
					}
					
					if (ERROR_TYPE == ERROR_NONE && resposeCode == HttpURLConnection.HTTP_OK) {
						try {
							String ret = "";
							BufferedReader rd = new BufferedReader(
									new InputStreamReader(
											http.getInputStream(), "UTF-8"));
							ret = rd.readLine();
							rd.close();
							sReceive = MPlusLib.decode(ret);
						} catch (Exception e) {
							ERROR_TYPE = ERROR_RECEIVING_INTERUPT;
						}
					}
					
					isProcessing = false;
					try {
						if (http != null)
							http.disconnect();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			threadProcess.start();
			int i = 0;
			try {
				while (isProcessing
						&& i < ((Config.itimeReceive + Config.itimeConnect + Config.itimeSend))) {
					Thread.sleep(10);
					i += 10;
				}
			} catch (Exception ex) {
			}
			try {
				threadProcess.interrupt();
				threadProcess = null;
			} catch (Exception ex) {
			}
			if (System.currentTimeMillis() - startTime > ((Config.itimeReceive
					+ Config.itimeConnect + Config.itimeSend))) {
				ERROR_TYPE = ERROR_RECEIVE_TIMEOUT;
			}
		} else {
			ERROR_TYPE = ERROR_NOT_CONNECT_INTERNET;
		}
		if (ERROR_TYPE != -1) {
			sReceive = getStringErr();
		} else {
			sReceive = getStringReceive();
		}

	}
}
