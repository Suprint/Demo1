package com.mpay.plus.system;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.mplus.FrmMain;
import com.mpay.plus.util.Util;

public class FrmForgotPassword_Re extends AMPlusCore implements IProcess {

	private EditText et_xacnhan_otp = null;
	private EditText et_password = null;
	private EditText et_password_confirm = null;
	private Button btn_tieptuc = null;
	private TextView textView1 = null;
	private String sTel = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setTheme(R.style.Theme_MPay_Camera);
		setContentView(R.layout.old_registry_forgot_pass_re);
		Bundle bund = getIntent().getExtras();
		sTel = bund.getString("getTel");
		setControls();
	}
	
	private void setControls() {
		et_xacnhan_otp = (EditText) findViewById(R.id.et_xacnhan_otp);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(getString(R.string.cmd_quenmatkhau).replace("?", "").toUpperCase());

		setEvents();
	}

	private void setEvents() {
		
		btn_tieptuc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		startActivity(new Intent(FrmForgotPassword_Re.this, FrmMain.class));
		goBack();
		return super.onKeyDown(keyCode, event);
	}

	private boolean checkData() {
		sThongBao = "";
		if("".equals(getOTP())){
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.actived_OTP);
			et_xacnhan_otp.requestFocus();
			return false;
		}if(getOTP().length() < Config.iPASS_MIN_LENGTH){
			sThongBao = getString(R.string.actived_OTP_wrong);
			et_xacnhan_otp.requestFocus();
			return false;
		}if (getPass().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.mpin);
			et_password.requestFocus();
			return false;
		}if (getPass().length() < 6) {
			sThongBao = getString(R.string.mpin) + " "
					+ getString(R.string.invalid)
					+ getString(R.string.nhap_lai);
			et_password.requestFocus();
			return false;
		}if (!getPass().equals(getConfirmPass())) {
			sThongBao = getString(R.string.mpin_notmatch)
					+ getString(R.string.nhap_lai);
			et_password_confirm.requestFocus();
			return false;
		}if (getConfirmPass().equals("")) {
			sThongBao = getString(R.string.phai_nhap)
					+ getString(R.string.mpin);
			et_password_confirm.requestFocus();
			return false;
		}
		return true;
	}
	
	private String getOTP() {
		return Util.keepABCAndNumber(et_xacnhan_otp.getText().toString());
	}

	private String getConfirmPass() {
		return Util.keepABCAndNumber(et_password_confirm.getText().toString()
				.trim());
	}
	
	private String getPass() {
		return Util.keepABCAndNumber(et_password.getText().toString().trim());
	}
	
	@Override
	public void cmdNextThongBao() {
		if(isFinished){
			User.isFisrt = true;
			startActivity(new Intent(FrmForgotPassword_Re.this, FrmMain.class));
			goBack();
		}
		super.cmdNextThongBao();
	}
	
	@Override
	public void goBack() {
		et_xacnhan_otp = null;
		et_password = null;
		et_password_confirm = null;
		btn_tieptuc = null;
		textView1 = null;
		sTel = null;
		super.goBack();
	}

	@Override
	public void goNext() {
			if(checkData()){
				threadThucThi = new ThreadThucThi(this);
				threadThucThi.setIProcess(this, (byte) 0);
				threadThucThi.Start();
			}else {
				onCreateMyDialog(THONG_BAO).show();
			}
		super.goNext();
	}
	
	@Override
	public void processDataSend(byte iTag) {
		ServiceCore.TaskQuenMatKhau_Re(sTel, getPass(), getOTP());
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		isFinished = false;
//			gia lap
//			dataReceived = "val:Hoang Ha|13121987|123|1";
			
			if(dataReceived.startsWith("val:")){
				
				if(!User.MID.endsWith(sTel) && !"".equals(User.MID)){
					User.sID_MENU_MAIN = "0";
					User.sID_NEWS = "0";
					User.sID_MENU_CARD = "0";
					User.sID_MENU_BILL = "0";
					User.sID_MENU_TOPUP = "0";
					User.sID_MENU_KENH = "0";
					User.sID_MENU_QUESTION = "0";
					deleteDatabase(DBAdapter.DATABASE_NAME);
				}

				User.sSMS_SERVERFONE =  ""; //Util.sCatVal(dataReceived);
				User.isRegisted = true;
				User.isActived = true;
				User.isRegNapTien = false;
				User.BANK_MID = "";
				User.sCustOTP = getOTP();
				User.MID = sTel;
				
				String dataRecei[] = Util.sCatVal(dataReceived).split("\\|", -1);

				if(dataRecei.length > 0){
					User.sCustName = dataRecei[0];
				}
				if(dataRecei.length > 1){
					User.sCustBirthday = dataRecei[1];
				}
				if(dataRecei.length > 2){
					User.sCustCMND = dataRecei[2];
				}
				if(dataRecei.length > 3){
					User.sCustQesID = dataRecei[3];
				}

				isFinished = true;
				saveUserTable();
				sThongBao = getString(R.string.actived_OTP_success);
				onCreateMyDialog(THONG_BAO).show();
			} else {
				User.setSeqNos("0");
//				User.MID = "";
				User.sprivateEncKey = "";
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
	}
}
