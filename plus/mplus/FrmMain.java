package com.mpay.plus.mplus;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.FunctionType;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.banks.FrmDangKyNapRutTien;
import com.mpay.plus.banks.FrmMenuNapTien;
import com.mpay.plus.banks.FrmSaoKe;
import com.mpay.plus.banks.FrmTransfer;
import com.mpay.plus.bill.FrmBillPayment;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.News;
import com.mpay.plus.database.User;
import com.mpay.plus.imart.FrmSale;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.system.FrmMenuLog;
import com.mpay.plus.system.FrmPenddingLog;
import com.mpay.plus.system.FrmRegistrationInfo;
import com.mpay.plus.topup.FrmMenuTopup;
import com.mpay.plus.util.ImageLoader;
import com.mpay.plus.util.Util;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

/**
 * 
 * @author THANHNAM
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmMain extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmMain";
	public static boolean isNoticeConnect = false;
	private MPNewsAdapter mAdapter;
	private ViewPager mViewPager;
	private PageIndicator mIndicator;
	private Timer mAdverdTimer;
	private ProgressBar mProgressBar;

	private Button btnBalance;
	private Button btnMiniStmt;
	private Button btnTransfer;
	private Button btnNapTien;
	// private Button btnRutTien;
	private Button btnSale;
	private Button btnBill;
	private Button btnTopup;
	private Button btnHistory;
	private Button btnQRCode;
	public static byte curAction = 0;
//	public static boolean isCheckPending = false;
	private ImageView imageLoad;
	private ImageLoader imageLoader;
//	private TextView infoTaiKhoan;
//	private TextView infoSodu;
//	private RelativeLayout infoLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_main_1);
		
		CurFuntion = FunctionType.MAIN;

		getSupportActionBar().setIcon(R.drawable.icon_navigation_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		imageLoader = new ImageLoader(getApplicationContext());
		this.setTitle(getString(R.string.app_name) + " " + User.sVERSION);

		setControls();

		if (savedInstanceState != null && !User.isActived) {
			// Load lai data trong truong hop bi out dot ngot
			loadData();
		}
	}

	private void activeSlideAdverds() {
		if (mViewPager != null && mAdverdTimer == null) {
			mAdverdTimer = new Timer();

			// Set the schedule function and rate
			mAdverdTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// We must use this function in order to change the text
					// view text
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (mViewPager != null) {
								try {
									if (mViewPager.getCurrentItem() < mViewPager
											.getChildCount() - 1)
										mViewPager.setCurrentItem(mViewPager
												.getCurrentItem() + 1);
									else
										mViewPager.setCurrentItem(0);
								} catch (Exception ex) {
								}
							}
						}
					});
				}
			}, 0, Config.itimeSlide);
		}
	}

	private void canceSlideAdverds() {
		if (mAdverdTimer != null) {
			mAdverdTimer.cancel();
			mAdverdTimer = null;
		}
	}

	/**
	 * Init controls
	 * */
	private void setControls() {
		try {
			mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
			imageLoad = (ImageView) findViewById(R.id.imageLoad);
			imageLoader.DisplayImage("", imageLoad, R.drawable.logo_default);
	
			if (User.isFisrt) {
				// ping connect
				PingTask mTask = new PingTask(getApplicationContext());
				mTask.execute();
			} else {
				setAdverds();
			}
	
			btnTransfer = (Button) findViewById(R.id.btnTransfer);
			btnTransfer.setOnClickListener(handleButton);
	
			btnBalance = (Button) findViewById(R.id.btnBalanceInquiry);
			btnBalance.setOnClickListener(handleButton);
	
			btnMiniStmt = (Button) findViewById(R.id.btnMiniStatement);
			btnMiniStmt.setOnClickListener(handleButton);
	
			btnNapTien = (Button) findViewById(R.id.btnNapTien);
			btnNapTien.setOnClickListener(handleButton);
	
			// btnRutTien = (Button) findViewById(R.id.btnRutTien);
			// btnRutTien.setOnClickListener(handleButton);
	
			btnSale = (Button) findViewById(R.id.btnSale);
			btnSale.setOnClickListener(handleButton);
	
			btnTopup = (Button) findViewById(R.id.btnTopup);
			btnTopup.setOnClickListener(handleButton);
	
			btnBill = (Button) findViewById(R.id.btnBillPayment);
			btnBill.setOnClickListener(handleButton);
	
			btnHistory = (Button) findViewById(R.id.btnHistory);
			btnHistory.setOnClickListener(handleButton);
			
			btnQRCode = (Button) findViewById(R.id.btn_BarCode_DeCode);
			btnQRCode.setOnClickListener(handleButton);
	
			if(isCheckInfo() == false && User.isRegisted == true || User.isRegisted == false){
			}else {
				confirmPending();
			}
			
		} catch (Exception e) {
			sendExceptionGA(FrmMain.this, e);
		}
	}
	
	private void setAdverds() {
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		
		if(imageLoad != null)
			imageLoad.setVisibility(ImageView.GONE);
		try {
			try {
				// Set news Slide
				Vector<News> news = null;

				boolean flag = true;
				while (news == null && flag) {
					news = getDba().getNews(DBAdapter.DB_GROUP_TYPE_NEWS);

					if (news == null) {
						// init default menu
						if (User.sLang.equals(Config.LANG_EN)) {
							saveMenuNews(Config.DEFAULT_MENU_NEWS[1]);
						} else {
							saveMenuNews(Config.DEFAULT_MENU_NEWS[0]);
						}

						news = getDba().getNews(DBAdapter.DB_GROUP_TYPE_NEWS);
						flag = false;
					}
				}

				mAdapter = new MPNewsAdapter(getSupportFragmentManager(), news,
						0);
				mViewPager = (ViewPager) findViewById(R.id.pager);
				mViewPager.setAdapter(mAdapter);

				mViewPager.setOnTouchListener(new View.OnTouchListener() {
					float oldX = 0, newX = 0, sens = 5;

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// handle scroll confict with scrollviewer
						v.getParent().requestDisallowInterceptTouchEvent(true);

						// handle tap to view detail
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							oldX = event.getX();
							break;

						case MotionEvent.ACTION_UP:
							newX = event.getX();
							if (Math.abs(oldX - newX) < sens) {
								oldX = 0;
								newX = 0;

								Bundle sendBundle = new Bundle();
								sendBundle.putInt("index",
										mViewPager.getCurrentItem());
								Intent intent = new Intent(FrmMain.this,
										FrmNews.class);
								intent.putExtras(sendBundle);
								startActivity(intent);
								return true;
							}
							break;
						}
						return false;
					}
				});

				mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int arg0) {
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						mViewPager.getParent()
								.requestDisallowInterceptTouchEvent(true);
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});

				mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
				mIndicator.setViewPager(mViewPager);
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "setControls", ex);
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * Xu ly bam button
	 * */
	private View.OnClickListener handleButton = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				if (!isBusy) {
					Intent intent = null;
					if (v.equals(btnSale)) {
						intent = new Intent(FrmMain.this, FrmSale.class);
						startActivity(intent);
					} else if (v.equals(btnTopup)) {
						intent = new Intent(FrmMain.this, FrmMenuTopup.class);
						startActivity(intent);
					} else if (v.equals(btnBill)) {
						intent = new Intent(FrmMain.this, FrmBillPayment.class);
						startActivity(intent);
					} else if (v.equals(btnHistory)) {
						intent = new Intent(FrmMain.this, FrmMenuLog.class);
						startActivity(intent);
					} else if (v.equals(btnBalance)) {
						curAction = iBALANCE_INQUIRY;
						// if (User.arrMyAcountList.length == 1) {
						onCreateMyDialog(MPIN).show();
						// } else {
						// onCreateDialog(ACCOUNT_LIST).show();
						// }
					} else if (v.equals(btnMiniStmt)) {
						curAction = iMINI_STATMENT;
						// if (User.arrMyAcountList.length == 1) {
						onCreateMyDialog(MPIN).show();
						// } else {
						// onCreateDialog(ACCOUNT_LIST).show();
						// }
					} else if (v.equals(btnTransfer)) {
						intent = new Intent(FrmMain.this, FrmTransfer.class);
						startActivity(intent);
						
	//					intent = new Intent(FrmMain.this, FrmMenuTransfer.class);
	//					startActivity(intent);
					} else if (v.equals(btnNapTien)) {
						
						startActivity(new Intent(FrmMain.this, FrmMenuNapTien.class));
	//					sThongBao = "";
	//					if (User.isRegisted && !User.isRegNapTien) {
	//						ACTION = iDANG_KY_NAP_RUT_TIEN;
	//						sThongBao = getString(R.string.msg_register_bank_account_require);
	//
	//						getMessageConfirm().setTitle(getString(R.string.title_xacnhangiaodich));
	//						getMessageConfirm().setOther(sThongBao);
	//						getMessageConfirm().setBtn_dongy(getString(R.string.btn_dongy));
	//						getMessageConfirm().setBtn_khongdongy(getString(R.string.btn_huy));
	//						getMessageConfirm().setStatus(false);
	//						onCreateMyDialog(XAC_NHAN).show();
	//					} else {
	//						intent = new Intent(FrmMain.this, FrmNapRutTien.class);
	//						Bundle sendBundle = new Bundle();
	//						sendBundle.putByte("type", (byte) 0);
	//						intent.putExtras(sendBundle);
	//						startActivityForResult(intent, 0);
	//					}
						
						
						// } else if (v.equals(btnRutTien)) {
						// if (User.isRegisted && !User.isRegNapTien) {
						// ACTION = iDANG_KY_NAP_RUT_TIEN;
						// sThongBao =
						// getString(R.string.msg_register_bank_account_require);
						// onCreateMyDialog(XAC_NHAN, getIconConfirm()).show();
						// } else {
						// intent = new Intent(FrmMain.this, FrmNapRutTien.class);
						// Bundle sendBundle = new Bundle();
						// sendBundle.putByte("type", (byte) 1);
						// intent.putExtras(sendBundle);
						// startActivityForResult(intent, 0);
						// }
					} else if (v.equals(btnQRCode)) {
//						intent = new Intent(FrmMain.this, CaptureActivity.class);
//						startActivity(intent);
					}
				}
			} catch (Exception e) {
				sendExceptionGA(FrmMain.this, e);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		 getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// MPlusLib.debug(TAG, "onOptionsItemSelected " + item.getItemId());
	//
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// toggle();
	// return true;
	//
	// case R.id.setting:
	//
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;
		if (!isBusy) {
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				toggle();
				break;
				
//			case R.id.settingPlus:
//				setSettingActivity(R.string.topup_game+"|icon_deliver_agent"+"|"+FrmMain.this.getClass().getName());
//				break;

//			case R.id.setting:
				// Intent intent = new Intent(FrmMain.this,
				// FrmMenuSystem.class);
				// startActivityForResult(intent, 0);
				// Intent intent = new Intent(FrmMain.this,
				// PreferencesActivity.class);
				// startActivityForResult(intent, 0);
//				break;

			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else
			result = false;

		return result;
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == CHANGE_LANG) {
			// setLocaleLang();
			// btnBank.setText(getString(R.string.bank_name));
		} else if (resultCode != SUCCESS) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void onPause() {
		System.gc();
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		canceSlideAdverds();
	}

	public void onDestroy() {
		canceSlideAdverds();
		if (mProgressBar != null) {
			mProgressBar.destroyDrawingCache();
			mProgressBar = null;
		}
		if (mViewPager != null) {
			mViewPager.destroyDrawingCache();
			mViewPager = null;
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// chay slide
		activeSlideAdverds();
		CurFuntion = FunctionType.MAIN;
		sThongBao = "";
		if(User.isRegisted == true && isCheckInfo() == false){
			sThongBao = getString(R.string.msg_actived_info);
			onCreateMyDialog(THONG_BAO).show();
		}
//		if(User.isRegisted == false) startActivity(new Intent(FrmMain.this, FrmRegistration.class));
	}
	
	private void confirmPending() {
		int count = getDba().getLogPendingCard().size();
		if (count > 0) {
			sThongBao = Util.insertString(
					getString(R.string.confirm_Pending),
					new String[] { String.valueOf(count)});
			
			setMessageConfirm(getString(R.string.thong_bao), "", "", "", sThongBao,
					getString(R.string.cmd_check), getString(R.string.btn_desau), false, true);
			onCreateMyDialog(XAC_NHAN)
					.show();
		}
	}

	/**
	 * Thong bao khong co ket noi
	 * */
//	private void notiveNotConnected() {
////			if (AMPlusCore.ACTION == AMPlusCore.iACTIVE_APP) {
////				String maDangKy = MPlusLib.getMaDangKy(User.sCustName, User.sCustBirthday, mpinTemp);
////				String activeCode = MPlusLib.getLenhKichHoat(maDangKy);
////				mpinTemp = null;
////				MPlusLib.callSmsSender(FrmMain.this, User.sSMS_SERVERFONE, activeCode);
////			} else if (AMPlusCore.ACTION == AMPlusCore.iCLOSE_AGENT) {
////				AMPlusCore.threadThucThi = new ThreadThucThi(
////						FrmMain.this);
////				AMPlusCore.threadThucThi.setIProcess(FrmMain.this,
////						AMPlusCore.iCLOSE_AGENT);
////				AMPlusCore.threadThucThi.Start();
////			}
//		if (!(User.iUPDATE_FLAG == 1 || User.iUPDATE_FLAG == 0 && isAllowUpdate)
//				&& !isNoticeConnect) {
//			try {
//				isNoticeConnect = true;
//				if (!isNetworkAvailable()) {
//					sThongBao = getString(R.string.connect_disable);
//					onCreateMyDialog(THONG_BAO, getIconNotice()).show();
//				} else {
//
//					int count = getDba().getLogPendingTopup().size();
//					if (count > 0) {
//						sThongBao = Util.insertString(
//								getString(R.string.confirm_Pending),
//								new String[] { String.valueOf(count),
//										getString(R.string.hotline) });
//						onCreateMyDialog(XAC_NHAN_PENDING, getIconConfirm())
//								.show();
//					}
//				}
//			} catch (Exception ex) {
//				MPlusLib.debug(TAG, "onResume", ex);
//			}
//		}
//	}
	
	@Override
	public void cmdNextThongBao() {
		if(isCheckInfo() == false)
			startActivity(new Intent(FrmMain.this, FrmRegistrationInfo.class));
//		else if(isFinished == false) onCreateMyDialog(MPIN).show();
		super.cmdNextThongBao();
	}
	
	public void cmdNextMPIN() {
		super.cmdNextMPIN();
//		if (isCheckPending) {
//			startActivity(new Intent(this, FrmPenddingLog.class));
//		} else {
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, curAction);
			threadThucThi.Start();
//		}
	}
	
	@Override
	public void cmdNextXacNhan(String data) {
		super.cmdNextXacNhan(data);
		startActivity(new Intent(this, FrmPenddingLog.class));
	}

	@Override
	public void cmdNextXacNhan() {
//			if (ACTION == iDANG_KY_NAP_RUT_TIEN) {
				Intent intent = new Intent(FrmMain.this, FrmDangKyNapRutTien.class);
				startActivity(intent);
//			} else {
//				isCheckPending = true;
//				onCreateMyDialog(MPIN).show();
//			}
	};

	public void goBack() {
		super.goBack();
		System.exit(1);
	}

	private void loadData() {
		try {
			try {
				if (AMPlusCore.dbAdapter == null)
					AMPlusCore.dbAdapter = new DBAdapter(FrmMain.this);

				AMPlusCore.getDba().loadData();
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "loadData", ex);
			} finally {
			}

			if (User.sprivateEncKey == null || "".equals(User.sprivateEncKey)) {
				User.isRegisted = false;
			} else
				User.isRegisted = true;

			String arrTemp[] = User.sVersionAndLink.split("\\|", -1);
			if (MPlusLib.isNewVersion(arrTemp[0])) {
				if (arrTemp.length >= 3) {
					User.iUPDATE_FLAG = (byte) ("1".equals(arrTemp[1]) ? 0 : 0);
					User.sVersionAndLink = arrTemp[2].trim();
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				Locale locale = new Locale(User.sLang);
				Locale.setDefault(locale);
				Configuration newConfig = new Configuration();
				newConfig.locale = locale;
				getBaseContext().getResources().updateConfiguration(newConfig,
						getBaseContext().getResources().getDisplayMetrics());
			} catch (Exception ex) {
			}
		}
	}

	// private void setLocaleLang() {
	// // ///////////////////////////
	// try {
	// Locale locale = new Locale(User.sLang);
	// Locale.setDefault(locale);
	// Configuration newConfig = new Configuration();
	// newConfig.locale = locale;
	// getBaseContext().getResources().updateConfiguration(newConfig,
	// getBaseContext().getResources().getDisplayMetrics());
	// } catch (Exception ex) {
	// }
	// // //////////////////////////
	// }

	public void cmdOKAccList() {
		super.cmdOKAccList();
		onCreateMyDialog(MPIN).show();
	}

	// #mark Connect
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case iBALANCE_INQUIRY:
			ServiceCore.TaskTraSoDu(getMyAccountIndex() + 1);
			break;

		case iMINI_STATMENT:
			ServiceCore.TaskSaoKeGiaoDich(getMyAccountIndex() + 1);
			break;
		}
	}

	public void processDataReceived(String sDataReceive, byte iTag, byte iTagErr) {
		saveUserTable();
		isFinished = false;
		if (sDataReceive.startsWith("val:")) {
			sDataReceive = Util.sCatVal(sDataReceive);
			Bundle sendBundle;
			Intent intent;
			switch (iTag) {

			case iBALANCE_INQUIRY:
				sThongBao = getString(R.string.so_du) + ": "
						+ Util.formatNumbersAsTien(sDataReceive) + " "
						+ getString(R.string.vnd);
				isFinished = true;
				onCreateMyDialog(THONG_BAO).show();
				break;

			case iMINI_STATMENT:
				if (!"".equals(sDataReceive) && !"0".equals(sDataReceive)
						&& !"_".equals(sDataReceive)) {
					sendBundle = new Bundle();
					sendBundle.putByte("value", iMINI_STATMENT);
					sendBundle
							.putStringArray("value1", sDataReceive.split("#"));
					sendBundle.putInt("acc_index", getMyAccountIndex());
					intent = new Intent(FrmMain.this, FrmSaoKe.class);
					intent.putExtras(sendBundle);
					startActivity(intent);
					isFinished = true;
				} else {
					sThongBao = getString(R.string.chua_co_giao_dich_nao);
					onCreateMyDialog(THONG_BAO).show();
				}
				break;
			}
		} else {
			sThongBao = Util.sCatVal(sDataReceive);
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	private class PingTask extends AsyncTask<String, String, String> {
		public String TAG = "PingTask";

		public PingTask(Context core) {

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isBusy = true;

			try {
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "onPreExecute", ex);
			}
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				Config.itimeReceive = 15000;
				ServiceCore.TaskPing();
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "doInBackground", ex);
			}
			return null;
		}

		// protected void onProgressUpdate(String... progress) {
		// }

		@Override
		protected void onPostExecute(String unused) {
			try {

				Config.itimeReceive = 70000;
				setAdverds();
				activeSlideAdverds();
//				if ((AMPlusCore.getConnection().getTagErr() == MPConnection.ERROR_NONE || AMPlusCore
//						.getConnection().getTagErr() > MPConnection.ERROR_NOT_SEND_DATA)) {
//				} else {
//					notiveNotConnected();
//				}
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "onPostExecute", ex);
			}

			User.isFisrt = false;
			isBusy = false;
		}
	}
//	@Override
//	public void setOnclick(int event) {
//		switch (event) {
//		case iMINI_STATMENT:
//
//			onCreateMyDialog(MPIN).show();
//			break;
//		case iBALANCE_INQUIRY:
//
//			onCreateMyDialog(MPIN).show();
//			break;
//
//		default:
//			break;
//		}
//	}
}