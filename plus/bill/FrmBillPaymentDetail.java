package com.mpay.plus.bill;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.zxing.client.android.CaptureActivity;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.LogBillItem;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.ImageLoader;
import com.mpay.plus.util.Util;

/**
 * @author quyenlm.vn@gmail.com
 * */

public class FrmBillPaymentDetail extends AMPlusCore implements IProcess {
	public final static String TAG = "FrmBillPaymentDetail";
	
	private ImageView imgSanPham = null;
	private TextView tvSanPham = null;
	private TextView tvMoTaSanPham = null;

	public Vector<String> vecDSHoaDon = null;
	private AutoCompleteTextView etMaHoaDon = null;
	private ListView lvHoaDon = null;
	private TextView tv_hotline = null;
	private TextView tvDSHoaDon = null;
	private TextView title_ = null;
	private Spinner spinHoaDon = null;
	private String[] arrHoaDonInfo = null;
	private String[] arrLogDSHoaDon = null;
	private BillAdapter adapter = null;
	private String arrOTP[] = null;
	private String textHind = "";
	private Button btn_thanhtoan = null;
	private String billDataCode = "";
	
	private Item mItem;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bill_payment_detail);
		

		if(CaptureActivity.FLAG_DATA){
			setDataQRCode();
		}else {
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
			setControls();
		}
	}
	
	private void setDataQRCode() {
		Bundle bund = getIntent().getExtras();
		billDataCode = bund.getString(CaptureActivity.PROCESS_DATA);
		arrOTP = new String[] { "", "", "" };
		goNext();
	}
	
//	private void setDataQRCode() {
//
//		Bundle bund = getIntent().getExtras();
//		billDataCode = bund.getString(CaptureActivity.PROCESS_DATA);
//		arrOTP = new String[] { "", "", "" };
//		
//
//		String dataReceived = "val:0000172642|500000|0|mua ve xem phim|51|0|0|2465";
//		setControlsQRCode(dataReceived);
//	}
	
