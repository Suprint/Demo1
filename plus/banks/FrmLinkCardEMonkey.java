package com.mpay.plus.banks;

/**
 * 
 * @author hadvlop@gmail.com
 * 
 */


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

public class FrmLinkCardEMonkey extends AMPlusCore implements IProcess{
	
	private RelativeLayout body_account_ = null;
	private AutoCompleteTextView et_to_account_card = null;
	private TextView tv_supported = null;
	private TextView tv_supported_ = null;
	private Button btn_tieptuc = null;
	private Button btn_huylienket = null;
	private String transCode = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_link);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setControls();
	}
	
	private void setControls() {

		setTitle(getString(R.string.bk_link_card));
		et_to_account_card = (AutoCompleteTextView) findViewById(R.id.et_to_account_card);
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		btn_huylienket = (Button) findViewById(R.id.btn_huylienket);
		body_account_ = (RelativeLayout) findViewById(R.id.body_account_);
		tv_supported = (TextView) findViewById(R.id.tv_supported);
		tv_supported_ = (TextView) findViewById(R.id.tv_supported_);
		
		setData();
	}
	
	private void setData() {
		if(!"".equals(User.myAccountLink)){
			body_account_.setVisibility(RelativeLayout.VISIBLE);
			tv_supported.setVisibility(TextView.VISIBLE);
			tv_supported_.setText(Util.formatNumbersAsMid(User.myAccountLink));
		}else {
			body_account_.setVisibility(RelativeLayout.GONE);
			tv_supported.setVisibility(TextView.GONE);
			tv_supported_.setText("");
		}
		et_to_account_card.setText("");
		setEvent();
	}
	
	private void setEvent() {
		et_to_account_card.addTextChangedListener(new TextWatcher() {
			
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
				Util.ChuanHoaMID(et_to_account_card, this, selection, beforesSelection);
			}
		});
		btn_tieptuc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AMPlusCore.ACTION = L_LINK_EMONKEY;
				transCode = Config.L_LINK_CARD;
				if(checkData()){
					if("".equals(User.myAccountLink)){
						onCreateMyDialog(MPIN).show();
					}else if(getAccountCard().equals(User.myAccountLink)){
						sThongBao = getString(R.string.confirm_cardlink_emonkey);
						onCreateMyDialog(THONG_BAO).show();
					}else {
						setMessageConfirm(getString(R.string.title_xacnhangiaodich), Util.insertString(getString(R.string.confirm_cardlink_emonkey_exists), new String[]{"<br/>"}),
								"", "", "", getString(R.string.btn_dongy), getString(R.string.btn_khong_desau), false, true);
						
						onCreateMyDialog(XAC_NHAN).show();
					}
				}else {
					onCreateMyDialog(THONG_BAO).show();
				}
			}
		});
		
		btn_huylienket.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AMPlusCore.ACTION = L_LINK_EMONKEY_REMOVE;
				transCode = Config.L_LINK_CARD_REMOVE;
				onCreateMyDialog(MPIN).show();
			}
		});
	}
	
	private String getAccountCard() {
		if(AMPlusCore.ACTION == L_LINK_EMONKEY_REMOVE)
			return Util.keepABCAndNumber(User.myAccountLink);
		else return Util.keepABCAndNumber(et_to_account_card.getText().toString().trim());
	}
	
	private boolean checkData() {
		sThongBao = "";
		if("".equals(getAccountCard())){
			sThongBao = getString(R.string.phai_nhap) + " " + getString(R.string.card_number_confirm);
			et_to_account_card.requestFocus();
			return false;
		}if(getAccountCard().length() != Config.iCARD_EMONKEY_MAX_LENGTH){
			sThongBao = Util.insertString(getString(R.string.card_number_wrong), new String[]{String.valueOf(Config.iCARD_EMONKEY_MAX_LENGTH), "<br/><br/>"});
			et_to_account_card.requestFocus();
			return false;
		}
		
		return true;
	}
	
	@Override
	public void cmdNextXacNhan(String data) {
//		if(checkData()){
			goNext();
//		}else {
//			onCreateMyDialog(THONG_BAO).show();
//		}
		super.cmdNextXacNhan(data);
	}
	
	@Override
	public void cmdNextMPIN() {
		goNext();
		super.cmdNextMPIN();
	}
	
	@Override
	public void goNext() {
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, AMPlusCore.ACTION);
			threadThucThi.Start();
		super.goNext();
	}
	
	@Override
	public void goBack() {
		et_to_account_card = null;
		btn_tieptuc = null;
		btn_huylienket = null;
		tv_supported = null;
		tv_supported_ = null;
		body_account_ = null;
		super.goBack();
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
	public void processDataSend(byte iTag) {
		ServiceCore.TaskLinkCard(transCode, getAccountCard());
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {

//		dataReceived = "val:OK";
		switch (iTag) {
		case L_LINK_EMONKEY:
			if (dataReceived.startsWith("val:")) {
				User.myAccountLink = getAccountCard();
				saveUserTable();
				
				sThongBao = Util.insertString(getString(R.string.confirm_cardlink_emonkey_success), new String[]{"", Util.formatNumbersAsMid(getAccountCard())}) ;
				onCreateMyDialog(THONG_BAO).show();
				setData();
			}else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		case L_LINK_EMONKEY_REMOVE:
			if (dataReceived.startsWith("val:")) {
				sThongBao = Util.insertString(getString(R.string.confirm_cardlink_emonkey_success), new String[]{ getString(R.string.btn_huy), Util.formatNumbersAsMid(getAccountCard())}) ;
				onCreateMyDialog(THONG_BAO).show();
				
				User.myAccountLink = "";
				saveUserTable();
				setData();
			}else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;

		default:
			break;
		}

	}
}
