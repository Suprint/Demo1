package com.mpay.plus.system;

import java.util.Vector;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.MPListAdapter;
import com.mpay.MPListAdapter.MPListType;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.Item;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * 
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmSuggestions extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmSendSuggestions";
		
	private EditText etSuggestion = null;	
	private Button btnSend = null;	
	private MPListAdapter mAdapter;
	private ListView lvContent;
	private Vector<Item> mItems;
	
	private boolean isSentOk = false;	
		
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_ht_suggestion);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();
	}

	private void setControls(){
		try{			
			etSuggestion = (EditText) findViewById(R.id.et_suggestions);
			btnSend = (Button) findViewById(R.id.btn_send);
			lvContent = (ListView) findViewById(R.id.lv_content);
			
			mAdapter = new MPListAdapter(FrmSuggestions.this);
			mAdapter.setType(MPListType.SUGGGESTION_ANSWER);
			lvContent.setAdapter(mAdapter);
			
			setEvent();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
		
		try{
			//Get data
			Bundle receiveBundle = this.getIntent().getExtras();
			String data = receiveBundle.getString("data");
			
			if(data != null && !data.equals(""))
			{
				mItems = parseConversation(data);
				data = null;
				if(mItems != null)
					mAdapter.setData(getItems());
			}
		} catch (Exception ex) {
		}
	}
	
	/**
	 * Lay thong tin
	 * */
	private Vector<Item> parseConversation(String data){
//		comm1|sdtm1|answ1|astm1#comm2|sdtm2|answ2|astm2
//		Nếu answ=null thì astm=sdtm và answ=M-Pay đang xem xét ý kiến của Quý khách. Xin trân trọng cảm ơn!
//		dataReceive = "val:Them xo so vao di|12/12/2012|uk thi them|23/12/2012#Them tien vao di|12/12/2012|uk thi them tien|23/12/2012#Them toan vao di|12/12/2012||#";
	
		Vector<Item> items = null;
		int len = data.length();
		if(len > 0 && data.charAt(len - 1) == '#'){
			data = data.substring(0, len - 1);
			len = data.length();
		}
							
		if(len > 0){
			String allData[] = data.split("#");
			if(allData != null && allData.length > 0){				
				int i;
				String[] conv; //conversation
				len = allData.length;				
				
				items = new Vector<Item>();
				
				for(i = 0; i < len; i++){
					if(allData[i].length() > 0){
						conv = allData[i].split("\\|", -1);
						if(conv != null && conv.length > 3){
							Item qust = new Item();
							qust.setTitle(conv[1] + ", " + getString(R.string.me) + ":");
							qust.setDescription(conv[0]);
							qust.setIsSpecial(true);
							
							Item answ = new Item();
							answ.setIsSpecial(false);
							if(conv[2] == null || conv[2].equals("")){
								answ.setTitle(getString(R.string.mpay_name) + ":");
								answ.setDescription(getString(R.string.suggestions_pending));
							} else {
								answ.setTitle(conv[3] + ", " + getString(R.string.mpay_name) + ":");
								answ.setDescription(conv[2]);	
							}

							items.add(qust);
							items.add(answ);
						}
					}
				}
			}
		}
		
		return items;
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

			case R.id.next:
				goNext();
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
		etSuggestion = null;
		super.goBack();
	}

	private void setEvent() {
		etSuggestion.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.chuanHoaNoiDung(etSuggestion, this);
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

		etSuggestion.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSuggestion.getWindowToken(), 0);
					return true;
				}

				return false;
			}
		});
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}
	
	private Vector<Item> getItems(){
		if(mItems == null)
			mItems = new Vector<Item>();
		return mItems;
	}
	
	private String getSuggestions() {
		return etSuggestion.getText().toString().trim();
	}

	private void checkData() {
		sThongBao = "";
		if (getSuggestions().length() < Config.iSUGGESTION_MIN_LENGTH) {
			sThongBao = getString(R.string.suggestions_invalid) + getString(R.string.nhap_lai);
		}
	}

	public void goNext() {
		super.goNext();
		checkData();
		if (sThongBao.equals("")) {
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, (byte) 0);
			threadThucThi.Start();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	@Override
	public void cmdNextThongBao() {
		if(isSentOk)
			goBack();
	}
	
	// #mark Connect
	public void processDataSend(byte iTag) {
		ServiceCore.TaskSendOrReceiveSuggestion(getSuggestions());
	}

	public void processDataReceived(String sDataReceive, byte iTag, byte iTagErr) {
		if (sDataReceive.startsWith("val:")) {
//			isSentOk = true;
//			sThongBao = getString(R.string.suggestions_sent);
//			onCreateDialog(THONG_BAO).show();
//			mAdapter.setData(data);
			Item qust = new Item();
			qust.setTitle(MPlusLib.getCurrentDateString() + ", " + getString(R.string.me) + ":");
			qust.setDescription(getSuggestions());
			qust.setIsSpecial(true);
			
			Item answ = new Item();
			answ.setTitle(getString(R.string.mpay_name) + ":");
			answ.setDescription(getString(R.string.suggestions_pending));
			answ.setIsSpecial(false);
			
			getItems().insertElementAt(answ, 0);
			getItems().insertElementAt(qust, 0);
			mAdapter.setData(getItems());
			etSuggestion.setText("");
		} else {
			sThongBao = Util.sCatVal(sDataReceive);
			onCreateMyDialog(THONG_BAO).show();
		}
	}
}