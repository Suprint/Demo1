package com.mpay.plus.topup;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.MPSupported;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.mplus.dialog.DialogFrmConfirm;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.AnyObject;
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
public class FrmTopupPrepaid extends AMPlusCore implements IProcess {
	public static final int TOPUP_MOBILE = 0;
	public static final String TAG = "FrmTopupPrepaid";

	private ImageView imgProduct = null;
	private ImageView imgNote = null;
	private TextView tvProduct = null;
	private TextView tvDescription = null;
	private TextView tvSupportCo = null;
	private AutoCompleteTextView etToAcc = null;
	private Button btn_chonmenhgia = null;
	private GridView grid_supported_co = null;

	private MPSupported mAdapterSupport = null;
	private Item mItem = null;
	
	private String[] arrLogDSTel = null;
	private String sTel = null;
	private String sGroupCode = null;
	private String sProductCode = null;
	private String sProductName = null;
	private ImageView btnDanhBa = null;
	
	private static String[] arrPrice;
	private static String[] arrPromo;
	
	public static int valueIndex = 0;
	
	public void goBack() {
		imgProduct = null;
		imgNote = null;
		tvProduct = null;
		tvDescription = null;
		etToAcc = null;
		tvSupportCo = null;
		btn_chonmenhgia = null;
		mItem = null;
		arrLogDSTel = null;
		sTel = null;
		sGroupCode = null;
		sProductCode = null;
		sProductName = null;
		grid_supported_co = null;
		mAdapterSupport = null;
		btnDanhBa = null;
		super.goBack();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_topup_prepaid);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		setControls();
	}

	private void setControls() {
		try {
			Bundle bund = getIntent().getExtras();
			mItem = (Item) bund.get(FrmMenuTopup.KEY_ITEM);

			btn_chonmenhgia = (Button) findViewById(R.id.btn_chonmenhgia);
			imgProduct = (ImageView) findViewById(R.id.img_product);
			imgNote = (ImageView) findViewById(R.id.img_note);
			tvProduct = (TextView) findViewById(R.id.tv_san_pham);
			tvDescription = (TextView) findViewById(R.id.tv_mo_ta_san_pham);

			etToAcc = (AutoCompleteTextView) findViewById(R.id.et_to_account);
			tvSupportCo = (TextView) findViewById(R.id.tv_supported_co);
			btnDanhBa = (ImageView) findViewById(R.id.btn_danhba);

			setEvent();

			etToAcc.setInputType(InputType.TYPE_CLASS_PHONE);
			arrLogDSTel = getCaches(4);

			setTopupData();

			if (arrLogDSTel != null && !"".equals(arrLogDSTel[0])) {
				etToAcc.setAdapter(new ArrayAdapter<String>(this,
						R.layout.old_tvitem, arrLogDSTel));
			}
			
			setTelco();

		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls",  ex);
		}
	}

	private void setTopupData() {
		try {
			if (mItem != null) {
				tvProduct.setText(mItem.getTitle());
				tvDescription.setText(mItem.getDescription());
				tvProduct.setText(mItem.getTitle());

				if (Util.isUri(mItem.getImage())) {
					// hien thi anh tu cache
					ImageLoader imageLoader = new ImageLoader(
							FrmTopupPrepaid.this);
					imageLoader.DisplayImage(mItem.getImage(), imgProduct);
				} else {
					// hien thi anh tu cache
					imgProduct.setImageDrawable(Util.getImage(
							FrmTopupPrepaid.this, mItem.getImage()));
				}

				if (mItem.getIsSpecial()) {
					// hien thi anh tu cache
					imgNote.setImageDrawable(Util.getImage(
							FrmTopupPrepaid.this, Config.ICON_HOT));
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setTopupGameData",  ex);
		}
	}
	
	private void setTelco() {
		
		List<Group> groups = null;

		boolean flag = true;
		while (groups == null && flag) {
			groups = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_TOPUP);

			if (groups == null) {
				// init default menu
				if (User.sLang.equals(Config.LANG_EN)) {
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

		if (groups != null && groups.size() > 0) {
			List<Item> items = getDba().getItems(groups.get(0).getGroupId(), DBAdapter.DB_GROUP_TYPE_TOPUP);

			if (items != null) {
				if (items != null) {
					// tim noi dung khuyen mai
					News adverd = null;
					for (Item item : items) {
						adverd = AMPlusCore.dbAdapter.getSpecialNews(item
								.getItemId());
						if (adverd != null) {
							item.setDescription(adverd.getDescription());
							item.setIsSpecial(true);
						}
					}
				}
				mAdapterSupport = new MPSupported(FrmTopupPrepaid.this, items);
				
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
			etToAcc.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					Util.ChuanHoaPhoneNumber(etToAcc, this, selection, beforesSelection);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					beforesSelection = before;
					selection = start;
				}
			});

		etToAcc.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etToAcc.getWindowToken(), 0);
					return true;
				}

				return false;
			}
		});
		
		btn_chonmenhgia.setOnClickListener(new View.OnClickListener() {
			
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

	public void setTelFromContact(String sTel) {
		etToAcc.setText(sTel);
	}

	private String getToAccount() {
		return Util.keepNumbersOnly(etToAcc.getText().toString());
	}

	private void checkData() {
		sThongBao = "";
		if (getToAccount().length() > 0
				&& (getToAccount().length() < Config.iTEL_MIN_LENGTH)) { 
			sThongBao = getString(R.string.topup_sodienthoai) + " " + getString(R.string.invalid)
					+ getString(R.string.nhap_lai);
		}
	}

	public void goNext() {
		super.goNext();
		checkData();
		if (sThongBao.equals("")) {
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, iGET_DETAIL);
			threadThucThi.Start();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}
	
	
	private void setData(String sTelephone, String strData) {
		try {
			this.sTel = sTelephone;

			// val: pdcd|pdnm|pdgrcd|mxvl|price|promo|regTel
			String[] arrStr = strData.split("\\|", -1);
			sProductCode = arrStr[0];
			sProductName = arrStr[1];
			sGroupCode = arrStr[2];

			while (arrStr[4].endsWith(",")) {
				arrStr[4] = arrStr[4].substring(0, arrStr[4].length() - 1);
			}

			while (arrStr[5].endsWith(",")) {
				arrStr[5] = arrStr[5].substring(0, arrStr[5].length() - 1);
			}

			arrPrice = arrStr[4].split(",", -1);
			arrPromo = arrStr[5].split(",", -1);
			if (getToAccount().equals("") && arrStr.length >= 7)
				sTel = arrStr[6];
			
			valueIndex = 0;
			FLAG_DIALOG = 6;
			onCreateMyDialog(LIST_OTHER).show();
			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setData",  ex);
		}
	}
	
	public static Vector<AnyObject> getPriceAndPromo(){
		Vector<AnyObject> products = new Vector<AnyObject>();
		for (int i = 0; i < arrPrice.length; i++) {
			AnyObject item = new AnyObject();
			item.setsPrice("".equals(arrPrice[i]) ? "0" : arrPrice[i]);
			item.setsPromo("".equals(arrPromo[i]) ? "0" : arrPromo[i]);
			products.add(item);
		}
		return products;
	}
	
	@Override
	public void cmdChoncauhoiList(int value, AnyObject data) {
		valueIndex = value;
		showConfirm();
		super.cmdChoncauhoiList(value, data);
	}
	
	public void showConfirm() {
		AMPlusCore.ACTION = iTOPUP_TRA_TRUOC;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setTitle(getString(R.string.confirm_topup));
		confirm.setItems(getConfirmContent());
		
		DialogFrmConfirm dialog = new DialogFrmConfirm(this, confirm);
		dialog.setIProcess(FrmTopupPrepaid.this);
		dialog.setParent(FrmTopupPrepaid.this);
		dialog.setTitles(getString(R.string.title_xacnhangiaodich));
		
		dialog.setMyMessage(getString(R.string.confirm_xacnhannaptientratruoc));
		dialog.setBtn_dongy(getString(R.string.btn_co_thanhtoanngay));
		dialog.setBtn_khongdongy(getString(R.string.btn_khong_desau));
		dialog.show();
	}
	
	private Vector<Item> getConfirmContent() {
		Vector<Item> items = new Vector<Item>();

		items.add(new Item(getString(R.string.topup_sodienthoai)  + ": ", Util
					.formatNumbersAsMid(sTel), ""));
		
		items.add(new Item(getString(R.string.san_pham) + ": ",
				getProductName(), ""));

		items.add(new Item(getString(R.string.menh_gia).replace(Config.FIELD_REQUIRED, "")  + ": ", Util
				.formatNumbersAsTien(getPrice()), ""));

		if (!getPromo().equals("0")) {
			items.add(new Item(getString(R.string.giam_gia) + ": ", Util
					.formatNumbersAsTien(getPromo()), ""));
		}

		items.add(new Item(getString(R.string.so_tien_thanh_toan) + " ("
				+ getString(R.string.vnd) + "): ", Util
				.formatNumbersAsTien(Util.keepNumbersOnly(getAmountCost(getPrice(), getPromo(), ""))), ""));
		return items;
	}
	
	private String getPrice() {	
		return Util.keepNumbersOnly(getPriceAndPromo().get(valueIndex == 0 ? valueIndex : valueIndex-1).getsPrice());
	}
	
	private String getPromo() {
		return getPriceAndPromo().get(valueIndex == 0 ? valueIndex : valueIndex-1).getsPromo();
	}
	
	private String getGroupCode() {
		return sGroupCode;
	}

	private String getProductCode() {
		return sProductCode;
	}

	private String getProductName() {
		return sProductName;
	}
	
	private String getSaveType() {
		return "1";
	}
	
	private int getMyAccIndex() {
		return 0;
	}

	@Override
	public void cmdBackXacNhan() {
		if (isFinished)
			goBack();
	}
	
	@Override
	public void cmdNextXacNhan() {
		if (isFinished) {
				String msg1 = getString(R.string.topup_prepaid_success_sms);
				String toAccount =  Util.formatNumbersAsMid(sTel);
				
				String msg = Util.insertString(msg1,
						new String[] {Util.formatNumbersAsTien(getPrice()), toAccount, Util.getTimeClient3(User.getSrvTime()) });
				
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
	}
	
//	@Override
//	public void cmdErrorConnect() {
//		// khi mat mang se Update trang thai giao dich loi~
//		saveLogTransaction("", User.getSeqNo(), "", "", "", "err:", "", "", "");
//		super.cmdErrorConnect();
//	}
	
	// #mark Connect
		public void processDataSend(byte iTag) {
				
			switch (iTag) {
			
				case iGET_DETAIL:
					ServiceCore.TaskTopUpPrepaidGet(getToAccount());
					break;
					
				case iTOPUP_TRA_TRUOC:
					// tesst 0982545486

//					ServiceCore.TaskTopup(isPendding, getSaveType(),
//							String.valueOf(getMyAccIndex()), getGroupCode(),
//							getProductCode(), getPrice(), getPromo(), "1",
//									sTel, User.MID);
					ServiceCore.TaskTopup(isPendding, getSaveType(), String.valueOf(getMyAccIndex()), getGroupCode(), getProductCode(), getProductName(), getPrice(), getPromo(), sTel, User.MID);

					break;
			}
		}

		public void processDataReceived(String sDataReceive, byte iTag, byte iTagErr) {
			isFinished = false;
			isPendding = false;
//			String arrTemp[];
			
			switch (iTag) {
				
				case iGET_DETAIL:
					if (sDataReceive.startsWith("val:")) {
						sDataReceive = Util.sCatVal(sDataReceive);
						
						setData( getToAccount(), sDataReceive.split("#")[0]);
					} else {
						sThongBao = Util.sCatVal(sDataReceive);
						onCreateMyDialog(THONG_BAO).show();
					}
					break;
					
				case iTOPUP_TRA_TRUOC:
					// update log result
					String sKetQua = "val";
					if (sDataReceive.startsWith("val")) {
						User.isActived = true;
						sKetQua = "val";
					} else if (sDataReceive.startsWith("pen")
							|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
						sKetQua = "";
					else
						sKetQua = sDataReceive;
					if (!sKetQua.equals(""))
						deleteLogPending(User.getSeqNo());

					saveUserTable();

					saveLogTransaction(User.getSrvTime(), User.getSeqNo(), getSaveType()
							+ User.getTransCode(), getToAccount(), getPrice(), sKetQua,
							"", "", getAmountCost(getPrice(), getPromo(), ""));

					if (sDataReceive.startsWith("val:")) {
						// topup_thanh_cong
						sThongBao = Util
								.insertString(getString(R.string.topup_prepaid_success),new String[] { getProductName(),
												Util.formatNumbersAsTien(getPrice()),
												"".equals(getToAccount()) ? Util.formatNumbersAsMid(User.MID) : 
													Util.formatNumbersAsMid(getToAccount()), Util.getTimeClient3(User.getSrvTime()) });

							sThongBao += "<br/>"
									+ Util.insertString(
											getString(R.string.so_du_hien_tai),
											new String[] { Util.formatNumbersAsTien(Util.sCatVal(sDataReceive))});
						
						setResult(SUCCESS);
						isFinished = true;

							sThongBao += "<br/><br/>"
								+ getString(R.string.confirm_send_sms_to_beneficiary);
							
							setMessageConfirm(getString(R.string.title_ketquagiaodich), "", "", "", sThongBao, 
									getString(R.string.btn_co_guiSMSngay), getString(R.string.btn_ketthuc), true, false);
							
							onCreateMyDialog(XAC_NHAN).show();
					} else if (sDataReceive.startsWith("pen:")) {
						AMPlusCore.saveLogPending(User.getSeqNo(), sLenh);
						saveLogTransaction(User.getSrvTime(), User.getSeqNo(), getSaveType()
								+ User.getTransCode(), getToAccount(), getPrice(), sKetQua,
								"", "", getAmountCost(getPrice(), getPromo(), ""));
						isPendding = true;
						// show Confirm Pedding
						onCreateMyDialog(PENDING).show();
					} else {
						sThongBao = Util.sCatVal(sDataReceive);
						onCreateMyDialog(THONG_BAO).show();
					}
	
					break;
			}
			
		}
}