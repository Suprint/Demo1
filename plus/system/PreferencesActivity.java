package com.mpay.plus.system;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.mplus.dialog.DialogConfirmMessage;
import com.mpay.mplus.dialog.DialogFrmConfirm;
import com.mpay.mplus.dialog.MessageConfirm;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * @author quyenlm.vn@gmail.com
 * */
public class PreferencesActivity extends SherlockPreferenceActivity implements
		IProcess {
	private String TAG = "PreferencesActivity";

	private String sThongBao = "";
	private String sLink = "";
	private boolean isExecuteOk;
	private String mpinTemp = "";
	public MessageConfirm messageConfirm = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		if(messageConfirm == null){
			messageConfirm = new MessageConfirm();
		}
		try {
			addPreferencesFromResource(R.xml.main_preferences);
			String desc = "";
			// Hotline
			if (User.arrHotline != null && User.arrHotline.length == 1) {
				Preference preHotLine = (Preference) findPreference("preHotline");
				if (preHotLine != null) {
					desc = Util.insertString(
							getString(R.string.hotline_name_desc),
							new String[] { Util
									.formatNumbersAsMid(User.arrHotline[0]) });
					preHotLine.setSummary(desc);
				}
			}
			
			if (User.isRegisted) {
				// removing Preference
				((PreferenceCategory) findPreference("precgSetting"))
						.removePreference(findPreference("preRegistry"));
			}

			if (!User.isRegisted) {
				// removing Preference
				((PreferenceCategory) findPreference("precgSetting"))
						.removePreference(findPreference("preCloseAccount"));
			}

			if (!User.isRegisted || (User.isRegisted && User.isActived)) {
				// removing Preference
				((PreferenceCategory) findPreference("precgSetting"))
						.removePreference(findPreference("preActiveApp"));
			}
			
			// ------------------
			if (!User.isRegisted) {
				// removing Preference
				((PreferenceCategory) findPreference("precgSetting"))
						.removePreference(findPreference("preBankAccReg"));
			}
			
			if (!User.isRegisted) {
				// removing Preference
				((PreferenceCategory) findPreference("precgSetting"))
						.removePreference(findPreference("preChangePass"));
			}
			
			if (!User.isRegisted) {
				// removing Preference
				((PreferenceCategory) findPreference("precgSetting"))
						.removePreference(findPreference("preChangeMobile"));
			}
			
			// add Card Link
			Preference linkCard = (Preference) findPreference("preLinkCard");
				if (linkCard != null) {
					if (!User.myAccountLink.equals("")) {
						desc = Util.insertString(
							getString(R.string.bk_link_card_detail_),
							new String[] { Util.formatNumbersAsMid(User.myAccountLink) });
					}else {
						desc = getString(R.string.bk_link_card_detail);
					}
					linkCard.setSummary(desc);
				}

			
			// add Mid information
			Preference linkAcc = (Preference) findPreference("preBankAccReg");
			if (linkAcc != null) {
				if (!User.BANK_MID.equals("")) {
					desc = Util.insertString(
							getString(R.string.dang_ky_nap_rut_tien_desc),
							new String[] { Util
									.formatNumbersAsMid(User.BANK_MID) });
				}else {
					desc = getString(R.string.title_dangky_);
				}
				linkAcc.setSummary(desc);
			}

			// Agent Mobile Number
			
			Preference curMobile = (Preference) findPreference("preChangeMobile");
			if (curMobile != null) {
				if("".equals(User.MID)){
					desc = getString(R.string.title_dangky);
				}else {
					desc = Util.insertString(
							getString(R.string.mobile_number_current),
							new String[] { Util.formatNumbersAsMid(User.MID) });
				}
				curMobile.setSummary(desc);
			}

			// Version
			Preference curVer = (Preference) findPreference("preUpdateApp");
			if (curVer != null) {
				desc = Util.insertString(getString(R.string.version_current),
						new String[] { User.sVERSION });
				curVer.setSummary(desc);
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "onCreate", ex);
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;

		// Xu ly menu
		switch (item.getItemId()) {
		case android.R.id.home:
			goBack();
			break;

		default:
			result = super.onMenuItemSelected(featureId, item);
			break;
		}

		return result;
	};

	private void goBack() {
		Intent result = new Intent();
		setResult(11, result);
		PreferencesActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			goBack();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		getMessageConfirm().desTructor();
		if (preference.getKey().equals("preHotline")) {
			// call hotline
			if (User.arrHotline != null && User.arrHotline.length == 1) {
				try {
					Intent callIntent = new Intent(Intent.ACTION_VIEW);
					callIntent.setData(Uri.parse("tel:"
							+ Uri.encode(User.arrHotline[0])));
					startActivity(callIntent);
				} catch (Exception ex) {
					MPlusLib.debug(TAG, "onPreferenceTreeClick", ex);
				}
			} else if (User.arrHotline != null && User.arrHotline.length > 1) {
				Intent callIntent = new Intent(PreferencesActivity.this,
						FrmAgentUtil.class);
				startActivity(callIntent);
			}
		} else if (preference.getKey().equals("preSendApp")) {
			// Gui tang ung dung qua sms
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body",
						getString(R.string.msg_introduction_content));
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "onPreferenceTreeClick", ex);
			}
		} else if (preference.getKey().equals("preSuggestion")) {
			AMPlusCore.threadThucThi = new ThreadThucThi(PreferencesActivity.this);
			AMPlusCore.threadThucThi.setIProcess(PreferencesActivity.this, AMPlusCore.iGET_SUGGESTIONS);
			AMPlusCore.threadThucThi.Start();
		} else if (preference.getKey().equals("preHelp")) {
			try {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.sURL_BANK_HELP));
				startActivity(browserIntent);
			} catch (Exception ex) {
			}
