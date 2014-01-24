package com.mpay.plus.imart;

import java.util.Vector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.AppMPlus;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.Product;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.ImageLoader;
import com.mpay.plus.util.Util;

/**
 * 
 * @author THANHNAM
 * @author quyenlm.vn@gmail.com
 * 
 */

public class FrmProductDetail extends AMPlusCore implements IProcess {
	public static final String TAG = "FrmProductDetail";

	private ImageView imgProduct = null;
	private ImageView imgNote = null;
	private TextView tvProduct = null;
	private TextView tvDescription = null;
	private TextView tv_soluong = null;

	private EditText etQuantity = null;
	private Button btnIncrease = null;
	private Button btnDecrease = null;

	private Button btn_listAmount;

	private String sProductName;
	private String sProductCode;
	private String sGroupCode;// 01:topup 02:the nap
	private String sMaxVolumn;
	private int iMaxVolumn;
	public static int valueIndex = 0;

	private static String[] arrPrice;
	private static String[] arrPromo;

	private String sPromoDialog = "";
	private String sPriceDialog = "";
//	private String sPromo = "";
	
	private Button btn_muathe;

	private Item mItem;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_imart_product_detail);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setControls();
	}

	/**
	 * Khoi tao cac dieu khien
	 * */
	private void setControls() {
		try {
			btn_muathe = (Button) findViewById(R.id.btn_muathe);
			imgProduct = (ImageView) findViewById(R.id.img_product);
			imgNote = (ImageView) findViewById(R.id.img_note);
			tvProduct = (TextView) findViewById(R.id.tv_san_pham);
			tvDescription = (TextView) findViewById(R.id.tv_mo_ta_san_pham);

			etQuantity = (EditText) findViewById(R.id.et_soluong);
			btnIncrease = (Button) findViewById(R.id.btn_increase);
			btnDecrease = (Button) findViewById(R.id.btn_decrease);
			tv_soluong = (TextView) findViewById(R.id.tv_soluong); 
			
			btn_listAmount = (Button) findViewById(R.id.btn_listAmount);

			setData();
			
			btnIncrease.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					setQuantity(true);
				}
			});
			
			btnDecrease.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					setQuantity(false);
				}
			});
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}
	
	private void setData() {
		try {
			String sData = this.getIntent().getExtras().getString("value");
			mItem = (Item) this.getIntent().getSerializableExtra("item");
			if (mItem != null) {
				tvProduct.setText(mItem.getTitle());
				tvDescription.setText(mItem.getDescription());
				tvProduct.setText(mItem.getTitle());

				if (Util.isUri(mItem.getImage())) {
					// hien thi anh tu cache
					ImageLoader imageLoader = new ImageLoader(
							FrmProductDetail.this);
					imageLoader.DisplayImage(mItem.getImage(), imgProduct);
				} else {
					// hien thi anh tu cache
					imgProduct.setImageDrawable(Util.getImage(
							FrmProductDetail.this, mItem.getImage()));
				}
				
				if(mItem.getIsSpecial()) {
					// hien thi anh tu cache
					imgNote.setImageDrawable(Util.getImage(FrmProductDetail.this, Config.ICON_HOT));
				}
			}
			String[] arrStr = sData.split("\\|", -1);
			sProductCode = arrStr[0];
			sProductName = arrStr[1];
			sGroupCode = arrStr[2];
			sMaxVolumn = arrStr[3];
			
			try{
				iMaxVolumn = Integer.parseInt(sMaxVolumn);
			} catch (Exception e) {
			}
			
			while (arrStr[4].endsWith(",")) {
				arrStr[4] = arrStr[4].substring(0, arrStr[4].length() - 1);
			}

			while (arrStr[5].endsWith(",")) {
				arrStr[5] = arrStr[5].substring(0, arrStr[5].length() - 1);
			}

			arrPrice = arrStr[4].split(",", -1);
			arrPromo = arrStr[5].split(",", -1);
			
			setsPriceDialog(getPriceAndPromo().get(0).getsPrice());
			btn_listAmount.setText(Util.formatNumbersAsTien(getAmount()));
				
			if (sMaxVolumn.equals("1") || sMaxVolumn.equals("0")
					|| sMaxVolumn.equals("")) {
				etQuantity.setEnabled(false);
				etQuantity.setFocusable(false);
			}

			etQuantity.setInputType(InputType.TYPE_CLASS_PHONE);
			etQuantity.setText("1");
			etQuantity.setSelection(etQuantity.getText().length());
			tv_soluong.setText("(" + getString(R.string.max) + " " + sMaxVolumn + ")   ");

			setEvent();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setData", ex);
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

	private void setQuantity(boolean isIncrease){
		try{
			int vl = Integer.parseInt(getQuantity());			
			
			if(isIncrease && vl < iMaxVolumn) {
				etQuantity.setText(String.valueOf(vl + 1));
			} else if(!isIncrease && vl > 1) {
				etQuantity.setText(String.valueOf(vl - 1));
			}else {
				Toast.makeText(FrmProductDetail.this, getString(R.string.so_luong) + " ( "
								+ getString(R.string.max) + " " + sMaxVolumn + " )"
											+ Config.FIELD_REQUIRED, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {}
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

	@Override
	public void goBack() {
		etQuantity = null;
		sProductName = null;
		sProductCode = null;
		sGroupCode = null;// 01:topup 02:the nap
		sMaxVolumn = null;
		arrPromo = null;
		arrPrice = null;
		mProduct = null;
		mItem = null;
		super.goBack();
	}

	public void onResume() {
		isAllowUpdate = false;
		super.onResume();
	}

	private void setEvent() {
		
		etQuantity.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				int vl = 1;
				boolean flag = false;
				try{
					vl = (int) (Float.parseFloat(etQuantity.getText().toString()));
					
					if(vl <= 0)
					{
						vl = 1;
						flag = true;
					}
					else if(vl > iMaxVolumn) {
						vl = iMaxVolumn;
						flag = true;
					}
				} catch (Exception ex){
				}
				
				if(flag){
					etQuantity.removeTextChangedListener(this);
					etQuantity.setText(String.valueOf(vl));
					etQuantity.setSelection(etQuantity.getText().toString().length());
					etQuantity.addTextChangedListener(this);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});
		
		etQuantity.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});

		btn_listAmount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getPriceAndPromo().size() > 0){
					FLAG_DIALOG = 4;
					onCreateMyDialog(LIST_OTHER).show();
				}else {
					sThongBao = getString(R.string.msg_message_emptymenhgia);
					onCreateMyDialog(THONG_BAO).show();
				}
			}
		});
		
		btn_muathe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goNext();
			}
		});
	}

	private boolean checkData() {
		sThongBao = "";
		if("".equals(getQuantity())){
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.so_luong);
			etQuantity.requestFocus();
			return false;
		}
		if("".equals(getAmount())){
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.menh_gia);
			btn_listAmount.requestFocus();
			return false;
		}else if (getAmount().length() > 1) {
			try {
				if (!Util.KiemTraSoTien(getAmount())) {
					sThongBao = getString(R.string.money_wrong0)
							+ getString(R.string.nhap_lai);
					btn_listAmount.requestFocus();
					return false;
				}
			} catch (Exception e) {
				sThongBao = getString(R.string.money_wrong0)
						+ getString(R.string.nhap_lai);
				btn_listAmount.requestFocus();
				return false;
			}
		}
		
		return true;
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

	private String getQuantity() {
//		if (arrPrice[0].equals("")) {
//			return "1";
//		} else
			return Util.keepNumbersOnly(etQuantity.getText().toString());
	}

	private String getAmount() {
		return Util.keepNumbersOnly(getsPriceDialog());
	}

	private String getPromo() {
		return getPriceAndPromo().get(valueIndex == 0 ? valueIndex : valueIndex-1).getsPromo();
	}
	
