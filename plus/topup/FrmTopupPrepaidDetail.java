//package com.mpay.plus.topup;
//
//import java.util.Vector;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//import com.mpay.agent.R;
//import com.mpay.business.IProcess;
//import com.mpay.business.ServiceCore;
//import com.mpay.mplus.dialog.DialogFrmConfirm;
//import com.mpay.plus.banks.FrmTransfer;
//import com.mpay.plus.baseconnect.MPConnection;
//import com.mpay.plus.config.Config;
//import com.mpay.plus.database.ConfirmItem;
//import com.mpay.plus.database.Item;
//import com.mpay.plus.database.User;
//import com.mpay.plus.lib.MPlusLib;
//import com.mpay.plus.lib.PriceAdapter;
//import com.mpay.plus.mplus.AMPlusCore;
//import com.mpay.plus.mplus.FrmConfirm;
//import com.mpay.plus.topup.FrmMenuTopup.TopupType;
//import com.mpay.plus.util.Util;
//
///**
// * @author THANHNAM
// * @author quyenlm.vn@gmail.com
// */
//public class FrmTopupPrepaidDetail extends AMPlusCore implements IProcess {
//	public static final String TAG = "FrmTopupPrepaidDetail";
//
//	private TextView tvToAccount = null;
//	private TextView tvProduct = null;
//	private ListView lvPrice = null;
//	private TextView tvSalePrice = null;
//	private EditText etToAccount = null;
//	private EditText etMoney = null;
//	// private Spinner spinMyAcc = null;
//	private PriceAdapter adapter;
//
//	private String seqNo = "";
//
//	private String sGroupCode;
//	private String sProductCode;
//	private String sProductName;
//	private String sTel;
//	private String[] arrPrice;
//	private String[] arrPromo;
//	private String arrOTP[];
//	private boolean isPendding = false;
//
//	private TopupType iType = TopupType.MOBILE_PREPAID;
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.topup_prepaid_detail);
//
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
//
//		setControls();
//	}
//
//	private void setControls() {
//		try {
//			Bundle receiveBundle = this.getIntent().getExtras();
//			iType = (TopupType) receiveBundle.getSerializable("type");
//		} catch (Exception ex) {
//		}
//
//		try {
//			// iPriceIndex =0;
//			// spinMyAcc = (Spinner) findViewById(R.id.spin_my_mid);
//			tvProduct = (TextView) findViewById(R.id.tv_san_pham);
//			tvToAccount = (TextView) findViewById(R.id.tv_to_account);
//			etToAccount = (EditText) findViewById(R.id.et_to_account);
//			etMoney = (EditText) findViewById(R.id.et_menhgia);
//			lvPrice = (ListView) findViewById(R.id.lvcontent);
//			tvSalePrice = (TextView) findViewById(R.id.tv_gia_ban);
//			tvSalePrice.setText(getString(R.string.gia_ban) + " ("
//					+ getString(R.string.vnd) + ")");
//
//			if (iType == TopupType.GAME_PREPAID) {
//				setTitle(R.string.topup_game);
//				tvToAccount.setText(R.string.topup_game_account);
//				tvToAccount.setHint(R.string.topup_game_account_hint);
//			}
//
//			setData();
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "setControls",  ex);
//		}
//	}
//
//	private void setData() {
//		try {
//			Bundle receiveBundle = this.getIntent().getExtras();
//			sTel = receiveBundle.getString("to_acc");
//			String str = receiveBundle.getString("data");
//
//			// val: pdcd|pdnm|pdgrcd|mxvl|price|promo|regTel
//			String[] arrStr = str.split("\\|", -1);
//			sProductCode = arrStr[0];
//			sProductName = arrStr[1];
//			sGroupCode = arrStr[2];
//
//			while (arrStr[4].endsWith(",")) {
//				arrStr[4] = arrStr[4].substring(0, arrStr[4].length() - 1);
//			}
//
//			while (arrStr[5].endsWith(",")) {
//				arrStr[5] = arrStr[5].substring(0, arrStr[5].length() - 1);
//			}
//
//			arrPrice = arrStr[4].split(",", -1);
//			arrPromo = arrStr[5].split(",", -1);
//			if (getToAccount().equals("") && arrStr.length >= 7)
//				sTel = arrStr[6];
//
//			// Get sale price
//			String[] arrSale = new String[arrPrice.length];
//			long salePrice = 0;
//			long price = 0;
//			long promo = 0;
//			for (int i = 0; i < arrPrice.length; i++) {
//				try {
//					price = Long.parseLong(arrPrice[i]);
//					promo = Long.parseLong(arrPromo[i]);
//					salePrice = price - promo;
//					arrSale[i] = String.valueOf(salePrice);
//				} catch (Exception ex) {
//					arrSale[i] = arrPrice[i];
//				}
//			}
//
//			adapter = new PriceAdapter(this, arrPrice, arrSale, true, true, 0);
//			lvPrice.setAdapter(adapter);
//
//			// setSpinDataSource(spinMyAcc, User.arrMyAcountList);
//			tvProduct.setText(getProductName());
//			etToAccount.setText(getToAccount());
//			etToAccount.setFocusable(false);
//
//			arrOTP = new String[] { "", "", "" };
//			setEvent();
//			if ("".equals(arrPrice[0])) {
//				lvPrice.setVisibility(View.INVISIBLE);
//				tvSalePrice.setVisibility(View.GONE);
//			} else
//				etMoney.setVisibility(View.INVISIBLE);
//
//			seqNo = User.getNxtSeqNo();
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "setData",  ex);
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Khoi tao menu
//		getSupportMenuInflater().inflate(R.menu.next, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		boolean result = true;
//
//		if (!isBusy) {
//			// Xu ly menu
//			switch (item.getItemId()) {
//			case android.R.id.home:
//				goBack();
//				break;
//
//			case R.id.next:
//				goNext();
//				break;
//
//			default:
//				result = super.onMenuItemSelected(featureId, item);
//				break;
//			}
//		} else
//			result = false;
//
//		return result;
//	};
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (isFinished) {
//			goBack();
//		} else
//			super.onActivityResult(requestCode, resultCode, data);
//	}
//	
//	public void goBack() {
//		seqNo = null;
//		lvPrice = null;
//		tvProduct = null;
//		etToAccount = null;
//		etMoney = null;
//		// spinMyAcc = null;
//		adapter = null;
//		sGroupCode = null;
//		sProductCode = null;
//		sProductName = null;
//		arrPrice = null;
//		arrPromo = null;
//		sTel = null;
//		arrOTP = null;
//
//		super.goBack();
//	}
//
//	private void setEvent() {
//		// lvPrice.setOnItemClickListener(new AdapterView.OnItemClickListener()
//		// {
//		// public void onItemClick(AdapterView<?> parent, View v,
//		// int position, long id) {
//		// ChangeRadioImg(iPriceIndex, false);
//		// ChangeRadioImg(position, true);
//		// iPriceIndex = position;
//		//
//		// }
//		// });
//		etMoney.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				Util.ChuanHoaSoTien(etMoney, this);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//			}
//		});
//	}
//
//	public void onResume() {
//		isAllowUpdate = false;
//		super.onResume();
//	}
//
//	private int getMyAccIndex() {
//		// int i = spinMyAcc.getSelectedItemPosition();
//		// if (i < 0)
//		// i = 0;
//		// return i + 1;
//		return 0;
//	}
//
//	// private void setSpinDataSource(Spinner spin, String[] arr) {
//	// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//	// android.R.layout.simple_spinner_item, arr);
//	// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	// spin.setAdapter(adapter);
//	// }
//
//	private String getToAccount() {
//		return sTel;
//	}
//
//	private String getGroupCode() {
//		return sGroupCode;
//	}
//
//	private String getProductCode() {
//		return sProductCode;
//	}
//
//	private String getProductName() {
//		return sProductName;
//	}
//
//	private String getPrice() {
//		return (arrPrice.length == 1 && arrPrice[0].equals("")) ? Util
//				.keepNumbersOnly(etMoney.getText().toString())
//				: Util.keepNumbersOnly(arrPrice[adapter
//						.getSelectedRadioChoiceIndex()]);
//	}
//
//	private String getPromo() {
//		String sKhuyenmai = "";
//		if (arrPrice.length == 1 && arrPrice[0].equals("")) {
//			sKhuyenmai = arrPrice[0];
//			try {
//				int iKhuyenMai = Integer.parseInt(getPrice())
//						* Integer.parseInt(sKhuyenmai) / 100;
//				sKhuyenmai = String.valueOf(iKhuyenMai);
//			} catch (Exception ex) {
//			}
//		} else {
//			int i = adapter.getSelectedRadioChoiceIndex();
//			if (i < 0)
//				i = 0;
//			sKhuyenmai = arrPromo[i];
//		}
//		if ("".equals(sKhuyenmai)) {
//			sKhuyenmai = "0";
//		}
//		return sKhuyenmai;
//	}
//
//	private String getSaveType() {
//		if (iType == TopupType.GAME_PREPAID)
//			return "3";
//		return "1";
//	}
//
//	/**
//	 * Lay danh sach thong tin xac nhan thanh toan
//	 * */
//	private Vector<Item> getConfirmContent() {
//		Vector<Item> items = new Vector<Item>();
//
//		// int pos = spinMyAcc.getSelectedItemPosition();
//		// items.add(new Item(getString(R.string.from_acc_card) + ": ",
//		// User.arrMyAcountList[pos], ""));
//		if (iType == TopupType.GAME_PREPAID)
//			items.add(new Item(tvToAccount.getText().toString().replace(Config.FIELD_REQUIRED, "")  + ": ",
//					getToAccount(), ""));
//		else
//			items.add(new Item(tvToAccount.getText().toString().replace(Config.FIELD_REQUIRED, "")  + ": ", Util
//					.formatNumbersAsMid(getToAccount()), ""));
//		
//		items.add(new Item(getString(R.string.san_pham) + ": ",
//				getProductName(), ""));
//
//		items.add(new Item(getString(R.string.menh_gia).replace(Config.FIELD_REQUIRED, "")  + ": ", Util
//				.formatNumbersAsTien(getPrice()), ""));
//
//		if (!getPromo().equals("0")) {
//			items.add(new Item(getString(R.string.giam_gia) + ": ", Util
//					.formatNumbersAsTien(getPromo()), ""));
//		}
//
//		int menhGia = Integer.parseInt(getPrice());
//		int khuyenMai = Integer.parseInt(getPromo());
//		int soTien = 0;
//		soTien = menhGia - khuyenMai;
//
//		items.add(new Item(getString(R.string.so_tien_thanh_toan) + " ("
//				+ getString(R.string.vnd) + "): ", Util
//				.formatNumbersAsTien(Util.keepNumbersOnly(String
//						.valueOf(soTien))), ""));
//		return items;
//	}
//
//	/**
//	 * Hien thi form xac nhan
//	 * 
//	 * */
////	public void showConfirm() {
////		FrmConfirm.setIProcess(FrmTopupPrepaidDetail.this);
////		FrmConfirm.setParent(FrmTopupPrepaidDetail.this);
////		AMPlusCore.ACTION = iTOPUP_TRA_TRUOC;
////
////		Intent intent = new Intent(this, FrmConfirm.class);
////		ConfirmItem confirm = new ConfirmItem();
////		confirm.setTitle(getString(R.string.confirm_topup));
////		confirm.setItems(getConfirmContent());
////		intent.putExtra("confirm_info", confirm);
////		startActivity(intent);
////		
////		MOBILE_PREPAID, MOBILE_POSTPAID, GAME_PREPAID,
////	}
//	
//	public void showConfirm() {
//		AMPlusCore.ACTION = iTOPUP_TRA_TRUOC;
//		
//		ConfirmItem confirm = new ConfirmItem();
//		confirm.setTitle(getString(R.string.confirm_topup));
//		confirm.setItems(getConfirmContent());
//		
//		DialogFrmConfirm dialog = new DialogFrmConfirm(this, confirm);
//		dialog.setIProcess(FrmTopupPrepaidDetail.this);
//		dialog.setParent(FrmTopupPrepaidDetail.this);
//		dialog.setTitles(getString(R.string.title_xacnhangiaodich));
//		
//		if(iType == TopupType.MOBILE_PREPAID){
//			dialog.setMyMessage(getString(R.string.confirm_xacnhannaptientratruoc));
//		}else if(iType == TopupType.MOBILE_POSTPAID){
//			dialog.setMyMessage(getString(R.string.confirm_xacnhannaptientrasau));
//		}else {
//			dialog.setMyMessage(getString(R.string.confirm_xacnhannaptiengame));
//		}
//		dialog.setBtn_dongy(getString(R.string.btn_co_thanhtoanngay));
//		dialog.setBtn_khongdongy(getString(R.string.btn_khong_desau));
//		dialog.show();
//	}
//
//	public void goNext() {
//		checkData();
//		if (sThongBao.equals("")) {
//			// threadThucThi = new ThreadThucThi(this);
//			// threadThucThi.setIProcess(this, iGET_OTP);
//			// threadThucThi.Start();
//
//			showConfirm();
//		} else {
//			sThongBao += getString(R.string.hotline);
//			onCreateMyDialog(THONG_BAO, getIconNotice()).show();
//		}
//	}
//
//	@Override
//	public void cmdNextXacNhan() {
//		if (isFinished) {
//			
//			String msg1 = iType == TopupType.MOBILE_PREPAID ? getString(R.string.topup_prepaid_success_sms)
//					: getString(R.string.topup_game_success_sms);
//			
//			String toAccount = iType == TopupType.MOBILE_PREPAID ? Util.formatNumbersAsMid(getToAccount()) : getToAccount();
//			
//			String msg = Util.insertString(msg1,
//					new String[] {Util.formatNumbersAsTien(getPrice()), toAccount, User.getSrvTime() });
//			
//			try {
//				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//				sendIntent.putExtra("sms_body", msg);
//				sendIntent.setType("vnd.android-dir/mms-sms");
//				startActivityForResult(sendIntent, 0);
//			} catch (Exception ex) {
//				MPlusLib.debug(TAG, "setControls", ex);
//			}
//		}
//	};
//	
//	@Override
//	public void cmdBackXacNhan() {
//		if (isFinished)
//			goBack();
//	};
//	
//	private void checkData() {
//		sThongBao = "";
//		if (getPrice().equals("")) {
//			sThongBao = getString(R.string.phai_nhap) + " "
//					+ getString(R.string.menh_gia);
//		} else if (!Util.KiemTraSoTien(getPrice())) {
//			sThongBao = getString(R.string.money_wrong0)
//					+ getString(R.string.nhap_lai);
//		}
//	}
//
//	// #mark Connect
//	public void processDataSend(byte iTag) {
//		switch (iTag) {
//		case iGET_OTP:
//			// ServiceCore.TaskTraCuuNameMID(User.MID);
//			break;
//
//		case iTOPUP_TRA_TRUOC:
//
//			ServiceCore.TaskMuaHangVaTopup(isPendding, getSaveType(),
//					String.valueOf(getMyAccIndex()), getGroupCode(),
//					getProductCode(), getPrice(), getPromo(), "1",
//					getToAccount(), arrOTP, seqNo);
//
//			break;
//		}
//	}
//
//	public void processDataReceived(final String dataReceived, byte iTag,
//			byte iTagErr) {
//		isPendding = false;
//		String arrTemp[];
//
//		switch (iTag) {
//		// case iGET_OTP:
//		// if (dataReceived.startsWith("val:")) {
//		// arrTemp = dataReceived.split("!");
//		// showConfirm();
//		// } else {
//		// sThongBao = Util.sCatVal(dataReceived);
//		// if (iTag == iTHANH_TOAN) {
//		// seqNo = User.getNxtSeqNo();
//		// }
//		// onCreateDialog(THONG_BAO).show();
//		// }
//		// break;
//
//		case iTOPUP_TRA_TRUOC:
//			// update log result
//			String sKetQua = "val";
//			if (dataReceived.startsWith("val")) {
//				User.isActived = true;
//				sKetQua = "val";
//			} else if (dataReceived.startsWith("pen")
//					|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
//				sKetQua = "";
//			else
//				sKetQua = dataReceived;
//			if (!sKetQua.equals(""))
//				deleteLogPending(seqNo);
//
//			User.setSeqNo(seqNo);
//			saveUserTable();
//
//			saveLogTransaction(User.getSrvTime(), User.sSeqNo, getSaveType()
//					+ User.getTransCode(), getToAccount(), getPrice(), sKetQua,
//					"", "");
//
//			if (dataReceived.startsWith("val:")) {
//				// topup_thanh_cong
//				if (iType == TopupType.GAME_PREPAID) {
//					saveCaches("", "", "", "", "", "", getToAccount());
//				} else {
//					saveCaches("", "", "", getToAccount(), "", "", "");
//				}
//
//				arrTemp = dataReceived.split("\\|", -1);
//
//				sThongBao = Util
//						.insertString(
//								iType == TopupType.MOBILE_PREPAID ? getString(R.string.topup_prepaid_success)
//										: getString(R.string.topup_game_success),
//								new String[] { getProductName(),
//										Util.formatNumbersAsTien(getPrice()),
//										getToAccount(), User.getSrvTime() });
//
//				if (arrTemp.length >= 2)
//					sThongBao += "\n"
//							+ Util.insertString(
//									getString(R.string.so_du_hien_tai),
//									new String[] { Util
//											.formatNumbersAsTien(arrTemp[1]) });
//				sThongBao += "\n\n"
//						+ getString(R.string.confirm_send_sms_to_beneficiary);
//				
//				setResult(SUCCESS);
//				isFinished = true;
//				onCreateMyDialog(SUCCESS, getIconSuccess()).show();
//			} else if (dataReceived.startsWith("pen:")) {
//				isPendding = true;
//				// show Confirm Pedding
//				onCreateMyDialog(PENDING, getIconPending()).show();
//			} else {
//				sThongBao = Util.sCatVal(dataReceived);
//				seqNo = User.getNxtSeqNo();
//				onCreateMyDialog(THONG_BAO, getIconNotice()).show();
//			}
//			break;
//		}
//	}
//}