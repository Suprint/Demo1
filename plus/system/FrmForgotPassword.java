package com.mpay.plus.system;

import java.util.UUID;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.mplus.dialog.DialogPending;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.mplus.FrmMain;
import com.mpay.plus.util.Util;

public class FrmForgotPassword extends AMPlusCore implements IProcess {

	private EditText et_tel = null;
	private Button btn_cauhoibimat = null;
	private EditText et_cautraloibimat = null;
	private Button btn_tieptuc = null;
	private static Vector<AnyObject> itemQuestion = null;
	public static int valueIndex = 0;
	private String question = "";
	private String qesID = "0";
	private String sOTP = "";
	private ImageView imageback = null;
	private TextView textView1 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setTheme(R.style.Theme_MPay_Camera);
		setContentView(R.layout.old_registry_forgot_pass);

		setControls();
	}

	private void setControls() {
		valueIndex = 0;
		et_tel = (EditText) findViewById(R.id.et_tel);
		btn_cauhoibimat = (Button) findViewById(R.id.btn_cauhoibimat);
		et_cautraloibimat = (EditText) findViewById(R.id.et_cautraloibimat);
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		imageback = (ImageView) findViewById(R.id.imageback);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(getString(R.string.cmd_quenmatkhau).replace("?", "").toUpperCase());
		
		setData();
		
		setEvents();

		if (getQuestionItem() == null || getQuestionItem().size() <= 0) {
			User.isFisrt = true;
			new PingTask(FrmForgotPassword.this).execute();
		}
		
		if(getQuestionItem() != null && User.isRegisted){
			for (int i = 0; i < getQuestionItem().size(); i++) {
				if(getQuestionItem().get(i).getsID().equals(User.sCustQesID)){
					valueIndex = i+1;
				}
			}

			setQesID(getQuestionItem().get(valueIndex-1).getsID());
			setQuestion(getQuestionItem().get(valueIndex-1).getsContent());
			btn_cauhoibimat.setText(getQuestionItem().get(valueIndex-1).getsContent());
			et_tel.setText(User.MID);
			et_tel.setEnabled(false);
		}
	}

	private void setData() {
		if (itemQuestion == null) 
			itemQuestion = new Vector<AnyObject>();
		itemQuestion = getDba().getKQuestion(DBAdapter.DB_GROUP_TYPE_QUESTION);
	}

	private void setEvents() {
		et_tel.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				beforesSelection = before;
				selection = start;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaMID(et_tel, this, selection, beforesSelection);
			}
		});

		if(User.isRegisted == false)
			btn_cauhoibimat.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					if(getQuestionItem() == null || getQuestionItem().size() <= 0)
						setData();
					if (getQuestionItem() != null && getQuestionItem().size() > 0) {
						FLAG_DIALOG = 7;
						onCreateMyDialog(LIST_OTHER).show();
					} else {
						sThongBao = getString(R.string.msg_message_emptyquestion);
						onCreateMyDialog(THONG_BAO).show();
					}
				}
			});

		btn_tieptuc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goNext();
			}
		});
		
		imageback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FrmForgotPassword.this, FrmMain.class));
				goBack();
			}
		});
	}

	private boolean checkData() {
		sThongBao = "";
		isFinished = false;
		if (getTel().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.mobile_number);
			et_tel.requestFocus();
			return false;
		}
		if (getTel().length() < Config.iTEL_MIN_LENGTH) {
			sThongBao = getString(R.string.mobile_number_wrong0)
					+ getString(R.string.nhap_lai);
			et_tel.requestFocus();
			return false;
		}
		if ("".equals(getCauTraLoi())) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.hint_cauhoibimat);
			et_cautraloibimat.requestFocus();
			return false;
		}
		if (getCauTraLoi().length() < Config.iANSWER_ACC_MIN_LENGTH) {
			sThongBao = getString(R.string.answer_bimat_wrong);
			et_cautraloibimat.requestFocus();
			return false;
		}
		if (valueIndex == 0) {
			sThongBao = getString(R.string.confirm_empty_cauhoi);
			btn_cauhoibimat.requestFocus();
			return false;
		}
		return true;
	}

	private String getTel() {
		return Util.keepNumbersOnly(et_tel.getText().toString().trim());
	}

	private String getCauTraLoi() {
		return Util.keepContent(et_cautraloibimat.getText().toString());
	}

	public String getsOTP() {
		return sOTP;
	}

	public void setsOTP(String sOTP) {
		this.sOTP = sOTP;
	}

	private String getQuestion() {
		return question;
	}

	private void setQuestion(String question) {
		this.question = question;
	}

	private String getQesID() {
		return String.valueOf(qesID);
	}

	private void setQesID(String qesID) {
		this.qesID = qesID;
	}

	public static Vector<AnyObject> getQuestionItem() {
		return itemQuestion;
	}

	@Override
	public void cmdNextThongBao() {
		if(isFinished)
			goBack();
		super.cmdNextThongBao();
	}

	@Override
	public void cmdChoncauhoiList(int value, AnyObject data) {
		valueIndex = value;
		setQesID(data.getsID());
		setQuestion(data.getsContent());
		btn_cauhoibimat.setText(getQuestion());
		super.cmdChoncauhoiList(value, data);
	}

	@Override
	public void goBack() {
		et_tel = null;
		btn_cauhoibimat = null;
		et_cautraloibimat = null;
		btn_tieptuc = null;
		itemQuestion = null;
		question = null;
		qesID = null;
		valueIndex = 0;
		imageback = null;
		textView1 = null;
		super.goBack();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(FrmForgotPassword.this, FrmMain.class));
			goBack();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void goNext() {
		if (checkData()) {
			if (User.sIMEI == null || User.sIMEI.equals("")
					|| User.sIMEI.equalsIgnoreCase("NULL")) {
				try {
					User.sIMEI = Util.getIMEI(FrmForgotPassword.this);
				} catch (Exception ex) {
				}
				if (User.sIMEI == null || User.sIMEI.equals("") || User.sIMEI.equalsIgnoreCase("NULL"))
					User.sIMEI = UUID.randomUUID().toString();
			}
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, (byte) 0);
			threadThucThi.Start();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
		super.goNext();
	}

	@Override
	public void processDataSend(byte iTag) {
		ServiceCore.TaskQuenMatKhauCheck(getTel(), getQesID(), getCauTraLoi());
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
//		dataReceived = "val:2342<><><>";

		String data = dataReceived.split("<>", -1)[0];
		if (data.startsWith("val:")) {
			User.setSeqNos(Util.sCatVal(data));
			Intent intent = new Intent(FrmForgotPassword.this,
					FrmForgotPassword_Re.class);
			intent.putExtra("getTel", getTel());
			startActivity(intent);
			goBack();
		} else {
			sThongBao = Util.sCatVal(data);
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	private class PingTask extends AsyncTask<String, String, String> {
		private DialogPending dialogpendding;
		
		public PingTask(Context context) {
			isFinished  = false;
			dialogpendding = new DialogPending(context);
			dialogpendding.setTitles(getString(R.string.dang_xu_ly_title));
			dialogpendding.setContents(getString(R.string.dang_xu_ly));
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPreExecute() {
			dialogpendding.show();
			super.onPreExecute();
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

		@Override
		protected void onPostExecute(String result) {

			Config.itimeReceive = 70000;
			if(dialogpendding != null && dialogpendding.isShowing()){
				try {
					dialogpendding.dismiss();
				} catch (Exception e) {
				}
			}
			
			sThongBao = AMPlusCore.mpConnect.getDataReceive2();
			if(sThongBao.startsWith("err:")){
				sThongBao = sThongBao.substring(4, sThongBao.length());
				onCreateMyDialog(THONG_BAO).show();
			}
			isFinished = true;
			super.onPostExecute(result);
		}
	}
}
