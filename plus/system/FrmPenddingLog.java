package com.mpay.plus.system;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.LogBillItem;
import com.mpay.plus.database.LogTransactionItem;
import com.mpay.plus.database.User;
import com.mpay.plus.imart.FrmPurcharsedProduct;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

public class FrmPenddingLog extends AMPlusCore implements IProcess {
	private Vector<LogTransactionItem> vecLog;
	private LogAdapter adLog;
	private LogTransactionItem mSelectedItem;

////	private Button btnHuongDan;
	private byte iType = iCHUYEN_KHOAN;
	private String sLogCommandType;
	private String sLogCmdToSave;
	private String sLogSoTien;
	private String sLogToDest;
	private String sLogNhaCungCap;
	private String sLogSoLuong;
	private String sLogDesc;
	private String sLogProductCode;
	private String sLogAmount_Cost;
	private String sLogQuantity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.old_tra_cuu_pendding);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		setControls();
	}

	private void setControls() {
		try {
			
			sThongBao = getString(R.string.huong_dan_chi_tiet);
			onCreateMyDialog(THONG_BAO).show();
			
//			if(FrmMain.isCheckPending){
//				FrmMain.isCheckPending = false;
//				vecLog = getDba().getLogPendingTopup();
//			}else {
			vecLog = getDba().getLogPending();
//			}
			
//			btnHuongDan = (Button) findViewById(R.id.btn_huongdan);
//			btnHuongDan.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					sThongBao = getString(R.string.huong_dan_chi_tiet)
//							+ getString(R.string.hotline);
//					onCreateMyDialog(THONG_BAO, getIconNotice()).show();
//				}
//			});

			if (getItems() != null && getItems().size() > 0) {
				ListView listview = (ListView) findViewById(R.id.lvcontent);
				adLog = new LogAdapter(this, 0);
				listview.setAdapter(adLog);
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
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

	public void goBack() {
		vecLog = null;
		adLog = null;
//		btnHuongDan = null;
		sLogCmdToSave = null;
		sLogCommandType = null;
		sLogSoTien = null;
		sLogToDest = null;
		sLogNhaCungCap = null;
		sLogSoLuong = null;
		super.goBack();
	}

	public void cmdBackPending() {
	}

	public void goNext() {
		super.goNext();
	}

	private LogTransactionItem getSelectedItem() {
		return mSelectedItem;
	}
	private void actionSelect(int iIndex) {
		
		mSelectedItem = getItems().elementAt(iIndex);
		sThongBao = getDetailLog();
		
		setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.title_kiemtraketqua),
				sThongBao.indexOf("|") > 0 ? sThongBao.replace("|", "") : sThongBao, "", "", getString(R.string.btn_dongy),
						getString(R.string.btn_huy), false, true);
		onCreateMyDialog(XAC_NHAN).show();
	}
	
	private String getDetailLog() {
		StringBuilder strData = new StringBuilder();
			String arr[] = getSelectedItem().getTranCommand().toString().trim().split("\\|", -1);
			sLogCommandType = arr[0];
			sLogCmdToSave = arr[0];
			iType = iCHUYEN_KHOAN;
			
			LogTransactionItem logItem = new LogTransactionItem();
			logItem.setLogTime(getSelectedItem().getLogTime());
			logItem.setSeqNo(getSelectedItem().getSeqNo());
			
			String dstLabel = "";
			if (arr[0].equals("0" + Config.M_IMART_THANH_TOAN)) {
				iType = iMUA_HANG;
				sLogCommandType = Config.M_IMART_THANH_TOAN;
				sLogQuantity = arr[7];
				logItem.setTranCommand(arr[0]);
				logItem.setDestinationNo(arr[3]);
				logItem.setDescription(arr[4]);
				logItem.setAmount(arr[5]);
				logItem.setAddParam(arr[7]);
				logItem.setmAmount_cost(getAmountCost(arr[5], arr[6], arr[7]));
			} else if (arr[0].equals("1" + Config.M_IMART_THANH_TOAN)) {
				iType = iTOPUP_TRA_TRUOC;
				sLogCommandType = Config.M_IMART_THANH_TOAN;
				sLogQuantity = arr[9];
				logItem.setTranCommand(arr[0]);
				logItem.setDestinationNo(arr[8]);
				logItem.setDescription(arr[4]);
				logItem.setAmount(arr[5]);
				logItem.setAddParam(arr[7]);
				logItem.setmAmount_cost(getAmountCost(arr[5], arr[6], arr[9]));
			} else if (arr[0].equals("2" + Config.M_IMART_THANH_TOAN)) {
				iType = iTOPUP_TRA_SAU;
				sLogCommandType = Config.M_IMART_THANH_TOAN;
				sLogQuantity = arr[9];
				logItem.setTranCommand(arr[0]);
				logItem.setDestinationNo(arr[3].indexOf(",") > 0 ? arr[3].split(",", -1)[1] : arr[3]);
				logItem.setDescription(arr[4]);
				logItem.setAmount(arr[5]);
				logItem.setAddParam(arr[8]);
				logItem.setmAmount_cost(getAmountCost(arr[5], arr[6], arr[9]));
			} else if (arr[0].equals("3" + Config.M_IMART_THANH_TOAN)) {
				iType = iTOPUP_GAME;
				sLogCommandType = Config.M_IMART_THANH_TOAN;
				sLogQuantity = arr[9];
				logItem.setTranCommand(arr[0]);
				logItem.setDestinationNo(arr[3].indexOf(",") > 0 ? arr[3].split(",", -1)[1] : arr[3]);
				logItem.setDescription(arr[4]);
				logItem.setAmount(arr[5]);
				logItem.setAddParam(arr[8]);
				logItem.setmAmount_cost(getAmountCost(arr[5], arr[6], arr[9]));
			} else if (arr[0].equals(Config.M_HOA_DON_THANH_TOAN)) {
				iType = iTHANH_TOAN_HOA_DON;
				sLogCommandType = arr[0];
				logItem.setTranCommand(arr[0]);
				logItem.setDestinationNo(arr[7].indexOf("\\.") > 0 ? arr[7].split("\\.", -1)[1] : arr[7]);
				logItem.setDescription(arr[6]);
				logItem.setAmount(arr[2]);
				logItem.setAddParam(arr[13]);
				logItem.setmAmount_cost("");
			} else if (arr[0].equals(Config.A_CK_AGENT)) {
				iType = iCHUYEN_KHOAN;
				sLogCommandType = Config.A_CK_AGENT;
			} else if (arr[0].equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
				iType = iNAP_TIEN_AGENT;
				sLogCommandType = Config.A_NAP_TIEN_TU_TK_BANK;
			} else if (arr[0].equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
				iType = iRUT_TIEN_AGENT;
				sLogCommandType = Config.A_RUT_TIEN_VE_TK_BANK;
			}
			
			
			if(!logItem.getTranCommand().equals("")){
				strData.append("<b>"+getTransactionName(logItem.getTranCommand(), 1));
				strData.append("</b><br/>");
			}
			
			if (!logItem.getDestinationNo().equals("")) {
				// Destination
				dstLabel = getTransactionDetail(logItem);
				if (!dstLabel.equals("")) {
					strData.append(dstLabel);
				}
			}
			
			if(!logItem.getAmount().equals("")){
				if(logItem.getmAmount_cost().equals("")){
					strData.append(getString(R.string.title_sotien_));
				}else {
					strData.append(getString(R.string.amount));
				}
				strData.append(": <b>");
				strData.append(Util.formatNumbersAsTien(logItem.getAmount()));
				strData.append("</b><br/>");
			}
			if (logItem.getTranCommand().equals("0" + Config.M_IMART_THANH_TOAN)) {
				// mua hang imart show so luong the
				if(!logItem.getAddParam().equals("")){
					strData.append(getString(R.string.so_luong));
					strData.append(": <b>");
					strData.append(logItem.getAddParam() + " " + getString(R.string.cmd_card));
					strData.append("</b><br/>");
				}
			}

			if(!logItem.getmAmount_cost().equals("")){
				strData.append(getString(R.string.title_sotien_));
				strData.append(": <b>");
				strData.append(Util.formatNumbersAsTien(logItem.getmAmount_cost()));
				strData.append("</b><br/>");
			}

			if(!logItem.getDescription().equals("")){
				strData.append(getString(R.string.thongtin));
				strData.append(": <b>");
				strData.append(logItem.getDescription());
				strData.append("</b><br/>");
			}
			
			if(!logItem.getSeqNo().equals("")){
				strData.append(getString(R.string.seqno));
				strData.append(": <b>");
				strData.append(logItem.getSeqNo());
				strData.append("</b><br/>");
			}
			
		return strData.toString();
	}
	
	
	private String getSeqNo() {
		return getSelectedItem().getSeqNo();
	}

	private Vector<LogTransactionItem> getItems() {
		return vecLog;
	}

	@Override
	public void cmdNextThongBao() {
		if (isFinished) {
			if (getItems().size() <= 1) {
				finish();
			} else {
				getItems().remove(adLog.getSelectedIndex());
				adLog.reset();
				adLog.notifyDataSetChanged();
			}
		}
	};
	
	@Override
	public void cmdNextXacNhan(String data) {
		threadThucThi = new ThreadThucThi(this);
		threadThucThi.setIProcess(this, iType);
		threadThucThi.Start();
		super.cmdNextXacNhan(data);
	}
	@Override
	public void cmdNextXacNhan() {
		if(isFinished){
			startActivity(new Intent(this, FrmPurcharsedProduct.class));
			goBack();
		}
		super.cmdNextXacNhan();
	}

//	public void cmdNextMPIN() {
//		super.cmdNextMPIN();
//		String arr[] = getSelectedItem().getTranCommand().split("\\|", -1);
//		sLogCommandType = arr[0];
//		sLogCmdToSave = arr[0];
//		byte iType = iCHUYEN_KHOAN;
//
//		if (arr[0].equals("0" + Config.M_IMART_THANH_TOAN)) {
//			iType = iMUA_HANG;
//			sLogCommandType = Config.M_IMART_THANH_TOAN;
//		} else if (arr[0].equals("1" + Config.M_IMART_THANH_TOAN)) {
//			iType = iTOPUP_TRA_TRUOC;
//			sLogCommandType = Config.M_IMART_THANH_TOAN;
//		} else if (arr[0].equals("2" + Config.M_IMART_THANH_TOAN)) {
//			iType = iTOPUP_TRA_SAU;
//			sLogCommandType = Config.M_IMART_THANH_TOAN;
//		} else if (arr[0].equals("3" + Config.M_IMART_THANH_TOAN)) {
//			iType = iTOPUP_GAME;
//			sLogCommandType = Config.M_IMART_THANH_TOAN;
//		} else if (arr[0].equals(Config.M_HOA_DON_THANH_TOAN)) {
//			iType = iTHANH_TOAN_HOA_DON;
//			sLogCommandType = arr[0];
//		} else if (arr[0].equals(Config.A_CK_AGENT)) {
//			iType = iCHUYEN_KHOAN;
//			sLogCommandType = Config.A_CK_AGENT;
//		} else if (arr[0].equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
//			iType = iNAP_TIEN_AGENT;
//			sLogCommandType = Config.A_NAP_TIEN_TU_TK_BANK;
//		} else if (arr[0].equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
//			iType = iRUT_TIEN_AGENT;
//			sLogCommandType = Config.A_RUT_TIEN_VE_TK_BANK;
//		}
//
//		threadThucThi = new ThreadThucThi(this);
//		threadThucThi.setIProcess(this, iType);
//		threadThucThi.Start();
//	}

	// #mark Connect
	public void processDataSend(byte iTag) {
		isFinished = false;
		String arr[] = getSelectedItem().getTranCommand().split("\\|", -1);
		String[] sOTP;
		User.setSeqNos(getSeqNo());
		switch (iTag) {
		case iMUA_HANG:
			// type+command|accIndex|groupCode|productCode|productName|sMoney|promo|volume
//			for (int i = 0; i < arr.length; i++) {
//				Log.i("Log : ", "arr["+i+"] :"+arr[i]);
//			}
			sLogSoLuong = arr[7];
			sLogProductCode = arr[2];
			sLogToDest = arr[3];
			sLogDesc = arr[4];
			sLogSoTien = arr[5];
			sLogAmount_Cost = arr[6];
			ServiceCore.TaskMuaHang(true, arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7]);
			break;

		case iTOPUP_TRA_TRUOC:
			// type+command|type|accIndex|groupCode|productCode+sTopupAccount|sProductName|sMoney|promo|topupAccount|volume
			sLogSoLuong = arr[9];
			sLogToDest = arr[7];
			sLogSoTien = arr[5];
			String arrCodeTratruoc[] = arr[3].split(",", -1);
			sLogDesc = arrCodeTratruoc[1];
			sLogAmount_Cost = arr[6];
			ServiceCore.TaskTopup(true, arr[0], arr[1], arr[2], arrCodeTratruoc[0], arr[4], arr[5], arr[6], arr[7], arr[8]);
			break;

		case iTOPUP_GAME:
			// type+command|type|accIndex|groupCode|productCode+sTopupAccount|sProductName|sMoney|promo|topupAccount|volume
			sLogSoLuong = arr[9];
			sLogToDest = arr[4];
			sLogSoTien = arr[5];
			String arrCodeGame[] = arr[3].split(",", -1);
			sLogDesc = arrCodeGame[1];
			sLogAmount_Cost = arr[6];
			ServiceCore.TaskTopup(true, arr[0] ,arr[1], arr[2], arrCodeGame[0], arr[4], arr[5], arr[6], arr[7], arr[8]);
			break;

		case iTOPUP_TRA_SAU:
			// type+command|type|accIndex|groupCode|productCode+sTopupAccount|sProductName|sMoney|promo|topupAccount|volume
			sLogSoLuong = arr[9];
			sLogToDest = arr[7];
			sLogSoTien = arr[5];
			String arrCodeTrasau[] = arr[3].split(",", -1);
			sLogDesc = arrCodeTrasau[1];
			sLogAmount_Cost = arr[6];
			ServiceCore.TaskTopup(true, arr[0], arr[1], arr[2], arrCodeTrasau[0], arr[4], arr[5], arr[6], arr[7], arr[8]);
			break;

		case iTHANH_TOAN_HOA_DON:
			// sCommand|iAcc|sSoTien|sKMai|sFee|sLocalSTS|sNoiDung|sIdTT|arrOTP[0]|arrOTP[1]|arrOTP[2]
			sLogToDest = arr[7];
			sLogSoTien = arr[2];
			sLogNhaCungCap = arr[11];
			sLogDesc = arr[6];
			sLogAmount_Cost = arr[3];
			String trid = arr.length > 12 ? arr[12] : "";
			sOTP = new String[] { arr[8], arr[9], arr[10] };
			String tenkhachhang = arr.length > 12 ? arr[13] : "";
			ServiceCore.TaskThanhToanHoaDon(true, arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], sOTP, trid, "", tenkhachhang);
			break;

		case iNAP_TIEN_AGENT:
			// sCommand|accIndex|sMoney|sDescription|arrOTP[0]|arrOTP[1]|arrOTP[2]
			sLogSoTien = arr[2];
			sLogToDest = User.MID;
			sLogDesc = arr[3];
			sLogAmount_Cost = "";
			sOTP = new String[] { arr[4], arr[5], arr[6] };
			ServiceCore.TaskNapTien(true, arr[1], arr[2], arr[3], sOTP);
			break;

		case iRUT_TIEN_AGENT:
			// sCommand|accIndex|sMoney|sFee|sDescription|arrOTP[0]|arrOTP[1]|arrOTP[2]
			sLogSoTien = arr[2];
			sLogToDest = User.MID;
			sLogDesc = arr[4];
			sLogAmount_Cost = "";
			sOTP = new String[] { arr[4], arr[5], arr[6] };
			ServiceCore.TaskRutTien(true, arr[1], arr[2], arr[3], arr[4], sOTP);
			break;

		case iCHUYEN_KHOAN:
			// sCommand|accIndex|sToAcc|sAmt|sDetail|sTelNhan|sOTP[0]|sOTP[1]|sOTP[2]
			sLogSoTien = arr[3];
			sLogToDest = arr[2];
			sLogDesc = arr[4];
			sLogAmount_Cost = "";
			sOTP = new String[] { arr[6], arr[7], arr[8] };
			ServiceCore.TaskChuyenKhoanAgent(true, sLogCommandType, arr[1],
					arr[2], arr[3], arr[4], arr[5], sOTP, "");
			break;
		}
	}

	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		sThongBao = "";
		String sKetQua = "val";
		
		// gia lap du lieu
