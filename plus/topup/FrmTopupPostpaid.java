package com.mpay.plus.topup;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.MPSupported;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.mplus.dialog.DialogFrmConfirm;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Group;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.News;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.ImageLoader;
import com.mpay.plus.util.Util;

/**
 * 
 * @author THANHNAM
 * @author quyenlm.vn@gmail.com
 */
public class FrmTopupPostpaid extends AMPlusCore implements IProcess {
	private MPSupported mAdapterSupport = null;
	private GridView grid_supported_co = null;
	private TextView tvSupportCo = null;
	private AutoCompleteTextView etToAccount = null;
	private EditText etPrice = null;
	private TextView tv_san_pham;
	private TextView tv_mo_ta_san_pham;
	private ImageView img_product;
	private Button btn_Thanhtoan;
	
	private String arrData[];
	private String[] arrLogDSTel;	
	private Item items = null;
	private ImageView btnDanhBa = null;
	
	public void goBack() {
		mAdapterSupport = null;
		grid_supported_co = null;
		tvSupportCo = null;
		etToAccount = null;
		etPrice = null;
		tv_san_pham = null;
		tv_mo_ta_san_pham = null;
		img_product = null;
		btn_Thanhtoan = null;
		arrData = null;
		arrLogDSTel = null;
		items = null;
		btnDanhBa = null;
		super.goBack();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_topup_postpaid);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();
	}
	
	private void setControls() {
		try {

			Bundle bund = getIntent().getExtras();
			items = (Item) bund.get(FrmMenuTopup.KEY_ITEM);
			
			btn_Thanhtoan = (Button) findViewById(R.id.btn_Thanhtoan);
			tv_san_pham = (TextView) findViewById(R.id.tv_san_pham);
			tv_san_pham.setText(items.getTitle());
			tv_mo_ta_san_pham = (TextView) findViewById(R.id.tv_mo_ta_san_pham);
			tv_mo_ta_san_pham.setText(items.getDescription());
			img_product = (ImageView) findViewById(R.id.img_product);
			etToAccount = (AutoCompleteTextView) findViewById(R.id.et_to_account);
			etPrice = (EditText) findViewById(R.id.et_sotien);
			tvSupportCo = (TextView) findViewById(R.id.tv_supported_co);
			btnDanhBa = (ImageView) findViewById(R.id.btn_danhba);
			
			if (Util.isUri(items.getImage())) {
				// hien thi anh tu cache
				ImageLoader imageLoader = new ImageLoader(
						FrmTopupPostpaid.this);
				imageLoader.DisplayImage(items.getImage(), img_product);
			} else {
				// hien thi anh tu cache
				img_product.setImageDrawable(Util.getImage(
						FrmTopupPostpaid.this, items.getImage()));
			}

			setEvent();
			
			arrLogDSTel = getCaches(7);
			if (arrLogDSTel != null && !"".equals(arrLogDSTel[0])) {
				etToAccount.setAdapter(new ArrayAdapter<String>(this, R.layout.old_tvitem, arrLogDSTel));
			}

			setTelco();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls",  ex);
		}
	}

	private void setTelco() {
		List<Group> groups = null;
		
		boolean flag = true;
    	while(groups == null && flag) {
    		groups = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_TOPUP);
			
	        if(groups == null){
	        	//init default menu
	        	if(User.sLang.equals(Config.LANG_EN)){
	        		saveMenuTopup(Config.DEFAULT_MENU_TOPUP[1]);
	        		saveUserTable();
	        	} else {
	        		saveMenuTopup(Config.DEFAULT_MENU_TOPUP[0]);
	        		saveUserTable();
	        	}
	        	
	        	groups = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_TOPUP);
	        	flag = false;
	        }
    	}
    	
		if (groups != null && groups.size() > 1) {
			List<Item> items = getDba().getItems(groups.get(1).getGroupId(), DBAdapter.DB_GROUP_TYPE_TOPUP);

			if (items != null) {
				if(items != null){
					// tim noi dung khuyen mai
					News adverd = null;
					for (Item item : items) {
						adverd = AMPlusCore.dbAdapter.getSpecialNews(item.getItemId());
						if(adverd != null){
							item.setDescription(adverd.getDescription());
							item.setIsSpecial(true);
						}
					}
				}
				
				mAdapterSupport = new MPSupported(FrmTopupPostpaid.this, items);
				
				grid_supported_co = (GridView) findViewById(R.id.grid_supported_co);
				grid_supported_co.setAdapter(mAdapterSupport);
				
				grid_supported_co.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// handle scroll confict with scrollviewer
						v.getParent().requestDisallowInterceptTouchEvent(true);
						return false;
					}
				});
			}
		}

		if (mAdapterSupport == null) {
			tvSupportCo.setVisibility(View.GONE);
			grid_supported_co.setVisibility(View.GONE);
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

	private void setEvent() {
		etToAccount.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaPhoneNumber(etToAccount, this, selection, beforesSelection);
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

		etPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaSoTien(etPrice, this);
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
		etToAccount.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					etToAccount.clearFocus();
					etPrice.requestFocus();

				}
				return false;
			}
		});
		etPrice.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etPrice.getWindowToken(), 0);
					return true;
				}

				return false;
			}
		});
		
		btn_Thanhtoan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
		
		btnDanhBa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pickContact();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (isFinished) {
			goBack();
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}
	
	// ////////////////////////////
	// Properties
	// ////////////////////////////
	
	private String getSaveType() {
		return "2";
	}
	
	private String getToAccount() {
		String sTel = Util.keepNumbersOnly(etToAccount.getText().toString());
		if (sTel.equals("") && arrData != null && arrData.length >= 7)
			sTel = arrData[6];
		return sTel;
	}

	private String getGroupCode(){
		return arrData[2];
	}
	
	private String getProductCode(){
		return arrData[0];
	}
	
	private String getProductName(){
		return arrData[1];
	}
	
	public String getPrice() {
		return Util.keepNumbersOnly(etPrice.getText().toString().trim());
	}

	private String getPromo() {
		return arrData[5].split(",", -1)[0];
	}

	public void setTelFromContact(String sTel) {
		etToAccount.setText(sTel);
	}

	private void checkData() {
		sThongBao = "";
		if (!getToAccount().equals("") && getToAccount().length() < Config.iTEL_MIN_LENGTH) {
			sThongBao = getString(R.string.topup_ts_tel) + " "
					+ getString(R.string.mobile_number_wrong0)
					+ getString(R.string.nhap_lai);
			etToAccount.requestFocus();
		} else if (getPrice().equals("")) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.title_sotien_);
			etPrice.requestFocus();
		} else {
			try {
				if (!Util.KiemTraSoTien(getPrice())) {
					sThongBao = getString(R.string.money_wrong0)
							+ getString(R.string.nhap_lai);
					etPrice.requestFocus();
				}
			} catch (Exception ex) {
				sThongBao = getString(R.string.amount) + " "
						+ getString(R.string.money_wrong0)
						+ getString(R.string.nhap_lai);
				etPrice.requestFocus();
			}
		}
	}

	private int getMyAccIndex() {
		return 0;
	}
	
	/**
	 * Lay danh sach thong tin xac nhan thanh toan
	 * */
	private Vector<Item> getConfirmContent() {
		Vector<Item> items = new Vector<Item>();

		items.add(new Item(getString(R.string.topup_ts_tel).replace(Config.FIELD_REQUIRED, "")  + ": ", Util
				.formatNumbersAsMid(getToAccount()), ""));
		
		items.add(new Item(getString(R.string.san_pham) + ": ", getProductName(), ""));
		
		items.add(new Item(getString(R.string.title_sotien_).replace(Config.FIELD_REQUIRED, "")  + ": ", Util
				.formatNumbersAsTien(getPrice()), ""));

		if (!getPromo().equals("0")) {
			items.add(new Item(getString(R.string.giam_gia) + ": ", Util
					.formatNumbersAsTien(getPromo()), ""));
		}

		int menhGia = Integer.parseInt(getPrice());
		int khuyenMai = Integer.parseInt(getPromo());
		int soTien = 0;
		soTien = menhGia - khuyenMai;

		items.add(new Item(getString(R.string.so_tien_thanh_toan) + " (" + getString(R.string.vnd) +  "): ", Util
				.formatNumbersAsTien(Util.keepNumbersOnly(String
						.valueOf(soTien))), ""));
		return items;
	}

	/**
	 * Hien thi form xac nhan
	 * 
	 * */
	
	public void showConfirm() {
		AMPlusCore.ACTION = iTOPUP_TRA_SAU;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setTitle(getString(R.string.confirm_topup));
		confirm.setItems(getConfirmContent());
		
		DialogFrmConfirm dialog = new DialogFrmConfirm(this, confirm);
		dialog.setIProcess(FrmTopupPostpaid.this);
		dialog.setParent(FrmTopupPostpaid.this);
		dialog.setTitles(getString(R.string.title_xacnhangiaodich));
		dialog.setMyMessage(getString(R.string.confirm_xacnhanthanhtoantrasau));
		dialog.setBtn_dongy(getString(R.string.btn_co_thanhtoanngay));
		dialog.setBtn_khongdongy(getString(R.string.btn_khong_desau));
		dialog.show();
	}


	@Override
	public void cmdNextXacNhan() {
		if (isFinished) {
			String msg = Util.insertString(
					getString(R.string.topup_ts_success_sms),
					new String[] { Util.formatNumbersAsTien(getPrice()), Util.formatNumbersAsMid(getToAccount()),
						Util.getTimeClient3(User.getSrvTime()) });
			
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", msg);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivityForResult(sendIntent, 0);
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "setControls", ex);
			}
			goBack();
		}
	};

	@Override
	public void cmdBackXacNhan() {
		if (isFinished)
			goBack();
	};
	
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
			threadThucThi.setIProcess(this, (byte) iGET_DETAIL);
			threadThucThi.Start();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	// #mark Connect
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case iGET_DETAIL:
			ServiceCore.TaskLayThongTinTopupTraSau(getToAccount(), getPrice());
			break;
			
		case iTOPUP_TRA_SAU:
