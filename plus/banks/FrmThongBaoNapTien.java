package com.mpay.plus.banks;

import java.util.Vector;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

public class FrmThongBaoNapTien extends AMPlusCore implements IProcess{
	
	private Button btn_listkenhnaptien = null;
	private AutoCompleteTextView et_to_account = null;
	private EditText et_amount = null;
	private EditText et_description = null;
	private Button btn_tieptuc = null;
	public static int valueIndex = 0;
	private String sKenhNaptien = "";
	private String sIDKenhNapTien = "";

	private static Vector<AnyObject> itemKenh = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_message);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setTitle(getString(R.string.bk_naptien_thongbao));
		
		setControls();
	}
	
	private void setControls() {
		valueIndex = 0;
		btn_listkenhnaptien = (Button) findViewById(R.id.btn_listkenhnaptien);
		et_to_account = (AutoCompleteTextView) findViewById(R.id.et_to_account);
		et_amount = (EditText) findViewById(R.id.et_amount);
		et_description = (EditText) findViewById(R.id.et_description);
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		
		if(itemKenh == null){
			itemKenh = new Vector<AnyObject>();
		}
		itemKenh = getDba().getKQuestion(DBAdapter.DB_GROUP_TYPE_KENH);
		
		setEvent();
	}
	
	private void setEvent() {
		btn_listkenhnaptien.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getKenhItem().size() > 0){
					FLAG_DIALOG = 0;
					onCreateMyDialog(LIST_OTHER).show();
				}else {
					sThongBao = getString(R.string.msg_message_emptykenh);
					onCreateMyDialog(THONG_BAO).show();
				}
			}
		});
		
		et_to_account.addTextChangedListener(new TextWatcher() {
			
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
				Util.ChuanHoaMID(et_to_account, this, selection, beforesSelection);
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
		
		btn_tieptuc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
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
	
	@Override
	public void goBack() {
		btn_listkenhnaptien = null;
		et_to_account = null;
		et_amount = null;
		et_description = null;
		itemKenh = null;
		sKenhNaptien = null;
		sIDKenhNapTien = null;
		super.goBack();
	}
	
	@Override
	public void goNext() {
		if (checkData()) {
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, (byte) 0);
			threadThucThi.Start();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
		super.goNext();
	}
	
	@Override
	public void cmdChoncauhoiList(int value, AnyObject data) {
		valueIndex = value;
		setsIDKenhNapTien(data.getsID());
		setsKenhNaptien(data.getsContent());
		btn_listkenhnaptien.setText(getsKenhNaptien());
		super.cmdChoncauhoiList(value, data);
	}
	
	private boolean checkData() {
		sThongBao = "";
		if(valueIndex == 0){
			sThongBao = getString(R.string.phai_chon_kenh_naptien);
			btn_listkenhnaptien.requestFocus();
			return false;
		}if("".equals(getToAccount())){
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.cmd_sotaikhoan);
			et_to_account.requestFocus();
			return false;
		}if(getToAccount().length() < Config.iCARD_MIN_LENGTH || getToAccount().length() > Config.iCARD_MAX_LENGTH){
			sThongBao = Util.insertString(getString(R.string.cmd_sotaikhoan_wrong), new String[]{String.valueOf( Config.iCARD_MIN_LENGTH), String.valueOf(Config.iCARD_MAX_LENGTH), "<br/>"});
			et_to_account.requestFocus();
			return false;
		}if("".equals(getAmount())){
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.title_sotien_);
			et_amount.requestFocus();
			return false;
		}if (!Util.KiemTraSoTien(getAmount())) {
			sThongBao = getString(R.string.money_wrong0)
					+ getString(R.string.nhap_lai);
			et_amount.requestFocus();
		}
		return true;
	}
	
	public String getsKenhNaptien() {
		return sKenhNaptien;
	}

	public String getsIDKenhNapTien() {
		return sIDKenhNapTien;
	}

	public void setsIDKenhNapTien(String sIDKenhNapTien) {
		this.sIDKenhNapTien = sIDKenhNapTien;
	}

	public void setsKenhNaptien(String sKenhNaptien) {
		this.sKenhNaptien = sKenhNaptien;
	} 
	
	private String getToAccount() {
		return Util.keepABCAndNumber(et_to_account.getText().toString());
	}
	
	private String getAmount() {
		return Util.keepNumbersOnly(et_amount.getText().toString());
	}
	
	private String getDescription() {
		return Util.keepContent(et_description.getText().toString());
	}
	
	public static Vector<AnyObject> getKenhItem() {
		return itemKenh;
	}
	
	@Override
	public void cmdNextThongBao() {
		if(isFinished){
			goBack();
		}
		super.cmdNextThongBao();
	}

	@Override
	public void processDataSend(byte iTag) {
		ServiceCore.TaskThongBaoNapTien(getsIDKenhNapTien(), getToAccount(), getAmount(), getDescription());
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		if(dataReceived.startsWith("val:")){
			isFinished = true;
			sThongBao = getString(R.string.msg_message_success);
			onCreateMyDialog(THONG_BAO).show();
		}else {
			sThongBao = Util.sCatVal(dataReceived);
			onCreateMyDialog(THONG_BAO).show();
		}
	}
}
