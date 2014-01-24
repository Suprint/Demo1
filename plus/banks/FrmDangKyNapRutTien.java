package com.mpay.plus.banks;

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
import com.mpay.plus.util.Util;

/**
 * 
 * 
 * @author quyenlm.vn@gmail.com
 */

public class FrmDangKyNapRutTien extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmDangKyNapRutTien";
	
	private EditText etMid = null;
	private EditText etNationId = null;
	private EditText etIssueDate = null;
	private EditText etPassword = null;
	private Button btn_dangky;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_dang_ky_nap_rut_tien);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();
	}

	private void setControls() {
		try {
			etMid = (EditText) findViewById(R.id.et_mid);
			etNationId = (EditText) findViewById(R.id.et_cmnd);
			etIssueDate = (EditText) findViewById(R.id.et_issue_date);
			etPassword = (EditText) findViewById(R.id.et_password);
			btn_dangky = (Button) findViewById(R.id.btn_dangky);
			
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

	public void onDestroy() {
		etMid = null;
		etPassword = null;
//		AMPlusCore.mpConnect = null;
		super.onDestroy();
	}

	private void setEvent() {
		etMid.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaPhoneNumber(etMid, this, selection, beforesSelection);
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

		etMid.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});

		etIssueDate.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaDate(etIssueDate, this);
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
		
		etNationId.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				Util.chuanHoaTenRieng(etNationId, this);
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

		etPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.chuanHoaMatKhau(etPassword, this);
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
		
		btn_dangky.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goNext();
			}
		});
	}
	
	private String getMid() {
		return Util.keepNumbersOnly(etMid.getText().toString().trim());
	}

	private String getNationId() {
		return etNationId.getText().toString().trim();
	}

	private String getIssueDate() {
		return etIssueDate.getText().toString().trim();
	}

	private String getPass() {
		return Util.keepABCAndNumber(etPassword.getText().toString().trim());
	}
	
	private String getHashPass() {
		return MPlusLib.getRSA().hashSHA(getPass());
	}
	
	public void checkData() {
		sThongBao = "";
		if (getMid().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.mid);
			etMid.requestFocus();
		} else if (getMid().length() < Config.iTEL_MIN_LENGTH || getMid().length() > Config.iMID_MAX_LENGTH) {
			sThongBao = getString(R.string.mid_wrong0) + getString(R.string.nhap_lai);
			etMid.requestFocus();
		} else if (getNationId().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.cmnd);
			etNationId.requestFocus();
		} else if (getIssueDate().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.ngay_sinh);
			etIssueDate.requestFocus();
		} else if (getPass().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.password);
			etPassword.requestFocus();
		} else if (getPass().length() < 6) {
			sThongBao = getString(R.string.password) + " "
					+ getString(R.string.invalid)
					+ getString(R.string.nhap_lai);
			etPassword.requestFocus();
		}
	}

	@Override
	public void goNext() {
		checkData();
		if (sThongBao.equals("")) {
			onCreateMyDialog(MPIN).show();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}
	
	@Override public void cmdNextMPIN() {		
		threadThucThi = new ThreadThucThi(FrmDangKyNapRutTien.this);
		threadThucThi.setIProcess(FrmDangKyNapRutTien.this, iDANG_KY_NAP_RUT_TIEN);
		threadThucThi.Start();		
	};
	
	public void cmdNextThongBao() {
		if(isFinished)
			goBack();
	}
	
	// #mark Connect
	public void processDataSend(byte iTag) {
		try {
			ServiceCore.TaskDangKyNapRutTien(getMid(), getNationId(), getIssueDate(), getHashPass());
		} catch (Exception ex) {
		}
	}

	public void processDataReceived(final String dataReceived, byte tag, byte tagErr) {
		String sData = dataReceived;
		if (sData.startsWith("val:")) {
			isFinished = true;
			User.isRegNapTien = true;
			User.BANK_MID = getMid();
			saveUserTable();
			
			sThongBao = Util.insertString(getString(R.string.msg_dang_ky_nap_rut_tien_success), new String[]{Util.formatNumbersAsMid(getMid()), Util.getTimeClient3(User.getSrvTime())});
			onCreateMyDialog(THONG_BAO).show();
		} else {
			sThongBao = Util.sCatVal(dataReceived);
			onCreateMyDialog(THONG_BAO).show();
		}
	}
}