//			ServiceCore.TaskTopup(isPendding, getSaveType(),
//					String.valueOf(getMyAccIndex()), getGroupCode(), getProductCode(),
//					getPrice(), getPromo(), "1", getToAccount() , User.MID);
			ServiceCore.TaskTopup(isPendding, getSaveType(), String.valueOf(getMyAccIndex()), getGroupCode(), getProductCode(), getProductName(), getPrice(), getPromo(), getToAccount(), User.MID);
			break;
		default:
			break;
		}
	}
	
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		isPendding = false;
		isFinished  =false;
		String result = Util.sCatVal(dataReceived).split("#")[0];

		switch (iTag) {
		case iGET_DETAIL:
			//pdcd|pdnm|pdgrcd|mxvl|price|promo|regTel#
			if (dataReceived.startsWith("val:")) {
				arrData = result.split("\\|", -1);
				showConfirm();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;

		case iTOPUP_TRA_SAU:

			String sKetQua = "val";
			if (dataReceived.startsWith("val")) {
				User.isActived = true;
				sKetQua = "val";
			} else if (dataReceived.startsWith("pen") || (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
				sKetQua = "";
			else
				sKetQua = dataReceived;
			
			if (!sKetQua.equals(""))
				deleteLogPending(User.getSeqNo());
			
			saveUserTable();
			saveLogTransaction(User.getSrvTime(), User.getSeqNo(), "2" + User.getTransCode(), getToAccount(), getPrice(), sKetQua, "", "", getAmountCost(getPrice(), getPromo(), ""));

			if (dataReceived.startsWith("val:")) {
				saveCaches("", "", "", "", "", "", getToAccount());

				sThongBao = Util.insertString(getString(R.string.topup_ts_success),
						new String[] { getProductName(), Util.formatNumbersAsTien(getPrice()),
								Util.formatNumbersAsMid(getToAccount()), Util.getTimeClient3(User.getSrvTime()) });
				sThongBao += "<br/>"+Util.insertString(getString(R.string.so_du_hien_tai), new String[]{ Util.formatNumbersAsTien(Util.sCatVal(dataReceived))});

				sThongBao += "<br/><br/>"
						+ getString(R.string.confirm_send_sms_to_beneficiary);
				
				isFinished = true;

				setMessageConfirm(getString(R.string.title_ketquagiaodich), "", "", "", sThongBao, 
						getString(R.string.btn_co_guiSMSngay), getString(R.string.btn_ketthuc), true, false);
				
				onCreateMyDialog(XAC_NHAN).show();
			} else if (dataReceived.startsWith("pen:")) {
				AMPlusCore.saveLogPending(User.getSeqNo(), sLenh);
				saveLogTransaction(User.getSrvTime(), User.getSeqNo(), "2" + User.getTransCode(), getToAccount(), getPrice(), sKetQua, "", "", getAmountCost(getPrice(), getPromo(), ""));
				isPendding = true;				
				// show Confirm Pedding
				onCreateMyDialog(PENDING).show();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		}
	}
}
