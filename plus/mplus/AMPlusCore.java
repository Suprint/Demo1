package com.mpay.plus.mplus;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.mpay.FunctionType;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ThreadThucThi;
import com.mpay.mplus.dialog.DialogConfirmMessage;
import com.mpay.mplus.dialog.DialogFrmConfirm;
import com.mpay.mplus.dialog.DialogMpin;
import com.mpay.mplus.dialog.DialogOTP;
import com.mpay.mplus.dialog.DialogOtherList;
import com.mpay.mplus.dialog.MessageConfirm;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.baseconnect.MPHttpConn;
import com.mpay.plus.baseconnect.MPSocketConn;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Group;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.LogTransactionItem;
import com.mpay.plus.database.News;
import com.mpay.plus.database.Product;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.system.AutoUpdate;
import com.mpay.plus.system.DynamicMenu;
import com.mpay.plus.system.FrmRegistration;
import com.mpay.plus.util.Util;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class AMPlusCore extends SlidingFragmentActivity {// implements
															// OnClickListener {
	public static final String TAG = "AMPlusCore";

	public ProgressDialog ProDialog;

	public static MPConnection mpConnect;
	public static ThreadThucThi threadThucThi;

	public static DBAdapter dbAdapter;
	public static byte ACTION = -1;

	public static final byte EXITCODE = 1;
	public static final byte DANG_KY = 2;
	public static final byte THONG_BAO = 3;
	public static final byte XAC_NHAN = 4;
	public static final byte MPIN = 5;
	public static final byte TRY_CONNECT = 6;
	public static final byte ACCOUNT_LIST = 7;
	public static final byte UPDATE = 8;
	public static final byte DELETE_LOG = 9;
	public static final byte GET_CONTACTS = 10;
	public static final byte SUCCESS = 11;

	public static final byte CHANGE_LANG = 12;
	public static final byte PENDING = 13;
	public static final byte TRANSACTION_SUCCESS = 14;

	public static final byte iGET_OTP = 15;
	public static final byte iTRA_CUU_NAME = 16;
	public static final byte iCHUYEN_KHOAN = 17;
	public static final byte iTHANH_TOAN = 18;
	public static final byte iGET_DETAIL = 19;
	public static final byte iBALANCE_INQUIRY = 20;
	public static final byte iMINI_STATMENT = 21;
	public static final byte iLOAN_INQUIRY = 22;
	public static final byte iNAP_TIEN_AGENT = 23;
	public static final byte iNAP_TIEN_AGENT_ATM = 45;
	public static final byte iRUT_TIEN_AGENT = 24;
	public static final byte iGET_PHI_RUT_TIEN_AGENT = 25;
	public static final byte iDANG_KY_NAP_RUT_TIEN = 26;
	public static final byte iACTIVE_APP = 27;
	public static final byte iACTIVE_APP_FORGOT_PASS = 49;
	public static final byte iACTIVE_APP_FORGOT_PASS_RE= 50;
	public static final byte iCLOSE_AGENT = 28;
	public static final byte iGET_VERSION_LINK = 29;
	public static final byte iGET_DETAIL_BILL_QRCODE = 30;

	public static final byte iGET_SUGGESTIONS = 31;
	public static final byte iSEND_SUGGESTION = 32;
	public static final byte iUSED = 33;
	public static final byte iTHANH_TOAN_HOA_DON = 34;
	public static final byte iMUA_HANG = 35;
	public static final byte iTOPUP_TRA_TRUOC = 36;
	public static final byte iTOPUP_TRA_SAU = 37;
	public static final byte iTOPUP_GAME = 38;
	public static final byte iGET_LOG = 39;
	public static final byte iXAC_NHAN_OTP = 40;
	public static final byte LIST_OTHER = 42;
	public static final byte XAC_NHAN_OTP = 43;
	public static final byte XAC_NHAN_PENDING = 44;
	public static final byte L_LINK_ATM = 45;
	public static final byte L_LINK_ATM_REMOVE = 46;
	public static final byte L_LINK_EMONKEY = 47;
	public static final byte L_LINK_EMONKEY_REMOVE = 48;
	
	public static byte FLAG_DIALOG = -1;
	
	public static FunctionType CurFuntion = FunctionType.COMMON;

	public static String sThongBao = "";
	//public static boolean isFisrt = true;// Kiem tra de lay ve Menu, News

	public static int iMyAccIndex = 0;
	public boolean isThucHienLai = false;
	public boolean isAllowUpdate;
	public boolean isDangKyForm; // Dùng để kiểm soát cho phép người dùng thực
									// hiện lệnh
	public boolean isDoiMPINForm;
	public boolean isFinished = false;
	public boolean isBusy = false;
	protected String mpinTemp;
	private static Context mContext;

	public static int selection = 0;
	public static int beforesSelection = 0;
	
	public String item_Amount_list;
	public static boolean isPendding = false;
	public MessageConfirm messageConfirm = null;

	public static Vector<Product> mProduct;
	protected static String[] stemp = null;
	public static String sLenh = "";
	
	public static DynamicMenu settingActivity = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			// Set Sliding menu
			setBehindContentView(R.layout.old_menu_frame);
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			t.replace(R.id.menu_frame, MenuMain.newInstance(AMPlusCore.this));
			t.commit();

			// customize the SlidingMenu
			SlidingMenu sm = getSlidingMenu();
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setShadowDrawable(R.drawable.menu_shadow);
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			sm.setFadeDegree(0.35f);
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "onCreate",  ex);
		}
		
		if(messageConfirm == null){
			messageConfirm = new MessageConfirm();
		}if(settingActivity == null){
			settingActivity = new DynamicMenu(AMPlusCore.this);
		}if (AMPlusCore.dbAdapter == null) {
			AMPlusCore.dbAdapter = new DBAdapter(AMPlusCore.this);
		}if(User.arrMyAcountATMLink == null){
			User.arrMyAcountATMLink = new ArrayList<String>();
		} 

		isDangKyForm = false;
		isAllowUpdate = true;
		isDoiMPINForm = false;
		setMyAccountIndex(0);
		
		
		hideKeyBoards();

	}
	
