package com.mpay.plus.baseconnect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.content.Context;

import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;

public class MPSocketConn extends MPConnection {
	private Socket socket;
	private ByteArrayOutputStream byteAOS;
	private Object lockConnect;
	private Object lockReceive;
	private ConnectProcess connectProcess;

	public MPSocketConn(Context context) {
		super(context);
		byteAOS = new ByteArrayOutputStream();
	}

	private Socket con2Server() {
		int dem = 0;
		do {
			Socket socket = null;
			try {
				MPlusLib.showDebug("open con2Server: "
						+ Config.aURL_SOCKET[User.iURL_INDEX]);
				String[] aUrl = Config.aURL_SOCKET[User.iURL_INDEX].split(":");
				InetAddress inteAddress = InetAddress.getByName(aUrl[0].trim());
				SocketAddress socketAddress = new InetSocketAddress(
						inteAddress, Integer.parseInt(aUrl[1]));
				socket = new Socket();
				socket.connect(socketAddress, Config.itimeConnect);
				socket.setSoTimeout(Config.itimeReceive);
				socket.setKeepAlive(true);
				dem = Config.aURL_SOCKET.length;
				return socket;
			} catch (UnknownHostException conex) {

				conex.printStackTrace();
				User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_SOCKET.length) ? 0
						: (User.iURL_INDEX + 1));
				ERROR_TYPE = ERROR_NOT_CONNECT_DOMAIN;
			} catch (SocketTimeoutException ste) {
				ste.printStackTrace();
				User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_SOCKET.length) ? 0
						: (User.iURL_INDEX + 1));
				ERROR_TYPE = ERROR_NOT_CONNECT_DOMAIN;
			} catch (IOException io) {
				io.printStackTrace();
				ERROR_TYPE = ERROR_NOT_CONNECT_DOMAIN;
				User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_SOCKET.length) ? 0
						: (User.iURL_INDEX + 1));
			} catch (Exception ex) {
				ex.printStackTrace();
				ERROR_TYPE = ERROR_NOT_CONNECT_DOMAIN;
				User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_SOCKET.length) ? 0
						: (User.iURL_INDEX + 1));
			}
			++dem;
		} while (dem < Config.aURL_SOCKET.length);
		return null;
	}

	class ConnectProcess extends Thread {
		Socket socketTemp;

		public ConnectProcess() {
		}

		public Socket getSocket() {
			return socketTemp;
		}
		
		private void closeSocket() {
			if(socketTemp != null){
				try {
					socketTemp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void run() {
			socketTemp = con2Server();
			if (lockConnect != null)
				try {
					synchronized (lockConnect) {
						lockConnect.notify();
					}
				} catch (Exception ex) {
				}
		}
	}

	private void checkConnection() {
		if (isNetworkAvailable()) {
			ERROR_TYPE = ERROR_NONE;
			User.iURL_INDEX = (byte) (User.iURL_INDEX >= Config.aURL_SOCKET.length ? 0
					: User.iURL_INDEX);
			boolean isConnectClosed = false;
			try {
				if (isSessionTimeOut() || socket == null || socket.isClosed()
						|| !socket.isConnected()) {
					isConnectClosed = true;
				}
			} catch (Exception ex) {
				isConnectClosed = true;
			}
			if (isConnectClosed) {
				close();
				lockConnect = new Object();
				connectProcess = new ConnectProcess();
				connectProcess.start();
				long start = System.currentTimeMillis();
				try {
					synchronized (lockConnect) {
						lockConnect.wait(Config.itimeConnect
								* Config.aURL_SOCKET.length);
						lockConnect.notify();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				lockConnect = null;
				if (!(System.currentTimeMillis() - start < Config.itimeConnect
						* Config.aURL_SOCKET.length)) {
					// start Connect Err
					if (ERROR_TYPE == ERROR_NONE)
						ERROR_TYPE = ERROR_OPEN_INTERUPT;
					close();
					User.iURL_INDEX = (byte) (((User.iURL_INDEX + 1) >= Config.aURL_SOCKET.length) ? 0
							: (User.iURL_INDEX + 1));
					try {
						connectProcess.interrupt();
						connectProcess = null;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					ERROR_TYPE = ERROR_NONE;
					socket = connectProcess.getSocket();
					try {
						connectProcess = null;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		} else {
			ERROR_TYPE = ERROR_NOT_CONNECT_INTERNET;
		}
	}

	private void send2Server(String message) {
		if (ERROR_TYPE == ERROR_NONE) {
			try {
				MPlusLib.showDebug("send2Server: " + message);
				message = MPlusLib.encode(message);
				lastSentTime = System.currentTimeMillis();
				byte arrSend[] = (message + "\r\n").getBytes();
				int dem = 0;
				while (dem < arrSend.length) {
					socket.getOutputStream().write(arrSend[dem]);
					dem++;
				}
				socket.getOutputStream().flush();
				ERROR_TYPE = ERROR_NONE;
			} catch (IOException ex) {
				ex.printStackTrace();
				ERROR_TYPE = ERROR_NOT_SEND_DATA;
				this.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				ERROR_TYPE = ERROR_NOT_SEND_DATA;
				this.close();
			}
		}
	}

	private void receiveFormServer(boolean isTransaction) {
		byteAOS.reset();
		if (ERROR_TYPE == -1) {
			try {
				byteReceive = -1;
				byteReceive = socket.getInputStream().read();
				while ((byteReceive != NULL_BYTE) && (byteReceive != -1)) {
					byteAOS.write((byte) byteReceive);
					byteReceive = socket.getInputStream().read();
				}
			} catch (Exception e) {
				ERROR_TYPE = ERROR_RECEIVING_INTERUPT;
			}
		}
	}

	class DataProcess implements Runnable {
		public void start() {
			new Thread(this).start();
		}

		public void run() {
			send2Server(sSendData);
			if (ERROR_TYPE == ERROR_NONE) {
				startTime = System.currentTimeMillis();
				receiveFormServer(isTransaction);
				if (lockReceive != null)
					try {
						synchronized (lockReceive) {
							lockReceive.notify();
						}
					} catch (Exception ex) {
					}
			} else {
				if (lockReceive != null)
					try {
						synchronized (lockReceive) {
							lockReceive.notify();
						}
					} catch (Exception ex) {
					}
			}
		}
	}

	public void sendAndReceive(String message, boolean isvTransaction) {
		sReceive = "";
		for (int i = 0; i < 2; ++i) {
			MPlusLib.showDebug("step: " + i);
			// <Open>
			checkConnection();
			// </Open>
			if (ERROR_TYPE == ERROR_NONE) {
				this.isTransaction = isvTransaction;
				lockReceive = new Object();
				sSendData = message;
				startTime = 0;
				DataProcess threadProcess = new DataProcess();
				threadProcess.start();
				// start wait connect
				try {
					synchronized (lockReceive) {
						lockReceive.wait(Config.itimeReceive);
						lockReceive.notify();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					threadProcess = null;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				lockReceive = null;
				String sKQ = "";
				if (ERROR_TYPE != ERROR_NOT_SEND_DATA) {
					i += 2;
					if (System.currentTimeMillis() - startTime <= Config.itimeReceive) {
						if (byteReceive == NULL_BYTE) {
							sKQ = MPlusLib.decode(new String(byteAOS.toByteArray()));
						} else {
							ERROR_TYPE = ERROR_RECEIVING_NOT_END;
							close();
						}
					} else if (ERROR_TYPE == ERROR_NONE) {
						ERROR_TYPE = ERROR_RECEIVE_TIMEOUT;
						close();
					}
					try {
						byteAOS.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					sReceive = sKQ;
					close();
					break;
				} else {
					continue;
				}
			} else {
				break;
			}
		}
		if (ERROR_TYPE != ERROR_NONE) {
			sReceive = getStringErr();
		} else {
			sReceive = getStringReceive();
		}
	}

	public void close() {
		super.close();
		
		if (socket != null) {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(connectProcess != null){
			try {
				connectProcess.closeSocket();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			socket = null;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			connectProcess = null;	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
