package com.mpay.plus.banks;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

public class FrmNapTheCao extends AMPlusCore implements IProcess{
	
	private AutoCompleteTextView et_card_cao = null;
	private AutoCompleteTextView et_amount = null;
	private Button btn_listproduct = null;
	private Button btn_tieptuc = null;
	public static int valueIndex = 0;
	private String sProDCd = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_nap_card_cao);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setTitle(getString(R.string.cmd_theCao));
		
		setControls();
	}
	
	private void setControls() {
		et_card_cao = (AutoCompleteTextView) findViewById(R.id.et_card_cao);
		et_amount = (AutoCompleteTextView) findViewById(R.id.et_amount);
		btn_listproduct = (Button) findViewById(R.id.btn_listproduct);
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		setEvent();
	}
	
	private void setEvent() {
		et_card_cao.addTextChangedListener(new TextWatcher() {
			
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
				Util.ChuanHoaMID(et_card_cao, this, selection, beforesSelection);
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
		
		btn_listproduct.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getTheNapItem().size() > 0){
					FLAG_DIALOG = 3;
					onCreateMyDialog(LIST_OTHER).show();
				}else {
					sThongBao = getString(R.string.msg_message_emptyTheCao);
					onCreateMyDialog(THONG_BAO).show();
				}
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
	
	private void showConfirm(String data) {
		AMPlusCore.ACTION = iNAP_TIEN_AGENT_ATM;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setItems(getConfirmContent(data));
		
		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_xacnhangiaodich_naptien),
				"", "", "", getString(R.string.btn_co_napngay), getString(R.string.btn_khong_desau), true, true);
		dialogConfrim(confirm, FrmNapTheCao.this, FrmNapTheCao.this);
	}
	
	private Vector<Item> getConfirmContent(String data) {
		Vector<Item> items = new Vector<Item>();
		
		String sCardetail[] = data.split("\\|", -1);

		items.add(new Item(getString(R.string.card_pin) + ": ", Util.formatNumbersAsMid(getCardPin()), ""));
		items.add(new Item(getString(R.string.title_sotien_) + ": ", Util.formatNumbersAsTien(getAmount()), ""));

		// Giảm giá
		if(!sCardetail[1].equals("")){
			items.add(new Item(getString(R.string.giam_gia) + ": ", Util.formatNumbersAsTien(sCardetail[1]), ""));
		}
		
		// Miễn phí giao dịch
		if(!sCardetail[0].equals("0")){
			items.add(new Item(getString(R.string.fee_giao_dich), "", ""));
		}
		
		return items;
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
	
	private boolean checkData() {
		sThongBao = "";
		if("".equals(getCardPin())){
			sThongBao = getString(R.string.phai_nhap) + "" + getString(R.string.card_pin);
			et_card_cao.requestFocus();
			return false;
		}if(getCardPin().length() < Config.iTEL_MIN_LENGTH || getCardPin().length() > Config.iTEL_MAX_LENGTH){
			sThongBao = Util.insertString(getString(R.string.card_pin_wrong), new String[]{ String.valueOf(Config.iTEL_MIN_LENGTH), String.valueOf(Config.iTEL_MAX_LENGTH)});
			et_card_cao.requestFocus();
			return false;
		}
		return true;
	}
	
	private String getCardPin() {
		return Util.keepABCAndNumber(et_card_cao.getText().toString());
	}
	
	private String getAmount() {
		return String.valueOf(Util.keepNumbersOnly(et_amount.getText().toString()));
	}
	
	private String getProDCd() {
		return sProDCd;
	}
	
	private void setProDCd(String sProDCd) {
		this.sProDCd = sProDCd;
	}
	
	public static Vector<AnyObject> getTheNapItem() {
		Vector<AnyObject> group = new Vector<AnyObject>();
    	List<Item> entries = AMPlusCore.dbAdapter.getItems("MOBILE", DBAdapter.DB_GROUP_TYPE_PRODUCT);
    	for (int i = 0; i < entries.size(); i++) {
    		AnyObject item = new AnyObject();
    		item.setsContent(entries.get(i).getTitle());
    		item.setsDescription(entries.get(i).getItemId());
    		group.add(item);
		}
		return group;
	}
	
	@Override
	public void cmdChoncauhoiList(int value, AnyObject data) {
		valueIndex = value;
		setProDCd(data.getsDescription());
		btn_listproduct.setText(data.getsContent());
		super.cmdChoncauhoiList(value, data);
	}
	
	@Override
	public void cmdNextConfirm() {
		AMPlusCore.ACTION = iNAP_TIEN_AGENT_ATM;
		goNext();
		super.cmdNextConfirm();
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
	public void goBack() {
		et_card_cao = null;
		et_amount = null;
		btn_listproduct = null;
		btn_tieptuc = null;
		sProDCd = "";
		super.goBack();
	}

	@Override
	public void processDataSend(byte iTag) {
		switch (iTag) {
			case iGET_DETAIL:
				ServiceCore.TaskLayThongTinThe(Config.N_BANK_TOPUP_CAO_INFO, getProDCd(), getAmount());
				break;
			case iNAP_TIEN_AGENT_ATM:
				ServiceCore.TaskNapTheCao(false, "0", getProDCd(), getAmount(), getCardPin());
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
				String data = Util.sCatVal(dataReceived);
				if(dataReceived.startsWith("val:")){
					showConfirm(data);
				}else {
					sThongBao = data;
					onCreateMyDialog(THONG_BAO).show();
				}
				break;
			case iNAP_TIEN_AGENT_ATM:
				
				if(dataReceived.startsWith("val:")){
					saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(),
							getAmount(), "val:", "", User.MID, "");
					
					sThongBao = Util.insertString(getString(R.string.success_naptien_cao), 
							new String[]{ Util.formatNumbersAsTien(getAmount()), 
								Util.formatNumbersAsMid(getCardPin()), Util.getTimeClient3(User.getSrvTime())});
					
					onCreateMyDialog(THONG_BAO).show();
					isFinished = true;
				}else if(dataReceived.startsWith("pen:")){
					saveLogPending(User.getSeqNo(), sLenh );
					saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(),
							getAmount(), "", "", User.MID, "");
					
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