//	private void setControlsQRCode(String dataReceived) {
//		arrHoaDonInfo = Util.sCatVal(dataReceived).split("\\|", -1);
//		mItem = new DBAdapter(this).getItem(getPayer(), DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
//
//		showConfirm();
//	}

	private void setControls() {
		try {
		
			btn_thanhtoan = (Button) findViewById(R.id.btn_thanhtoan);
			imgSanPham = (ImageView) findViewById(R.id.img_product);
			tvSanPham = (TextView) findViewById(R.id.tv_san_pham);
			tvMoTaSanPham = (TextView) findViewById(R.id.tv_mo_ta_san_pham);

			title_ = (TextView) findViewById(R.id.title_);
			tvDSHoaDon = (TextView) findViewById(R.id.tv_ds_hoa_don);
			lvHoaDon = (ListView) findViewById(R.id.lv_content);
			tv_hotline = (TextView) findViewById(R.id.hotline);
			etMaHoaDon = (AutoCompleteTextView) findViewById(R.id.et_ma_hoa_don);
			spinHoaDon = (Spinner) findViewById(R.id.spin_ma_hoa_don);
			setData();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}

	private void setData() {
		try {
			mItem = (Item) this.getIntent().getSerializableExtra("item");
			if (mItem != null) {
				tvSanPham.setText(mItem.getTitle());
				tvMoTaSanPham.setText(mItem.getDescription());
				tvSanPham.setText(mItem.getTitle());

				if (Util.isUri(mItem.getImage())) {
					// hien thi anh tu cache
					ImageLoader imageLoader = new ImageLoader(
							FrmBillPaymentDetail.this);
					imageLoader.DisplayImage(mItem.getImage(), imgSanPham);
				} else {
					// hien thi anh tu cache
					imgSanPham.setImageDrawable(Util.getImage(
							FrmBillPaymentDetail.this, mItem.getImage()));
				}

				if (mItem.getSupplyType() == DBAdapter.DB_SUPPLIER_TYPE_CUSTOM_CODE) {
					textHind = getString(R.string.nhap) + " " + getString(R.string.ma_khach_hang)
							+ " (*)";
				} else if (mItem.getSupplyType() == DBAdapter.DB_SUPPLIER_TYPE_BILL_CODE) {
					textHind = getString(R.string.nhap) + " " + getString(R.string.so_hoa_don) + " (*)";
				} else if (mItem.getSupplyType() == DBAdapter.DB_SUPPLIER_TYPE_BOOKING_CODE) {
					textHind = getString(R.string.nhap) + " " + getString(R.string.ma_dat_cho) + " (*)";
				}
				
				etMaHoaDon.setHint(textHind);
			}

			spinHoaDon.setPrompt(textHind);
			etMaHoaDon.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

			setEvent();
			title_.setVisibility(TextView.VISIBLE);
			tvDSHoaDon.setVisibility(View.INVISIBLE);
			lvHoaDon.setVisibility(View.INVISIBLE);
			arrOTP = new String[] { "", "", "" };

			loadCache();

			// ////////////////////
			if (arrLogDSHoaDon != null) {
				etMaHoaDon.setAdapter(new ArrayAdapter<String>(this,
						R.layout.old_tvitem, arrLogDSHoaDon));
				spinHoaDon.setAdapter(new ArrayAdapter<String>(this,
						R.layout.old_tvitem, arrLogDSHoaDon));
			} else {
				spinHoaDon.setVisibility(View.INVISIBLE);
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setData", ex);
		}
	}

	private void loadCache() {
		try {
			String arrTemp[] = getCaches(6);
			if (arrTemp != null && !"".equals(arrTemp[0])) {
				String sTemp = "";
				for (int i = 0; i < arrTemp.length; ++i) {
					if (getSupplier().getItemId().equals(
							arrTemp[i].split("\\.")[0])) {
						sTemp += "|" + arrTemp[i].split("\\.")[1];
					}
				}
				if (!"".equals(sTemp)) {
					arrLogDSHoaDon = sTemp.substring(1).split("\\|");
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "loadCaches", ex);
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

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (isFinished && requestCode == 0) {
//			handlePaymentResult();
//		} else
//			super.onActivityResult(requestCode, resultCode, data);
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(!CaptureActivity.FLAG_DATA){
			if (isFinished && requestCode == 0) {
				handlePaymentResult();
			} else
				super.onActivityResult(requestCode, resultCode, data);
		}else {
			goBack();
		}
	}
	
	public void goBack() {
		super.goBack();
		imgSanPham = null;
		tvSanPham = null;
		tvMoTaSanPham = null;
		vecDSHoaDon = null;
		etMaHoaDon = null;
		lvHoaDon = null;
		tv_hotline = null;
		tvDSHoaDon = null;
		title_ = null;
		spinHoaDon = null;
		arrHoaDonInfo = null;
		arrLogDSHoaDon = null;
		adapter = null;
		arrOTP = null;
		textHind = "";
		btn_thanhtoan = null;
		billDataCode = "";
		CaptureActivity.FLAG_DATA = false;
	}

	public void onResume() {
		isAllowUpdate = false;
		super.onResume();
	}

	private void setEvent() {
		etMaHoaDon.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				Util.chuanHoaMaHoaDon(etMaHoaDon, this);
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

		etMaHoaDon.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etMaHoaDon.getWindowToken(), 0);
					return true;
				}

				return false;
			}
		});

		spinHoaDon.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				spinHoaDon.setSelection(0, true);
				etMaHoaDon.setText(arrLogDSHoaDon[0].toUpperCase());
				return false;
			}
		});

		spinHoaDon
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						etMaHoaDon.setText(arrLogDSHoaDon[position]
								.toUpperCase());
					}

					@Override
					public void onNothingSelected(AdapterView<?> parentView) {
					}
				});
		btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}

	private void checkData() {
		sThongBao = "";
		textHind = (textHind.indexOf("(*)") > 0 ? textHind.replace("(*)", "") : textHind);
		etMaHoaDon.setText(etMaHoaDon.getText().toString().toUpperCase());
		if ("".equals(getBillCodeInput())) {
			sThongBao = getString(R.string.phai_nhap) + " "	+ textHind;
		} else if (getBillCodeInput().length() < Config.iBILL_CODE_MIN_LENGTH) {
			sThongBao = Util.insertString(getString(R.string.billcode_wrong0), 
					new String[]{textHind, textHind, getString(R.string.nhap_lai)});
		}
	}
