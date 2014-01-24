package com.mpay.plus.system;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.mplus.MenuMain;
import com.mpay.plus.util.Util;

/**
 * 
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmChangeMobileNumber extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmChangeMobileNumber";
		
	private EditText etTel = null;	
//	private boolean isSmsResult = false;
	private Button btn_doisodienthoai = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_ht_change_mobile_number);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();
	}

	private void setControls(){
		try{			
			etTel = (EditText) findViewById(R.id.et_tel);
			btn_doisodienthoai = (Button) findViewById(R.id.btn_doisodienthoai);
			setEvent();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Khoi tao menu
//		getSupportMenuInflater().inflate(R.menu.ok, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;

		if (!isBusy) {
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				goBack();
				break;
//
//			case R.id.ok:
//				goNext();
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
		if(isFinished)
			goBack();
		else
			super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void goBack() {
		etTel = null;
		super.goBack();
	}

	private void setEvent() {
		etTel.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaPhoneNumber(etTel, this, selection, beforesSelection);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				beforesSelection = before;
				selection = start;
			}
		});

		etTel.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});
		btn_doisodienthoai.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}
	
	private String getMobileNumber() {
		return Util.keepNumbersOnly(etTel.getText().toString().trim());
	}

	private void checkData() {
		sThongBao = "";
		if (getMobileNumber().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.mobile_number);
			etTel.requestFocus();
		} else if (getMobileNumber().length() < Config.iTEL_MIN_LENGTH || getMobileNumber().length() > Config.iTEL_MAX_LENGTH) {
			sThongBao = getString(R.string.mobile_number_wrong0) + " " + getString(R.string.nhap_lai);
			etTel.requestFocus();
		} else if ( getMobileNumber().equals(User.MID)){
			sThongBao = getString(R.string.mobile_number_conflict) + " " + getString(R.string.nhap_lai);
			etTel.requestFocus();
		}
	}

	public void goNext() {
		super.goNext();
		checkData();
		if (sThongBao.equals("")) {
			ACTION = iACTIVE_APP;
			onCreateMyDialog(MPIN).show();					
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	@Override public void cmdNextMPIN() {
		threadThucThi = new ThreadThucThi(this);
		threadThucThi.setIProcess(this, (byte) 0);
		threadThucThi.Start();
	};
	
//	@Override
//	public void cmdNextXacNhan() {
//		try{			
//			String activeCode = MPlusLib.getLenhKichHoat(MPlusLib.getMaDangKy(User.sCustAnser , mpinTemp));
//			mpinTemp = null;
//			
//			MPlusLib.callSmsSender(FrmChangeMobileNumber.this, User.sSMS_SERVERFONE, activeCode);
//			
////			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
////			sendIntent.putExtra("sms_body",  activeCode);
////			sendIntent.putExtra("address", Config.sSMS_SERVERFONE);
////			sendIntent.setType("vnd.android-dir/mms-sms");
////			startActivity(sendIntent);
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "cmdNextXacNhan", ex);
//		}
//	};
	
//	@Override
//	public void cmdBackXacNhan() {
//		goBack();
//	};
	
//	@Override
//	public void cmdNextThongBao() {
//		if(isSmsResult)
//			goBack();
//	}
	
	@Override
	public void cmdNextThongBao() {
		if(isFinished){
			goBack();
			MenuMain.showInfoAcount();
		}
		super.cmdNextThongBao();
	}
	
	// #mark Connect
	public void processDataSend(byte iTag) {
		ServiceCore.TaskChangeMobileNumber(getMobileNumber());
	}

	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		if (dataReceived.startsWith("val:")) {
			
			User.MID = getMobileNumber();			
			User.isRegisted = true;
			User.isActived = false;
			User.isRegNapTien = false;
			User.BANK_MID = "";
			
			saveUserTable();
			
			isFinished = true;
			sThongBao = getString(R.string.mobile_number_change_success);
//			onCreateMyDialog(XAC_NHAN).show();
		} else {
			sThongBao = Util.sCatVal(dataReceived);
		}
		onCreateMyDialog(THONG_BAO).show();
	}
}