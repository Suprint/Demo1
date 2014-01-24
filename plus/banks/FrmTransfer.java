/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpay.plus.banks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.FunctionType;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * 
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmTransfer extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmTransfer";

	private Spinner spinMID = null;
	private AutoCompleteTextView etToAccOrCard = null;
	private EditText etAmount = null;
	private EditText etContent = null;
//	private ImageButton btnDanhBaMID;
	private ImageView btnDanhBaMID_;
	private String nguoiThuHuong = "";
	private String arrLogAcc[] = null;
	private Button btn_chuyentien = null;
	private String arrOTP[];

//	private boolean isLapGD = false;
//	private boolean isPendding = false;
	
	public void goBack() {
		spinMID = null;
		etToAccOrCard = null;
		etAmount = null;
		etContent = null;
		nguoiThuHuong = "";
		arrLogAcc = null;
		arrOTP = null;
		btnDanhBaMID_ = null;
		btn_chuyentien = null;
		super.goBack();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_transfer);

		AMPlusCore.CurFuntion = FunctionType.FUND_TRANSFER;

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		setControls();
	}

	private void setControls() {
		try {
			setTitle(getString(R.string.transfer_name));
			arrLogAcc = getCaches(1);

			etToAccOrCard = (AutoCompleteTextView) findViewById(R.id.et_beneficiary);
			spinMID = (Spinner) findViewById(R.id.spin_beneficiary);
			etAmount = (EditText) findViewById(R.id.et_amount);
			etContent = (EditText) findViewById(R.id.et_description);
			etContent.setText(this.getString(R.string.bk_ck_content_default_tranfer));
			btnDanhBaMID_ = (ImageView) findViewById(R.id.btn_cache);
			btn_chuyentien = (Button) findViewById(R.id.btn_chuyentien);

			SimpleAdapter ladapter = new SimpleAdapter(this, getData(),
					R.layout.old_ht_xemlog_item, new String[] { "name", "cmd" },
					new int[] { R.id.textview1, R.id.textview2 });
			SimpleAdapter ladapter_spin = new SimpleAdapter(this, getData(),
					R.layout.old_ht_xemlog_item, new String[] { "name", "cmd" },
					new int[] { R.id.textview1, R.id.textview2 });

			etToAccOrCard.setAdapter(ladapter);
			if (arrLogAcc != null && arrLogAcc.length > 0
					&& arrLogAcc[0].length() > 1)
				spinMID.setAdapter(ladapter_spin);
			else
				spinMID.setEnabled(false);
			etToAccOrCard.setInputType(InputType.TYPE_CLASS_PHONE);
			etAmount.setInputType(InputType.TYPE_CLASS_PHONE);
			etContent.setInputType(InputType.TYPE_CLASS_TEXT);

			InputFilter[] filterArray = new InputFilter[1];

			btnDanhBaMID_.setVisibility(View.VISIBLE);
			spinMID.setVisibility(View.INVISIBLE);
			filterArray[0] = new InputFilter.LengthFilter(getResources()
					.getInteger(R.integer.mid));
			etToAccOrCard.setFilters(filterArray);

			setEvent();

			arrOTP = new String[] { "", "", "" };
			etToAccOrCard.setText("");
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (isFinished) {
			goBack();
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}


	private void setEvent() {
		
		btnDanhBaMID_.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pickContact();
			}
		});
