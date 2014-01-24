package com.mpay.plus.banks;

import java.util.Vector;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.FunctionType;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * 
 * 
 * @author quyenlm.vn@gmail.com
 */
public class FrmNapRutTien extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmNapRutTien";
	
	public static final byte NAP_TIEN = 0;
	public static final byte RUT_TIEN = 1;
	private byte iType = NAP_TIEN;
	
	private EditText etAmount = null;
	private EditText etContent = null;
	private Button btn_napruttien = null;
	
	private String arrOTP[];
	private String sFee = "";

//	private boolean isPendding = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.old_bk_nap_rut_tien);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		setControls();
	}

	private void setControls() {
		try {
			Bundle receiveBundle = this.getIntent().getExtras();
			iType = receiveBundle.getByte("type");
		} catch (Exception ex) {
		}
		
		try {
			if(iType == NAP_TIEN) {
				AMPlusCore.CurFuntion = FunctionType.NAPTIEN_AGENT;
				this.setTitle(R.string.bk_naptien_mplus);
			}
			else {
				AMPlusCore.CurFuntion = FunctionType.RUTTIEN_AGENT;
				this.setTitle(R.string.rut_tien_agent);
			}
			
			etAmount = (EditText) findViewById(R.id.et_amount);
			etContent = (EditText) findViewById(R.id.et_description);
			btn_napruttien = (Button) findViewById(R.id.btn_napruttien);
			etContent.setText(this.getString(R.string.bk_ck_content_default));

			etAmount.setInputType(InputType.TYPE_CLASS_PHONE);
			etContent.setInputType(InputType.TYPE_CLASS_TEXT);
			setEvent();
			arrOTP = new String[] { "", "", "" };
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

	public void goBack() {	
		etAmount = null;
		etContent = null;
		arrOTP = null;
		super.goBack();
	}

	private void setEvent() {

		etAmount.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					etAmount.clearFocus();
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
		
		btn_napruttien.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!User.isRegisted){
					onCreateMyDialog(DANG_KY).show();
				}else {
					goNext();
				}
			}
		});
	}

	private String getBeneficiary() {
		return User.MID;
	}

	private String getBeneficiaryName() {
		return "";
	}

	private String getAmount() {
		return Util.keepNumbersOnly(etAmount.getText().toString().trim());
	}

	private String getFee() {
		return sFee;
	}
	
	private String getContent() {
		return Util.keepContent(etContent.getText().toString().trim());
	}

	public int getMyAccountIndex() {
		return 0;
	}

	private void checkData() {
		sThongBao = "";
		if (getAmount().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.title_sotien_);
			etAmount.requestFocus();
		} else if (!Util.KiemTraSoTien(getAmount())) {
			sThongBao = getString(R.string.money_wrong0)
					+ getString(R.string.nhap_lai);
			etAmount.requestFocus();
		}
	}

	/**
	 * 
	 * Hien thi form xac nhan
	 * 
	 * */
	
	public void showConfirmNapTien() {
		AMPlusCore.ACTION = iNAP_TIEN_AGENT;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setTitle(getString(R.string.confirm_nap_tien_agent));
		
		Vector<Item> items = new Vector<Item>();
		items.add(new Item(getString(R.string.receive_account) + ": ", Util.formatNumbersAsMid(getBeneficiary()), ""));
		items.add(new Item(getString(R.string.title_sotien_) + ": ", Util.formatNumbersAsTien(getAmount()), ""));
		items.add(new Item(getString(R.string.noi_dung) + ": ", getContent(),""));				
		confirm.setItems(items);

		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_nap_tien_agent),
				"", "", "", getString(R.string.btn_co_napngay), getString(R.string.btn_khong_desau), true, true);
		dialogConfrim(confirm, FrmNapRutTien.this, FrmNapRutTien.this);
	}
	
	/**
	 * 
	 * Hien thi form xac nhan
	 * 
	 * */
	
	public void showConfirmRutTien() {
		
		AMPlusCore.ACTION = iRUT_TIEN_AGENT;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setTitle(getString(R.string.confirm_rut_tien_agent));
		
		Vector<Item> items = new Vector<Item>();
		items.add(new Item(getString(R.string.amount) + ": ", Util.formatNumbersAsTien(getAmount()), ""));
		items.add(new Item(getString(R.string.fee_giao_dich) + ": ", Util.formatNumbersAsTien(getFee()), ""));
		items.add(new Item(getString(R.string.noi_dung) + ": ", getContent(), ""));					
		confirm.setItems(items);
		
		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_xacnhangiaodich),
				"", "", "", getString(R.string.btn_co_chuyenngay), getString(R.string.btn_khong_desau), true, true);
		
		dialogConfrim(confirm, FrmNapRutTien.this, FrmNapRutTien.this);
	}
	
	@Override
	public void cmdNextThongBao() {
		if(isFinished)
			goBack();
		super.cmdNextThongBao();
	}

	public void goNext() {
		super.goNext();
		checkData();
		if (sThongBao.equals("")) {
			if (iType == NAP_TIEN) {
				showConfirmNapTien();
			} else if (iType == RUT_TIEN){
				 threadThucThi = new ThreadThucThi(FrmNapRutTien.this);
				 threadThucThi.setIProcess(this, iGET_PHI_RUT_TIEN_AGENT);
				 threadThucThi.Start();
			}			
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	// #mark Connect
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case iNAP_TIEN_AGENT:

			String sNoiDungiNAP = isThucHienLai ? (getContent() + Util.getMinute())
					: getContent();
			ServiceCore.TaskNapTien(isPendding,
					String.valueOf(getMyAccountIndex()), getAmount(),
					sNoiDungiNAP, arrOTP);
			break;
			
		case iGET_PHI_RUT_TIEN_AGENT:
			ServiceCore.TaskGetPhiRutTienAgent(getAmount());
			break;
			
		case iRUT_TIEN_AGENT:

			String sNoiDungiRUT = isThucHienLai ? (getContent() + Util.getMinute())
					: getContent();
			ServiceCore.TaskRutTien(isPendding, String.valueOf(getMyAccountIndex()), getAmount(), getFee(), sNoiDungiRUT, arrOTP);
			break;
		}
	}

	public void processDataReceived(final String dataReceived, byte iTag, byte iTagErr) {		
		isPendding = false;
		isFinished = false;
		isThucHienLai = false;

		switch (iTag) {

		case iNAP_TIEN_AGENT:
			saveUserTable();
			
			String sKetQua = "val";
			if (dataReceived.startsWith("val")) {
				sKetQua = "val";
			} else if (dataReceived.startsWith("pen")
					|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
				sKetQua = "";
			else
				sKetQua = dataReceived;
			if (!sKetQua.equals(""))
				deleteLogPending(User.getSeqNo());
			
			saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, getContent(), "", "");

			if (dataReceived.startsWith("val:")) {
				String balance = Util.sCatVal(dataReceived);

				sThongBao = Util.insertString(getString(R.string.topup_agent_success), 
						new String[] { Util.formatNumbersAsTien(getAmount()), Util.formatNumbersAsMid(User.MID) , Util.getTimeClient3(User.getSrvTime()) });

				if (!balance.equals(""))
					sThongBao += "<br/>" + Util.insertString(
									getString(R.string.so_du_hien_tai), new String[] { Util.formatNumbersAsTien(balance) });
				isFinished = true;
				onCreateMyDialog(THONG_BAO).show();			
			} else if (dataReceived.startsWith("pen:")) {
				saveLogPending(User.getSeqNo(), sLenh + "|" +  getBeneficiaryName());
				saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, getContent(), "", "");
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

		case iGET_PHI_RUT_TIEN_AGENT:

			if (dataReceived.startsWith("val:")) {
				sFee = Util.sCatVal(dataReceived);
				showConfirmRutTien();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
			
		case iRUT_TIEN_AGENT:
			saveUserTable();
			
			sKetQua = "val";
			if (dataReceived.startsWith("val")) {
				sKetQua = "val";
			} else if (dataReceived.startsWith("pen")
					|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT)){
				sKetQua = "";
			}else{
				sKetQua = dataReceived;
			}
			
			if (!sKetQua.equals("")){
				deleteLogPending(User.getSeqNo());
			}
			
			saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, getContent(), "", "");

			if (dataReceived.startsWith("val:")) {
				String balance = Util.sCatVal(dataReceived);

				sThongBao = Util.insertString(getString(R.string.rut_tien_agent_success), 
						new String[] { Util.formatNumbersAsTien(getAmount()), Util.getTimeClient3(User.getSrvTime()) });

				if (!balance.equals(""))
					sThongBao += "<br/>" + Util.insertString(
									getString(R.string.so_du_hien_tai), new String[] { Util.formatNumbersAsTien(balance) });
				isFinished = true;
				onCreateMyDialog(THONG_BAO).show();
			} else if (dataReceived.startsWith("pen:")) {
				saveLogPending(User.getSeqNo(), sLenh + "|" + getBeneficiaryName());
				saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, getContent(), "", "");
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