//		} else if (preference.getKey().equals("preActiveApp")) {
//			// kich hoat ung dung
//			AMPlusCore.ACTION = AMPlusCore.iACTIVE_APP;
//			sThongBao = getString(R.string.confirm_active_app);
//			onCreateMyDialog(AMPlusCore.XAC_NHAN, getIconConfirm()).show();
		} else if (preference.getKey().equals("preUpdateApp")) {
			// Cap nhat phien ban
			AMPlusCore.threadThucThi = new ThreadThucThi(
					PreferencesActivity.this);
			AMPlusCore.threadThucThi.setIProcess(PreferencesActivity.this,
					AMPlusCore.iGET_VERSION_LINK);
			AMPlusCore.threadThucThi.Start();
		} else if (preference.getKey().equals("preCloseAccount")) {
			// Dong tai khoan agent
			AMPlusCore.ACTION = AMPlusCore.iCLOSE_AGENT;
			sThongBao = getString(R.string.confirm_close_agent);

			getMessageConfirm().setTitle(getString(R.string.title_xacnhangiaodich));
			getMessageConfirm().setOther(sThongBao);
			getMessageConfirm().setBtn_dongy(getString(R.string.btn_dongy));
			getMessageConfirm().setBtn_khongdongy(getString(R.string.btn_huy));
			getMessageConfirm().setInitMpin(false);
			onCreateMyDialog(AMPlusCore.XAC_NHAN).show();
		} else if (preference.getKey().equals("preInfo")) {
			Intent intent = new Intent(PreferencesActivity.this, FrmRegistrationInfo.class);
			intent.putExtra("isFlag", true);
			startActivity(intent);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
	
	/**
	 * THONG_BAO Tạo dialog thông báo XAC_NHAN Tạo dialog xác nhận giao dịch
	 * MPIN Tạo dialog nhập MPIN
	 */
	public Dialog onCreateMyDialog(int id) {
//		LayoutInflater factory = LayoutInflater.from(PreferencesActivity.this);
//		View view;
		switch (id) {
		case AMPlusCore.THONG_BAO:
			

			final DialogConfirmMessage dialogConfirmMessage = new DialogConfirmMessage(PreferencesActivity.this);
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
					dialogConfirmMessage.dismiss();
				}
			});
			
			return dialogConfirmMessage;
			