//	public void setSettingActivity(String data) {
//		settingActivity.setSettingActivity(data);
//	}
//	
//	public static List<String> getSettingActivityMenu() {
//		if(dataSetting == null || dataSetting.size() <= 0)
//			initSettingActivityMenu();
//		return dataSetting;
//	}
//	
//	private static void initSettingActivityMenu() {
//		String settingItem[] = settingActivity.getSettingActivity().split(",", -1);
//		for (int i = 0; i < settingItem.length; i++) {
//			dataSetting.add(settingItem[i]);
//		}
//	}
	

	@Override
	public void onResume() {
		super.onResume();
		if (User.iUPDATE_FLAG == 1 || User.iUPDATE_FLAG == 0 && isAllowUpdate) {
			showDiaLogUpdateVersion();
		}
//		initSettingActivityMenu();
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;

		if (!isBusy) {
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				toggle();
				break;

			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else
			result = false;

		return result;
	};

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == EXITCODE) {
			setResult(EXITCODE, null);
			finish();
		} else if (resultCode == SUCCESS) {
			setResult(SUCCESS, null);
			finish();
		} else if (resultCode == RESULT_OK) {
			if (requestCode == GET_CONTACTS) {
				Cursor cursor = null;
				String Number = "";
				try {
					Uri result = data.getData();
					String id = result.getLastPathSegment();
					cursor = getContentResolver().query(Phone.CONTENT_URI,
							null, Phone.CONTACT_ID + "=?", new String[] { id },
							null);
					int emailIdx = cursor.getColumnIndex(Phone.DATA);
					if (cursor.moveToFirst()) {
						Number = cursor.getString(emailIdx);
					} else {
					}
				} catch (Exception e) {
				} finally {
					if (cursor != null) {
						cursor.close();
					}
					if (Number.startsWith("+84"))
						Number = Number.replace("+84", "0");
					else if (Number.startsWith("084"))
						Number = Number.replace("084", "0");
					setTelFromContact(Util.keepNumbersOnly(Number));
				}
			}
		}
	}

	public void onStop() {
		EasyTracker.getInstance().activityStop(AMPlusCore.this);
		clear();
		super.onStop();
	}

	@Override
	protected void onStart() {
		EasyTracker.getInstance().activityStart(AMPlusCore.this);
		super.onStart();
	}
	
	public static void sendEventGA(String sCategory, String sAction, String sLable, Long sValues) {
		EasyTracker.getInstance().setContext(mContext);
		EasyTracker.getTracker().sendEvent(sCategory, sAction, sLable, sValues);
	}
	
	public static void sendExceptionGA(Context mContext, Exception e) {
		String error = e.getMessage()+ " \n";
			for (int i = 0; i < e.getStackTrace().length; i++) {
				if(i > 1)
					break;
				error += e.getStackTrace()[i] + " \n";
			}
		EasyTracker.getInstance().setContext(mContext);
		EasyTracker.getTracker().sendException(error, false);
	}
	
	
	public void onDestroy() {
		// contextMPCore = null;
		sThongBao = "";
		try {
			threadThucThi.clearData();
			threadThucThi = null;
		} catch (Exception ex) {
		}
		clear();
		super.onDestroy();
	}

	/**
	 * THONG_BAO Tạo dialog thông báo XAC_NHAN Tạo dialog xác nhận giao dịch
	 * MPIN Tạo dialog nhập MPIN
	 */
	public Dialog onCreateMyDialog(int id) {
		switch (id) {
		case THONG_BAO:
			
			DialogConfirmMessage dialogConfirmMessage = new DialogConfirmMessage(AMPlusCore.this);
			dialogConfirmMessage.setMyMessage(sThongBao);
			dialogConfirmMessage.setDongy(getString(R.string.cmd_ok));
			dialogConfirmMessage.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
				
				@Override
				public void onclickKhongDongY() {
				}
				
				@Override
				public void onclickDongY() {
					sThongBao = "";
					cmdNextThongBao();
				}
			});
			
			return dialogConfirmMessage;

		case XAC_NHAN:
			DialogFrmConfirm dialogConfirm = new DialogFrmConfirm(AMPlusCore.this);
			dialogConfirm.setMPinEnable(getMessageConfirm().getStatus());
			dialogConfirm.setTitles(getMessageConfirm().getTitle());
			dialogConfirm.setMyMessage(getMessageConfirm().getMessage());
			dialogConfirm.setMyMessage1(getMessageConfirm().getMessage1());
			dialogConfirm.setMyMessage_(getMessageConfirm().getMessage_());
			dialogConfirm.setOther(getMessageConfirm().getOther());
			dialogConfirm.setBtn_dongy(getMessageConfirm().getBtn_dongy());
			dialogConfirm.setBtn_khongdongy(getMessageConfirm().getBtn_khongdongy());
			dialogConfirm.setInitMpin(getMessageConfirm().getInitMpin());
			dialogConfirm.setClickDialogConfirm_Dongy(new DialogFrmConfirm.onClickDialogConfirm_Dongy() {
				
				@Override
				public void onClickDonY() {
					if (User.isRegisted) {
						cmdNextXacNhan();
					} else {
						onCreateMyDialog(DANG_KY).show();
					}
				}
			});
			
			dialogConfirm.setClickDialogConfirm_Dongy_(new DialogFrmConfirm.onClickDialogConfirm_Dongy_() {
				
				@Override
				public void onClickDonY_(String data) {
					cmdNextXacNhan(data);
				}
			});
			dialogConfirm.setClickDialogConfirm_Khongdongy(new DialogFrmConfirm.onClickDialogConfirm_Khongdongy() {
				
				@Override
				public void onClickKhongDongY() {
					cmdBackXacNhan();
				}
			});
			
//			dialogConfirm.setKhongdongy(getString(R.string.btn_khong_desau));
//			dialogConfirm.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
//				
//				@Override
//				public void onclickKhongDongY() {
//					cmdBackXacNhan();
//				}
//				
//				@Override
//				public void onclickDongY() {
//					if (User.isRegisted) {
//						cmdNextXacNhan();
//					} else {
//						onCreateMyDialog(DANG_KY).show();
//					}
//				}
//			});
			return dialogConfirm;
			
		case XAC_NHAN_OTP:
			final DialogOTP dialogConfirm_otp = new DialogOTP(AMPlusCore.this);
			dialogConfirm_otp.setMessages_(sThongBao);
			dialogConfirm_otp.setOnClickClickDialogMpin(new DialogOTP.onClickDialogMpin() {
				
				@Override
				public void clickKhongDongY() {
					cmdBackXacNhanOTP();
				}
				
				@Override
				public void clickDongY(String data) {
					cmdNextXacNhanOTP(data);
				}
			});
			
			return dialogConfirm_otp;
			
		case XAC_NHAN_PENDING:
			
			final DialogConfirmMessage dialogxacnhan = new DialogConfirmMessage(AMPlusCore.this);
			dialogxacnhan.setTitles(getString(R.string.xac_nhan));
			dialogxacnhan.setMyMessage(sThongBao);
			dialogxacnhan.setDongy(getString(R.string.cmd_check));
			dialogxacnhan.setKhongdongy(getString(R.string.cmd_back));
			dialogxacnhan.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
				
				@Override
				public void onclickKhongDongY() {
					dialogxacnhan.dismiss();
					cmdBackXacNhan();
				}
				
				@Override
				public void onclickDongY() {
					if (User.isRegisted) {
						cmdNextXacNhan();
						dialogxacnhan.dismiss();
					} else {
						onCreateMyDialog(DANG_KY).show();
					}
				}
			});
			return dialogxacnhan;

		case SUCCESS:
			
			final DialogConfirmMessage dialogsuccess = new DialogConfirmMessage(AMPlusCore.this);
			dialogsuccess.setMyMessage(sThongBao);
			dialogsuccess.setDongy(getString(R.string.cmd_yes));
			dialogsuccess.setKhongdongy(getString(R.string.cmd_no));
			dialogsuccess.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
				
				@Override
				public void onclickKhongDongY() {
					dialogsuccess.dismiss();
					cmdBackXacNhan();
				}
				
				@Override
				public void onclickDongY() {
					if(User.isRegisted){
						cmdNextXacNhan();
					}else {
						onCreateMyDialog(DANG_KY).show();
					}
					dialogsuccess.dismiss();
				}
			});
			return dialogsuccess;

			
		case PENDING:
			
			final DialogConfirmMessage dialogpending = new DialogConfirmMessage(AMPlusCore.this);
			dialogpending.setMyMessage(getString(R.string.transaction_pedding));
			dialogpending.setDongy(getString(R.string.cmd_check));
			dialogpending.setKhongdongy(getString(R.string.cmd_back));
			dialogpending.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
				
				@Override
				public void onclickKhongDongY() {
					dialogpending.dismiss();
					cmdBackPending();
				}
				
				@Override
				public void onclickDongY() {
					if(User.isRegisted){
						cmdOKPending();
					}else {
						onCreateMyDialog(DANG_KY).show();
					}
				}
			});
			return dialogpending;

		case MPIN:
			
		final DialogMpin dialogMpin = new DialogMpin(this);
		dialogMpin.setOnClickClickDialogMpin(new DialogMpin.onClickDialogMpin() {
			
			@Override
			public void clickKhongDongY() {
				cmdBackMPIN();
			}
			
			@Override
			public void clickDongY(String data) {
				dialogMpin.dismiss();
				if (User.isRegisted) {
					if(data.length() < 6){
						sThongBao = getString(R.string.confirm_mpin_wrong);
						showDiaLogWrongMPin(false);
					}else if (kiemTraMPIN(data)) {
							if (ACTION == iACTIVE_APP
									|| ACTION == iCHUYEN_KHOAN) {
								mpinTemp = data;
							}
							cmdNextMPIN();
					}
				} else {
					onCreateMyDialog(DANG_KY).show();
				}
			}
		});
		return dialogMpin;

		case TRY_CONNECT:
			
			final DialogConfirmMessage dialogconfirmconnect = new DialogConfirmMessage(AMPlusCore.this);
			dialogconfirmconnect.setMyMessage(sThongBao);
			dialogconfirmconnect.setDongy(getString(R.string.cmd_back));