//	private String getPromo() {
//		String sKhuyenmai = "";
//		if (arrPrice.length == 1 && arrPrice[0].equals("")) {
//			sKhuyenmai = arrPrice[0];
//			try {
//				int iKhuyenMai = Integer.parseInt(getAmount())
//						* Integer.parseInt(sKhuyenmai) / 100;
//				sKhuyenmai = String.valueOf(iKhuyenMai);
//			} catch (Exception ex) {
//			}
//			get
//		} else {
//			int i = indexCurrent;
//			if (i < 0)
//				i = 0;
//			sKhuyenmai = arrPromo[i];
//		}
//		if ("".equals(sKhuyenmai)) {
//			sKhuyenmai = "0";
//		}
//		return sKhuyenmai;
//	}
	
	public String getsPromoDialog() {
		return sPromoDialog;
	}

	public void setsPromoDialog(String sPromoDialog) {
		this.sPromoDialog = sPromoDialog;
	}

	public String getsPriceDialog() {
		return sPriceDialog;
	}

	public void setsPriceDialog(String sPriceDialog) {
		this.sPriceDialog = sPriceDialog;
	}

	private int getMyAccIndex() {
		return 0;
	}
	
	@Override
	public void cmdChoncauhoiList(int value, AnyObject data) {
		valueIndex = value;
		setsPriceDialog(data.getsPrice());
		setsPromoDialog(data.getsPromo());
		btn_listAmount.setText(Util.formatNumbersAsTien(getsPriceDialog()));
		super.cmdChoncauhoiList(value, data);
	}

	/**
	 * Lay danh sach thong tin xac nhan thanh toan
	 * */
	private Vector<Item> getConfirmContent() {
		Vector<Item> items = new Vector<Item>();

		items.add(new Item(getString(R.string.san_pham) + ": ",
				getProductName(), ""));
		items.add(new Item(getString(R.string.menh_gia).replace(Config.FIELD_REQUIRED, "") + ": ", Util
				.formatNumbersAsTien(getAmount()), ""));

		if (!getPromo().equals("")) {
			items.add(new Item(getString(R.string.giam_gia) + ": ",
					((!getsPriceDialog().equals("")) ? Util
							.formatNumbersAsTien(getPromo())
							: (getPromo() + " %")), ""));
		}

		items.add(new Item(getString(R.string.so_luong) + ": ", getQuantity(),	""));
		items.add(new Item(getString(R.string.so_tien_thanh_toan) + " (" + getString(R.string.vnd) +  "): ", Util
				.formatNumbersAsTien(Util.keepNumbersOnly(getAmountCost(getAmount(), getPromo(), getQuantity()))), ""));

		return items;
	}

	/**
	 * Hien thi form xac nhan
	 * 
	 * */
	
	public void showConfirm() {
		AMPlusCore.ACTION = iMUA_HANG;
		
		ConfirmItem confirm = new ConfirmItem();
		confirm.setTitle(getString(R.string.confirm_purchase));
		confirm.setItems(getConfirmContent());
		
		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.confirm_xacnhanmuathe), 
				"", "", "", getString(R.string.btn_co_muangay), getString(R.string.btn_khong_desau), true, true);
		dialogConfrim(confirm, FrmProductDetail.this, FrmProductDetail.this);
	}


	@Override
	public void goNext() {
		// super.goNext();
		if(checkData()){
			showConfirm();
		} else {
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	public void cancelNotification(Context ctx, int notifyId) {
		try {
			// Xoa notification
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
			nMgr.cancel(notifyId);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "cancelNotification - cancel notification", ex);
		}
	}
	
	private void showNotify(String strNote, String title, String msg, int action) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getBaseContext())
				.setSmallIcon(R.drawable.icon_purcharse)
				.setTicker(strNote)
				.setContentTitle(title)
				.setContentText(msg)
				.setContentIntent(
						PendingIntent.getActivity(this, 0, new Intent(this,
								AppMPlus.class),
								Notification.FLAG_ONLY_ALERT_ONCE))
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		Notification build = mBuilder.build();
		getNotificationManager().notify(Config.PRODUCT_NOTIFY_ID, build);
	}
	
	private NotificationManager mgr = null;
	
	private NotificationManager getNotificationManager() {
		if (mgr == null)
			mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		return mgr;
	}
	
	// #mark Connect
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case iMUA_HANG:
//			showNotify(MPlusLib.getResult("M-Plus Agent"), "Quý khách vừa mua thẻ", "Click vào đây để nhận mã thẻ", 0);
			ServiceCore.TaskMuaHang(isPendding, String.valueOf(getMyAccIndex()), getGroupCode(), getProductCode(), getProductName(),getAmount(), getPromo(), getQuantity());
			break;
		}
	}

	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		isPendding = false;
		isFinished = false;
		switch (iTag) {
		case iMUA_HANG:
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
			saveLogTransaction(User.getSrvTime(), User.getSeqNo(), "0" + User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, "", "", getAmountCost(getAmount(), getPromo(), getQuantity()));

			if (dataReceived.startsWith("val:")) {
				String data = Util.sCatVal(dataReceived);
				isFinished = true;
				parseProductFromIMart(getProductCode(), getAmount(), data);

				if(stemp != null){
					if(stemp.length == 2){
						sThongBao = getProductInfo();
						setMessageConfirm(getString(R.string.title_ketquagiaodich), getString(R.string.confirm_giaodich_thanhcong),
								"", sThongBao, getString(R.string.confirm_guimathe), getString(R.string.btn_co_guiSMSngay),
								getString(R.string.btn_ketthuc), true, false);
					}else if(stemp.length > 2){
						sThongBao = Util.insertString(getString(R.string.thongbao_nhieuthe), new String[]{getQuantity(), getProductName(), Util.formatNumbersAsTien(getAmount())+" "+getString(R.string.vnd), Util.getTimeClient3(User.getSrvTime())});
						setMessageConfirm(getString(R.string.title_ketquagiaodich), "", "", "", sThongBao, 
								getString(R.string.btn_vaokhothe), getString(R.string.btn_ketthuc), true, false);
					}
				}
				onCreateMyDialog(XAC_NHAN).show();

			} else if (dataReceived.startsWith("pen:")) {
				saveLogPending(User.getSeqNo(), sLenh);
				saveLogTransaction(User.getSrvTime(), User.getSeqNo(), "0" + User.getTransCode(), User.getDstNo(), getAmount(), sKetQua, "", "", getAmountCost(getAmount(), getPromo(), getQuantity()));
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
	
//	@Override
//	public void cmdUpdateLog_errorConnect() {
//		saveLogTransaction("", User.getSeqNo(), "", "", "", "err:", "", "", "");
//		super.cmdUpdateLog_errorConnect();
//	}

	private String getProductInfo() {
		String msg = "";
		if (getProducts() != null) {

			StringBuffer sBuf = new StringBuffer();

			if (!getProducts().get(0).getPin().equals("")) {
				sBuf.append(getString(R.string.card_pin));
				sBuf.append(": ");
				sBuf.append("<b>"+getProducts().get(0).getPin()+"</b>");
			}

			sBuf.append("<br/>");
			sBuf.append(getString(R.string.menh_gia));
			sBuf.append(": ");
			sBuf.append("<b>"+Util.formatNumbersAsTien(getProducts().get(0).getPrice()));
			sBuf.append(" "+getString(R.string.vnd)+"</b>");
			
			if (!getProducts().get(0).getAccNo().equals("")) {
				sBuf.append("<br/>");
				sBuf.append(getString(R.string.acc));
				sBuf.append(": ");
				sBuf.append("<b>"+getProducts().get(0).getAccNo()+"</b>");
			}

			if (!getProducts().get(0).getSeri().equals("")) {
				sBuf.append("<br/>");
				sBuf.append(getString(R.string.card_seri));
				sBuf.append(": ");
				sBuf.append("<b>"+getProducts().get(0).getSeri()+"</b>");
			}
			
			if (!getProducts().get(0).getExDate().equals("")) {
				sBuf.append("<br/>");
				sBuf.append(getString(R.string.card_exdate));
				sBuf.append(": ");
				sBuf.append("<b>"+getProducts().get(0).getExDate()+"</b>");
			}

//			if (!getProducts().get(0).getHotLine().equals("")) {
//				sBuf.append("\n");
//				sBuf.append(getString(R.string.card_hotline));
//				sBuf.append(": ");
//				sBuf.append(getProducts().get(0).getHotLine());
//			}

			msg = sBuf.toString();
		}
		return msg;
	}
	
	private Vector<Product> getProducts() {
		return mProduct;
	}
	
//	@Override
//	public void cmdErrorConnect() {
//		// khi mat mang se Update trang thai giao dich loi~
//		saveLogTransaction("", User.getSeqNo(), "", "", "", "err:", "", "", "");
//		super.cmdErrorConnect();
//	}
	
	@Override
	public void cmdNextXacNhan() {
		if(stemp != null){
			if(stemp.length == 2){
				processProduct(getProducts().get(0));
			}else if (stemp.length > 2){
				Intent intent = new Intent(this, FrmPurcharsedProduct.class);
				startActivityForResult(intent, 0);
				goBack();
			}
		}
		super.cmdNextXacNhan();
	}
	
	@Override
	public void cmdBackXacNhan() {
		if(isFinished)
			goBack();
		super.cmdBackXacNhan();
	}
	
	private void processProduct(Product product) {
		sendSMS(product);
		getProducts().get(0).setIsUsed(1);
		getDba().insertOrUpdateProduct(getProducts().get(0));
		goBack();
	}
}