//			return new AlertDialog.Builder(PreferencesActivity.this)
//					.setTitle(getString(R.string.thong_bao))
//					.setMessage(sThongBao)
//					.setCancelable(false)
//					.setIcon(iconId)
//					.setPositiveButton(getString(R.string.cmd_ok),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									sThongBao = "";
//									cmdNextThongBao();
//									dialog.dismiss();
//								}
//							}).create();

		case AMPlusCore.XAC_NHAN:
			
			final DialogFrmConfirm dialogConfirm = new DialogFrmConfirm(PreferencesActivity.this, false);
			dialogConfirm.setMPinEnable(getMessageConfirm().getStatus());
			dialogConfirm.setTitles(getMessageConfirm().getTitle());
			dialogConfirm.setMyMessage(getMessageConfirm().getMessage());
			dialogConfirm.setMyMessage1(getMessageConfirm().getMessage1());
			dialogConfirm.setMyMessage_(getMessageConfirm().getMessage_());
			dialogConfirm.setOther(getMessageConfirm().getOther());
			dialogConfirm.setBtn_dongy(getMessageConfirm().getBtn_dongy());
			dialogConfirm.setBtn_khongdongy(getMessageConfirm().getBtn_khongdongy());
			dialogConfirm.setInitMpin(getMessageConfirm().getInitMpin());
			dialogConfirm.setClickDialogConfirm_Dongy_(new DialogFrmConfirm.onClickDialogConfirm_Dongy_() {
				
				@Override
				public void onClickDonY_(String data) {
					if (User.isRegisted) {
						cmdNextXacNhan(data);
					} else {
						onCreateMyDialog(AMPlusCore.DANG_KY).show();
					}					
				}
			});
//			dialogConfirm.setClickDialogConfirm_Dongy(new DialogFrmConfirm.onClickDialogConfirm_Dongy() {
//				
//				@Override
//				public void onClickDonY() {
//					if (User.isRegisted) {
//						cmdNextXacNhan();
//					} else {
//						onCreateMyDialog(AMPlusCore.DANG_KY).show();
//					}
//				}
//			});
			dialogConfirm.setClickDialogConfirm_Khongdongy(new DialogFrmConfirm.onClickDialogConfirm_Khongdongy() {
				
				@Override
				public void onClickKhongDongY() {
					cmdBackXacNhan();
				}
			});
			
//			final DialogConfirmMessage dialogConfirm = new DialogConfirmMessage(PreferencesActivity.this);
//			dialogConfirm.setTitles(getString(R.string.xac_nhan));
//			dialogConfirm.setMyMessage(sThongBao);
//			dialogConfirm.setDongy(getString(R.string.cmd_ok));
//			dialogConfirm.setKhongdongy(getString(R.string.cmd_back));
//			dialogConfirm.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
//				
//				@Override
//				public void onclickKhongDongY() {
//					dialogConfirm.dismiss();
//					cmdBackXacNhan();
//				}
//				
//				@Override
//				public void onclickDongY() {
//					if (User.isRegisted) {
//						cmdNextXacNhan();
//						dialogConfirm.dismiss();
//					} else {
//						onCreateMyDialog(AMPlusCore.DANG_KY,
//								getIconConfirm()).show();
//					}
//				}
//			});
			return dialogConfirm;
			
//			return new AlertDialog.Builder(PreferencesActivity.this)
//					.setTitle(getString(R.string.xac_nhan))
//					.setMessage(sThongBao)
//					.setCancelable(false)
//					.setIcon(getIconConfirm())
//					.setPositiveButton(getString(R.string.cmd_ok),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									if (User.isRegisted) {
//										cmdNextXacNhan();
//										dialog.dismiss();
//									} else {
//										onCreateMyDialog(AMPlusCore.DANG_KY,
//												getIconConfirm()).show();
//									}
//								}
//							})
//					.setNegativeButton(getString(R.string.cmd_back),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									dialog.dismiss();
//									cmdBackXacNhan();
//								}
//							}).create();