//		dataReceived = "val:66322455|3BVNP-9999-1077266!417/02/2015!1BVN1077266!(18001090)";
		
		if (dataReceived.startsWith("val")) {
			sKetQua = "val";
			User.isActived = true;
		} else if (dataReceived.startsWith("pen")
				|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
			sKetQua = "";
		else
			sKetQua = dataReceived;
		if (!sKetQua.equals("")) {
			deleteLogPending(getSeqNo());
			saveLogTransaction(User.getSrvTime(), getSeqNo(), sLogCmdToSave, User.getDstNo(), sLogSoTien, sKetQua, sLogDesc, "", getAmountCost(sLogSoTien, sLogAmount_Cost, sLogQuantity)
					);
			isFinished = true;
		}
		saveUserTable();

		String sTime = "";
		try {
			sTime = Util.getTimeClient3(getSelectedItem().getLogTime());
		} catch (Exception ex) {
		}

		switch (iTag) {
		case iCHUYEN_KHOAN:
			Util.insertString(getString(R.string.pendding_chuyen_khoan),
					new String[] { Util.formatNumbersAsTien(sLogSoTien),
							sLogToDest, " "+sTime });
			break;

		case iTHANH_TOAN_HOA_DON:
			// Update Log Bill Payment Result
			LogBillItem logBill = new LogBillItem();
			logBill.setLogTime("");
			logBill.setSeqNo(getSeqNo());
			logBill.setAmount("");
			logBill.setDescription("");
			logBill.setResult(sKetQua.equals("err") ? dataReceived : sKetQua);
			
			String[] bill = TextUtils.split(sLogToDest, "\\.");
			if(bill.length > 1){
				logBill.setSupplierId(bill[0]);
				logBill.setBillCode(bill[1]);
			} else logBill.setBillCode(sLogToDest);
			
			getDba().insertOrUpdateBillLog(logBill);

			sThongBao = Util.insertString(
					getString(R.string.pendding_tt_hoa_don),
					new String[] { sLogToDest.split("\\.", -1)[1],
							sLogNhaCungCap.split(":", -1)[0],
							Util.formatNumbersAsTien(sLogSoTien), " "+sTime });
			break;

		case iMUA_HANG:
			sThongBao = Util.insertString(
					getString(R.string.pendding_imart),
					new String[] { sLogSoLuong, sLogToDest,
							Util.formatNumbersAsTien(sLogSoTien), " "+sTime });
			break;

		case iTOPUP_TRA_TRUOC:
			sThongBao = Util.insertString(getString(R.string.pendding_topup),
					new String[] { Util.formatNumbersAsTien(sLogSoTien),
							sLogToDest, " "+sTime });
			break;

		case iTOPUP_GAME:

			sThongBao = Util.insertString(
					getString(R.string.pendding_topup_game), new String[] {
							Util.formatNumbersAsTien(sLogSoTien), sLogToDest,
							" "+sTime });
			break;

		case iTOPUP_TRA_SAU:
			sThongBao = Util.insertString(
					getString(R.string.pendding_topup_ts),
					new String[] { Util.formatNumbersAsTien(sLogSoTien),
							sLogToDest, " "+sTime });
			break;

		case iNAP_TIEN_AGENT:
			Util.insertString(
					getString(R.string.pendding_nap_tien_agent),
					new String[] { Util.formatNumbersAsTien(sLogSoTien), " "+sTime });
			break;

		case iRUT_TIEN_AGENT:
			Util.insertString(
					getString(R.string.pendding_rut_tien_agent),
					new String[] { Util.formatNumbersAsTien(sLogSoTien), " "+sTime });
			break;
		}

		if (dataReceived.startsWith("val:")) {
			
			dataReceived = Util.sCatVal(dataReceived);
			// String arrTemp[];
			switch (iTag) {
			case iRUT_TIEN_AGENT:
			case iNAP_TIEN_AGENT:
			case iTHANH_TOAN_HOA_DON:
			case iTOPUP_TRA_TRUOC:
			case iTOPUP_TRA_SAU:
			case iTOPUP_GAME:
			case iCHUYEN_KHOAN:
				sThongBao += "<br/><br/>" + getString(R.string.pend_ketqua_thanhcong);
				onCreateMyDialog(THONG_BAO).show();
				break;
			case iMUA_HANG:
				String data = Util.sCatVal(dataReceived);
				isFinished = true;
				parseProductFromIMart(sLogProductCode, sLogSoTien, data);
				getItems().remove(adLog.getSelectedIndex());
				adLog.reset();
				adLog.notifyDataSetChanged();
				
				sThongBao += "<br/><br/>" + getString(R.string.pend_ketqua_thanhcong);
				
				setMessageConfirm(getString(R.string.title_ketquagiaodich), "", "", "", sThongBao, 
						getString(R.string.btn_vaokhothe), getString(R.string.btn_ketthuc), true, false);
				onCreateMyDialog(XAC_NHAN).show();
//				adLog.reset();
//				adLog.notifyDataSetChanged();
//				// ////////////////////////////////////
//				Intent intent = new Intent(this, FrmPurcharsedProduct.class);
//				Bundle sendBundle = new Bundle();
//				sendBundle.putString("msg", sThongBao);
//				sendBundle.putString("data", dataReceived);
//				sendBundle.putString("pdcd", sLogToDest);
//				sendBundle.putString("price", sLogSoTien);
//				sendBundle.putByte("islog", (byte) 2);
//				intent.putExtras(sendBundle);
//				startActivityForResult(intent, 0);
//				finish();

				break;
			}
		} else if (dataReceived.startsWith("pen:")) {
			// show Confirm Pedding
			sThongBao += "<br/><br/>" + getString(R.string.pend_ketqua_pend);
			onCreateMyDialog(THONG_BAO).show();
		} else {
			// err
			sThongBao += "<br/><br/>" + getString(R.string.pend_ketqua_err) + " "
					+ Util.sCatVal(dataReceived);
			onCreateMyDialog(THONG_BAO).show();
		}
	}

	public class LogAdapter extends BaseAdapter {
		private Context mContext;
		private int iSelectedIndex;
		private int iLength;

		public LogAdapter(Context context, int ivIndex) {
			super();
			mContext = context;
			iSelectedIndex = ivIndex;
			iLength = getItems().size();
		}

		public void reset() {
			iSelectedIndex = 0;
			iLength = getItems().size();
		}

		public int getCount() {
			return iLength;
		}

		public int getSelectedIndex() {
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

			if (getItems() != null && getItems().size() > 0) {
				LogTransactionItem log = getItems().elementAt(position);
				String sTime = "";
				String sSoTien = "";
				String sSoLuong = "";
				String sToDes = "";
				String strTemp = "";

				try {
					sTime = Util.getTimeClient3(log.getLogTime());
				} catch (Exception ex) {
				}

				String arr[] = log.getTranCommand().split("\\|", -1);

				if (arr[0].equals(Config.A_CK_AGENT)) {
					// sCommand|accIndex|sToAcc|sAmt|sDetail|sTelNhan|sOTP[0]|sOTP[1]|sOTP[2]
					sSoTien = Util.formatNumbersAsTien(arr[3]);
					sToDes = Util.formatNumbersAsMid(arr[2]);
					strTemp = Util.insertString(
							getString(R.string.pendding_chuyen_khoan),
							new String[] { sSoTien, sToDes });
				} else if (arr[0].equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
					// sCommand|accIndex|sMoney|sDescription|arrOTP[0]|arrOTP[1]|arrOTP[2]
					sSoTien = Util.formatNumbersAsTien(arr[2]);
					strTemp = Util.insertString(
							getString(R.string.pendding_nap_tien_agent),
							new String[] { sSoTien });
				} else if (arr[0].equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
					// sCommand|accIndex|sMoney|sFee|sDescription|arrOTP[0]|arrOTP[1]|arrOTP[2]
					sSoTien = Util.formatNumbersAsTien(arr[3]);
					strTemp = Util.insertString(
							getString(R.string.pendding_rut_tien_agent),
							new String[] { sSoTien });
				} else if (arr[0].equals(Config.M_HOA_DON_THANH_TOAN)) {
					// sCommand|iAcc|sSoTien|sKMai|sFee|sLocalSTS|sNoiDung|sIdTT|arrOTP[0]|arrOTP[1]|arrOTP[2]
					sToDes = arr[7].split("\\.", -1)[1];
					sSoTien = Util.formatNumbersAsTien(arr[2]);
					String sNhacungCap = arr[11].split(":", -1)[0];
					strTemp = Util
							.insertString(
									getString(R.string.pendding_tt_hoa_don),
									new String[] { sToDes, sNhacungCap,
											sSoTien });
				} else if (arr[0].equals("0" + Config.M_IMART_THANH_TOAN)) {
					// type+command|accIndex|groupCode|productCode|productName|sMoney|promo|volume
					sSoLuong = arr[7];
					sToDes = arr[4];
					sSoTien = Util.formatNumbersAsTien(arr[5]);
					strTemp = Util.insertString(
							getString(R.string.pendding_imart), new String[] {
									sSoLuong, sToDes, sSoTien });
				} else if (arr[0].equals("1" + Config.M_IMART_THANH_TOAN)) {
					// type+command|type|accIndex|groupCode|productCode+sTopupAccount|sProductName|sMoney|promo|topupAccount|volume
					sToDes = arr[7];
					sSoTien = Util.formatNumbersAsTien(arr[5]);
					strTemp = Util.insertString(
							getString(R.string.pendding_topup), new String[] {
									sSoTien, Util.formatNumbersAsMid(sToDes) });
				} else if (arr[0].equals("2" + Config.M_IMART_THANH_TOAN)) {
					// type+command|type|accIndex|groupCode|productCode+sTopupAccount|sProductName|sMoney|promo|topupAccount|volume
					sToDes = arr[7];
					sSoTien = Util.formatNumbersAsTien(arr[5]);
					strTemp = Util.insertString(
							getString(R.string.pendding_topup_ts),
							new String[] { sSoTien, Util.formatNumbersAsMid(sToDes) });
				} else if (arr[0].equals("3" + Config.M_IMART_THANH_TOAN)) {
					// /type+command|accIndex|groupCode|productCode|sMoney|promo|volume|topupAccount|otp|otpid|otpdate
					sToDes = arr[7];
					sSoTien = Util.formatNumbersAsTien(arr[5]);
					strTemp = Util.insertString(
							getString(R.string.pendding_topup_game),
							new String[] { sSoTien, sToDes });
				}
				tvTitle.setText(sTime);
				tvDetail.setText(strTemp);

				if (position == iSelectedIndex)
					radio.setChecked(true);
				else
					radio.setChecked(false);
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						iSelectedIndex = position;
						actionSelect(iSelectedIndex);
						LogAdapter.this.notifyDataSetChanged();
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