//		btnDanhBaMID.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				pickContact();
//			}
//		});

		spinMID.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (arrLogAcc != null && arrLogAcc.length >= 1) {
					spinMID.setSelection(0, true);
					etToAccOrCard.setText(arrLogAcc[0].split("\\:")[1].trim());
				}
				return false;
			}
		});

		spinMID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (arrLogAcc != null && arrLogAcc.length >= 1) {
					etToAccOrCard.setText(arrLogAcc[position].split("\\:")[1]
							.trim());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		etToAccOrCard.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				if (etToAccOrCard.getAdapter().getItem(pos).toString() != null
						&& etToAccOrCard.getAdapter().getItem(pos).toString()
								.length() > 1)
					etToAccOrCard.setText(etToAccOrCard.getAdapter().getItem(pos)
							.toString().split("\\,")[0].replaceAll("\\{cmd=",
							"").trim());
				etAmount.setNextFocusDownId(R.id.et_description);
				etAmount.requestFocus();
			}
		});
		etToAccOrCard.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					etAmount.setNextFocusDownId(R.id.et_description);
					etAmount.requestFocus();

				}
				return false;
			}
		});
		etAmount.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					etAmount.clearFocus();
					etContent.setNextFocusDownId(R.id.et_description);
					etContent.requestFocus();

				}
				return false;
			}
		});
		etContent.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
					return true;

				}
				return false;
			}
		});
	
		etContent.setOnFocusChangeListener(new OnFocusChangeListener(){
		    public void onFocusChange(View v, boolean hasFocus){
		        if (hasFocus && v == etContent) {
		        	if(etContent.getText().toString().equals(getString(R.string.bk_ck_content_default))) {
		        		etContent.selectAll();
		        	}
		        }
		    }
		});

		etContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.chuanHoaNoiDung(etContent, this);
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
		
		etToAccOrCard.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaMID(etToAccOrCard, this, selection, beforesSelection);
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
		etAmount.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaSoTien(etAmount, this);
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
		
		btn_chuyentien.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}

	public void setTelFromContact(String sTel) {
		etToAccOrCard.setText(Util.formatNumbersAsMid(sTel));
	}

	private String getBeneficiaryName() {
		return nguoiThuHuong;
	}

	private String getToAccountCard() {
		return Util.keepNumbersOnly(etToAccOrCard.getText().toString().trim());
	}

	private String getAmount() {
		return Util.keepNumbersOnly(etAmount.getText().toString().trim());
	}

	private String getContent() {
		return Util.keepContent(etContent.getText().toString().trim());
	}

	private String getTelToSms() {
		return "";
	}

	public int getMyAccountIndex() {
		return 0;
	}

	private List<Map<String, Object>> getData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map_row = new HashMap<String, Object>();
		if (arrLogAcc != null && arrLogAcc.length > 0
				&& arrLogAcc[0].length() > 1)
			for (int i = 0; i < arrLogAcc.length; ++i) {
				if (arrLogAcc[i].length() > 2) {
					map_row = new HashMap<String, Object>();
					map_row.put("name", arrLogAcc[i].split("\\:")[0]);
					map_row.put("cmd", arrLogAcc[i].split("\\:")[1]);
					list.add(map_row);
				}
			}
		return list;
	}

	private void checkData() {
		sThongBao = "";

		if ("".equals(getToAccountCard())) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.beneficiary_hint);
		} else if (getToAccountCard().length() < Config.iTEL_MIN_LENGTH) {
			sThongBao = getString(R.string.beneficiary_hint) + " "
					+ getString(R.string.invalid)
					+ getString(R.string.nhap_lai);
		} else if (getAmount().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.title_sotien_);
			etAmount.requestFocus();
		} else if (!Util.KiemTraSoTien(getAmount())) {
			sThongBao = getString(R.string.money_wrong0) + " " 
					+ getString(R.string.nhap_lai);
			etAmount.requestFocus();
		}
	}

	/**
	 * Lay danh sach thong tin xac nhan thanh toan
	 * */
	private Vector<Item> getConfirmContent() {
		Vector<Item> items = new Vector<Item>();

		items.add(new Item(getString(R.string.beneficiary).replace(Config.FIELD_REQUIRED, "") + ": ", Util
				.formatNumbersAsMid(getToAccountCard()), ""));

		items.add(new Item(getString(R.string.nguoi_thu_huong).replace(Config.FIELD_REQUIRED, "") + ": ",
				getBeneficiaryName(), ""));

		items.add(new Item(getString(R.string.title_sotien_).replace(Config.FIELD_REQUIRED, "")  + ": ", Util
				.formatNumbersAsTien(getAmount()), ""));

		items.add(new Item(getString(R.string.noi_dung) + ": ", getContent(),
				""));
		return items;
	}

	/**
	 * Hien thi form xac nhan
	 * 
	 * */
	
	public void showConfirm() {
		AMPlusCore.ACTION = iCHUYEN_KHOAN;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setItems(getConfirmContent());
		
		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_xacnhangiaodich),
				"", "", "", getString(R.string.btn_co_chuyenngay), getString(R.string.btn_khong_desau), true, true);
		
		dialogConfrim(confirm, FrmTransfer.this, FrmTransfer.this);
	}

	@Override
	public void cmdNextXacNhan() {
		if (isFinished) {
			String msg = Util.insertString(
					getString(R.string.transfer_success_sms),
					new String[] { Util.formatNumbersAsTien(getAmount()), getBeneficiaryName(), Util.getTimeClient3(User.getSrvTime())});
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", msg);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivityForResult(sendIntent, 0);
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "cmdNextXacNhan", ex);
			}
			goBack();
		}
	};
	
	@Override
	public void cmdBackXacNhan() {
		if (isFinished)
			goBack();
	}
	