//		case AMPlusCore.MPIN:
//			
//			final DialogMpin dialogMpin = new DialogMpin(this);
//			dialogMpin.setOnClickClickDialogMpin(new DialogMpin.onClickDialogMpin() {
//				
//				@Override
//				public void clickKhongDongY() {
//					cmdBackMPIN();
//				}
//				
//				@Override
//				public void clickDongY(String data) {
//					if (User.isRegisted) {
//							if (kiemTraMPIN(data)) {
//								if (AMPlusCore.ACTION == AMPlusCore.iACTIVE_APP) {
//									mpinTemp = data;
//								}
//								cmdNextMPIN();
//								dialogMpin.dismiss();
//							} else {
////								Toast.makeText(PreferencesActivity.this, getString(R.string.alert_message_nhap_sai_pin)
////										+ User.iSoLanSaiMPIN
////										+ "/"
////										+ Config.iMPIN_MAX_WRONG
////										+ ")"
////										+ (User.iSoLanSaiMPIN < Config.iMPIN_MAX_WRONG ? getString(R.string.alert_message_nhap_sai_pin2)
////												: getString(R.string.alert_message_nhap_sai_pin_yeu_cau)),
////										Toast.LENGTH_LONG).show();
//								dialogMpin.dismiss();
//							}
//					} else {
//						onCreateMyDialog(AMPlusCore.DANG_KY).show();
//					}
//				}
//			});
//			return dialogMpin;
			
			
//			view = factory.inflate(R.layout.mpin, null);
//			final EditText etMPIN = (EditText) view.findViewById(R.id.etmpin);
//			etMPIN.setInputType(InputType.TYPE_CLASS_PHONE);
//			etMPIN.setTransformationMethod(PasswordTransformationMethod
//					.getInstance());
//			etMPIN.requestFocus();
//			etMPIN.addTextChangedListener(new TextWatcher() {
//
//				@Override
//				public void afterTextChanged(Editable s) {
//					Util.ChuanHoaMPIN(etMPIN, this);
//				}
//
//				@Override
//				public void beforeTextChanged(CharSequence s, int start,
//						int count, int after) {
//				}
//
//				@Override
//				public void onTextChanged(CharSequence s, int start,
//						int before, int count) {
//				}
//
//			});
//
//			return new AlertDialog.Builder(PreferencesActivity.this)
//					.setTitle(getString(R.string.mpin))
//					.setView(view)
//					.setCancelable(false)
//					.setIcon(getIconPin())
//					.setPositiveButton(getString(R.string.cmd_ok),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									dialog.dismiss();
//									if (User.isRegisted) {
//										if (!etMPIN.getText().toString().trim()
//												.equals("")) {
//											if (kiemTraMPIN(etMPIN.getText()
//													.toString())) {
//												if (AMPlusCore.ACTION == AMPlusCore.iACTIVE_APP) {
//													mpinTemp = etMPIN.getText()
//															.toString();
//												}
//												etMPIN.setText("");
//												cmdNextMPIN();
//											} else {
//												etMPIN.setText("");
//											}
//										} else {
//											onCreateMyDialog(AMPlusCore.MPIN,
//													getIconPin()).show();
//											Toast.makeText(
//													PreferencesActivity.this,
//													getString(R.string.phai_nhap)
//															+ " "
//															+ getString(R.string.mpin),
//													Toast.LENGTH_LONG).show();
//										}
//									} else {
//										onCreateMyDialog(AMPlusCore.DANG_KY,
//												getIconConfirm()).show();
//									}
//
//								}
//							})
//					.setNegativeButton(getString(R.string.cmd_back),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									etMPIN.setText("");
//									dialog.dismiss();
//									cmdBackMPIN();
//								}
//							}).create();

		case AMPlusCore.TRY_CONNECT:
			
			final DialogConfirmMessage dialogconfirmconnect = new DialogConfirmMessage(PreferencesActivity.this);
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
//					finish();
//					IProcess iprocess = AMPlusCore.threadThucThi
//							.getIProcess();
//					byte iTag = AMPlusCore.threadThucThi
//							.getTag();
//					AMPlusCore.threadThucThi = new ThreadThucThi(
//							PreferencesActivity.this);
//					AMPlusCore.threadThucThi.setIProcess(
//							iprocess, iTag);
//					AMPlusCore.threadThucThi.Start();
				}
			});
			
			return dialogconfirmconnect;
			
