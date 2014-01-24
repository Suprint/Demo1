package com.mpay.plus.system;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
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
import com.mpay.plus.util.Util;

/**
 *
 * @author quyenlm.vn@gmail.com
 */

public class FrmChangeMPin extends AMPlusCore implements IProcess {

	private EditText etMpinCu = null;
	private EditText etMpinMoi = null;
	private EditText etMpinMoi_re = null;
	private Button btn_doimatkhaugiaodich = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_ht_doi_mpin);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();		
	}

	private void setControls(){
		try{
			etMpinCu = (EditText) findViewById(R.id.etmpincu);
			etMpinMoi = (EditText) findViewById(R.id.etmpinmoi);
			etMpinMoi_re = (EditText) findViewById(R.id.etmpinmoi_re);
			btn_doimatkhaugiaodich = (Button) findViewById(R.id.btn_doimatkhaugiaodich);

			etMpinCu.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			etMpinMoi.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			etMpinMoi_re.setTransformationMethod(PasswordTransformationMethod
					.getInstance());

			isDoiMPINForm = true;
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

			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else
			result = false;

		return result;
	};
	
	public void goBack() {
		etMpinCu = null;
		etMpinMoi = null;
		etMpinMoi_re = null;
		super.goBack();
	}

	private void setEvent() {
		etMpinCu.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaMPIN(etMpinCu, this);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});
		etMpinMoi.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaMPIN(etMpinMoi, this);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});
		etMpinMoi_re.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaMPIN(etMpinMoi_re, this);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});
		btn_doimatkhaugiaodich.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}

	public String getMPINCu() {
		return Util.keepNumbersOnly(etMpinCu.getText().toString().trim());
	}

	public String getMPINMoi() {
		return Util.keepNumbersOnly(etMpinMoi.getText().toString().trim());
	}

	public String getXacNhanMPINMoi() {
		return Util.keepNumbersOnly(etMpinMoi_re.getText().toString().trim());
	}

	private void checkData() {
		sThongBao = "";
		if (getMPINCu().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.mpin_old);
			etMpinCu.requestFocus();
		} else if (getMPINMoi().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.mpin_new);
			etMpinMoi.requestFocus();
		} else if (getMPINMoi().length() < 6) {
			sThongBao = getString(R.string.mpin_new) + " "
					+ getString(R.string.mpin_wrong0)
					+ getString(R.string.nhap_lai);
			etMpinMoi.requestFocus();
		} else if (getXacNhanMPINMoi().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.mpin_new_confirm);
			etMpinMoi_re.requestFocus();
		} else if (getXacNhanMPINMoi().length() < 6) {
			sThongBao = getString(R.string.mpin_new_confirm) + " "
					+ getString(R.string.mpin_wrong0)
					+ getString(R.string.nhap_lai);
			etMpinMoi_re.requestFocus();
		} else if (!getMPINMoi().equals(getXacNhanMPINMoi())) {
			sThongBao = getString(R.string.mpin_notmatch)
					+ getString(R.string.nhap_lai);
			etMpinMoi_re.requestFocus();
		}
	}
	
	@Override
	public void cmdNextThongBao() {
		if(isFinished){
			goBack();
		}
		super.cmdNextThongBao();
	}

	public void goNext() {
		super.goNext();
		if (getMPINCu().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.mpin_old);
			etMpinCu.requestFocus();
			onCreateMyDialog(THONG_BAO).show();
		} else if (kiemTraMPIN(getMPINCu())) {
			checkData();
			if (sThongBao.equals("")) {
				threadThucThi = new ThreadThucThi(this);
				threadThucThi.setIProcess(this, (byte) 0);
				threadThucThi.Start();
			} else {
				onCreateMyDialog(THONG_BAO).show();
			}
		}
	}

	// #mark Connect
	public void processDataSend(byte iTag) {
//		String strAnswer = MPlusLib.getStrAnswerAES(User.sCustAnser, getMPINCu());
		ServiceCore.TaskChangePIN(MPlusLib.getMaDangKy(Config.DANG_KY_APP_KEY, getMPINMoi()));
	}

	public void processDataReceived(String sDataReceive, byte iTag, byte iTagErr) {
		if (sDataReceive.startsWith("val:")) {
			try {
				MPlusLib.privateClient = MPlusLib.getRSA().decryptAES(User.sprivateEncKey, getMPINCu());
			} catch (Exception e) {
			}
			
			MPlusLib.setKey(getMPINMoi());
//			User.sCustAnser = MPlusLib.getStrAnswerEnAES();
			saveUserTable();
			sThongBao = Util.insertString(getString(R.string.password_change_success), new String[]{ Util.getTimeClient3(User.getSrvTime())});
			onCreateMyDialog(THONG_BAO).show();
			isFinished = true;
		} else {
			sThongBao = Util.sCatVal(sDataReceive);
			onCreateMyDialog(THONG_BAO).show();
		}
	}
}
