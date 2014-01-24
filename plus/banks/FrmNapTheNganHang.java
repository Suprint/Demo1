package com.mpay.plus.banks;

import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

public class FrmNapTheNganHang extends AMPlusCore implements IProcess{
	
	private Button btn_listtheatm = null;
	private EditText et_amount = null;
	private EditText et_description = null;
	private Button btn_tieptuc = null;
	
	public static int valueIndex = 0;
	
	private String sBankName = "";
	private String sBankID = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_nap_card);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setTitle(getString(R.string.nganhang));
		
		setControls();
	}
	
	private void setControls() {
		btn_listtheatm = (Button) findViewById(R.id.btn_listtheatm);
		et_amount = (EditText) findViewById(R.id.et_amount);
		et_description = (EditText) findViewById(R.id.et_description);
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		setEvent();
	}
	private void setEvent() {
		
		btn_listtheatm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(getAllLinkATM().size() > 0){
					FLAG_DIALOG = 2;
					onCreateMyDialog(LIST_OTHER).show();
				}else {
					sThongBao = Util.insertString(getString(R.string.msg_message_emptytheATM), new String []{"<br/>"});
					setMessageConfirm(getString(R.string.title_xacnhangiaodich), "", "", "",
							sThongBao, getString(R.string.btn_tieptuc), getString(R.string.btn_khong_desau), true, false);
					onCreateMyDialog(XAC_NHAN).show();
				}
			}
		});
		
		et_amount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				beforesSelection = before;
				selection = start;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaSoTien(et_amount, this);
			}
		});
		et_description.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				beforesSelection = before;
				selection = start;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Util.chuanHoaNoiDung(et_description, this);
			}
		});
		
		btn_tieptuc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AMPlusCore.ACTION = iGET_DETAIL;
				goNext();
			}
		});
	}
	
	private boolean checkData() {
		sThongBao = "";
		if("".equals(getCarAccount())){
			sThongBao = getString(R.string.confirm_empty_atm);
			btn_listtheatm.requestFocus();
			return false;
		}else if (getAmount().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "	+ getString(R.string.title_sotien_);
			et_amount.requestFocus();
			return false;
		} else if (!Util.KiemTraSoTien(getAmount())) {
			sThongBao = getString(R.string.money_wrong0) + " " + getString(R.string.nhap_lai);
			et_amount.requestFocus();
			return false;
		}
		
		return true;
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
	}
	
	/**
	 * Hien thi form xac nhan
	 * 
	 * */
	
	private void showConfirm(String data) {
		AMPlusCore.ACTION = iNAP_TIEN_AGENT_ATM;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setItems(getConfirmContent(data));
		
		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_xacnhangiaodich_naptien),
				"", "", "", getString(R.string.btn_co_napngay), getString(R.string.btn_khong_desau), true, true);
		
		dialogConfrim(confirm, FrmNapTheNganHang.this, FrmNapTheNganHang.this);
	}
	
	/**
	 * Lay danh sach thong tin xac nhan
	 * */
	private Vector<Item> getConfirmContent(String data) {

//		data = CARDNO|CARDNAME|BANKNAME|FEE|PROMO
//		data val:FEE|PROMO|CARDNO|CARDNAME|BANKNAME
		String atmLinkDetail[] = data.split("\\|", -1); 
		
		Vector<Item> items = new Vector<Item>();
		items.add(new Item(getString(R.string.tainganhang)+ ": ", Util.formatNumbersAsMid(atmLinkDetail[4]), ""));
		items.add(new Item(getString(R.string.fullName)+ ": ", Util.formatNumbersAsMid(atmLinkDetail[3]), ""));
		items.add(new Item(getString(R.string.msg_from)+ " " + getString(R.string.card_number_atm) + ": ", Util
				.formatNumbersAsMid(atmLinkDetail[2]), ""));
		items.add(new Item(getString(R.string.msg_to_tkvi)  + ": ", Util.formatNumbersAsMid(User.MID), ""));
		items.add(new Item(getString(R.string.title_sotien_).replace(Config.FIELD_REQUIRED, "")  + ": ", Util
				.formatNumbersAsTien(getAmount()), ""));

		// Giảm giá
		if(!"".equals(atmLinkDetail[1])){
			items.add(new Item(getString(R.string.giam_gia)+ ": ", Util.formatNumbersAsTien(atmLinkDetail[1]), ""));
		}
		if(!"".equals(getDescription())){
			items.add(new Item(getString(R.string.noi_dung) + ": ", getDescription(), ""));
		}
		
		// Miễn phí giao dịch
		if(atmLinkDetail[0].equals("0")){
			items.add(new Item(getString(R.string.fee_giao_dich)+ ": ", "" , ""));
		}else {
			items.add(new Item(getString(R.string.fee_giao_dich_)+ ": ", Util.formatNumbersAsTien(atmLinkDetail[0]) , ""));
		}
		return items;
	}
	
	private String getCarAccount() {
		return Util.keepABCAndNumber(btn_listtheatm.getText().toString());
	}
	private String getAmount() {
		return Util.keepABCAndNumber(et_amount.getText().toString());
	}
	private String getDescription() {
		return Util.keepABCAndNumber(et_description.getText().toString());
	}
	
	public static Vector<AnyObject> getAllLinkATM() {
		Vector<AnyObject> itemLinkATM = new Vector<AnyObject>();
		if(User.arrMyAcountATMLink.size() > 0){
			for (int i = 0; i < User.arrMyAcountATMLink.size(); i++) {
				String atmDetail[] = User.arrMyAcountATMLink.get(i).split("-", -1);
				AnyObject item = new AnyObject();
				item.setsID(atmDetail[0]);
				item.setsDescription(atmDetail[1]);
				item.setsContent(atmDetail[2]);
				itemLinkATM.add(item);
			}
		}
			
		return itemLinkATM;
	}
	
	private String getBankName() {
		return sBankName;
	}
	
	private String getBankID() {
		return sBankID;
	}
	
	private void setBankName(String sBankName) {
		this.sBankName = sBankName;
	}
	
	private void setBankID(String sBankID) {
		this.sBankID = sBankID;
	}
	
	
	@Override
	public void cmdChoncauhoiList(int value, AnyObject data) {
		valueIndex = value;
		setBankID(data.getsContent());
		setBankName(data.getsContent());
		btn_listtheatm.setText(getBankName());
		super.cmdChoncauhoiList(value, data);
	}
	
	@Override
	public void cmdNextXacNhan() {
		startActivity(new Intent(FrmNapTheNganHang.this, FrmLinkCardATM.class));
		super.cmdNextXacNhan();
	}
	
	@Override
	public void cmdNextConfirm() {
		goNext();
		super.cmdNextConfirm();
	}
	
	@Override
	public void goBack() {
		btn_listtheatm = null;
		et_amount = null;
		et_description = null;
		btn_tieptuc = null;
		super.goBack();
	}
	
	@Override
	public void goNext() {
		if(checkData()){
			if(AMPlusCore.ACTION == iGET_DETAIL){
				threadThucThi = new ThreadThucThi(this);
				threadThucThi.setIProcess(this, iGET_DETAIL);
				threadThucThi.Start();
			}else {
				threadThucThi = new ThreadThucThi(this);
				threadThucThi.setIProcess(this, iNAP_TIEN_AGENT_ATM);
				threadThucThi.Start();
			}
		}else {
			onCreateMyDialog(THONG_BAO).show();
		}
		super.goNext();
	}
	
	@Override
	public void processDataSend(byte iTag) {
		switch (iTag) {
			case iGET_DETAIL:
				ServiceCore.TaskLayThongTinThe(Config.N_BANK_TOPUP_INFO, getBankID(), getAmount());
				break;
			case iNAP_TIEN_AGENT_ATM:
				ServiceCore.TaskNapTheNganHang(false, "0", "", "", "", getBankID(), getAmount(), getDescription());
				break;
	
			default:
				break;
		}
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		isPendding = false;
		isFinished = false;
		
		switch (iTag) {
			case iGET_DETAIL:
	//			ga|AGID|VERS|IMEI|TIME|CARDID|AMOUNT
	//			val:CARDNO|CARDNAME|BANKNAME|FEE|PROMO
//				old val:FEE|PROMO|CARDNO|CARDNAME|BANKNAME
				
				String data = Util.sCatVal(dataReceived);
				if(dataReceived.startsWith("val:")){
					
					showConfirm(data);
				}else {
					sThongBao = data;
					onCreateMyDialog(THONG_BAO).show();
				}
				break;
			case iNAP_TIEN_AGENT_ATM:
	//			pa|AGID|VERS|IMEI|TIME|TRIF
	//			TRIF=RAS(ODNO|TRID|TRDT|OTP|CARDID|AMOUNT|NOTE)
				
				if(dataReceived.startsWith("val:")){
					
					saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(),
							getAmount(), "val:", getDescription(), User.MID, "");
					
					sThongBao = Util.insertString(getString(R.string.success_naptien_atm), 
							new String[]{ Util.formatNumbersAsTien(getAmount()), 
								Util.formatNumbersAsMid(getCarAccount()), Util.getTimeClient3(User.getSrvTime())});
					
					onCreateMyDialog(THONG_BAO).show();
					isFinished = true;
				}else if(dataReceived.startsWith("pen:")){
					saveLogPending(User.getSeqNo(), sLenh );
					saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(),
							getAmount(), "", getDescription(), User.MID, "");
					
					isPendding = true;				
					onCreateMyDialog(PENDING).show();
				}else{
					sThongBao = Util.sCatVal(dataReceived);
					onCreateMyDialog(THONG_BAO).show();
				}
				break;
				
			default:
				break;
		}
	}
}