//			return new AlertDialog.Builder(PreferencesActivity.this)
//					.setTitle(getString(R.string.thong_bao))
//					.setMessage(sThongBao)
//					.setCancelable(false)
//					.setIcon(getIconError())
//					.setPositiveButton(getString(R.string.cmd_next),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									dialog.dismiss();
//									IProcess iprocess = AMPlusCore.threadThucThi
//											.getIProcess();
//									byte iTag = AMPlusCore.threadThucThi
//											.getTag();
//									AMPlusCore.threadThucThi = new ThreadThucThi(
//											PreferencesActivity.this);
//									AMPlusCore.threadThucThi.setIProcess(
//											iprocess, iTag);
//									AMPlusCore.threadThucThi.Start();
//								}
//							})
//					.setNegativeButton(getString(R.string.cmd_back),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									dialog.dismiss();
//								}
//							}).create();

		case AMPlusCore.DANG_KY:
			
			final DialogConfirmMessage dialogdangky = new DialogConfirmMessage(PreferencesActivity.this);
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
					startActivityForResult(new Intent(PreferencesActivity.this,
							FrmRegistration.class), 0);
					goBack();
				}
			});
			return dialogdangky;
			
//		case AMPlusCore.MPIN:
//			
//			final DialogMpin dialogMpin = new DialogMpin(this);
//			dialogMpin.setOnClickClickDialogMpin(new DialogMpin.onClickDialogMpin() {
//				
//				@Override
//				public void clickKhongDongY() {
//					cmdBackMPIN();
//				}
//				
//				@Override
//				public void clickDongY(String data) {
//					dialogMpin.dismiss();
//					if (User.isRegisted) {
//							if (kiemTraMPIN(data)) {
//								if (ACTION == iACTIVE_APP
//										|| ACTION == iCHUYEN_KHOAN) {
//									mpinTemp = data;
//								}
//								cmdNextMPIN();
//							}
//					} else {
//						onCreateMyDialog(DANG_KY).show();
//					}
//				}
//			});
//			return dialogMpin;
			
//			return new AlertDialog.Builder(PreferencesActivity.this)
//					.setTitle(getString(R.string.xac_nhan))
//					.setMessage(getString(R.string.not_activation))
//					.setCancelable(false)
//					.setIcon(getIconConfirm())
//					.setPositiveButton(getString(R.string.cmd_ok),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									startActivityForResult(new Intent(
//											PreferencesActivity.this,
//											FrmRegistration.class), 0);
//								}
//							})
//					.setNegativeButton(getString(R.string.cmd_back),
//							new DialogInterface.OnClickListener() {
//
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									dialog.dismiss();
//								}
//							}).create();
		}
		return null;
	}

	public MessageConfirm getMessageConfirm() {
		return messageConfirm;
	}

	public void setMessageConfirm(MessageConfirm messageConfirm) {
		this.messageConfirm = messageConfirm;
	}

//	public boolean kiemTraMPIN(String vmpin) {
//		try {
//			if (MPlusLib.loadKey(vmpin) != null) {
//				User.iSoLanSaiMPIN = 0;
//				AMPlusCore.saveUserTable();
//				return true;
//			} else {
//				User.iSoLanSaiMPIN++;
//
//				if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
//					User.sprivateEncKey = "";
//				}
//				showDiaLogWrongMPin();
//				AMPlusCore.saveUserTable();
//				return false;
//			}
//		} catch (Exception e) {
//			return false;
//		}
//	}