//			dialogconfirmconnect.setKhongdongy(getString(R.string.cmd_back));
			dialogconfirmconnect.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
				
				@Override
				public void onclickKhongDongY() {
					dialogconfirmconnect.dismiss();
				}
				
				@Override
				public void onclickDongY() {
					dialogconfirmconnect.dismiss();
//					cmdErrorConnect();
//					goBack();
//					IProcess iprocess = AMPlusCore.threadThucThi
//							.getIProcess();
//					byte iTag = AMPlusCore.threadThucThi
//							.getTag();
//					AMPlusCore.threadThucThi = new ThreadThucThi(
//							AMPlusCore.this);
//					AMPlusCore.threadThucThi.setIProcess(
//							iprocess, iTag);
//					AMPlusCore.threadThucThi.Start();
				}
			});
			
			return dialogconfirmconnect;

		case DANG_KY:
			
			final DialogConfirmMessage dialogdangky = new DialogConfirmMessage(AMPlusCore.this);
			dialogdangky.setTitles(getString(R.string.xac_nhan));
			dialogdangky.setMyMessage(getString(R.string.dangky_alert_message_));
			dialogdangky.setDongy(getString(R.string.cmd_ok));
			dialogdangky.setKhongdongy(getString(R.string.cmd_back));
			dialogdangky.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
				
				@Override
				public void onclickKhongDongY() {
					dialogdangky.dismiss();
				}
				
				@Override
				public void onclickDongY() {
					startActivityForResult(new Intent(AMPlusCore.this,
							FrmRegistration.class), 0);
					dialogdangky.dismiss();
				}
			});
			return dialogdangky;