//	private String getBillCodeInput() {
//		if(CaptureActivity.FLAG_DATA){
//			return arrHoaDonInfo[6];
//		}else {
//			return Util.keepABCAndNumber(etMaHoaDon.getText().toString().trim()
//					.toUpperCase());
//		}
//	}
	private String getBillCodeInput() {
		if(CaptureActivity.FLAG_DATA){
			return getBillCodeReceived();
		}else {
			return Util.keepABCAndNumber(etMaHoaDon.getText().toString().trim()
					.toUpperCase());
		}
	}

	private int getMyAccIndex() {
		return 0;
	}

	private void setAdapterHoaDon() {
		adapter = new BillAdapter(this, 0);
		lvHoaDon.setAdapter(adapter);

	}

	public void goNext() {
		if(CaptureActivity.FLAG_DATA){
			threadThucThi = new ThreadThucThi(this);
			threadThucThi.setIProcess(this, iGET_DETAIL_BILL_QRCODE);
			threadThucThi.Start();
		}else {
			if (getBills() == null) {
				// Tra cuu hoa don
				checkData();
				if (sThongBao.equals("")) {
					threadThucThi = new ThreadThucThi(this);
					threadThucThi.setIProcess(this, iGET_DETAIL);
					threadThucThi.Start();
				} else {
					onCreateMyDialog(THONG_BAO).show();
				}
			} else {
				// Xac nhan thanh toan
				arrHoaDonInfo = getBills().elementAt(
						adapter.getSelectedRadioChoiceIndex()).split("\\|");
				showConfirm();
			}
		}
	}

	public Vector<String> getBills() {
		return vecDSHoaDon;
	}

	private Item getSupplier() {
		return mItem;
	}

	private String getSupplierFormat() {
		StringBuffer sBuf = new StringBuffer();
		sBuf.append(getSupplier().getTitle());
		sBuf.append(":");
		sBuf.append(getSupplier().getDescription());
		sBuf.append(":");
		sBuf.append(getSupplier().getSupplyType());
		sBuf.append(":");
		sBuf.append(getSupplier().getSaveType());
		return sBuf.toString();
	}

	private String getLookupCode() {
		return mItem.getItemId() + "." + getBillCodeInput();
	}

	private String getBillCodeReceived() {
		if (arrHoaDonInfo.length > 7)
			return arrHoaDonInfo[7];
		else
			return getBillCodeInput();
	}

	private String getPaymentCode() {
		if (arrHoaDonInfo.length > 7)
			return getSupplier().getItemId() + "." + getBillCodeReceived();
		else
			return getLookupCode();
	}

	private String getAmount() {
		return arrHoaDonInfo[1];
	}

	private String getPromo() {
		if ("".equals(arrHoaDonInfo[2]))
			return "0";
		return arrHoaDonInfo[2];
	}

	private String getDescription() {
		return arrHoaDonInfo[3];
	}

	private String getPayer() {
		return arrHoaDonInfo[4];
	}

	private String getInFee() {
		return arrHoaDonInfo[5];
	}

	private String getLocalStatus() {
		return arrHoaDonInfo[6];
	}

	private String getTrid() {
		return arrHoaDonInfo[0];
	}
	
	public String getEnQRCode() {
		return billDataCode;
	}

	/**
	 * Lay danh sach thong tin xac nhan thanh toan
	 * */
	private Vector<Item> getConfirmContent() {
		Vector<Item> items = new Vector<Item>();

		String sHoaDonTemp = getBillCodeReceived();
		
		items.add(new Item(getString(R.string.biller) + ": ", getSupplier().getTitle(), ""));
		items.add(new Item(conVertHint(textHind).trim() + ": ", sHoaDonTemp, ""));
		items.add(new Item(getString(R.string.payer) + ": ", getPayer(), ""));

		items.add(new Item(getString(R.string.noi_dung) + ": ", getDescription(),
				"")); 
		
		items.add(new Item(getString(R.string.title_menhgia_).replace(Config.FIELD_REQUIRED, "") + ": ", Util
				.formatNumbersAsTien(getAmount()), ""));

		if (!getPromo().equals("0"))
			items.add(new Item(getString(R.string.giam_gia) + ": ", Util
					.formatNumbersAsTien(getPromo()), ""));

		int isoTienThanhToan = Integer.parseInt(getAmount())
				- Integer.parseInt(getPromo());
		items.add(new Item(getString(R.string.so_tien_thanh_toan) + " ("
				+ getString(R.string.vnd) + "): ", Util
				.formatNumbersAsTien(String.valueOf(isoTienThanhToan)), ""));
		
		if (getInFee().equals("1") || getPromo().equals("0"))
			items.add(new Item("", getString(R.string.fee_giao_dich), ""));

		return items;
	}
	
	private String conVertHint(String hint) {
		String nhap = getString(R.string.nhap);
		return hint.indexOf(nhap) >= 0 ? hint.replace(nhap, "") : hint;
	}

	/**
	 * Hien thi form xac nhan
	 * 
	 * */
	
	public void showConfirm() {
		
		AMPlusCore.ACTION = iTHANH_TOAN_HOA_DON;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setTitle(getString(R.string.confirm_payment));
		confirm.setItems(getConfirmContent());

		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_xacnhanthanhtoanhoadon),
				"", "", "", getString(R.string.btn_co_thanhtoanngay), getString(R.string.btn_khong_desau), true, true);
		dialogConfrim(confirm, FrmBillPaymentDetail.this, FrmBillPaymentDetail.this);
	}

	private void handlePaymentResult(){
		if (isFinished) {
			if (getBills().size() < 2) {
				goBack();
			} else {
				isFinished = false;
				getBills().remove(adapter.getSelectedRadioChoiceIndex());
				adapter.reset();
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void cmdNextXacNhan() {
		if (isFinished) {
			String msg = Util.insertString(getString(R.string.bill_payment_success_sms), new String[] { getBillCodeReceived(), getSupplier().getTitle(), Util.formatNumbersAsTien(getAmount()), Util.getTimeClient3(User.getSrvTime()) });
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", msg);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivityForResult(sendIntent, 0);
			} catch (Exception ex) {
				handlePaymentResult();
				MPlusLib.debug(TAG, "setControls", ex);
			}
		}
	};

	@Override
	public void cmdBackXacNhan() {
		if(CaptureActivity.FLAG_DATA){
			goBack();
		}else {
			handlePaymentResult();
		}
	};

	private void setControlsQRCode(String dataReceived) {
		arrHoaDonInfo = Util.sCatVal(dataReceived).split("\\|", -1);
		mItem = new DBAdapter(this).getItem(getPayer(), DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
		showConfirm();
	}
	
	// #mark Connect
	public void processDataSend(byte iTag) {
		isFinished = false;
		switch (iTag) {
		
		case iGET_DETAIL_BILL_QRCODE:
			ServiceCore.TaskGetDetailBillQRCode(isPendding, String.valueOf(getMyAccIndex()), getEnQRCode());
			break;
			
		case iGET_DETAIL:
			ServiceCore.TaskGetHoaDonRemote(getLookupCode());
			break;

		case iTHANH_TOAN_HOA_DON:
			ServiceCore.TaskThanhToanHoaDon(isPendding,
					String.valueOf(getMyAccIndex()), getAmount(),
					getPromo(), getInFee(), getLocalStatus(), getDescription(),
					getPaymentCode(), arrOTP, getTrid(), getSupplierFormat(), getPayer());
			break;
		}
	}

	public void processDataReceived(final String dataReceived, byte iTag,
			byte iTagErr) {
		isPendding = false;
		isFinished = false;
		String msg = "";
		switch (iTag) {
		
		case iGET_DETAIL_BILL_QRCODE:
			if (dataReceived.startsWith("val:")) {
				// mplus:
				// val:SoTien|KhuyenMai|NoiDung|NhaCungcap|Free|Lcsts|MaHoaDon#
				setControlsQRCode(dataReceived);
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		case iGET_DETAIL:
			if (dataReceived.startsWith("val:")) {
				// mplus:
				// val:SoTien|KhuyenMai|NoiDung|NguoiTra|Free|Lcsts|MaHoaDon#SoTien|KhuyenMai|NoiDung|NguoiTra|Free|Lcsts|MaHoaDon#
				// agemt:
				// trid|sotien|khuyenmai|noidung|nguoitra|fee|lcsts#trid|sotien|khuyenmai|noidung|nguoitra|fee|lcsts#
				msg = Util.sCatVal(dataReceived);
				String arrTemp1[] = msg.split("#");
				vecDSHoaDon = new Vector<String>();
				for (int i = 0; i < arrTemp1.length; ++i) {
					getBills().add(arrTemp1[i]);
				}
				setAdapterHoaDon();

				arrHoaDonInfo = getBills().elementAt(0).split("\\|", -1);
				etMaHoaDon.setEnabled(false);
				etMaHoaDon.setFocusable(false);
				title_.setVisibility(TextView.GONE);
				tvDSHoaDon.setVisibility(View.VISIBLE);
				lvHoaDon.setVisibility(View.VISIBLE);
				tv_hotline.setVisibility(View.GONE);
				if (getBills().size() == 1) {
					showConfirm();
				}
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;

		case iTHANH_TOAN_HOA_DON:

			// update log result	
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

			saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, getDescription(), getPayer(), getAmountCost(getAmount(), getPromo(), ""));

			// Save Log Bill Payment
			LogBillItem logBill = new LogBillItem();
			logBill.setLogTime(User.getSrvTime());
			logBill.setSeqNo(User.getSeqNo());
			logBill.setAmount(getAmount());
			logBill.setBillCode(getBillCodeReceived());
			logBill.setDescription(getDescription());
			logBill.setResult(sKetQua.equals("err") ? dataReceived : sKetQua);
			logBill.setSupplierId(getSupplier().getItemId());
			getDba().insertOrUpdateBillLog(logBill);

			if (dataReceived.startsWith("val:")) {
				isFinished = true;
				msg = Util.sCatVal(dataReceived);

				// save supplier
				saveCaches("", "", "", "", getSupplierFormat(), "", "");
				if (getSupplier().getItemId().length() >= 4
						&& getSupplier().getSaveType() == 1) {
					// save bill cache
					saveCaches("", "", "", "", "", getLookupCode(), "");
				}

				sThongBao = Util.insertString(getString(R.string.tt_hoa_don_thanh_cong),
						new String[] { getBillCodeReceived(),
								getSupplier().getTitle(),
								Util.formatNumbersAsTien(getAmount()),
								Util.getTimeClient3(User.getSrvTime()) });
				
				sThongBao += "<br/>"
						+ Util.insertString(
								getString(R.string.so_du_hien_tai),
								new String[] { Util.formatNumbersAsTien(Util.sCatVal(dataReceived))});

				sThongBao += "<br/><br/>" + getString(R.string.confirm_send_sms_to_beneficiary);
				
				isFinished = true;

				setMessageConfirm(getString(R.string.title_ketquagiaodich), "", "", "",
						sThongBao, getString(R.string.btn_co_guiSMSngay), getString(R.string.btn_ketthuc), true, false);
				onCreateMyDialog(XAC_NHAN).show();
			} else if (dataReceived.startsWith("pen:")) {
				saveLogPending(User.getSeqNo(), sLenh + "|" + getSupplierFormat() + "|" + getTrid() + "|" + getPayer());
				saveLogTransaction(User.getSrvTime(), User.getSeqNo(), User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, getDescription(), getPayer(), getAmountCost(getAmount(), getPromo(), ""));
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

	private class BillAdapter extends BaseAdapter {
		private Context mContext;
		private int iSelectedIndex;
		int iLength;

		public BillAdapter(Context context, int ivIndex) {
			super();
			mContext = context;
			iSelectedIndex = ivIndex;
			iLength = getBills().size();
		}

		public void reset() {
			iSelectedIndex = 0;
			iLength = getBills().size();
		}

		public int getCount() {
			return iLength;
		}

		public int getSelectedRadioChoiceIndex() {
			return iSelectedIndex;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.old_list_item_radio, null);
			}
			
			RadioButton radio = (RadioButton) view.findViewById(R.id.rdo);
			TextView tvTitle = (TextView) view.findViewById(R.id.tvrdo_title);
			TextView tvDetail = (TextView) view.findViewById(R.id.tvrdo_detail);

			if (position < iLength) {
				String[] arrStri = getBills().elementAt(position).split("\\|");
				
				tvTitle.setText(Util.formatNumbersAsTien(arrStri[1]) + " " + getString(R.string.vnd));
				tvDetail.setText(arrStri[3] + "\n" + getString(R.string.payer) + ": " + arrStri[4]);
				
				// ////////
				if (position == iSelectedIndex)
					radio.setChecked(true);
				else
					radio.setChecked(false);
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						iSelectedIndex = position;
						arrHoaDonInfo = getBills().elementAt(iSelectedIndex)
								.split("\\|");
//						showConfirm();
						BillAdapter.this.notifyDataSetChanged();
					}
				});
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}