//	public void showDiaLogWrongMPin() {
//
//		DialogConfirmMessage dialogConfirmMessage = new DialogConfirmMessage(PreferencesActivity.this);
//		dialogConfirmMessage.setMyMessage(getString(R.string.alert_message_nhap_sai_pin)
//
//				+ User.iSoLanSaiMPIN
//				+ "/"
//				+ Config.iMPIN_MAX_WRONG
//				+ ")"
//				+ (User.iSoLanSaiMPIN < Config.iMPIN_MAX_WRONG ? getString(R.string.alert_message_nhap_sai_pin2)
//						: getString(R.string.alert_message_nhap_sai_pin_yeu_cau)));
//		dialogConfirmMessage.setDongy(getString(R.string.cmd_ok));
//		dialogConfirmMessage.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
//			
//			@Override
//			public void onclickKhongDongY() {
//			}
//			
//			@Override
//			public void onclickDongY() {
//				if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
//					setResult(AMPlusCore.EXITCODE, null);
//					User.iSoLanSaiMPIN = 0;
//					User.isRegisted = false;
//					User.MID = "";
//					User.BANK_MID = "";
//					finish();
//				} else 
//					onCreateMyDialog(AMPlusCore.MPIN).show();
//			}
//		});
//		
//		dialogConfirmMessage.show();
//		
//		
////		AlertDialog.Builder alert = new AlertDialog.Builder(this);
////		alert.setTitle(getString(R.string.thong_bao));
////		alert.setIcon(R.drawable.icon_notice);
////		alert.setMessage(getString(R.string.alert_message_nhap_sai_pin)
////
////				+ User.iSoLanSaiMPIN
////				+ "/"
////				+ Config.iMPIN_MAX_WRONG
////				+ ")"
////				+ (User.iSoLanSaiMPIN < Config.iMPIN_MAX_WRONG ? getString(R.string.alert_message_nhap_sai_pin2)
////						: getString(R.string.alert_message_nhap_sai_pin_yeu_cau)));
////		alert.setPositiveButton(getString(R.string.cmd_ok),
////				new DialogInterface.OnClickListener() {
////					public void onClick(DialogInterface dialog, int whichButton) {
////						if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
////							setResult(AMPlusCore.EXITCODE, null);
////							finish();
////						} else
////							onCreateMyDialog(AMPlusCore.MPIN, getIconPin())
////									.show();
////					}
////
////				});
////		alert.show();
//	}
	
	/**
	 * Xử lý MPIN người dùng vừa nhập
	 */
	public void cmdNextMPIN() {
		if (AMPlusCore.ACTION == AMPlusCore.iACTIVE_APP) {
			String maDangKy = MPlusLib.getMaDangKy(User.sCustAnser, mpinTemp);
			String activeCode = MPlusLib.getLenhKichHoat(maDangKy);
			mpinTemp = null;
			MPlusLib.callSmsSender(PreferencesActivity.this, User.sSMS_SERVERFONE, activeCode);
		} else if (AMPlusCore.ACTION == AMPlusCore.iCLOSE_AGENT) {
			AMPlusCore.threadThucThi = new ThreadThucThi(
					PreferencesActivity.this);
			AMPlusCore.threadThucThi.setIProcess(PreferencesActivity.this,
					AMPlusCore.iCLOSE_AGENT);
			AMPlusCore.threadThucThi.Start();
		}
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
		if (isExecuteOk && AMPlusCore.ACTION == AMPlusCore.iCLOSE_AGENT) {
			Intent intent = new Intent(PreferencesActivity.this,
					FrmRegistration.class);
			startActivity(intent);
		}
	}

	public void cmdNextXacNhan(String data) {
		if (AMPlusCore.ACTION == AMPlusCore.iACTIVE_APP) {
			mpinTemp = data;
		}
		cmdNextMPIN();
	};

	/**
	 * Thoát khỏi Form Xác nhận và quay về Form trước
	 */
	public void cmdBackXacNhan() {

	}

	private void downloadApp() {
		AutoUpdate update = new AutoUpdate(this, sLink);
		update.check();
	}

	@Override
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case AMPlusCore.iGET_SUGGESTIONS:
			ServiceCore.TaskSendOrReceiveSuggestion("");
			break;

			// Err_1
		case AMPlusCore.iCLOSE_AGENT:
			ServiceCore.TaskCloseAgent();
			break;

		case AMPlusCore.iGET_VERSION_LINK:
			ServiceCore.TaskUpdateVersion();
			break;
		}
	}

	@Override
	public void processDataReceived(final String dataReceived, byte iTag,
			byte iTagErr) {
		String data = "";
//		dataReceived = "val:http://test.m-pay.vn/vietbank/v40/demo/mplus.apk|4.0<><><><><><>AG!3.0|1|1";
		switch (iTag) {
		case AMPlusCore.iGET_SUGGESTIONS:
			if (dataReceived.startsWith("val:")) {
				data = Util.sCatVal(dataReceived);
			}

			Bundle sendBundle = new Bundle();
			sendBundle.putString("data", data);
			Intent intent = new Intent(PreferencesActivity.this,
					FrmSuggestions.class);
			intent.putExtras(sendBundle);
			startActivityForResult(intent, 0);
			break;

			// Err_1
		case AMPlusCore.iCLOSE_AGENT:
			if (dataReceived.startsWith("val:")) {

				User.isRegisted = false;
				User.isActived = false;
				User.isRegNapTien = false;
				User.BANK_MID = "";
				User.MID = "";

				User.sprivateEncKey = "";
				AMPlusCore.saveUserTable();

				// Xac nhan kich hoat qua sms
				sThongBao = getString(R.string.msg_close_agent_success);
				isExecuteOk = true;
				onCreateMyDialog(AMPlusCore.THONG_BAO).show();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(AMPlusCore.THONG_BAO).show();
			}
			break;

		case AMPlusCore.iGET_VERSION_LINK:
			
			if (dataReceived.startsWith("val:")) {
				data = Util.sCatVal(dataReceived);
				String arrVersionLinkTemp[] = data.split("\\|");
				if (arrVersionLinkTemp.length >= 2) {
					if (!MPlusLib.isNewVersion(data.split("\\|")[1].trim())) {
						if (arrVersionLinkTemp[1].trim().equals(User.sVERSION)) {
							sThongBao = Util.insertString(
									getString(R.string.version_newest),
									new String[] { User.sVERSION });
							sLink = arrVersionLinkTemp[0];

							getMessageConfirm().setTitle(getString(R.string.title_xacnhangiaodich));
							getMessageConfirm().setOther(sThongBao);
							getMessageConfirm().setBtn_dongy(getString(R.string.btn_dongy));
							getMessageConfirm().setBtn_khongdongy(getString(R.string.btn_huy));
							onCreateMyDialog(AMPlusCore.XAC_NHAN).show();
						} else {
							sThongBao = getString(R.string.msg_ulastest);
							onCreateMyDialog(AMPlusCore.THONG_BAO).show();
						}
					} else {
						sLink = arrVersionLinkTemp[0];
						downloadApp();
					}
				} else {
					sThongBao = data;
					onCreateMyDialog(AMPlusCore.THONG_BAO).show();
				}
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(AMPlusCore.THONG_BAO).show();
			}
			break;
		}
	}

	public void processErrConnect(String sErr, byte iTagErr) {
		if (iTagErr == MPConnection.ERROR_NOT_ACTIVATION) {
			
			onCreateMyDialog(AMPlusCore.DANG_KY).show();
		} else {
			sThongBao = Util.sCatVal(sErr);
			onCreateMyDialog(AMPlusCore.TRY_CONNECT).show();
		}
	}
}