//	@Override
//	public void cmdErrorConnect() {
//		// khi mat mang se Update trang thai giao dich loi~
//		saveLogTransaction("", User.getSeqNo(), "", "", "", "err:", "", "", "");
//		super.cmdErrorConnect();
//	}
	
	public void goNext() {
		super.goNext();
		checkData();
		if (sThongBao.equals("")) {
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, iTRA_CUU_NAME);
			threadThucThi.Start();
			// }
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	// #mark Connect
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case iTRA_CUU_NAME:
			ServiceCore.TaskTraCuuNameAgent(getToAccountCard(),
					getMyAccountIndex());
			break;

		case iCHUYEN_KHOAN:
			String sNoiDung = isThucHienLai ? (getContent() + Util.getMinute())
					: getContent();
			ServiceCore.TaskChuyenKhoanAgent(isPendding,
					Config.A_CK_AGENT, String.valueOf(getMyAccountIndex()),
					getToAccountCard(), getAmount(), sNoiDung, getTelToSms(),
					arrOTP, User.MID);
			break;
		}
	}

	public void processDataReceived(final String dataReceived, byte iTag,
			byte iTagErr) {
		isPendding = false;
		isThucHienLai = false;
		isFinished = false;
		switch (iTag) {
		case iTRA_CUU_NAME:

			if (dataReceived.startsWith("val:")) {
				nguoiThuHuong = Util.sCatVal(dataReceived);
				showConfirm();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;

		case iCHUYEN_KHOAN:

			String sKetQua = "val";
			if (dataReceived.startsWith("val")) {
				User.isActived = true;
				sKetQua = "val";
			} else if (dataReceived.startsWith("pen")
					|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
				sKetQua = "";
			else
				sKetQua = dataReceived;
			if (!sKetQua.equals(""))
				deleteLogPending(User.getSeqNo());
			saveLogTransaction(User.getSrvTime(), User.getSeqNo(),
					User.getTransCode(), User.getDstNo(), getAmount(), sKetQua,
					getContent(), getBeneficiaryName(), "");

			if (dataReceived.startsWith("val:")) {
				String balance = Util.sCatVal(dataReceived);

				saveCaches(
						getBeneficiaryName() + ":"
								+ Util.formatNumbersAsMid(getToAccountCard()),
						"", "", "", "", "", "");

				sThongBao = Util
						.insertString(
								getString(R.string.success_chuyen_khoan),
								new String[] {
										Util.formatNumbersAsTien(getAmount()), Util.formatNumbersAsMid(getToAccountCard()), Util.getTimeClient3(User.getSrvTime()) });
				
				if (!balance.equals(""))
					sThongBao += "<br/>"
							+ Util.insertString(
									getString(R.string.so_du_hien_tai),
									new String[] { Util
											.formatNumbersAsTien(balance) });

				sThongBao += "<br/><br/>" + getString(R.string.confirm_send_sms_to_beneficiary);

				isFinished = true;
				
				setMessageConfirm(getString(R.string.title_ketquagiaodich), "", "", "", 
						sThongBao, getString(R.string.btn_co_guiSMSngay), getString(R.string.btn_ketthuc), true, false);
				onCreateMyDialog(XAC_NHAN).show();
			} else if (dataReceived.startsWith("pen:")) {
				
				saveLogPending(User.getSeqNo(), sLenh + "|" + getToAccountCard());
				saveLogTransaction(User.getSrvTime(), User.getSeqNo(),
						User.getTransCode(), User.getDstNo(), getAmount(), sKetQua,
						getContent(), getBeneficiaryName(), "");
				isPendding = true;				
				// show Confirm Pedding
				onCreateMyDialog(PENDING).show();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				if (sThongBao.equals("28")) {
					isThucHienLai = true;
					sThongBao = getString(R.string.msg_lapgd);
				}
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		}
	}
}