//		case LIST_AMOUNT:
//			
//			final DialogListPriceDetail dialogListPriceDetail = new DialogListPriceDetail(AMPlusCore.this, lstPrice, lstPromo, indexCurrent);
//			dialogListPriceDetail.setClickDialogListener(new DialogListPriceDetail.onClickDialogListener() {
//				
//				@Override
//				public void onCancel() {
//					dialogListPriceDetail.dismiss();
//				}
//				
//				@Override
//				public void onAccept(int value) {
//					cmdChonAmountList(value);
//					dialogListPriceDetail.dismiss();
//				}
//			});
//			
//			return dialogListPriceDetail;
			
		case LIST_OTHER:
			
			final DialogOtherList dialogListcauhoi = new DialogOtherList(AMPlusCore.this);
			dialogListcauhoi.setClickDialogListener(new DialogOtherList.onClickDialogListener() {
				
				@Override
				public void onCancel() {
					dialogListcauhoi.dismiss();
				}
				
				@Override
				public void onAccept(int index, AnyObject data) {
					cmdChoncauhoiList(index, data);
					dialogListcauhoi.dismiss();
				}
			});
		return dialogListcauhoi;
		}
		return null;
	}
	
	public void dialogConfrim(ConfirmItem confirm, IProcess iProcess, AMPlusCore amPlusCore) {
		DialogFrmConfirm dialog = new DialogFrmConfirm(this, confirm);
		dialog.setIProcess(iProcess);
		dialog.setParent(amPlusCore);
		dialog.setMPinEnable(getMessageConfirm().getStatus());
		dialog.setTitles(getMessageConfirm().getTitle());
		dialog.setMyMessage(getMessageConfirm().getMessage());
		dialog.setMyMessage1(getMessageConfirm().getMessage1());
		dialog.setMyMessage_(getMessageConfirm().getMessage_());
		dialog.setOther(getMessageConfirm().getOther());
		dialog.setBtn_dongy(getMessageConfirm().getBtn_dongy());
		dialog.setBtn_khongdongy(getMessageConfirm().getBtn_khongdongy());
		dialog.setInitMpin(getMessageConfirm().getInitMpin());
		dialog.setClickDialogConfirm_Dongy(new DialogFrmConfirm.onClickDialogConfirm_Dongy() {
			
			@Override
			public void onClickDonY() {
				cmdNextConfirm();
			}
		});
		dialog.setClickDialogConfirm_Khongdongy(new DialogFrmConfirm.onClickDialogConfirm_Khongdongy() {
			
			@Override
			public void onClickKhongDongY() {
				cmdBackConfrim();
			}
		});
		dialog.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (getSlidingMenu().isMenuShowing())
				toggle();
			else
				goBack();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static void clear() {
		System.gc();
		Runtime.getRuntime().gc();
	}

//	protected int getIconNotice() {
//		return R.drawable.icon_notice;
//	}
//
//	protected int getIconSuccess() {
//		return R.drawable.icon_success;
//	}
//
//	protected int getIconPending() {
//		return R.drawable.icon_notice;
//	}
//
//	protected int getIconError() {
//		return R.drawable.icon_error;
//	}
//
//	protected int getIconConfirm() {
//		return R.drawable.icon_confirm;
//	}
//
//	protected int getIconPin() {
//		return R.drawable.icon_key;
//	}


	public void setMessageConfirm(String sTitle, String sMessage, String sMessage1,
			String sMessage_, String sOther, String btn_dongy, String btn_khongdongy,
			boolean init, boolean status) {
			messageConfirm.desTructor();
			messageConfirm.setTitle(sTitle);
			messageConfirm.setMessage(sMessage);
			messageConfirm.setMessage1(sMessage1);
			messageConfirm.setMessage_(sMessage_);
			messageConfirm.setOther(sOther);
			messageConfirm.setBtn_dongy(btn_dongy);
			messageConfirm.setBtn_khongdongy(btn_khongdongy);
			messageConfirm.setInitMpin(init);
			messageConfirm.setStatus(status);
	}
	
	public MessageConfirm getMessageConfirm() {
		return messageConfirm;
	}

	/**
	 * Chuyen sang activity tiep hoac thuc hien lenh
	 * */
	public void goNext() {

	}

	/**
	 * Quay lai Activity truoc
	 */
	public void goBack() {
		sThongBao = "";
		sLenh = "";
		isPendding = false;
		isFinished = false;
		settingActivity = null;
		finish();
	}

	public int getMyAccountIndex() {
		return iMyAccIndex;
	}

	public static Context getContext() {
		return mContext;
	}
	
	public static void setContext(Context context) {
		mContext = context;
	}
	
	public static MPConnection getConnection() {
		if (AMPlusCore.mpConnect == null) {
			if (User.sKetNoi.equals("WEB")) {
				AMPlusCore.mpConnect = new MPHttpConn(getContext());
			} else
				AMPlusCore.mpConnect = new MPSocketConn(getContext());
		}

		return AMPlusCore.mpConnect;
	}

	public static DBAdapter getDba() {
		return dbAdapter;
	}

	public void setMyAccountIndex(int index) {
		iMyAccIndex = index;
	}

	/**
	 * Xử lý MPIN người dùng vừa nhập
	 */
	public void cmdNextMPIN() {

	}

	/**
	 * Thoát khỏi Form MPIN
	 */
	public void cmdBackMPIN() {
	}

	/**
	 * Được sử dụng khi người dùng nhấn OK
	 */
	public void cmdNextThongBao() {
//		if (isFinished)
//			goBack();
	}

	/**
	 * Được sử dụng khi người dùng nhấn OK
	 */
	public void cmdNextXacNhan() {

	}
	
	public void cmdNextXacNhan(String data) {

	}

	public void cmdErrorConnect() {

	}
	/**
	 * Thoát khỏi Form Xác nhận và quay về Form trước
	 */
	public void cmdBackXacNhan() {

	}
	
	public void cmdNextXacNhanOTP(String otp) {

	}

	/**
	 * Thoát khỏi Form Xác nhận và quay về Form trước
	 */
	public void cmdBackXacNhanOTP() {

	}
	
	public void cmdNextConfirm() {

	}
	public void cmdBackConfrim() {

	}

	/**
	 * Được sử dụng khi người dùng nhấn OK
	 */
	public void cmdOKAccList() {

	}

	/**
	 * Thoát khỏi Form Danh sach Account Or Card
	 */
	public void cmdBackAccList() {

	}
	
	public void cmdChonAmountList(int value) {

	}
	
	public void cmdChoncauhoiList(int value, AnyObject data) {

	}

	public void cmdOKPending() {
		IProcess iprocess = AMPlusCore.threadThucThi.getIProcess();
		byte iTag = AMPlusCore.threadThucThi.getTag();
		AMPlusCore.threadThucThi = new ThreadThucThi(AMPlusCore.this);
		AMPlusCore.threadThucThi.setIProcess(iprocess, iTag);
		AMPlusCore.threadThucThi.Start();
	}

	public void cmdBackPending() {
		goBack();	
		isPendding = false;
	}

	@SuppressLint("NewApi")
	public void pickContact() {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, GET_CONTACTS);
	}


	public void setTelFromContact(String sTel) {

	}

	public boolean kiemTraMPIN(String vmpin) {
		try {
			if (MPlusLib.loadKey(vmpin) != null) {
				User.iSoLanSaiMPIN = 0;
				saveUserTable();
				return true;
			} else {
				User.iSoLanSaiMPIN++;

				if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
					User.sprivateEncKey = "";
				}
				showDiaLogWrongMPin(true);
				saveUserTable();
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static void saveLogTransaction(String time, String seqNo, String tranCode,
			String dstNo, String amount, String result, String desc,
			String addParam, String amount_cost) {

		try {
			LogTransactionItem log = new LogTransactionItem();
			log.setLogTime(time);
			log.setSeqNo(seqNo);
			log.setTranCommand(tranCode);
			log.setDestinationNo(dstNo);
			log.setAmount(amount);
			log.setResult(result);
			if (result.length() > 40)
				result = result.substring(0, 40);
			log.setDescription(desc);
			log.setAddParam(addParam);
			log.setmAmount_cost(amount_cost);

			getDba().insertOrUpdateLogTransaction(log);
		} catch (Exception ex) {
		}
	}

	public void saveCaches(String sMID, String sACC, String sCard,
			String sTelTopup, String sNhaCungCap, String sHoaDon,
			String sTelTopupTraSau) {
		try {
			getDba().saveCaches(sMID, sACC, sCard, sTelTopup, sNhaCungCap,
					sHoaDon, sTelTopupTraSau, "");
		} catch (Exception ex) {
		}
	}

	public String[] getCaches(int column) {
		String arrStr[] = new String[] { "" };
		try {
			arrStr = getDba().getCaches(column).split("\\|");
		} catch (Exception ex) {
		}
		return arrStr;
	}

	public static void saveLogPending(String sSeqNo, String sLenh) {
		try {
			getDba().insertLogPending(Util.getTimeClient2(), sSeqNo, sLenh);
		} catch (Exception ex) {
		}
	}

	public static void deleteLogPending(String sSeqNo) {

		try {
			getDba().deleteLogPending(sSeqNo);
		} catch (Exception ex) {
		}
	}

	public void showDiaLogWrongMPin(boolean isStatus) {
		
		final DialogConfirmMessage dialogConfirmMessage = new DialogConfirmMessage(AMPlusCore.this);
		dialogConfirmMessage.setMyMessage(isStatus == false ? sThongBao : getString(R.string.alert_message_nhap_sai_pin)

				+ User.iSoLanSaiMPIN
				+ "/"
				+ Config.iMPIN_MAX_WRONG
				+ ")"
				+ (User.iSoLanSaiMPIN < Config.iMPIN_MAX_WRONG ? getString(R.string.alert_message_nhap_sai_pin2)
						: getString(R.string.alert_message_nhap_sai_pin_yeu_cau)));
		dialogConfirmMessage.setDongy(getString(R.string.cmd_ok));
		dialogConfirmMessage.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
			
			@Override
			public void onclickKhongDongY() {
			}
			@Override
			public void onclickDongY() {
					if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
						setResult(EXITCODE, null);
						User.iSoLanSaiMPIN = 0;
						User.isRegisted = false;
//						User.MID = "";
						User.BANK_MID = "";
						dialogConfirmMessage.dismiss();
						startActivity(new Intent(mContext, FrmRegistration.class));
						finish();
					} else if (!isDoiMPINForm)
						onCreateMyDialog(MPIN).show();
			}
		});
		
		dialogConfirmMessage.show();
		
	}

	private void downloadApp() {
		AutoUpdate update = new AutoUpdate(this, User.sVersionAndLink);
		update.check();
	}

	public void showDiaLogUpdateVersion() {
		
		
		final DialogConfirmMessage dialogConfirmMessage = new DialogConfirmMessage(AMPlusCore.this);
		dialogConfirmMessage.setMyMessage(User.iUPDATE_FLAG == 1 ? getString(R.string.updatebb)
				: getString(R.string.updatenew));
		dialogConfirmMessage.setDongy(getString(R.string.cmd_ok));
		if (User.iUPDATE_FLAG == 0){
			dialogConfirmMessage.setKhongdongy(getString(R.string.cmd_back));
		}
		dialogConfirmMessage.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
			
			@Override
			public void onclickKhongDongY() {
				dialogConfirmMessage.dismiss();
				User.iUPDATE_FLAG = -1;
			}
			
			@Override
			public void onclickDongY() {
				if (User.iUPDATE_FLAG != 1)
					User.iUPDATE_FLAG = -1;
				downloadApp();
			}
		});
		
		dialogConfirmMessage.show();
		
	}

	public static void saveUserTable() {
		try {
			getDba().saveUserTable();
		} catch (Exception ex) {
		}
	}

	public static void sendData(String sMessage, boolean isTransaction) {
		String sKQ = "";
		if (!User.isRegisted && !User.getTransCode().equals(Config.DANG_KY_APP) && !User.getTransCode().equals(Config.ACTIVE_APP) 
					&& !User.getTransCode().equals(Config.H_PING) && !User.getTransCode().equals(Config.ACTIVE_APP_FORGOT) && !User.getTransCode().equals(Config.ACTIVE_APP_FORGOT_RE)) {
			getConnection().setTagErr(MPConnection.ERROR_NOT_ACTIVATION);
		} else if (!sMessage.equals("")) {

			MPlusLib.debug("SendData: ", sMessage);

			getConnection().sendAndReceive(sMessage, isTransaction);
			sKQ = getConnection().getDataReceive();
			String[] ast = sKQ.split("<>", -1);
			sKQ = ast[0];

			if (User.isFisrt) {
				// MAIN<>MENU<>CARD<>TOPUP<>BILL<>NEWS<>UPLK

				if (ast.length > 1) {
					// save menu main
					if (saveMenuMain(ast[1]))
						// ;
						MPlusLib.debug("Save menu main: ", ast[1]);
				}

				if (ast.length > 2) {
					// luu menu ban hang iMart
					if (saveMenuProduct(ast[2]))
						// ;
						MPlusLib.debug("Save menu iMart: ", ast[2]);
				}

				if (ast.length > 3) {
					// luu menu topup
					if (saveMenuTopup(ast[3]))
						// ;
						MPlusLib.debug("Save menu Topup: ", ast[3]);
				}

				if (ast.length > 4) {
					// luu menu bill payment
					if (saveMenuBillPayment(ast[4]))
						// ;
						MPlusLib.debug("Save menu Bill: ", ast[4]);
				}

				if (ast.length > 5) {
					// save menu news
					if (saveMenuNews(ast[5]))
						// ;
						MPlusLib.debug("Save menu news: ", ast[5]);
				}
				
				if (ast.length > 6 && !sMessage.startsWith(Config.DANG_KY)) {
					// handle update info
					handleUpdateInfo(ast[6]);
				}
				
				if(ast.length > 7){
					// save menu Question
					if(saveQuestion_Kenh(ast[7], DBAdapter.DB_GROUP_TYPE_QUESTION))
						MPlusLib.debug("Save menu Question: ", ast[7]);
				}
				
				if(ast.length > 8){
					// save menu Kenh
					if(saveQuestion_Kenh(ast[8], DBAdapter.DB_GROUP_TYPE_KENH))
						MPlusLib.debug("Save menu Kenh: ", ast[8]);
				}
				
				User.isFisrt = false;
			}

			MPlusLib.debug("Rerturn: ", getConnection().getDataReceive());
		} else
			getConnection().setTagErr(MPConnection.ERROR_NOT_ENCODE_DATA);
	}

	public static void handleUpdateInfo(String updateInfo) {
		// updateInfo =
		// MPlusLib.getResult("4.0|1|http://test.m-pay.vn/a/mplus.apk");
		if (updateInfo != null && updateInfo.length() > 5) {
			try {
				// UPLK=NEVR|STS|LINK (ex:
				// 4.0|1|http://m-plus.vn/sacom/v40/demo/mplus.apk)
				String arrTam[] = updateInfo.split("\\|");

				if (MPlusLib.isNewVersion(arrTam[0])) {
					getDba().saveMenuId(updateInfo, "", "", "", "");

					User.iUPDATE_FLAG = (byte) ("1".equals(arrTam[1]) ? 1 : 0);
					User.sVersionAndLink = arrTam[2].trim();
					if (User.iUPDATE_FLAG == 1)
						getConnection().setTagErr(MPConnection.UPDATE_REQUIRED);
				}
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "handleUpdateInfo", ex);
			}
		}
	}

	/**
	 * Luu menu main vao db
	 * */
	public static boolean saveMenuMain(String menuInfo) {
		boolean isSaved = false;

		try {
			menuInfo = menuInfo.trim();
			while (menuInfo.endsWith("#"))
				menuInfo = menuInfo.substring(0, menuInfo.length() - 1);

			if (menuInfo != null && menuInfo.length() > 2) {
				String[] group = menuInfo.split("#");

				if (group != null && group.length > 0) {
					if (!User.sID_MENU_MAIN.equals(group[0])) {
						// Chi cap nhat khi menu chua ton tai

						// luu menu id
						User.sID_MENU_MAIN = group[0];
					}
				}
			}
			isSaved = true;
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveMenuMain", ex);			
		}
		return isSaved;
	}
	
	/**
	 * Luu menu product vao db
	 * */
	public static boolean saveMenuProduct(String menuInfo) {
		boolean isSaved = false;

		try {
			// GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK#GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK
			// card_id=09
			// card_vn=CARDMOBILE|Thẻ nạp điện thoại|Các loại thẻ Viettel,
			// vinaphone, mobifone, vietnamobile...|Link!MOBI|Thẻ
			// Mobifone|Khuyến mãi 50% giá trị thẻ nạp từ
			// 26/12/2012|http://test.m-pay.vn/mobi09.png!VINA|Thẻ
			// Vinaphone|Khuyến mãi 50% giá trị thẻ nạp từ
			// 31/12/2012|http://test.m-pay.vn/vina.png#
			// card_en=CARDMOBILE|Mobile Cards|Viettel, vinaphone, mobifone,
			// vietnamobile...|Link!MOBI|Mobifone card|From 12/26/2012,
			// promo....|http://test.m-pay.vn/mobi09.png!VINA|Vinaphone
			// card|From 12/31/2012, promo....|http://test.m-pay.vn/vina.png

			menuInfo = menuInfo.trim();
			while (menuInfo.endsWith("#"))
				menuInfo = menuInfo.substring(0, menuInfo.length() - 1);

			if (menuInfo != null && menuInfo.length() > 2) {
				String[] group = menuInfo.split("#");

				if (group != null && group.length > 0) {
					if (!User.sID_MENU_CARD.equals(group[0])) {
						// Chi cap nhat khi menu chua ton tai

						// luu menu id
						User.sID_MENU_CARD = group[0];

						// bo kich hoat cac old item
						getDba().activeGroups(DBAdapter.DB_GROUP_TYPE_PRODUCT,
								DBAdapter.DB_IS_NOT_ACTIVED);
						getDba().activeItems(DBAdapter.DB_GROUP_TYPE_PRODUCT,
								DBAdapter.DB_IS_NOT_ACTIVED);

						// luu menu detail
						for (int gIndex = 1; gIndex < group.length; gIndex++) {
							group[gIndex] = group[gIndex].trim();
							String[] groupInfo = group[gIndex].split("!");
							String dbGroupId = "";

							if (groupInfo != null && groupInfo.length > 1) {
								for (int iIndex = 0; iIndex < groupInfo.length; iIndex++) {
									String[] arrItemInfo = groupInfo[iIndex].trim().split("\\|");

									if (arrItemInfo != null
											&& arrItemInfo.length > 1) {
										if (iIndex == 0) {
											try {
												Group dbGroup = new Group();
												dbGroupId = arrItemInfo[0];
												dbGroup.setGroupId(arrItemInfo[0]);

												if (arrItemInfo.length > 1)
													dbGroup.setTitle(arrItemInfo[1]);
												else
													dbGroup.setDescription(arrItemInfo[0]);

												if (arrItemInfo.length > 2)
													dbGroup.setDescription(arrItemInfo[2]);
												else
													dbGroup.setDescription(arrItemInfo[0]);

												if (arrItemInfo.length > 3)
													dbGroup.setImage(Util
															.getSuitImageUrl(arrItemInfo[3]));
												else
													dbGroup.setImage(Util
															.getSuitImageUrl(arrItemInfo[0]));

												dbGroup.setGroupType(DBAdapter.DB_GROUP_TYPE_PRODUCT);
												dbGroup.setIndex(gIndex + 1);
												dbGroup.setIsActived(DBAdapter.DB_IS_ACTIVED);

												getDba().insertOrUpdateGroup(
														dbGroup);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuProduct", ex);
											}
										} else {
											// Cac item trong nhom
											try {
												Item item = new Item();
												item.setItemId(arrItemInfo[0]);
												item.setTitle(arrItemInfo[1]);
												item.setIndex(iIndex);
												item.setGroupId(dbGroupId);
												item.setGroupType(DBAdapter.DB_GROUP_TYPE_PRODUCT);
												item.setIsActived(DBAdapter.DB_IS_ACTIVED);

												if (arrItemInfo.length > 2)
													item.setDescription(arrItemInfo[2]);
												else
													item.setDescription(arrItemInfo[1]);

												if (arrItemInfo.length > 3)
													item.setImage(Util
															.getSuitImageUrl(arrItemInfo[3]));
												else
													item.setImage(Config.ICON_DEFAULT_PRODDUCT);

												getDba().insertOrUpdateItem(
														item);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuProduct", ex);
											}
										}
									}
								}
							}
						}

						// Xoa cac menu khong duoc kich hoat
						getDba().deleteNotActivedItems(
								DBAdapter.DB_GROUP_TYPE_PRODUCT);
						getDba().deleteNotActivedGroups(
								DBAdapter.DB_GROUP_TYPE_PRODUCT);

						isSaved = true;
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveMenuProduct", ex);
		}

		return isSaved;
	}

	/**
	 * Luu menu Topup vao db
	 * */
	public static boolean saveMenuTopup(String menuInfo) {
		boolean isSaved = false;
		try {
			// #format topup=topup_id#topup_ct (topup_vn hoặc topup_en)
			// topup_ct=GRCD|GRNM|GRDE|GRLK!TPCD|TPNM|TPDE|TPLK!TPCD|TPNM|TPDE|TPLK#GRCD|GRNM|GRDE|GRLK!TPCD|TPNM|TPDE|TPLK!TPCD|TPNM|TPDE|TPLK

			menuInfo = menuInfo.trim();
			while (menuInfo.endsWith("#"))
				menuInfo = menuInfo.substring(0, menuInfo.length() - 1);

			if (menuInfo != null && menuInfo.length() > 2) {
				String[] group;

				group = menuInfo.split("#");

				if (group != null && group.length > 0) {
					if (!User.sID_MENU_TOPUP.equals(group[0])) {
						// Chi cap nhat khi menu chua ton tai

						// luu menu id
						User.sID_MENU_TOPUP = group[0];

						// bo kich hoat cac old item
						getDba().activeGroups(DBAdapter.DB_GROUP_TYPE_TOPUP,
								DBAdapter.DB_IS_NOT_ACTIVED);
						getDba().activeItems(DBAdapter.DB_GROUP_TYPE_TOPUP,
								DBAdapter.DB_IS_NOT_ACTIVED);

						// luu menu detail
						for (int gIndex = 1; gIndex < group.length; gIndex++) {
							group[gIndex] = group[gIndex].trim();
							String[] groupInfo = group[gIndex].split("!");
							String dbGroupId = "";

							if (groupInfo != null && groupInfo.length > 1) {
								for (int iIndex = 0; iIndex < groupInfo.length; iIndex++) {
									String itemInfo = groupInfo[iIndex].trim();
									String[] arrItemInfo = itemInfo
											.split("\\|");

									if (arrItemInfo != null
											&& arrItemInfo.length > 1) {
										if (iIndex == 0) {
											// Ten nhom item
											try {
												Group dbGroup = new Group();
												dbGroupId = arrItemInfo[0];
												dbGroup.setGroupId(arrItemInfo[0]);
												dbGroup.setTitle(arrItemInfo[1]);
												dbGroup.setDescription(arrItemInfo[2]);
												dbGroup.setImage(Util
														.getSuitImageUrl(arrItemInfo[3]));
												dbGroup.setGroupType(DBAdapter.DB_GROUP_TYPE_TOPUP);
												dbGroup.setIndex(gIndex + 1);
												dbGroup.setLink("");
												dbGroup.setAddParam("");
												dbGroup.setIsActived(DBAdapter.DB_IS_ACTIVED);

												getDba().insertOrUpdateGroup(
														dbGroup);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuTopup", ex);
											}
										} else {
											// Cac item trong nhom
											try {
												Item item = new Item();
												item.setItemId(arrItemInfo[0]);
												item.setTitle(arrItemInfo[1]);
												item.setIndex(iIndex);
												item.setGroupId(dbGroupId);
												item.setGroupType(DBAdapter.DB_GROUP_TYPE_TOPUP);
												item.setIsActived(DBAdapter.DB_IS_ACTIVED);

												if (arrItemInfo.length > 2)
													item.setDescription(arrItemInfo[2]);
												else
													item.setDescription(arrItemInfo[1]);

												if (arrItemInfo.length > 3)
													item.setImage(Util
															.getSuitImageUrl(arrItemInfo[3]));
												else
													item.setImage(Config.ICON_DEFAULT_PRODDUCT);

												getDba().insertOrUpdateItem(
														item);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuTopup", ex);
											}
										}
									}
								}
							}
						}

						isSaved = true;
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveMenuTopup", ex);
		}

		return isSaved;
	}

	/**
	 * Luu menu bill vao db
	 * */
	public static boolean saveMenuBillPayment(String menuInfo) {
		boolean isSaved = false;

		try {
			// GRCD|GRNM|GRDE|GRLK!BLCD|BLNM|BLDE|BLLK!BLCD|BLNM|BLDE|BLLK#GRCD|GRNM|GRDE|GRLK!BLCD|BLNM|BLDE|BLLK!BLCD|BLNM|BLDE|BLLK
			// bill_id=01
			// bill_vn=ORTHER|Danh sách nhà cung cấp|EVN HCMC,
			// AIRMEKONG,...|link!600001|Điện lực Hồ Chí Minh|Nhập mã khách hàng
			// là số PE trên hóa đơn điện hàng tháng|link
			// bill_en=ORTHER|Biller List|EVN HCMC,
			// AIRMEKONG,...|link!600001|EVN Ho Chi Minh|Input biller code -
			// code PE in your bill|link

			menuInfo = menuInfo.trim();
			while (menuInfo.endsWith("#"))
				menuInfo = menuInfo.substring(0, menuInfo.length() - 1);

			if (menuInfo != null && menuInfo.length() > 2) {
				int iValue = 0;
				int iLen = 0;
				String[] group;

				// GRCD|GRNM|GRDE|GRLK ! BLCD|BLNM|BLDE|BLLK !
				// BLCD|BLNM|BLDE|BLLK#
				// GRCD|GRNM|GRDE|GRLK!BLCD|BLNM|BLDE|BLLK!BLCD|BLNM|BLDE|BLLK
				group = menuInfo.split("#");

				if (group != null && group.length > 0) {
					if (!User.sID_MENU_BILL.equals(group[0])) {
						// Chi cap nhat khi menu chua ton tai
						// luu menu id
						User.sID_MENU_BILL = group[0];

						// bo kich hoat cac old item
						getDba().activeGroups(
								DBAdapter.DB_GROUP_TYPE_BILLPAYMENT,
								DBAdapter.DB_IS_NOT_ACTIVED);
						getDba().activeItems(
								DBAdapter.DB_GROUP_TYPE_BILLPAYMENT,
								DBAdapter.DB_IS_NOT_ACTIVED);

						// luu menu detail
						for (int gIndex = 1; gIndex < group.length; gIndex++) {
							group[gIndex] = group[gIndex].trim();
							String[] groupInfo = group[gIndex].split("!");
							String dbGroupId = "";

							if (groupInfo != null && groupInfo.length > 1) {
								for (int iIndex = 0; iIndex < groupInfo.length; iIndex++) {
									String itemInfo = groupInfo[iIndex].trim();
									String[] arrItemInfo = itemInfo
											.split("\\|");

									if (arrItemInfo != null
											&& arrItemInfo.length > 1) {
										if (iIndex == 0) {
											// Ten nhom item
											try {
												Group dbGroup = new Group();
												dbGroupId = arrItemInfo[0];
												dbGroup.setGroupId(arrItemInfo[0]);
												dbGroup.setTitle(arrItemInfo[1]);
												dbGroup.setDescription(arrItemInfo[2]);
												dbGroup.setImage(Util
														.getSuitImageUrl(arrItemInfo[3]));
												dbGroup.setGroupType(DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
												dbGroup.setIndex(gIndex + 1);
												dbGroup.setLink("");
												dbGroup.setAddParam("");
												dbGroup.setIsActived(DBAdapter.DB_IS_ACTIVED);

												getDba().insertOrUpdateGroup(
														dbGroup);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuBillPayment", ex);
											}
										} else {
											// Cac item trong nhom
											try {
												// 60000121: BLCD=EVN HCM,
												// LBTP=2, SVTP=1
												Item item = new Item();

												iLen = arrItemInfo[0].length();
												if (iLen > 2) {
													item.setItemId(arrItemInfo[0]
															.substring(
																	0,
																	arrItemInfo[0]
																			.length() - 2));

													iValue = DBAdapter.DB_SUPPLIER_TYPE_CUSTOM_CODE;
													try {
														iValue = Integer
																.parseInt(String
																		.valueOf(arrItemInfo[0]
																				.charAt(iLen - 2)));
													} catch (Exception e) {
													}
													item.setSupplyType(iValue);

													iValue = 0;
													try {
														iValue = Integer
																.parseInt(String
																		.valueOf(arrItemInfo[0]
																				.charAt(iLen - 1)));
													} catch (Exception e) {
													}
													item.setSaveType(iValue);
												} else {
													item.setItemId(arrItemInfo[0]);
													item.setSupplyType(DBAdapter.DB_SUPPLIER_TYPE_CUSTOM_CODE);
													item.setSaveType(0);
												}

												item.setTitle(arrItemInfo[1]);
												item.setIndex(iIndex);
												item.setGroupId(dbGroupId);
												item.setIsActived(DBAdapter.DB_IS_ACTIVED);
												item.setGroupType(DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);

												if (arrItemInfo.length > 2)
													item.setDescription(arrItemInfo[2]);
												else
													item.setDescription(arrItemInfo[1]);

												if (arrItemInfo.length > 3)
													item.setImage(Util
															.getSuitImageUrl(arrItemInfo[3]));
												else
													item.setImage(Config.ICON_DEFAULT_PRODDUCT);

												getDba().insertOrUpdateItem(
														item);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuBillPayment", ex);
											}
										}
									}
								}
							}
						}

						isSaved = true;
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveMenuBillPayment", ex);
		}

		return isSaved;
	}

	/**
	 * 
	 * Luu news vao db
	 * 
	 * */
	
	public static boolean saveMenuNews(String menuInfo) {
		boolean isSaved = false;
		try {
			// #format news: //NEID#NECD|NENM|NEDE|NELK!NECD|NENM|NEDE|NELK
			// news_id=01
			// news_vn=001|CTKM "Chung một niềm vui" của Sacombank|Từ ngày...
			// Sacombank tặng 1000 thẻ nạp điện thoại 100K cho 1000 khách hàng
			// đầu tiên đăng ký và sử dụng dịch vụ mobileBanking mPlus|link!
			// news_en=001|Promo "ABC DEF" of Sacombank|From...
			// Sacombank...|link!

			menuInfo = menuInfo.trim();
			while (menuInfo.endsWith("#"))
				menuInfo = menuInfo.substring(0, menuInfo.length() - 1);

			if (menuInfo != null && menuInfo.length() > 2) {
				String[] group;

				group = menuInfo.split("#");

				if (group != null && group.length > 0) {
					if (!User.sID_NEWS.equals(group[0])) {
						// chi cap nhat khi menu chua ton tai
						// luu menu id
						User.sID_NEWS = group[0];

						// bo kich hoat cac old item
						getDba().activeGroups(DBAdapter.DB_GROUP_TYPE_NEWS,
								DBAdapter.DB_IS_NOT_ACTIVED);
						getDba().activeNews(DBAdapter.DB_GROUP_TYPE_NEWS,
								DBAdapter.DB_IS_NOT_ACTIVED);

						// luu menu detail
						for (int gIndex = 1; gIndex < group.length; gIndex++) {
							group[gIndex] = group[gIndex].trim();
							String[] groupInfo = group[gIndex].split("!");
							String dbGroupId = "";

							if (groupInfo != null && groupInfo.length > 1) {
								for (int iIndex = 0; iIndex < groupInfo.length; iIndex++) {
									String itemInfo = groupInfo[iIndex].trim();
									String[] arrItemInfo = itemInfo
											.split("\\|");

									if (arrItemInfo != null
											&& arrItemInfo.length > 1) {
										if (iIndex == 0) {
											// Ten nhom item
											try {
												Group dbGroup = new Group();
												dbGroupId = arrItemInfo[0];
												dbGroup.setGroupId(arrItemInfo[0]);
												dbGroup.setTitle(arrItemInfo[1]);
												dbGroup.setDescription(arrItemInfo[2]);
												dbGroup.setImage(Util
														.getSuitImageUrl(arrItemInfo[3]));
												dbGroup.setGroupType(DBAdapter.DB_GROUP_TYPE_NEWS);
												dbGroup.setIndex(gIndex + 1);
												dbGroup.setLink("");
												dbGroup.setAddParam("");
												dbGroup.setIsActived(DBAdapter.DB_IS_ACTIVED);

												getDba().insertOrUpdateGroup(
														dbGroup);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuNews", ex);
											}
										} else {
											// Cac item trong nhom
											try {
												News item = new News();
												item.setNewsId(arrItemInfo[0]);
												item.setTitle(arrItemInfo[1]);
												item.setIndex(iIndex);
												item.setGroupId(dbGroupId);
												item.setGroupType(DBAdapter.DB_GROUP_TYPE_NEWS);
												item.setIsActived(DBAdapter.DB_IS_ACTIVED);

												if (arrItemInfo.length > 2)
													item.setDescription(arrItemInfo[2]);
												else
													item.setDescription(arrItemInfo[1]);

												if (arrItemInfo.length > 3)
													item.setImage(Util
															.getSuitImageUrl(arrItemInfo[3]));

												getDba().insertOrUpdateNews(
														item);
											} catch (Exception ex) {
												MPlusLib.debug(TAG, "saveMenuNews", ex);
											}
										}
									}
								}
							}
						}

						// Xoa cac menu khong duoc kich hoat
						getDba().deleteNotActivedNews(
								DBAdapter.DB_GROUP_TYPE_NEWS);
						getDba().deleteNotActivedGroups(
								DBAdapter.DB_GROUP_TYPE_NEWS);

						isSaved = true;
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveMenuNews",  ex);
		}

		return isSaved;
	}
	
	/**
	 * 
	 * @param save Question and Kenh
	 * 
	 */
	
	private static boolean saveQuestion_Kenh(String menuInfo, int stype) {
		boolean isSaved = false;
		try {
			
			// type Question = 22, Kenh = 23;
			
//			#format qest: qest_id#qest_ct
//			qest_ct = QSCD|QSNM|QSDE|QSLK!QSCD|QSNM|QSDE|QSLK
//			qest_id = 01
//			qest_vn = 01|Cau hoi 1||!02|Cau hoi 2||
//			qest_en = 01|Question 1||!02|Question 2||
			
//			#format chan: chan_id#chan_ct
//			chan_ct = CHCD|CHNM|CHDE|CHLK!CHCD|CHNM|CHDE|CHLK
//			chan_id = 01
//			chan_vn = 01|Phong giao dich||!02|Intenet Baning||!03|SMS Baning||!04|Mobile Baning||!05|M-plus Mobile Baning||
//			chan_en = 01|Office||!02|Intenet Baning||!03|SMS Baning||!04|Mobile Baning||!05|M-plus Mobile Baning||

			menuInfo = menuInfo.trim();
			while (menuInfo.endsWith("#"))
				menuInfo = menuInfo.substring(0, menuInfo.length() - 1);

			if (menuInfo != null && menuInfo.length() > 2) {
				String[] group;

				group = menuInfo.split("#");
				
				String sIDMenu = "";
				if(stype == DBAdapter.DB_GROUP_TYPE_QUESTION){
					sIDMenu = User.sID_MENU_QUESTION;
				}else if (stype == DBAdapter.DB_GROUP_TYPE_KENH) {
					sIDMenu = User.sID_MENU_KENH;
				}

				if (group != null && group.length > 0) {
					if (!sIDMenu.equals(group[0])) {
						// chi cap nhat khi menu chua ton tai
						// luu menu id
						
						if(stype == DBAdapter.DB_GROUP_TYPE_QUESTION){
							User.sID_MENU_QUESTION = group[0];
						}else if (stype == DBAdapter.DB_GROUP_TYPE_KENH) {
							User.sID_MENU_KENH = group[0];
						}
						
						// Active Question, Kenh
						getDba().activeGroups(stype, DBAdapter.DB_IS_NOT_ACTIVED);
						getDba().activeQuestion_Kenh(stype, DBAdapter.DB_IS_NOT_ACTIVED);

						// luu menu detail
						for (int gIndex = 1; gIndex < group.length; gIndex++) {
							group[gIndex] = group[gIndex].trim();
							String[] groupInfo = group[gIndex].split("!");

							if (groupInfo != null && groupInfo.length > 1) {
								for (int iIndex = 0; iIndex < groupInfo.length; iIndex++) {
									String itemInfo = groupInfo[iIndex].trim();
									String[] arrItemInfo = itemInfo
											.split("\\|");
									
									if(arrItemInfo != null && arrItemInfo.length > 1){
										if(iIndex == 0){
											Group dbGroup = new Group();
											dbGroup.setGroupId(arrItemInfo[0]);
											dbGroup.setTitle(arrItemInfo[1]);
											dbGroup.setDescription(arrItemInfo[2]);
											dbGroup.setImage(Util
													.getSuitImageUrl(arrItemInfo[3]));
											dbGroup.setGroupType(stype);
											dbGroup.setIndex(gIndex + 1);
											dbGroup.setLink("");
											dbGroup.setAddParam("");
											dbGroup.setIsActived(DBAdapter.DB_IS_ACTIVED);

											getDba().insertOrUpdateGroup(
													dbGroup);
										}else {
											AnyObject kQues = new AnyObject();
											kQues.setsID(arrItemInfo[0]);
											kQues.setsContent(arrItemInfo[1]);
											kQues.setsDescription(arrItemInfo[2]);
											kQues.setsLink(arrItemInfo[3]);
											kQues.setsType(String.valueOf(stype));
											kQues.setsIsActive(String.valueOf(DBAdapter.DB_IS_ACTIVED));
											getDba().insertOrUpdateKQuestion(kQues);
										}
									}
								}
							}
						}

						// Xoa cac menu khong duoc kich hoat
						getDba().deleteNotActivedKQuestion(stype);
						getDba().deleteNotActivedGroups(stype);

						isSaved = true;
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveMenuNews",  ex);
		}
		
		
		return isSaved;
	}
	
	public void napThe(Product product) {
	String sPinCode = product.getPin();
	if (!sPinCode.equals(""))
		try {
			Intent callIntent = new Intent(Intent.ACTION_VIEW);
			callIntent.setData(Uri.parse("tel:"
					+ Uri.encode("*100*" + sPinCode + "#")));
			startActivity(callIntent);
			isFinished = false;
		} catch (Exception e) {
		}
}
	
	public void sendSMS(Product produc) {
		try {
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", getProductInfoSms(produc));
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
			isFinished = false;
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "sendSMS", ex);
		}
	}

	private String getProductInfoSms(Product product) {
		String msg = "";

		StringBuffer sBuf = new StringBuffer();
		sBuf.append(product.getProductCode());
		sBuf.append(" ");
		sBuf.append(Util.formatNumbersAsTien(product.getPrice()));
		sBuf.append(" VND:");
		if (!product.getAccNo().equals("")) {
			sBuf.append(getString(R.string.acc_none));
			sBuf.append(" ");
			sBuf.append(product.getAccNo());
			sBuf.append(", ");
		}

		if (!product.getPin().equals("")) {
			sBuf.append(getString(R.string.card_pin_none));
			sBuf.append(" ");
			sBuf.append(product.getPin());
			sBuf.append(", ");
		}

		if (!product.getExDate().equals("")) {
			sBuf.append(getString(R.string.card_exdate_none));
			sBuf.append(" ");
			sBuf.append(Util.getTimeClient4(product.getExDate()));
			sBuf.append(", ");
		}

		if (!product.getSeri().equals("")) {
			sBuf.append(getString(R.string.card_seri));
			sBuf.append(" ");
			sBuf.append(product.getSeri());
			sBuf.append(", ");
		}

		if (!product.getHotLine().equals("")) {
			sBuf.append(getString(R.string.card_hotline_none));
			sBuf.append(" ");
			sBuf.append(product.getHotLine());
			sBuf.append(", ");
		}

		msg = sBuf.toString().trim();
		if (msg.charAt(msg.length() - 1) == ',')
			msg = msg.substring(0, msg.length() - 1);

		return msg;
	}
	
	public static String getAmountCost(String amount, String promo, String quantity){
		int quan = Integer.parseInt(quantity.equals("") ? "1" : quantity);
		return String.valueOf((Integer.parseInt(amount.equals("") ? "0" : amount) * quan) 
				- (Integer.parseInt(promo.equals("") ? "0" : promo) * quan));
	}
	
//	public static boolean isNetworkAvailable() {
//		try {
//			ConnectivityManager cm = (ConnectivityManager)	getContext()
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
//			if (activeNetworkInfo != null
//					&& activeNetworkInfo.isAvailable()
//					&& activeNetworkInfo.isConnected()
//					&& activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED)
//				return true;
//			return false;
//		} catch (Exception ex) {
//			return false;
//		}
//	}
	
	/**
	 * type = 1: get tranName; type = 2 : get dstName
	 * */
	public static String getTransactionName(String command, int type) {
		String tranName = command;

		if (command.equals("0" + Config.M_IMART_THANH_TOAN)) {
			// mua hang imart
			tranName = mContext.getString(R.string.imart_name);
		} else if (command.equals("1" + Config.M_IMART_THANH_TOAN)) {
			// topup mobile tra truoc
			tranName = mContext.getString(R.string.topup_tra_truoc);
		} else if (command.equals("2" + Config.M_IMART_THANH_TOAN)) {
			// topup mobile tra sau
			tranName = mContext.getString(R.string.topup_tra_sau);
		} else if (command.equals("3" + Config.M_IMART_THANH_TOAN)) {
			// topup game
			tranName = mContext.getString(R.string.topup_game);
		} else if (command.equals(Config.M_IMART_THANH_TOAN)) {
			// topup game
			tranName = mContext.getString(R.string.imart_name) + "/"
					+ mContext.getString(R.string.topup_name);
		} else if (command.equals(Config.M_HOA_DON_THANH_TOAN)) {
			tranName = mContext.getString(R.string.bill_payment_name);
		} else if (command.equals(Config.A_CK_AGENT)) {
			tranName = mContext.getString(R.string.transfer_name);
		} else if (command.equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
			tranName = mContext.getString(R.string.nap_tien_agent);
		} else if (command.equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
			tranName = mContext.getString(R.string.rut_tien_agent);
		}

		return tranName;
	}
	
	/**
	 * type = 1: get tranName; type = 2 : get dstName
	 * */
	public static String getTransactionDetail(LogTransactionItem log) {
		String command = log.getTranCommand();

		StringBuffer sBuf = new StringBuffer();

		if (command.equals("0" + Config.M_IMART_THANH_TOAN)) {
			// mua hang imart
			sBuf.append(mContext.getString(R.string.san_pham));
			sBuf.append(": |<b>");
			sBuf.append(log.getDestinationNo());
			sBuf.append("</b>|<br/>");
		} else if (command.equals("1" + Config.M_IMART_THANH_TOAN)) {
			// topup mobile tra truoc
				sBuf.append(mContext.getString(R.string.msg_from_tkvi));
				sBuf.append(": |<b>");
				sBuf.append(Util.formatNumbersAsMid(User.MID));
				sBuf.append("</b>|<br/>");
			sBuf.append(mContext.getString(R.string.msg_to_sdt));
			sBuf.append(": |<b>");
			sBuf.append(Util.formatNumbersAsMid(log.getAddParam()));
			sBuf.append("</b>|<br/>");
		} else if (command.equals("2" + Config.M_IMART_THANH_TOAN)) {
			// topup mobile tra sau
				sBuf.append(mContext.getString(R.string.msg_from_tkvi));
				sBuf.append(": |<b>");
				sBuf.append(Util.formatNumbersAsMid(User.MID));
				sBuf.append("</b>|<br/>");
			sBuf.append(mContext.getString(R.string.msg_to_sdt));
			sBuf.append(": |<b>");
			sBuf.append(Util.formatNumbersAsMid(log.getAddParam()));
			sBuf.append("</b>|<br/>");
		} else if (command.equals("3" + Config.M_IMART_THANH_TOAN)) {
			// topup game
				sBuf.append(mContext.getString(R.string.msg_from_tkvi));
				sBuf.append(": |<b>");
				sBuf.append(Util.formatNumbersAsMid(User.MID));
				sBuf.append("</b>|<br/>");
			sBuf.append(mContext.getString(R.string.msg_to_tk));
			sBuf.append(": |<b>");
			sBuf.append(Util.formatNumbersAsMid(log.getAddParam()));
			sBuf.append("</b>|<br/>");
		} else if (command.equals(Config.M_HOA_DON_THANH_TOAN)) {
			// Bill payment
			String[] pcd = log.getDestinationNo().split("\\.");
			String supplierName = "";
			String billCode = log.getDestinationNo();
			if (pcd.length > 1) {
				billCode = pcd[1];
				Item supplier = getDba().getItem(pcd[0],
						DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
				if (supplier != null) {
					supplierName = supplier.getTitle();
				} else
					supplierName = pcd[0];
			}
			// supplier name
			sBuf.append(mContext.getString(R.string.biller));
			sBuf.append(": |<b>");
			sBuf.append(supplierName);
			sBuf.append("</b>|<br/>");
			// payment code
			sBuf.append(mContext.getString(R.string.so_hoa_don));
			sBuf.append(": |<b>");
			sBuf.append(billCode);
			sBuf.append("</b>|<br/>");
			
			// Customer Name
			if(!log.getAddParam().equals("")){
				sBuf.append(mContext.getString(R.string.msg_to_hoadon));
				sBuf.append(": |<b>");
				sBuf.append(log.getAddParam());
				sBuf.append("</b>|<br/>");
			}
		} else if (command.equals(Config.A_CK_AGENT)) {
			sBuf.append(mContext.getString(R.string.msg_to));
			sBuf.append(": |<b>");
			sBuf.append(Util.formatNumbersAsMid(log.getDestinationNo()));
			sBuf.append("</b>|<br/>");
		} else if (command.equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
			sBuf.append(mContext.getString(R.string.msg_from));
			sBuf.append(": |<b>");
			sBuf.append(Util.formatNumbersAsMid(log.getDestinationNo()));
			sBuf.append("</b>|<br/>");
		} else if (command.equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
			sBuf.append(mContext.getString(R.string.msg_to));
			sBuf.append(": |<b>");
			sBuf.append(Util.formatNumbersAsMid(log.getDestinationNo()));
			sBuf.append("</b>|<br/>");
		}
		
		return sBuf.toString();
	}

	public static void parseProductFromIMart(String pdcd, String price, String data) {
		try {
			String sSeri = "";
			String sAccNo = "";
			String sPin = "";
			String sexDate = "";
			String sSupport = "";

			stemp = data.split("\\|");
			if(mProduct == null)
				mProduct = new Vector<Product>(stemp.length - 1);

			for (int i = 1; i < stemp.length; i++) {
				if (!stemp[i].trim().equals("")) {
					String[] sarrTemp = stemp[i].split("!", -1);
					sSupport = sarrTemp[sarrTemp.length - 1];
					for (int j = 0; j < sarrTemp.length - 1; ++j) {
						if (!sarrTemp[j].trim().equals("")) {
							if (sarrTemp[j].trim().startsWith("1"))
								sSeri = sarrTemp[j].trim().substring(1);
							else if (sarrTemp[j].trim().startsWith("2"))
								sAccNo = sarrTemp[j].trim().substring(1);
							else if (sarrTemp[j].trim().startsWith("3"))
								sPin = sarrTemp[j].trim().substring(1);
							else if (sarrTemp[j].trim().startsWith("4"))
								sexDate = sarrTemp[j].trim().substring(1);
						}
					}

					Product product = new Product();
					product.setProductCode(pdcd);
					product.setPrice(price);
					product.setAccNo(sAccNo);
					product.setPin(sPin);
					product.setSeri(sSeri);
					product.setExDate(sexDate);
					product.setHotLine(sSupport);
					product.setIsUsed(0);
					product.setId(getDba().insertOrUpdateProduct(product));
					mProduct.add(product);

					sSeri = "";
					sAccNo = "";
					sPin = "";
					sexDate = "";
					sSupport = "";
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "parseProductFromIMart", ex);
		}
	}

	public void processErrConnect(String sErr, byte iTagErr) {
		if (iTagErr == MPConnection.UPDATE_REQUIRED) {
			showDiaLogUpdateVersion();
		} else if (iTagErr == MPConnection.ERROR_NOT_ACTIVATION) {
			onCreateMyDialog(DANG_KY).show();
		} else{
//			cmdErrorConnect();
			sThongBao = Util.sCatVal(sErr);
			onCreateMyDialog(TRY_CONNECT).show();
		}
	}
	
	public boolean isCheckInfo() {
		if ("".equals(User.sCustName) || "".equals(User.sCustCMND) || "".equals(User.sCustBirthday) 
				|| "".equals(User.sCustQesID)) return false;
		
		return true;
	}
	
	public void hideKeyBoards() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}